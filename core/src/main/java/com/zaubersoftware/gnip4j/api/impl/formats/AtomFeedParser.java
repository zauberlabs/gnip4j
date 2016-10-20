/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api.impl.formats;


import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.zaubersoftware.gnip4j.api.impl.ISO8601DateParser;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Actor;
import com.zaubersoftware.gnip4j.api.model.Author;
import com.zaubersoftware.gnip4j.api.model.Category;
import com.zaubersoftware.gnip4j.api.model.Generator;
import com.zaubersoftware.gnip4j.api.model.InReplyTo;
import com.zaubersoftware.gnip4j.api.model.Links;
import com.zaubersoftware.gnip4j.api.model.MatchingRules;
import com.zaubersoftware.gnip4j.api.model.Object;
import com.zaubersoftware.gnip4j.api.model.Provider;
import com.zaubersoftware.gnip4j.api.model.Source;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;


/**
 * Parse the activity stream which is in XML format 
 * 
 * @author Juan F. Codagnone
 * @since Dec 13, 2012
 */
public class AtomFeedParser {
    private static final String ACTIVITY_NS = "http://activitystrea.ms/spec/1.0/";
    private static final QName ACTIVITY_ACTOR = new QName(ACTIVITY_NS, "actor");
    private static final QName ACTIVITY_OBJECT_TYPE = new QName(ACTIVITY_NS, "object-type");
    private static final QName ACTIVITY_OBJECT = new QName(ACTIVITY_NS, "object");
    private static final QName ACTIVITY_VERB = new QName(ACTIVITY_NS, "verb");

    private static final String SERVICE_NS = "http://activitystrea.ms/service-provider";
    private static final QName SERVICE_PROVIDER = new QName(SERVICE_NS, "provider");
    
    private static final String GNIP_NS = "http://www.gnip.com/schemas/2010";
    private static final QName GNIP_MATCHING_RULES = new QName(GNIP_NS, "matching_rules");
    private static final QName GNIP_MATCHING_RULE = new QName(GNIP_NS, "matching_rule");
    private static final QName GNIP_RULE = new QName(GNIP_NS, "rule");
    private static final QName GNIP_FAVORITE_COUNT = new QName(GNIP_NS, "statistics");
    
    private static final String FB_NS = "http://www.facebook.com";
    private static final QName FB_ID = new QName(FB_NS, "id");
    private static final QName FB_FRIEND_COUNT = new QName(FB_NS, "friend_count");
    
    private static final String THR_NS = "http://purl.org/syndication/thread/1.0";
    private static final QName THR_IN_REPLY_TO = new QName(THR_NS, "in-reply-to");
    
    private static final String ATOM_NS = "http://www.w3.org/2005/Atom";
    private static final QName ATOM_ENTRY = new QName(ATOM_NS, "entry");
    private static final QName ATOM_ID = new QName(ATOM_NS, "id");
    private static final QName ATOM_CREATED = new QName(ATOM_NS, "created");
    private static final QName ATOM_UPDATED = new QName(ATOM_NS, "updated");
    private static final QName ATOM_PUBLISHED = new QName(ATOM_NS, "published");
    private static final QName ATOM_NAME = new QName(ATOM_NS, "name");
    private static final QName ATOM_URI = new QName(ATOM_NS, "uri");
    private static final QName ATOM_ICON = new QName(ATOM_NS, "icon");
    private static final QName ATOM_TITLE = new QName(ATOM_NS, "title");
    private static final QName ATOM_CATEGORY = new QName(ATOM_NS, "category");
    private static final QName ATOM_LINK = new QName(ATOM_NS, "link");
    private static final QName ATOM_CAPTION = new QName(ATOM_NS, "caption");
    private static final QName ATOM_COMMENT = new QName(ATOM_NS, "comment");
    private static final QName ATOM_SOURCE = new QName(ATOM_NS, "source");
    private static final QName ATOM_CONTENT = new QName(ATOM_NS, "content");
    private static final QName ATOM_SUMMARY = new QName(ATOM_NS, "summary");
    private static final QName ATOM_AUTHOR = new QName(ATOM_NS, "author");
    private static final QName ATOM_GENERATOR = new QName(ATOM_NS, "generator");
    private static final QName ATOM_SUBTITLE = new QName(ATOM_NS, "subtitle");
    private static final QName ATOM_IS_VERIFIED = new QName(ATOM_NS, "is_verified");
    private static final QName ATOM_FAN_COUNT = new QName(ATOM_NS, "fan_count");

