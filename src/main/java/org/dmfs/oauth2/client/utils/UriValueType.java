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

import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.parameters.ValueType;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc3986.uris.Text;


/**
 * The {@link ValueType} of {@link Uri} parameters.
 *
 * @author Marten Gajda
 */
public final class UriValueType implements ValueType<Uri>
{
    @Override
    public Uri parsedValue(CharSequence valueText)
    {
        return new LazyUri(new Precoded(valueText));
    }


    @Override
    public CharSequence serializedValue(Uri value)
    {
        return new Text(value);
    }
}
