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
import org.dmfs.httpessentials.executors.useragent.Branded;
import org.dmfs.httpessentials.types.Product;
import org.dmfs.httpessentials.types.VersionedProduct;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc5545.Duration;

import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;


/**
 * Basic implementation of an {@link OAuth2Client}.
 *
 * @author Marten Gajda
 */
public final class BasicOAuth2Client implements OAuth2Client
{
    private final static String STATE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-";
    private final static Product PRODUCT = new VersionedProduct(BuildConfig.NAME, BuildConfig.VERSION);

    private final OAuth2AuthorizationProvider mProvider;
    private final OAuth2ClientCredentials mCredentials;
    private final Uri mRedirectUri;


    public BasicOAuth2Client(OAuth2AuthorizationProvider provider, OAuth2ClientCredentials credentials, URI redirectUri)
    {
        this(provider, credentials, new LazyUri(new Precoded(redirectUri == null ? "" : redirectUri.toString())));
    }


    public BasicOAuth2Client(OAuth2AuthorizationProvider provider, OAuth2ClientCredentials credentials, Uri redirectUri)
    {
        mProvider = provider;
        mCredentials = credentials;
        mRedirectUri = redirectUri;
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequest<OAuth2AccessToken> tokenRequest, HttpRequestExecutor executor) throws RedirectionException,
            UnexpectedStatusException, IOException, ProtocolError, ProtocolException
    {
        return mProvider.accessToken(mCredentials.authenticatedRequest(tokenRequest), new Branded(executor, PRODUCT));
    }


    @Override
    public URI authorizationUrl(OAuth2AuthorizationRequest authorizationRequest)
    {
        return mProvider.authorizationUrl(authorizationRequest.withClientId(mCredentials.clientId()).withRedirectUri(mRedirectUri));
    }


    @Override
    public Uri redirectUri()
    {
        return mRedirectUri;
    }


    @Override
    public Duration defaultTokenTtl()
    {
        return mProvider.defaultTokenTtl();
    }


    /**
     * {@inheritDoc}
     * <p>
     * Note: Client on platforms with insecure {@link SecureRandom} implementations should decorate this implementation and return a secure random string.
     */
    @Override
    public CharSequence randomChars()
    {
        StringBuilder result = new StringBuilder(64);
        SecureRandom random = new SecureRandom();
        for (int i = 0, count = result.capacity(); i < count; ++i)
        {
            result.append(STATE_CHARS.charAt(random.nextInt(STATE_CHARS.length())));
        }
        return result.toString();
    }
}