    private final Stack<State> state = new Stack<>();

    /** process an atom feed  */
    public Activity process(final XMLStreamReader reader) throws XMLStreamException, ParseException {
        final Activity activity = new Activity();
        state.push(new StartState(activity));
        while(reader.hasNext()) {
            boolean reprocess = false; 
            reader.next();
            do {
                final State s = state.peek();
                reprocess = s.process(reader);
            } while(reprocess);
        }
        while(!state.isEmpty()) {
            final State s = state.pop();
            s.onDone();
        }
        
        return activity;
    }
    
    /** useful to detect if we are done parsing a complex xml element */
    static final class ExitStrategy {
        private int i = 1;
        
        /** notify every start element */
        public void notifyStartElement(final XMLStreamReader reader) {
            i++;
        }
        
        /** notify every end element */
        public void notifyEndElement(final XMLStreamReader reader) {
            i--;
        }
        
        public boolean isDone() {
            return i == 0;
        }
    }
    
    /** base behaviour for states */
    abstract class State {
        private final Logger logger = LoggerFactory.getLogger(getClass());
        private final ExitStrategy exit = new ExitStrategy();
        /** input for the state machine  */
        public final boolean process(final XMLStreamReader reader) throws XMLStreamException, ParseException {
            boolean ret = false;
            
            if(reader.isStartElement()) {
                exit.notifyStartElement(reader);
            } else if(reader.isEndElement()) {
                exit.notifyEndElement(reader);
            }
            if(exit.isDone()) {
                onDone();
                state.pop();
            } else {
                ret = doProcess(reader);
            }
            
            return ret;
        }

        /** do the real processing  */
        public abstract boolean doProcess(final XMLStreamReader reader) throws XMLStreamException, ParseException;
        
        /** template method executed when all work is done */
        public void onDone() {
            // void
        }
        
        /** default handler for events not handled */
        protected  void onUnhandledEvent(final XMLStreamReader reader) {
            if(reader.isStartElement()) {
               final String key = getClass().getName() + reader.getName().toString();
               if(!reportedUnhandled.contains(key)) {
                   logger.warn("The element `{}' is not being handled by the parser in class."
                              + " You could enhace this situation :)", reader.getName());
                   reportedUnhandled.add(key);
               }
            }
        }
    }
    private static final Set<String> reportedUnhandled = new HashSet<>();
    
    /** initial state */
    class StartState extends State {
        private final Activity activity;

        public StartState(final Activity activity) {
            this.activity = activity;
        }
        
        @Override
        public boolean doProcess(final XMLStreamReader reader) {
            if(reader.isStartElement() && ATOM_ENTRY.equals(reader.getName())) {
                state.push(new ActivityState(activity));
            } else {
                onUnhandledEvent(reader);
            }
            
            return false;
        }
    }
    
    /**
     * When {@link XMLStreamReader#getElementText()} is used, the cursor is advanced to the next
     * end document event. We must process this event before reading other. This function reads the event
     * and notifies the state machine
     */
    public static String getText(final AtomicBoolean ret, final XMLStreamReader reader) throws XMLStreamException {
        final String s = reader.getElementText();
        ret.set(true);
        return s;
    }
    
    /** NPE free trim */
    public static String trim(final String s) {
        String ret = s;
        
        if(ret != null) {
            ret = s.trim();
        }
        return ret;
    }
    
    /** parse atom:entry */
    private class ActivityState extends State {
        private final Activity activity;
        private String fbId;
        private Actor fbActor;
        
        public ActivityState(final Activity activity) {
            this.activity = activity;
        }

        private Activity getActivity() {
            return activity;
        }
        
