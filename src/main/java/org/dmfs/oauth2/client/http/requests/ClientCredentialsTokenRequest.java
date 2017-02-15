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
import org.dmfs.oauth2.client.scope.EmptyScope;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.parametersets.Appending;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;

import static org.dmfs.oauth2.client.utils.Parameters.GRANT_TYPE;
import static org.dmfs.oauth2.client.utils.Parameters.SCOPE;


/**
 * {@link HttpRequest} to retrieve an access token in an OAuth2 Client Credentials Grant.
 * <p>
 * Note that this request must be authenticated using the client credentials.
 *
 * @author Marten Gajda
 */
public final class ClientCredentialsTokenRequest extends AbstractAccessTokenRequest
{
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
     *         The {@link OAuth2Scope} to request.
     */
    public ClientCredentialsTokenRequest(OAuth2Scope scope)
    {
        super(scope);
        mScope = scope;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        ParameterList parameters = new BasicParameterList(GRANT_TYPE.parameter("client_credentials"));
        return new XWwwFormUrlEncodedEntity(mScope.isEmpty() ? parameters : new Appending(parameters, SCOPE.parameter(mScope)));
    }
}
