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

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.support.http.Errors;
import com.zaubersoftware.gnip4j.api.support.http.Errors.Error;
import com.zaubersoftware.gnip4j.api.support.http.Errors.RuleErrorDetail;

/**
 * Exception class that knows how to parse the 422 message error into a list of
 * {@link OffendingRule}s. This message is not the full json message but the
 * content of the {@literal error->message} field.
 */
public class GnipUnprocessableEntityException extends GnipException {

  /** <code>serialVersionUID</code> */
  private static final long serialVersionUID = -7879783187295834960L;

  /** pattern that matches the message inside the "meessage */
  private static final Pattern errorMessagePattern = Pattern.compile("Rule '(.+)' is invalid\\.\\s*(.+)");

  private final List<OffendingRule> offendingRules;

  public GnipUnprocessableEntityException(final String message, final String serverMessage) {
    super(message + "\n" + serverMessage);

    if (serverMessage == null || serverMessage.isEmpty()) {
      throw new IllegalArgumentException("server message cannot be empty");
    }

    // parse
    offendingRules = parseMessage(serverMessage);

    // need to know that at least one offending rule was parsed
    if (offendingRules.isEmpty()) {
      throw new IllegalArgumentException("server message couldn't be parsed into offending rules");
    }
  }

  private static String toMessage(final String message, final Errors errors) {
      String ret;
      
      if(message == null && errors == null) {
          ret = "Error description was not provided";
      } else if(message != null && errors == null) {
          ret = message;
      } else {
          ret = message + "\n" +  errors.toHumanMessage();
      }
      
      return ret;
  }
  public  GnipUnprocessableEntityException(final String message, final Errors errors) {
      super(toMessage(message, errors));

      // v2 circa Jan 2016 
      final Error error = errors.getError();
      if(error != null && error.getRules() != null) {
          offendingRules = Objects.requireNonNull(error.getRules());
      } else {
          // v2 circa Jun 2016
          final List<RuleErrorDetail> details = errors.getDetail();
          if(details == null) {
              final String msg = errors.toHumanMessage();
              if(msg == null) {
                  offendingRules = new LinkedList<OffendingRule>();
              } else {
                  offendingRules = parseMessage(msg);
              }
          } else {
              offendingRules = new LinkedList<OffendingRule>();
              
              for(final RuleErrorDetail detail : details) {
                  if(detail != null) {
                      final OffendingRule offendingRule = detail.toOffendingRule();
                      if(offendingRule != null) {
                          offendingRules.add(offendingRule);
                      }
                      
                  }
              }
          }
      }
  }

  public List<OffendingRule> getOffendingRules() {
    return offendingRules;
  }

  /** parses the message using the pattern to obtain the offending rule */
  private static List<OffendingRule>  parseMessage(final String serverMessage) {
    String offendingRule = null;
    StringBuilder messageBuilder = new StringBuilder();
    final List<OffendingRule> offendingRules = new LinkedList<OffendingRule>();

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
    
    return offendingRules;
  }

  /** helper method to create the offending rule */
  private static OffendingRule createOffendingRule(final String rule, final String errorMessage) {
    final Rule offending = new Rule();
    offending.setValue(rule);
    return new OffendingRule(offending, errorMessage);
  }
}