        @Override
        public void onDone() {
            super.onDone();
            
            if(fbId != null) {
                activity.setId(fbId);
            }
            
            if(fbActor != null) {
                final Actor actor = activity.getActor();
                if(actor == null) {
                    activity.setActor(fbActor);
                }
            }
        }
        
        @Override
        public boolean doProcess(final XMLStreamReader reader) throws XMLStreamException, ParseException {
            final AtomicBoolean ret = new AtomicBoolean();
            
            if(reader.isStartElement() && ATOM_ID.equals(reader.getName())) {
                getActivity().setId(getText(ret, reader));
            } else if(reader.isStartElement() && FB_ID.equals(reader.getName())) {
                fbId = getText(ret, reader);
            } else if(reader.isStartElement() && ATOM_PUBLISHED.equals(reader.getName())) {
                getActivity().setPostedTime(ISO8601DateParser.parse(getText(ret, reader)));
            } else if(reader.isStartElement() && ATOM_CREATED.equals(reader.getName())) {
                getActivity().setCreated(ISO8601DateParser.parse(getText(ret, reader)));
            } else if(reader.isStartElement() && ATOM_UPDATED.equals(reader.getName())) {
                getActivity().setUpdated(ISO8601DateParser.parse(getText(ret, reader)));
            } else if(reader.isStartElement() && ATOM_TITLE.equals(reader.getName())) {
                getActivity().setTitle(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_CATEGORY.equals(reader.getName())) {
                final Category category = new Category();
                for(int i = 0; i < reader.getAttributeCount(); i++) {
                    final String name = reader.getAttributeName(i).getLocalPart();
                    if("term".equals(name)) {
                        category.setTerm(reader.getAttributeValue(i));
                    } else if("label".equals(name)) {
                        category.setLabel(reader.getAttributeValue(i));
                    }
                }
                getActivity().getCategoriesRules().add(category);
            } else if(reader.isStartElement() && ATOM_LINK.equals(reader.getName())) {
                getActivity().getLinks().add(parseLink(reader));
            } else if(reader.isStartElement() && ATOM_SOURCE.equals(reader.getName())) {
                state.push(new SourceState() {
                    @Override
                    public void onDone() {
                        super.onDone();
                        
                        getActivity().setSource(source);
                    }
                });
            } else if(reader.isStartElement() && ATOM_CAPTION.equals(reader.getName())) {
                getActivity().setCaption(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_COMMENT.equals(reader.getName())) {
                getActivity().setComment(getText(ret, reader));
            } else if(reader.isStartElement() && ACTIVITY_ACTOR.equals(reader.getName())) {
                state.push(new ActorState() {
                    @Override
                    public void onDone() {
                        super.onDone();
                        
                        getActivity().setActor(actor);
                    }
                });
            } else if(reader.isStartElement() && GNIP_MATCHING_RULES.equals(reader.getName())) {
                state.push(new MatchingRulesState() {
                    @Override
                    public void onDone() {
                        super.onDone();
                        
                        getActivity().getGnip().getMatchingRules().addAll(rules);
                    }
                });
            } else if(reader.isStartElement() && GNIP_FAVORITE_COUNT.equals(reader.getName())) {
                for(int i = 0; i < reader.getAttributeCount(); i++) {
                    final String name = reader.getAttributeName(i).getLocalPart();
                    if("favoriteCount".equals(name) && !reader.getAttributeValue(i).isEmpty()) {
                        getActivity().getGnip().setFavoriteCount(Long.parseLong(reader.getAttributeValue(i)));
                    }
                }
            } else if(reader.isStartElement() && ACTIVITY_OBJECT.equals(reader.getName())) {
                final Object o = new Object();
                state.push(new ActivityState(o) {
                    @Override
                    public void onDone() {
                        super.onDone();
                        
                        getActivity().setObject(o);
                    }
                });
            } else if(reader.isStartElement() && SERVICE_PROVIDER.equals(reader.getName())) {
                state.push(new ServiceProvider() {
                    @Override
                    public void onDone() {
                        super.onDone();
                        
                        getActivity().setProvider(provider);
                    }
                });
            } else if(reader.isStartElement() && ACTIVITY_VERB.equals(reader.getName())) {
                getActivity().setVerb(trim(getText(ret, reader)));
            } else if(reader.isStartElement() && ACTIVITY_OBJECT_TYPE.equals(reader.getName())) {
                getActivity().setObjectType(trim(getText(ret, reader)));
            } else if(reader.isStartElement() && ATOM_SUMMARY.equals(reader.getName())) {
                getActivity().setSummary(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_AUTHOR.equals(reader.getName())) {
                state.push(new AuthorState() {
                    @Override
                    public void onDone() {
                        super.onDone();
                        
                        getActivity().setAuthor(getAuthor());
                        ActivityState.this.fbActor = super.fbActor;
                    }
                });
            } else if(reader.isStartElement() && ATOM_CONTENT.equals(reader.getName())) {
                getActivity().setContent(getText(ret, reader));
            } else if(reader.isStartElement() && THR_IN_REPLY_TO.equals(reader.getName())) {
                getActivity().setInReplyTo(parseInReplyTo(reader));
            } else if(reader.isStartElement() && ATOM_GENERATOR.equals(reader.getName())) {
                getActivity().setGenerator(parseGenerator(ret, reader));
            } else if(reader.isStartElement() && ATOM_SUBTITLE.equals(reader.getName())) {
                getActivity().setSubtitle(getText(ret, reader));
            } else {
                onUnhandledEvent(reader);
            }
            return ret.get();
        }
    }
    
    /** parse an atom:link */
    private static Links parseLink(final XMLStreamReader reader) {
        final Links link = new Links();
        for(int i = 0; i < reader.getAttributeCount(); i++) {
            final String name = reader.getAttributeName(i).getLocalPart();
            if("rel".equals(name)) {
                link.setRel(reader.getAttributeValue(i));
            } else if("type".equals(name)) {
                link.setType(reader.getAttributeValue(i));
            } else if("href".equals(name)) {
                link.setHref(reader.getAttributeValue(i));
            }
        }
        return link;
    }
    
    /** parse an atom:link */
    private static InReplyTo parseInReplyTo(final XMLStreamReader reader) {
        final InReplyTo to = new InReplyTo();
        for(int i = 0; i < reader.getAttributeCount(); i++) {
            final String name = reader.getAttributeName(i).getLocalPart();
            if("type".equals(name)) {
                to.setType(reader.getAttributeValue(i));
            } else if("href".equals(name)) {
                to.setLink(reader.getAttributeValue(i));
            }
        }
        
        return to;
    }
    
    /** parse an atom:link  */
    private static Generator parseGenerator(final AtomicBoolean ret, final XMLStreamReader reader) 
                throws XMLStreamException {
        final Generator to = new Generator();
        
        for(int i = 0; i < reader.getAttributeCount(); i++) {
            final String name = reader.getAttributeName(i).getLocalPart();
            if("uri".equals(name)) {
                to.setLink(reader.getAttributeValue(i));
            }
        }
        to.setDisplayName(getText(ret, reader));
        
        return to;
    }

    /** parse the Actor entity */
    class ActorState extends State {
        protected final Actor actor = new Actor();
        
        @Override
        public boolean doProcess(final XMLStreamReader reader) throws XMLStreamException {
            final AtomicBoolean ret = new AtomicBoolean(false);
            
            if(reader.isStartElement() && ACTIVITY_OBJECT_TYPE.equals(reader.getName())) {
                actor.setObjectType(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_LINK.equals(reader.getName())) {
                actor.getLinks().add(parseLink(reader));
            } else {
                onUnhandledEvent(reader);
            }
            
            return ret.get();
        }
    }
    
    /** parses gnip:matching_rules */
    class MatchingRulesState extends State {
        protected final List<MatchingRules> rules = new ArrayList<>();

        @Override
        public boolean doProcess(final XMLStreamReader reader) throws XMLStreamException {
            final AtomicBoolean ret = new AtomicBoolean(false);
            if(reader.isStartElement() && GNIP_MATCHING_RULE.equals(reader.getName())) {
                final MatchingRules rule = new MatchingRules();

                for (int i = 0; i < reader.getAttributeCount(); i++) {
                    final String name = reader.getAttributeName(i).getLocalPart();
                    
                    if ("tag".equals(name))
                        rule.setTag(reader.getAttributeValue(i));
                }

                rule.setValue(getText(ret, reader));
                
                rules.add(rule);
            } else {
                onUnhandledEvent(reader);
            } 
            return ret.get();
        }
    }
    
    
    /** parses service:provider */
    class ServiceProvider extends State {
        protected final Provider provider = new Provider();
        
        @Override
        public boolean doProcess(final XMLStreamReader reader) throws XMLStreamException {
            final AtomicBoolean ret = new AtomicBoolean(false);
            if(reader.isStartElement() && ATOM_NAME.equals(reader.getName())) {
                provider.setDisplayName(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_URI.equals(reader.getName())) {
                provider.setLink(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_ICON.equals(reader.getName())) {
                provider.setIcon(getText(ret, reader));
            } else {
                onUnhandledEvent(reader);
            } 
            
            return ret.get();
        }
    }

    /** parse atom:source */
    class SourceState extends State {
        protected final Source source = new Source();
        
        @Override
        public boolean doProcess(final XMLStreamReader reader) throws XMLStreamException, ParseException {
            final AtomicBoolean ret = new AtomicBoolean(false);
            
            if(reader.isStartElement() && ATOM_LINK.equals(reader.getName())) {
                source.getLinks().add(parseLink(reader));
            } else if(reader.isStartElement() && ATOM_TITLE.equals(reader.getName())) {
                source.setTitle(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_UPDATED.equals(reader.getName())) {
                source.setUpdated(ISO8601DateParser.parse(getText(ret, reader)));
            } else if(reader.isStartElement() && GNIP_RULE.equals(reader.getName())) {
                final MatchingRules rule = new MatchingRules();
                rule.setValue(getText(ret, reader));
                source.getGnip().getMatchingRules().add(rule);
            } else {
                onUnhandledEvent(reader);
            }
            
            return ret.get();
        }
    }
    
    /** parse author */
    class AuthorState extends State {
        private final Author author = new Author();
        protected Actor fbActor;
        
        public Author getAuthor() {
            return author;
        }
        
        @Override
        public boolean doProcess(final XMLStreamReader reader) throws XMLStreamException, ParseException {
            final AtomicBoolean ret = new AtomicBoolean(false);
            
            if(reader.isStartElement() && ATOM_NAME.equals(reader.getName())) {
                author.setName(getText(ret, reader));
            } else if(reader.isStartElement() && ATOM_URI.equals(reader.getName())) {
                author.setUri(getText(ret, reader));
            } else if(reader.isStartElement() && FB_ID.equals(reader.getName())) {
                fbActor().setId(getText(ret, reader));
            } else if(reader.isStartElement() && FB_FRIEND_COUNT.equals(reader.getName())) {
                fbActor().setFriendsCount(new BigInteger(getText(ret, reader)));
            } else if(reader.isStartElement() && ATOM_FAN_COUNT.equals(reader.getName())) {
                fbActor().setFollowersCount(new BigInteger(getText(ret, reader)));
            } else if(reader.isStartElement() && ATOM_IS_VERIFIED.equals(reader.getName())) {
                fbActor().setVerified(Integer.valueOf(getText(ret, reader)) != 0);
            } else if(reader.isStartElement() && ATOM_ICON.equals(reader.getName())) {    
                fbActor().setImage(getText(ret, reader));
            } else {
                onUnhandledEvent(reader);
            }
            
            return ret.get();
        }

        private Actor fbActor() {
            if(fbActor == null) {
                fbActor = new Actor();
            }
            return fbActor;
        }
        
        @Override
        public void onDone() {
            super.onDone();
            
            if(fbActor != null) {
                fbActor.setDisplayName(author.getName());
                fbActor.setLink(author.getUri());
            }
            
            super.onDone();
        }
    }
}