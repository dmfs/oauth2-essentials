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

package org.dmfs.oauth2.client.tokens;

import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.jems.optional.Optional;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.adapters.OptionalParameter;
import org.dmfs.rfc3986.parameters.adapters.TextParameter;
import org.dmfs.rfc3986.parameters.adapters.XwfueParameterList;
import org.dmfs.rfc3986.parameters.parametertypes.BasicParameterType;
import org.dmfs.rfc3986.parameters.valuetypes.TextValueType;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;

import java.util.NoSuchElementException;

import static org.dmfs.oauth2.client.utils.Parameters.ACCESS_TOKEN;
import static org.dmfs.oauth2.client.utils.Parameters.EXPIRES_IN;
import static org.dmfs.oauth2.client.utils.Parameters.SCOPE;
import static org.dmfs.oauth2.client.utils.Parameters.STATE;
import static org.dmfs.oauth2.client.utils.Parameters.TOKEN_TYPE;


/**
 * Represents an {@link OAuth2AccessToken} received from an Implicit Grant.
 *
 * @author Marten Gajda
 */
public final class ImplicitGrantAccessToken implements OAuth2AccessToken
{
    private final Uri mRedirectUri;
    private final ParameterList mRedirectUriParameters;
    private final DateTime mIssueDate;
    private final OAuth2Scope mScope;
    private final Duration mDefaultExpiresIn;


    /**
     * Represents the {@link OAuth2AccessToken} that's contained in the provided redirect URI.
     *
     * @param redirectUri
     *         The URI the user agent was redirected to.
     * @param scope
     *         The scope that has been requested from the server.
     * @param state
     *         The state that was provided to the authorization endpoint.
     * @param defaultExpiresIn
     *         The default expiration duration to assume if no expiration duration was provided with the response.
     *
     * @throws ProtocolException
     *         If the state doesn't match the one returned by the server.
     */
    public ImplicitGrantAccessToken(Uri redirectUri, OAuth2Scope scope, CharSequence state, Duration defaultExpiresIn) throws ProtocolException
    {
        mRedirectUri = redirectUri;
        mRedirectUriParameters = new XwfueParameterList(redirectUri.fragment().value());
        if (!state.toString().equals(new TextParameter(STATE, mRedirectUriParameters).toString()))
        {
            throw new ProtocolException("State in redirect uri doesn't match the original state!");
        }

        mIssueDate = DateTime.now();
        mScope = scope;
        mDefaultExpiresIn = defaultExpiresIn;
    }


    @Override
    public CharSequence accessToken() throws ProtocolException
    {
        OptionalParameter<CharSequence> accessToken = new OptionalParameter<>(ACCESS_TOKEN, mRedirectUriParameters);
        if (!accessToken.isPresent())
        {
            throw new ProtocolException(String.format("Missing access_token in fragment '%s'", mRedirectUri.fragment().value()));
        }
        return accessToken.value("");
    }


    @Override
    public CharSequence tokenType() throws ProtocolException
    {
        OptionalParameter<CharSequence> tokenType = new OptionalParameter<>(TOKEN_TYPE, mRedirectUriParameters);
        if (!tokenType.isPresent())
        {
            throw new ProtocolException(String.format("Missing token_type in fragment '%s'", mRedirectUri.fragment().value()));
        }
        return tokenType.value("");
    }


    @Override
    public boolean hasRefreshToken()
    {
        // implicit grants don't issue a refresh token
        return false;
    }


    @Override
    public CharSequence refreshToken() throws ProtocolException
    {
        throw new NoSuchElementException("Implicit grants do no issue refresh tokens");
    }


    @Override
    public DateTime expirationDate() throws ProtocolException
    {
        return mIssueDate.addDuration(new OptionalParameter<>(EXPIRES_IN, mRedirectUriParameters).value(mDefaultExpiresIn));
    }


    @Override
    public OAuth2Scope scope() throws ProtocolException
    {
        return new OptionalParameter<>(SCOPE, mRedirectUriParameters).value(mScope);
    }


    @Override
    public Optional<CharSequence> extraParameter(final String parameterName)
    {
        return new OptionalParameter<>(new BasicParameterType<>(parameterName, TextValueType.INSTANCE), mRedirectUriParameters);
    }
}
