package com.zaubersoftware.gnip4j.api.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.impl.ImmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.model.Rules;
import com.zaubersoftware.gnip4j.http.HttpClientRemoteResourceProvider;

public class GnipRulesTestDriver {

	private GnipFacade gnip;
	private String domain;
	
	@Before
	public void setUp() {
		final String username = System.getProperty("gnip.username");
        final String password = System.getProperty("gnip.password");
        domain = System.getProperty("gnip.domain");
        
        if(username == null) {
            throw new IllegalArgumentException("Missing gnip.username");
        }
        if(password == null) {
            throw new IllegalArgumentException("Missing gnip.password");
        }
        if(domain == null) {
            throw new IllegalArgumentException("Missing gnip.domain");
        }
        
        gnip = new DefaultGnipFacade(new HttpClientRemoteResourceProvider(
        		new ImmutableGnipAuthentication(username, password)));
	}
	
	@Test
	public final void testGetRules() {
        final Rules rules = gnip.getRules(domain, 1);
        
        for (final Rule rule : rules.getRules()) {
        	System.out.println("Found rule " + rule.getValue() + " with tag: " + rule.getTag());
        }
	}
	
	@Test
	public final void testAddRule() {
		final Rule rule = new Rule();
		rule.setValue("#neverevergonnahappen88");
		
		gnip.addRule(domain, 1, rule);
		
		final Rules rules = gnip.getRules(domain, 1);
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
}
