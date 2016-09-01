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
package com.zaubersoftware.gnip4j.api.support.logging.slf4j;

import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;
import com.zaubersoftware.gnip4j.api.support.logging.spi.LoggerFactory;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public class SLF4JTargetLoggerFactory implements LoggerFactory {

    @Override
    public final Logger getLogger(final Class<?> logger) {
        return new SLF4JLogger(org.slf4j.LoggerFactory.getLogger(logger));
    }

}
