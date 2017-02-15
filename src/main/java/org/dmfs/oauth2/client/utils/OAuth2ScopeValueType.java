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
import org.dmfs.oauth2.client.scope.StringScope;
import org.dmfs.rfc3986.parameters.ValueType;


/**
 * The {@link ValueType} {@link OAuth2Scope} parameters.
 *
 * @author Marten Gajda
 */
public final class OAuth2ScopeValueType implements ValueType<OAuth2Scope>
{
    @Override
    public OAuth2Scope parsedValue(CharSequence valueText)
    {
        return new StringScope(valueText.toString());
    }


    @Override
    public CharSequence serializedValue(OAuth2Scope value)
    {
        return value.toString();
    }
}
