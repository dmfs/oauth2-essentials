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
import org.dmfs.httpessentials.entities.XWwwFormUrlEncodedEntity;
import org.dmfs.iterables.elementary.PresentValues;
import org.dmfs.jems.iterable.composite.Joined;
import org.dmfs.jems.iterable.elementary.Seq;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.requests.parameters.GrantTypeParam;
import org.dmfs.oauth2.client.http.requests.parameters.OptionalScopeParam;
import org.dmfs.oauth2.client.http.requests.parameters.RefreshTokenParam;


/**
 * An {@link HttpRequest} to refresh an OAuth2 access token.
 *
 * @author Marten Gajda
 */
public final class RefreshTokenRequest extends AbstractAccessTokenRequest
{
    /**
     * Creates an {@link RefreshTokenRequest} using the given refresh token and scopes.
     *
     * @param refreshToken
     *         The refresh token.
     * @param scope
     *         An {@link OAuth2Scope}.
     *
     * @deprecated in favour of {@link #RefreshTokenRequest(OAuth2Scope, CharSequence)}
     */
    @Deprecated
    public RefreshTokenRequest(CharSequence refreshToken, OAuth2Scope scope)
    {
        this(scope, refreshToken);
    }


    /**
     * Creates an {@link RefreshTokenRequest} using the given refresh token and scopes.
     *
     * @param scope
     *         An {@link OAuth2Scope}.
     * @param refreshToken
     *         The refresh token.
     */
    public RefreshTokenRequest(OAuth2Scope scope, CharSequence refreshToken)
    {
        super(scope,
                new XWwwFormUrlEncodedEntity(
                        new Joined<>(
                                new Seq<>(
                                        new GrantTypeParam("refresh_token"),
                                        new RefreshTokenParam(refreshToken)),
                                new PresentValues<>(new OptionalScopeParam(scope)))));
    }
}
