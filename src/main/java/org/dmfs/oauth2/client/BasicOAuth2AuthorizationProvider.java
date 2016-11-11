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
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.rfc5545.Duration;

import java.io.IOException;
import java.net.URI;


/**
 * Basic implementation of an OAuth2 authorization provider.
 *
 * @author Marten Gajda
 */
public final class BasicOAuth2AuthorizationProvider implements OAuth2AuthorizationProvider
{
    private final URI mAuthorizationEndpoint;
    private final URI mTokenEndpoint;
    private final Duration mDefaultTokenTtl;


    public BasicOAuth2AuthorizationProvider(URI authorizationEndpoint, URI tokenEndpoint, Duration defaultTokenTtl)
    {
        mAuthorizationEndpoint = authorizationEndpoint;
        mTokenEndpoint = tokenEndpoint;
        mDefaultTokenTtl = defaultTokenTtl;
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequest<OAuth2AccessToken> tokenRequest, HttpRequestExecutor executor) throws RedirectionException,
            UnexpectedStatusException, IOException, ProtocolError, ProtocolException
    {
        return executor.execute(mTokenEndpoint, tokenRequest);
    }


    @Override
    public URI authorizationUrl(OAuth2AuthorizationRequest authorizationRequest)
    {
        return authorizationRequest.authorizationUri(mAuthorizationEndpoint);
    }


    @Override
    public Duration defaultTokenTtl()
    {
        return mDefaultTokenTtl;
    }
}
