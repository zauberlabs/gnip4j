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
package com.zaubersoftware.gnip4j.api.exception;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.model.Rule;

/**
 * Unit test for {@link OffendingRule}
 */
public class OffendingRuleTest {

  private static final String mensaje_error = "your rule is not correctly done";
  private static final String value = "value";
  private static final String tag = "tag";

  private Rule rule;
  private OffendingRule offending;

  @Before
  public final void before() {
    rule = createTestRule(value, tag);
    offending = new OffendingRule(rule, mensaje_error);
  }

  @Test
  public final void getters_should_return_constructor_set_values() {
    assertEquals(rule, offending.getOffendingRule());
    assertEquals(mensaje_error, offending.getErrorMessage());
  }

  @Test
  public final void should_match_with_own_rule() {
    assertTrue(offending.matches(offending.getOffendingRule()));
  }

  @Test
  public final void should_match_with_rule_with_differentTag() {
    assertTrue(offending.matches(createTestRule(value, "another test")));
  }

  @Test
  public final void should_match_with_null_values() {
    offending.getOffendingRule().setValue(null);
    assertTrue(offending.matches(createTestRule(null, "")));
  }

  @Test
  public final void should_not_match_with_null_rule() {
    assertFalse(offending.matches(null));
  }

  /** helper metod to create a rule in one line */
  private Rule createTestRule(final String value, final String tag) {
    final Rule rule = new Rule();
    rule.setTag(tag);
    rule.setValue(value);
    return rule;
  }
}
