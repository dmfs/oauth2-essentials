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

import org.dmfs.httpessentials.client.HttpRequest;


/**
 * Interface of an object that knows the client credentials of an OAuth2 client.
 *
 * @author Marten Gajda
 */
public interface OAuth2ClientCredentials
{

    /**
     * Authenticates the given request using the <code>Basic</code> authentication scheme.
     *
     * @param request
     *         The {@link HttpRequest} to authenticate.
     *
     * @return An authenticated {@link HttpRequest}.
     */
    public <T> HttpRequest<T> authenticatedRequest(HttpRequest<T> request);

    /**
     * Returns the client ID of the client.
     *
     * @return The client ID.
     */
    public String clientId();

}
