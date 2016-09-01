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

import org.codehaus.jackson.annotate.JsonProperty;

import com.zaubersoftware.gnip4j.api.model.Rule;

/**
 * object to contain an offending rule. This is a rule that was reported by the
 * server as sintactically or semantically incorrect. It stores the rule and
 * message error sent by the server. As the server does not send back the tag we
 * are only storing the name that's way we added a {@link #matches(Rule)} method
 * that test "equality" only checking the value of the Rule.
 */

public class OffendingRule {
  private final Rule offendingRule;
  private final String errorMessage;

  /**
   * constructor that sets the values.
   *
   * @throws IllegalArgumentException
   *           if any of the parameters is empty or null.
   */
    public OffendingRule(@JsonProperty("rule") final Rule offendingRule,
            @JsonProperty("message") final String errorMessage) {
    super();

    if (offendingRule == null) {
      throw new IllegalArgumentException("null offending rule");
    }
    if (errorMessage == null || errorMessage.isEmpty()) {
      throw new IllegalArgumentException("empty error message");
    }

    this.offendingRule = offendingRule;
    this.errorMessage = errorMessage;
  }

  public Rule getOffendingRule() {
    return offendingRule;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * test the equality of the parameter against the rule only checking the
   * values and ignoring the tags
   */
  public boolean matches(final Rule rule) {
    return rule != null
        && ((offendingRule.getValue() == null && rule.getValue() == null) || (offendingRule.getValue()
            .equals(rule.getValue())));
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
    result = prime * result + ((offendingRule == null) ? 0 : offendingRule.hashCode());
    return result;
  }

  @Override
  public final boolean equals(final Object obj) {
    if (this == obj) {
      return Boolean.TRUE;
    }
    if (obj == null) {
      return Boolean.FALSE;
    }
    if (getClass() != obj.getClass()) {
      return Boolean.FALSE;
    }
    final OffendingRule other = (OffendingRule) obj;
    if (errorMessage == null) {
      if (other.errorMessage != null) {
        return Boolean.FALSE;
      }
    } else if (!errorMessage.equals(other.errorMessage)) {
      return Boolean.FALSE;
    }
    if (offendingRule == null) {
      if (other.offendingRule != null) {
        return Boolean.FALSE;
      }
    } else if (!offendingRule.equals(other.offendingRule)) {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  @Override
  public final String toString() {
    final StringBuilder sb = new StringBuilder();
    if (offendingRule != null) {
      sb.append(offendingRule.toString());
    }
    if (sb.length() != 0) {
      sb.append(' ');
    }
    sb.append(errorMessage);
    return sb.toString();
  }
}