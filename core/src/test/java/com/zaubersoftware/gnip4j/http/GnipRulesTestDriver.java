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
package com.zaubersoftware.gnip4j.http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.impl.ImmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.model.Rules;
import com.zaubersoftware.gnip4j.api.model.ruleValidation.RulesValidation;
import com.zaubersoftware.gnip4j.api.support.http.JRERemoteResourceProvider;

public class GnipRulesTestDriver {

	private GnipFacade gnip;
	private GnipFacade gnipV2;
	private String account;
	private String streamName;
	
	@BeforeEach
	public void setUp() {
	    final String username = System.getProperty("gnip.username");
        final String password = System.getProperty("gnip.password");
        account = System.getProperty("gnip.account");
        streamName = System.getProperty("gnip.stream");
        
        if(username == null) {
            throw new IllegalArgumentException("Missing gnip.username");
        }
        if(password == null) {
            throw new IllegalArgumentException("Missing gnip.password");
        }
        if(account == null) {
            throw new IllegalArgumentException("Missing gnip.account");
        }
        if(streamName == null) {
            throw new IllegalArgumentException("Missing gnip.stream");
        }
        
        gnip = new DefaultGnipFacade(new JRERemoteResourceProvider(new ImmutableGnipAuthentication(username, password)));
		gnipV2 = DefaultGnipFacade.createPowertrackV2(new ImmutableGnipAuthentication(username, password));
	}
	
	@Test
	public final void testGetRules() {
        final Rules rules = gnip.getRules(account, streamName);
        
        for (final Rule rule : rules.getRules()) {
        	System.out.println("Found rule " + rule.getValue() + " with tag: " + rule.getTag());
        }
	}
	
	@Test
	public final void testAddRule() {
		final Rule rule = new Rule();
		rule.setValue("#neverevergonnahappen88");
		
		gnip.addRule(account, streamName, rule);
		
		final Rules rules = gnip.getRules(account, streamName);
		boolean ruleAdded = false;
		for (final Rule existingRule : rules.getRules()) {
			if (existingRule.getValue().equals("#neverevergonnahappen88")) {
				System.out.println("Found rule #neverevergonnahappen88, which was just added");
				ruleAdded = true;
				break;
			}
		}
		assertTrue(ruleAdded);
	}

	@Test
	public void testValidateRules() throws Exception {

		final Rules rules = new Rules();
		final Rule r1 = new Rule();
		r1.setTag("ExampleTag,123");
		r1.setValue("abc AND 123");
		rules.getRules().add(r1);

		final Rule r2 = new Rule();
		r2.setTag("AnotherTag,456");
		r2.setValue("(abc OR def) lang:SV");
		rules.getRules().add(r2);

		final RulesValidation validation = gnipV2.validateRules(account, streamName, rules);

		assertNotNull(validation);
		assertEquals((Integer) 1, validation.getSummary().getValid());
		assertEquals((Integer) 1, validation.getSummary().getNotValid());
	}
}
