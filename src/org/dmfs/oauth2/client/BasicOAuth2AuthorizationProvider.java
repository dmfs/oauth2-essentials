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

import org.dmfs.httpclient.HttpRequest;
import org.dmfs.httpclient.HttpRequestExecutor;
import org.dmfs.httpclient.callbacks.FollowSecureRedirectCallback;
import org.dmfs.httpclient.exceptions.ProtocolError;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.exceptions.RedirectionException;
import org.dmfs.httpclient.exceptions.UnexpectedStatusException;
import org.dmfs.rfc5545.Duration;


/**
 * Basic implementation of an OAuth2 authorization provider.
 * 
 * @author Marten Gajda <marten@dmfs.org>
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
		return executor.execute(mTokenEndpoint, tokenRequest, FollowSecureRedirectCallback.getInstance());
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
