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

package org.dmfs.oauth2.providers;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.oauth2.client.BasicOAuth2AuthorizationProvider;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2AuthorizationProvider;
import org.dmfs.oauth2.client.OAuth2AuthorizationRequest;
import org.dmfs.rfc5545.Duration;

import java.io.IOException;
import java.net.URI;


/**
 * The Google OAuth2 authorization provider. A list of valid scopes is available at <a href="https://developers.google.com/identity/protocols/googlescopes">OAuth
 * 2.0 Scopes for Google APIs</a>.
 *
 * @author Marten Gajda
 * @see <a href="https://developers.google.com/identity/protocols/OAuth2InstalledApp">https://developers.google.com/identity/protocols/OAuth2InstalledApp</a>
 */
public final class GoogleAuthorizationProvider implements OAuth2AuthorizationProvider
{
    private final static String GOOGLE_AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private final static String GOOGLE_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";

    private final OAuth2AuthorizationProvider delegate;


    public GoogleAuthorizationProvider()
    {
        delegate = new BasicOAuth2AuthorizationProvider(URI.create(GOOGLE_AUTH_ENDPOINT),
                URI.create(GOOGLE_TOKEN_ENDPOINT), new Duration(1, 0, 3600) /* 1 hour */);
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequest<OAuth2AccessToken> tokenRequest, HttpRequestExecutor executor) throws RedirectionException, UnexpectedStatusException, IOException, ProtocolError, ProtocolException
    {
        return delegate.accessToken(tokenRequest, executor);
    }


    @Override
    public URI authorizationUrl(OAuth2AuthorizationRequest authorizationRequest)
    {
        return delegate.authorizationUrl(authorizationRequest);
    }


    @Override
    public Duration defaultTokenTtl()
    {
        return delegate.defaultTokenTtl();
    }
}
