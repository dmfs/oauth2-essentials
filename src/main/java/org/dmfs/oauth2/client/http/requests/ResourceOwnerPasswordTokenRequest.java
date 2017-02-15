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
import static org.dmfs.oauth2.client.utils.Parameters.PASSWORD;
import static org.dmfs.oauth2.client.utils.Parameters.SCOPE;
import static org.dmfs.oauth2.client.utils.Parameters.USERNAME;


/**
 * {@link HttpRequest} to retrieve an access token in an OAuth2 Resource Owner Password Credentials Grant.
 * <p>
 * Note that this request must be authenticated using the client credentials.
 *
 * @author Marten Gajda
 */
public final class ResourceOwnerPasswordTokenRequest extends AbstractAccessTokenRequest
{
    private final CharSequence mUsername;
    private final CharSequence mPassword;
    private final OAuth2Scope mScope;


    /**
     * Creates a {@link ResourceOwnerPasswordTokenRequest} with the given scopes.
     *
     * @param scope
     *         An {@link OAuth2Scope}.
     * @param username
     *         The user name of the resource owner.
     * @param password
     *         The password of the resource owner.
     */
    public ResourceOwnerPasswordTokenRequest(OAuth2Scope scope, CharSequence username, CharSequence password)
    {
        super(scope);
        mUsername = username;
        mPassword = password;
        mScope = scope;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        ParameterList parameters = new BasicParameterList(
                GRANT_TYPE.parameter("password"),
                USERNAME.parameter(mUsername),
                PASSWORD.parameter(mPassword));
        return new XWwwFormUrlEncodedEntity(mScope.isEmpty() ? parameters : new Appending(parameters, SCOPE.parameter(mScope)));
    }
}
