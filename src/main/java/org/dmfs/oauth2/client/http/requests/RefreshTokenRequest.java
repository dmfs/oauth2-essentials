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

package org.dmfs.oauth2.client.http.requests;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.entities.XWwwFormUrlEncodedEntity;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.parametersets.Appending;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;

import static org.dmfs.oauth2.client.utils.Parameters.GRANT_TYPE;
import static org.dmfs.oauth2.client.utils.Parameters.REFRESH_TOKEN;
import static org.dmfs.oauth2.client.utils.Parameters.SCOPE;


/**
 * An {@link HttpRequest} to refresh an OAuth2 access token.
 *
 * @author Marten Gajda
 */
public final class RefreshTokenRequest extends AbstractAccessTokenRequest
{
    private final CharSequence mRefreshToken;
    private final OAuth2Scope mScope;


    /**
     * Creates an {@link RefreshTokenRequest} using the given refresh token and scopes.
     *
     * @param refreshToken
     *         The refresh token.
     * @param scope
     *         An {@link OAuth2Scope}.
     */
    public RefreshTokenRequest(CharSequence refreshToken, OAuth2Scope scope)
    {
        super(scope);
        mRefreshToken = refreshToken;
        mScope = scope;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        ParameterList parameters = new BasicParameterList(GRANT_TYPE.parameter("refresh_token"), REFRESH_TOKEN.parameter(mRefreshToken));
        return new XWwwFormUrlEncodedEntity(mScope.isEmpty() ? parameters : new Appending(parameters, SCOPE.parameter(mScope)));
    }
}
