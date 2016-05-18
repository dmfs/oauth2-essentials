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

package org.dmfs.oauth2.client.tokens;

import java.net.URI;
import java.util.NoSuchElementException;

import org.dmfs.httpclient.converters.PlainStringHeaderConverter;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.parameters.BasicParameterType;
import org.dmfs.httpclient.parameters.ParameterType;
import org.dmfs.httpclient.typedentity.EntityConverter;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.scope.StringScope;
import org.dmfs.oauth2.client.utils.StructuredStringFragment;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;


/**
 * Represents an {@link OAuth2AccessToken} received from an Implicit Grant.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class ImplicitGrantAccessToken implements OAuth2AccessToken
{
	private static final ParameterType<OAuth2Scope> SCOPE = new BasicParameterType<OAuth2Scope>("scope", new EntityConverter<OAuth2Scope>()
	{
		@Override
		public OAuth2Scope value(String valueString)
		{
			return new StringScope(valueString);
		}


		public String valueString(OAuth2Scope value)
		{
			return value.toString();
		};
	});

	private static final ParameterType<String> ACCESS_TOKEN = new BasicParameterType<String>("access_token", PlainStringHeaderConverter.INSTANCE);
	private static final ParameterType<String> TOKEN_TYPE = new BasicParameterType<String>("token_type", PlainStringHeaderConverter.INSTANCE);
	private static final ParameterType<String> STATE = new BasicParameterType<String>("state", PlainStringHeaderConverter.INSTANCE);
	private static final ParameterType<Duration> EXPIRES_IN = new BasicParameterType<Duration>("expires_in", new EntityConverter<Duration>()
	{
		@Override
		public Duration value(String valueString)
		{
			return new Duration(1, 0, Integer.parseInt(valueString));
		}


		@Override
		public String valueString(Duration value)
		{
			return value.toString();
		}
	});

	private final StructuredStringFragment mRedirectUriFragment;
	private final DateTime mIssueDate;
	private final OAuth2Scope mScope;
	private final Duration mDefaultExpiresIn;


	/**
	 * Represents the {@link OAuth2AccessToken} that's contained in the provided redirect URI.
	 * 
	 * @param redirectUri
	 *            The URI the user agent was redirected to.
	 * @param scope
	 *            The scope that has been requested from the server.
	 * @param state
	 *            The state that was provided to the authorization endpoint.
	 * @param defaultExpiresIn
	 *            The default expiration duration to assume if no expiration duration was provided with the response.
	 * @throws ProtocolException
	 *             If the state doesn't match the one returned by the server.
	 */
	public ImplicitGrantAccessToken(URI redirectUri, OAuth2Scope scope, String state, Duration defaultExpiresIn) throws ProtocolException
	{
		mRedirectUriFragment = new StructuredStringFragment(redirectUri.getFragment());
		if (!state.equals(mRedirectUriFragment.firstParameter(STATE, "")))
		{
			throw new ProtocolException("State in redirect uri doesn't match the original state!");
		}

		mIssueDate = DateTime.now();
		mScope = scope;
		mDefaultExpiresIn = defaultExpiresIn;
	}


	@Override
	public String accessToken() throws ProtocolException
	{
		if (!mRedirectUriFragment.hasParameter(ACCESS_TOKEN))
		{
			throw new ProtocolException(String.format("Missing access_token in fragment '%s'", mRedirectUriFragment.toString()));
		}
		return mRedirectUriFragment.firstParameter(ACCESS_TOKEN, "").value();
	}


	@Override
	public String tokenType() throws ProtocolException
	{
		if (!mRedirectUriFragment.hasParameter(TOKEN_TYPE))
		{
			throw new ProtocolException(String.format("Missing token_type in fragment '%s'", mRedirectUriFragment.toString()));
		}
		return mRedirectUriFragment.firstParameter(TOKEN_TYPE, "").value();
	}


	@Override
	public boolean hasRefreshToken()
	{
		// implicit grants don't issue a refresh token
		return false;
	}


	@Override
	public String refreshToken() throws ProtocolException
	{
		throw new NoSuchElementException("Implicit grants do no issue refresh tokens");
	}


	@Override
	public DateTime expiriationDate() throws ProtocolException
	{
		return mIssueDate.addDuration(mRedirectUriFragment.firstParameter(EXPIRES_IN, mDefaultExpiresIn).value());
	}


	@Override
	public OAuth2Scope scope() throws ProtocolException
	{
		return mRedirectUriFragment.firstParameter(SCOPE, mScope).value();
	}
}
