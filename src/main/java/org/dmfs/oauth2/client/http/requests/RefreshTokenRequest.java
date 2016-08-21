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
import org.dmfs.oauth2.client.utils.ImmutableEntry;


/**
 * An {@link HttpRequest} to refresh an OAuth2 access token.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class RefreshTokenRequest extends AbstractAccessTokenRequest
{
    private final ImmutableEntry GRANT_TYPE = new ImmutableEntry("grant_type", "refresh_token");
    private final String mRefreshToken;
    private final OAuth2Scope mScope;


    /**
     * Creates an {@link RefreshTokenRequest} using the given refresh token and scopes.
     *
     * @param refreshToken
     *         The refresh token.
     * @param scope
     *         An {@link OAuth2Scope}.
     */
    public RefreshTokenRequest(String refreshToken, OAuth2Scope scope)
    {
        super(scope);
        mRefreshToken = refreshToken;
        mScope = scope;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return new XWwwFormUrlEncodedEntity(new ImmutableEntry[] {
                GRANT_TYPE, new ImmutableEntry("refresh_token", mRefreshToken),
                new ImmutableEntry("scope", mScope.toString()) });
    }
}
