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

package org.dmfs.oauth2.client;

import org.dmfs.oauth2.client.pkce.PkceCodeChallenge;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.XWwwFormUrlEncoded;
import org.dmfs.rfc3986.parameters.FluentParameterList;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;
import org.dmfs.rfc3986.parameters.parametersets.Fluent;
import org.dmfs.rfc3986.parameters.parametersets.Replacing;

import java.net.URI;
import java.net.URISyntaxException;

import static org.dmfs.oauth2.client.utils.Parameters.CLIENT_ID;
import static org.dmfs.oauth2.client.utils.Parameters.CODE_CHALLENGE;
import static org.dmfs.oauth2.client.utils.Parameters.CODE_CHALLENGE_METHOD;
import static org.dmfs.oauth2.client.utils.Parameters.REDIRECT_URI;
import static org.dmfs.oauth2.client.utils.Parameters.RESPONSE_TYPE;
import static org.dmfs.oauth2.client.utils.Parameters.SCOPE;
import static org.dmfs.oauth2.client.utils.Parameters.STATE;


/**
 * A basic {@link OAuth2AuthorizationRequest} implementation.
 * <p>
 * Note: Usually you don't need to instantiate this directly.
 *
 * @author Marten Gajda
 */
public final class BasicOAuth2AuthorizationRequest implements OAuth2AuthorizationRequest
{
    private final FluentParameterList mParameters;


    public BasicOAuth2AuthorizationRequest(String responseType, CharSequence state)
    {
        this(new Fluent(new BasicParameterList(RESPONSE_TYPE.parameter(responseType), STATE.parameter(state))));
    }


    public BasicOAuth2AuthorizationRequest(String responseType, CharSequence state, ParameterList customParameters)
    {
        this(new Fluent(new Replacing(customParameters, RESPONSE_TYPE.parameter(responseType), STATE.parameter(state))));
    }


    public BasicOAuth2AuthorizationRequest(String responseType, OAuth2Scope scope, CharSequence state)
    {
        this(new Fluent(new BasicParameterList(RESPONSE_TYPE.parameter(responseType), SCOPE.parameter(scope), STATE.parameter(state))));
    }


    public BasicOAuth2AuthorizationRequest(String responseType, OAuth2Scope scope, CharSequence state, ParameterList customParameters)
    {
        this(new Fluent(new Replacing(customParameters, RESPONSE_TYPE.parameter(responseType), SCOPE.parameter(scope), STATE.parameter(state))));
    }


    private BasicOAuth2AuthorizationRequest(FluentParameterList parameters)
    {
        mParameters = parameters;
    }


    @Override
    public OAuth2AuthorizationRequest withClientId(String clientId)
    {
        return new BasicOAuth2AuthorizationRequest(mParameters.ratherWith(CLIENT_ID.parameter(clientId)));
    }


    @Override
    public OAuth2AuthorizationRequest withRedirectUri(Uri redirectUri)
    {
        return new BasicOAuth2AuthorizationRequest(mParameters.ratherWith(REDIRECT_URI.parameter(redirectUri)));
    }


    @Override
    public OAuth2AuthorizationRequest withCodeChallenge(PkceCodeChallenge codeChallenge)
    {
        return new BasicOAuth2AuthorizationRequest(mParameters.ratherWith(
                CODE_CHALLENGE_METHOD.parameter(codeChallenge.method()),
                CODE_CHALLENGE.parameter(codeChallenge.challenge())));
    }


    @Override
    public URI authorizationUri(URI authorizationEndpoint)
    {
        // TODO: refuse to return a URI without a client id.
        try
        {
            return URI.create(new URI(authorizationEndpoint.getScheme(), authorizationEndpoint.getAuthority(),
                    authorizationEndpoint.getPath(), null, null) + "?" + new XWwwFormUrlEncoded(mParameters).toString());
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException("Can't create valid authorization URI", e);
        }
    }

}
