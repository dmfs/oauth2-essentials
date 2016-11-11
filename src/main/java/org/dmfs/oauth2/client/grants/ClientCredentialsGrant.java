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

package org.dmfs.oauth2.client.grants;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2Client;
import org.dmfs.oauth2.client.OAuth2Grant;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.requests.ClientCredentialsTokenRequest;

import java.io.IOException;


/**
 * Implements the OAuth2 Client Credentials Grant as specified in <a href="https://tools.ietf.org/html/rfc6749#section-4.4">RFC 6749, Section 4.4</a>.
 *
 * @author Marten Gajda
 */
public final class ClientCredentialsGrant implements OAuth2Grant
{
    private final OAuth2Client mClient;
    private final OAuth2Scope mScope;


    /**
     * Creates a {@link ClientCredentialsGrant} for the given {@link OAuth2Client}.
     *
     * @param client
     *         The {@link OAuth2Client} that is requesting access.
     * @param scope
     *         The requested {@link OAuth2Scope}.
     */
    public ClientCredentialsGrant(OAuth2Client client, OAuth2Scope scope)
    {
        mClient = client;
        mScope = scope;
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
    {
        return mClient.accessToken(new ClientCredentialsTokenRequest(mScope), executor);
    }
}
