/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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

import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;

import org.dmfs.httpclient.HttpRequest;
import org.dmfs.httpclient.HttpRequestExecutor;
import org.dmfs.httpclient.exceptions.ProtocolError;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.exceptions.RedirectionException;
import org.dmfs.httpclient.exceptions.UnexpectedStatusException;
import org.dmfs.rfc5545.Duration;


/**
 * Basic implementation of an {@link OAuth2Client}.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class BasicOAuth2Client implements OAuth2Client
{
	private final static String STATE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-";

	private final OAuth2AuthorizationProvider mProvider;
	private final OAuth2ClientCredentials mCredentials;
	private final URI mRedirectUri;


	public BasicOAuth2Client(OAuth2AuthorizationProvider provider, OAuth2ClientCredentials credentials, URI redirectUri)
	{
		mProvider = provider;
		mCredentials = credentials;
		mRedirectUri = redirectUri;
	}


	@Override
	public OAuth2AccessToken accessToken(HttpRequest<OAuth2AccessToken> tokenRequest, HttpRequestExecutor executor) throws RedirectionException,
		UnexpectedStatusException, IOException, ProtocolError, ProtocolException
	{
		return mProvider.accessToken(mCredentials.authenticatedRequest(tokenRequest), executor);
	}


	@Override
	public URI authorizationUrl(OAuth2AuthorizationRequest authorizationRequest)
	{
		return mProvider.authorizationUrl(authorizationRequest.withClientId(mCredentials.clientId()).withRedirectUri(mRedirectUri));
	}


	@Override
	public URI redirectUri()
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
	 * <p />
	 * Note: Client on platforms with insecure {@link SecureRandom} implementations should decorate this implementation and return a secure random string.
	 */
	@Override
	public String generatedRandomState()
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
