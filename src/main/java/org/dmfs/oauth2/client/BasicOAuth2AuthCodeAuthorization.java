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

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.parameters.Parametrized;
import org.dmfs.httpessentials.types.UrlFormEncodedKeyValues;
import org.dmfs.rfc3986.Uri;


/**
 * A basic {@link OAuth2AuthCodeAuthorization} implementation.
 * <p>
 * Note: Usually you don't need to instantiate this directly.
 *
 * @author Marten Gajda
 */
public final class BasicOAuth2AuthCodeAuthorization implements OAuth2AuthCodeAuthorization
{
    private static final ParameterType<String> AUTH_CODE = new BasicParameterType<String>("code",
            PlainStringHeaderConverter.INSTANCE);
    private static final ParameterType<String> STATE = new BasicParameterType<String>("state",
            PlainStringHeaderConverter.INSTANCE);

    private final Parametrized mFragment;
    private final OAuth2Scope mScope;


    public BasicOAuth2AuthCodeAuthorization(Uri redirectUri, OAuth2Scope requestedScope, String state) throws ProtocolException
    {
        mFragment = new UrlFormEncodedKeyValues(redirectUri.query().toString());
        if (!state.equals(mFragment.firstParameter(STATE, "").value()))
        {
            throw new ProtocolException("State in redirect uri doesn't match the original state!");
        }
        if (!mFragment.hasParameter(AUTH_CODE))
        {
            // fail early, because we can't do that in #code()
            throw new ProtocolException(String.format("Missing access_token in fragment '%s'", mFragment.toString()));
        }
        mScope = requestedScope;
    }


    @Override
    public String code()
    {
        return mFragment.firstParameter(AUTH_CODE, "").value();
    }


    @Override
    public OAuth2Scope scope()
    {
        return mScope;
    }

}
