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
import org.dmfs.jems.function.Function;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.optional.elementary.NullSafe;
import org.dmfs.jems.single.combined.Backed;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.scope.StringScope;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Duration;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.NoSuchElementException;


/**
 * An access token loaded from an {@link JSONObject} instance.
 *
 * @author Marten Gajda
 */
public final class JsonAccessToken implements OAuth2AccessToken
{
    private final JSONObject mTokenResponse;
    private final OAuth2Scope mScope;
    private final DateTime mIssueDate;


    public JsonAccessToken(JSONObject tokenResponse, OAuth2Scope scope)
    {
        mScope = scope;
        mTokenResponse = tokenResponse;
        mIssueDate = DateTime.now();
    }


    @Override
    public CharSequence accessToken() throws ProtocolException
    {
        try
        {
            return mTokenResponse.getString("access_token");
        }
        catch (JSONException e)
        {
            throw new ProtocolException("Can't read access_token from token response", e);
        }
    }


    @Override
    public CharSequence tokenType() throws ProtocolException
    {
        try
        {
            return mTokenResponse.getString("token_type");
        }
        catch (JSONException e)
        {
            throw new ProtocolException("Can't read token_type from token response", e);
        }
    }


    @Override
    public boolean hasRefreshToken()
    {
        return mTokenResponse.optString("refresh_token", null) != null;
    }


    @Override
    public CharSequence refreshToken() throws ProtocolException
    {
        if (!hasRefreshToken())
        {
            throw new NoSuchElementException("No refresh token found");
        }

        try
        {
            return mTokenResponse.getString("refresh_token");
        }
        catch (JSONException e)
        {
            throw new ProtocolException("Can't read refresh_token from token response", e);
        }
    }


    @Override
    public DateTime expirationDate() throws ProtocolException
    {
        try
        {
            return mIssueDate.addDuration(new Duration(1, 0, mTokenResponse.getInt("expires_in")));
        }
        catch (JSONException e)
        {
            throw new ProtocolException("Can't read expires_in from token response", e);
        }
    }


    @Override
    public OAuth2Scope scope() throws ProtocolException
    {
        return new Backed<>(new Mapped<>(new OAuth2ScopeFunction(), new NullSafe<>(mTokenResponse.optString("scope", null))), mScope).value();
    }


    @Override
    public Optional<CharSequence> extraParameter(final String parameterName)
    {
        return new NullSafe<CharSequence>(mTokenResponse.optString(parameterName, null));
    }


    /**
     * A {@link Function} which converts a String into an {@link OAuth2Scope}.
     */
    private static class OAuth2ScopeFunction implements Function<String, OAuth2Scope>
    {
        @Override
        public OAuth2Scope value(String argument)
        {
            return new StringScope(argument);
        }
    }
}
