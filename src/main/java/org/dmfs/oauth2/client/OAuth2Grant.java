/*
 * Copyright 2016 dmfs GmbH
 *
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

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;


/**
 * Interface of an object that knows how to get an access token using a specific OAuth2 grant type.
 *
 * @author Marten Gajda
 */
public interface OAuth2Grant
{
    /**
     * Executes the grant and returns an {@link OAuth2AccessToken}.
     *
     * @param executor
     *         An {@link HttpRequestExecutor} to execute the request.
     *
     * @return An {@link OAuth2AccessToken}.
     *
     * @throws IOException
     *         If the request failed on a low level.
     * @throws ProtocolError
     *         If the server responded with an error that's part of the protocol.
     * @throws ProtocolException
     *         If the server returned an invalid response.
     */
    public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException;
}
