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

import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.adapters.OptionalParameter;
import org.dmfs.rfc3986.parameters.adapters.TextParameter;
import org.dmfs.rfc3986.parameters.adapters.XwfueParameterList;

import static org.dmfs.oauth2.client.utils.Parameters.AUTH_CODE;
import static org.dmfs.oauth2.client.utils.Parameters.STATE;


/**
 * A basic {@link OAuth2AuthCodeAuthorization} implementation.
 * <p>
 * Note: Usually you don't need to instantiate this directly.
 *
 * @author Marten Gajda
 */
public final class BasicOAuth2AuthCodeAuthorization implements OAuth2AuthCodeAuthorization
{
    private final ParameterList mQueryParameters;
    private final OAuth2Scope mScope;


    public BasicOAuth2AuthCodeAuthorization(Uri redirectUri, OAuth2Scope requestedScope, CharSequence state) throws ProtocolException
    {
        mQueryParameters = new XwfueParameterList(redirectUri.query().value());
        if (!state.toString().equals(new TextParameter(STATE, mQueryParameters).toString()))
        {
            throw new ProtocolException("State in redirect uri doesn't match the original state!");
        }
        if (!new OptionalParameter<CharSequence>(AUTH_CODE, mQueryParameters).isPresent())
        {
            // fail early, because we can't do that in code()
            throw new ProtocolException(String.format("Missing auth code in fragment '%s'", redirectUri.query().value()));
        }
        mScope = requestedScope;
    }


    @Override
    public CharSequence code()
    {
        return new TextParameter(AUTH_CODE, mQueryParameters);
    }


    @Override
    public OAuth2Scope scope()
    {
        return mScope;
    }

}
