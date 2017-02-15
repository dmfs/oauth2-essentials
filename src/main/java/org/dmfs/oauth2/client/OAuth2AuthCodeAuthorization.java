/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.oauth2.client;

/**
 * Represents an authorization that was issued by an OAuth2 authorization server.
 *
 * @author Marten Gajda
 */
public interface OAuth2AuthCodeAuthorization
{
    /**
     * Returns the actual authorization code.
     *
     * @return
     */
    public CharSequence code();

    /**
     * Returns the scope that this authorization has been granted for.
     *
     * @return
     */
    public OAuth2Scope scope();
}
