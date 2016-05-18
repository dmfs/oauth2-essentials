/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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
 * 
 */

package org.dmfs.oauth2.client.http.requests;

import java.net.URI;

import org.dmfs.httpclient.HttpRequestEntity;
import org.dmfs.oauth2.client.OAuth2AuthCodeAuthorization;
import org.dmfs.oauth2.client.http.entities.XWwwFormUrlEncodedEntity;
import org.dmfs.oauth2.client.utils.ImmutableEntry;


/**
 * Represents the requests to retrieve the token in an Authorization Code Grant.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class AuthorizationCodeTokenRequest extends AbstractAccessTokenRequest
{
	private final ImmutableEntry GRANT_TYPE = new ImmutableEntry("grant_type", "authorization_code");
	private final OAuth2AuthCodeAuthorization mAuthorization;
	private final URI mRedirectUri;


	/**
	 * Creates a token request for an authorization code flow.
	 * 
	 * @param authorization
	 *            The authorization code as returned by the authorization endpoint.
	 * @param redirecUri
	 *            The redirect URI.
	 */
	public AuthorizationCodeTokenRequest(OAuth2AuthCodeAuthorization authorization, URI redirecUri)
	{
		super(authorization.scope());
		mAuthorization = authorization;
		mRedirectUri = redirecUri;
	}


	@Override
	public HttpRequestEntity requestEntity()
	{
		return new XWwwFormUrlEncodedEntity(new ImmutableEntry[] { GRANT_TYPE, new ImmutableEntry("code", mAuthorization.code()),
			new ImmutableEntry("redirect_uri", mRedirectUri.toASCIIString()) });
	}
}
