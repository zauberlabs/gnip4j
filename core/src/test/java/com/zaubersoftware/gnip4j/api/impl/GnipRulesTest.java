package com.zaubersoftware.gnip4j.api.impl;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.model.Rules;
import com.zaubersoftware.gnip4j.api.support.http.JRERemoteResourceProvider;

public class GnipRulesTest {

	@Test
	public final void testGetRules() {
		final String username = System.getProperty("gnip.username");
        final String password = System.getProperty("gnip.password");
        final String domain = System.getProperty("gnip.domain");
        
        if(username == null) {
            throw new IllegalArgumentException("Missing gnip.username");
        }
        if(password == null) {
            throw new IllegalArgumentException("Missing gnip.password");
        }
        if(domain == null) {
            throw new IllegalArgumentException("Missing gnip.domain");
        }
        
        final GnipFacade gnip = new DefaultGnipFacade(
                new JRERemoteResourceProvider(
                        new ImmutableGnipAuthentication(username, password)));
        Rules rules = gnip.getRules(domain, 1);
        
        for (Rule rule : rules.getRules()) {
        	System.out.println("Found rule " + rule.getValue() + " with tag: " + rule.getTag());
        }
	}
}
