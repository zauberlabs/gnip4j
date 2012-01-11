/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
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
