/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.oauth2.client.utils;

import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.parameters.ParameterType;
import org.dmfs.rfc3986.parameters.parametertypes.BasicParameterType;
import org.dmfs.rfc3986.parameters.valuetypes.TextValueType;
import org.dmfs.rfc5545.Duration;


/**
 * A collection of parameter types as specified in <a href="https://tools.ietf.org/html/rfc6749">RFC 6749</a>.
 *
 * @author Marten Gajda
 */
public final class Parameters
{
    public final static ParameterType<CharSequence> ACCESS_TOKEN = new BasicParameterType<>("access_token", TextValueType.INSTANCE);

    public final static ParameterType<CharSequence> AUTH_CODE = new BasicParameterType<>("code", TextValueType.INSTANCE);

    public final static ParameterType<CharSequence> CLIENT_ID = new BasicParameterType<>("client_id", TextValueType.INSTANCE);

    public final static ParameterType<CharSequence> CODE_CHALLENGE = new BasicParameterType<>("code_challenge", TextValueType.INSTANCE);

    public final static ParameterType<CharSequence> CODE_CHALLENGE_METHOD = new BasicParameterType<>("code_challenge_method", TextValueType.INSTANCE);

    public final static ParameterType<Duration> EXPIRES_IN = new BasicParameterType<>("expires_in", new DurationValueType());

    public final static ParameterType<Uri> REDIRECT_URI = new BasicParameterType<>("redirect_uri", new UriValueType());

    public final static ParameterType<CharSequence> RESPONSE_TYPE = new BasicParameterType<>("response_type", TextValueType.INSTANCE);

    public final static ParameterType<OAuth2Scope> SCOPE = new BasicParameterType<OAuth2Scope>("scope", new OAuth2ScopeValueType());

    public final static ParameterType<CharSequence> STATE = new BasicParameterType<>("state", TextValueType.INSTANCE);

    public final static ParameterType<CharSequence> TOKEN_TYPE = new BasicParameterType<>("token_type", TextValueType.INSTANCE);


    private Parameters()
    {
    }
}
