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

import org.dmfs.httpclient.HttpRequest;
import org.dmfs.httpclient.HttpRequestEntity;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.entities.XWwwFormUrlEncodedEntity;
import org.dmfs.oauth2.client.utils.ImmutableEntry;


/**
 * {@link HttpRequest} to retrieve an access token in an OAuth2 Resource Owner Password Credentials Grant.
 * <p />
 * Note that this request must be authenticated using the client credentials.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class ResourceOwnerPasswordTokenRequest extends AbstractAccessTokenRequest
{
	private final ImmutableEntry GRANT_TYPE = new ImmutableEntry("grant_type", "password");
	private final String mUsername;
	private final String mPassword;
	private final OAuth2Scope mScope;


	/**
	 * Creates a {@link ResourceOwnerPasswordTokenRequest} with the given scopes.
	 * 
	 * @param scope
	 *            An {@link OAuth2Scope}.
	 * @param username
	 *            The user name of the resource owner.
	 * @param password
	 *            The password of the resource owner.
	 */
	public ResourceOwnerPasswordTokenRequest(OAuth2Scope scope, String username, String password)
	{
		super(scope);
		mUsername = username;
		mPassword = password;
		mScope = scope;
	}


	@Override
	public HttpRequestEntity requestEntity()
	{
		return new XWwwFormUrlEncodedEntity(new ImmutableEntry[] { GRANT_TYPE, new ImmutableEntry("username", mUsername),
			new ImmutableEntry("password", mPassword), new ImmutableEntry("scope", mScope.toString()) });
	}
}
