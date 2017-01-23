/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.oauth2.client.pkce;

import org.dmfs.httpessentials.types.Token;


/**
 * The interface of a PKCE Code Challenge as per <a href="https://tools.ietf.org/html/rfc7636#section-4.2">RFC 7636, section 4.2</a>
 *
 * @author Marten Gajda
 */
public interface PkceCodeChallenge
{
    /**
     * Returns a {@link Token} that identifies the method this code challenge uses.
     *
     * @return A {@link Token} containing the method name.
     */
    Token method();

    /**
     * Returns the value of the code challenge.
     *
     * @return
     */
    CharSequence challenge();
}
