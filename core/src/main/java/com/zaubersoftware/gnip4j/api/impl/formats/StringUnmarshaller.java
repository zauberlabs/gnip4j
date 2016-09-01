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

/**
 * An unmarshaller that simply passes through the source value. Useful in 
 * cases where no parsing is desired coming off the stream. For example, 
 * pushing activities into a queue for asynchronous processing.  
 */
public class StringUnmarshaller implements Unmarshaller<String> {

	@Override
	public String unmarshall(final String s) {
		return s;
	}
}
