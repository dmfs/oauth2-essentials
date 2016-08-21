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

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.entities.XWwwFormUrlEncodedEntity;
import org.dmfs.oauth2.client.scope.EmptyScope;
import org.dmfs.oauth2.client.utils.ImmutableEntry;


/**
 * {@link HttpRequest} to retrieve an access token in an OAuth2 Client Credentials Grant.
 * <p />
 * Note that this request must be authenticated using the client credentials.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class ClientCredentialsTokenRequest extends AbstractAccessTokenRequest
{
	private final ImmutableEntry GRANT_TYPE = new ImmutableEntry("grant_type", "client_credentials");
	private final OAuth2Scope mScope;


	/**
	 * Creates a {@link ClientCredentialsTokenRequest} without a specific scope.
	 */
	public ClientCredentialsTokenRequest()
	{
		this(EmptyScope.INSTANCE);
	}


	/**
	 * Creates a {@link ClientCredentialsTokenRequest} with the given scopes.
	 * 
	 * @param scope
	 *            The {@link OAuth2Scope} to request.
	 */
	public ClientCredentialsTokenRequest(OAuth2Scope scope)
	{
		super(scope);
		mScope = scope;
	}


	@Override
	public HttpRequestEntity requestEntity()
	{
		if (mScope.isEmpty())
		{
			return new XWwwFormUrlEncodedEntity(new ImmutableEntry[] { GRANT_TYPE });
		}
		return new XWwwFormUrlEncodedEntity(new ImmutableEntry[] { GRANT_TYPE, new ImmutableEntry("scope", mScope.toString()) });
	}
}
