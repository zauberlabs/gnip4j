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
package com.zaubersoftware.gnip4j.api.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zaubersoftware.gnip4j.api.model.Rule;

/**
 * Exception class that knows how to parse the 422 message error into a list of
 * {@link OffendingRule}s. This message is not the full json message but the
 * content of the error->message field.
 */
public class GnipUnprocessableEntityException extends GnipException {

  /** <code>serialVersionUID</code> */
  private static final long serialVersionUID = -7879783187295834960L;

  /** pattern that matches the message inside the "meessage */
  private static final Pattern errorMessagePattern = Pattern.compile("Rule '(.+)' is invalid\\.\\s*(.+)");

  private final List<OffendingRule> offendingRules;

  /** */
  public GnipUnprocessableEntityException(final String message, final String serverMessage) {
    super(message + "\n" + serverMessage);

    if (serverMessage == null || serverMessage.isEmpty()) {
      throw new IllegalArgumentException("server message cannot be empty");
    }

    // parse
    offendingRules = new ArrayList<OffendingRule>();
    parseMessage(serverMessage);

    // need to know that at least one offending rule was parsed
    if (offendingRules.isEmpty()) {
      throw new IllegalArgumentException("server message couldn't be parsed into offending rules");
    }
  }

  /**  */
  public GnipUnprocessableEntityException(String message, List<OffendingRule> rules) {
      super(message);
      offendingRules = Objects.requireNonNull(rules);
  }

  public List<OffendingRule> getOffendingRules() {
    return offendingRules;
  }

  /** parses the message using the pattern to obtain the offending rule */
  private void parseMessage(final String serverMessage) {
    String offendingRule = null;
    StringBuilder messageBuilder = new StringBuilder();

    final String[] lines = serverMessage.split("\\n");
    for (int i = 0, length = lines.length; i < length; i++) {
      final String line = lines[i];
      if (line != null && !line.isEmpty()) {
        final Matcher matcher = errorMessagePattern.matcher(line);
        if (matcher.matches()) {
          if (offendingRule != null) {
            offendingRules.add(createOffendingRule(offendingRule, messageBuilder.toString()));
            messageBuilder = new StringBuilder();
          }
          messageBuilder.append(matcher.group(2)).append('\n');
          offendingRule = matcher.group(1);
        } else {
          messageBuilder.append(line).append('\n');
        }
      }
    }
    if (offendingRule != null) {
      offendingRules.add(createOffendingRule(offendingRule, messageBuilder.toString()));
    }
  }

  /** helper method to create the offending rule */
  private OffendingRule createOffendingRule(final String rule, final String errorMessage) {
    final Rule offending = new Rule();
    offending.setValue(rule);
    return new OffendingRule(offending, errorMessage);
  }
}
