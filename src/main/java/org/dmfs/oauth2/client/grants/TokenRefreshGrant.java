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
import org.dmfs.oauth2.client.http.requests.RefreshTokenRequest;

import java.io.IOException;


/**
 * Implements the OAuth2 Client Credentials Grant as specified in <a href="https://tools.ietf.org/html/rfc6749#section-6">RFC 6749, Section 6</a>.
 *
 * @author Marten Gajda
 */
public final class TokenRefreshGrant implements OAuth2Grant
{
    private final OAuth2Client mClient;
    private final OAuth2AccessToken mAccessToken;


    /**
     * Creates an {@link OAuth2Grant} to refresh an {@link OAuth2AccessToken}.
     *
     * @param client
     *         The {@link OAuth2Client}.
     * @param accessToken
     *         An {@link OAuth2AccessToken} that has a refresh token.
     *
     * @throws ProtocolException
     */
    public TokenRefreshGrant(OAuth2Client client, OAuth2AccessToken accessToken) throws ProtocolException
    {
        mClient = client;
        mAccessToken = accessToken;
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
    {
        return mClient.accessToken(new RefreshTokenRequest(mAccessToken.scope(), mAccessToken.refreshToken()), executor);
    }
}
