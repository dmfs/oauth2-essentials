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

package org.dmfs.oauth2.client.http.entities;

import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.elementary.Present;
import org.dmfs.rfc3986.encoding.XWwwFormUrlEncoded;
import org.dmfs.rfc3986.parameters.Parameter;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


/**
 * An {@link HttpRequestEntity} that encodes key-value pairs using <code>application/x-www-form-urlencoded</code> encoding.
 *
 * @author Marten Gajda
 */
public final class XWwwFormUrlEncodedEntity implements HttpRequestEntity
{
    private static final String ENCODING = "UTF-8";
    private static final Optional<MediaType> CONTENT_TYPE = new Present<MediaType>(new StructuredMediaType("application", "x-www-form-urlencoded", ENCODING));

    private final ParameterList mValues;


    public XWwwFormUrlEncodedEntity(Parameter... values)
    {
        mValues = new BasicParameterList(values);
    }


    public XWwwFormUrlEncodedEntity(ParameterList values)
    {
        mValues = values;
    }


    @Override
    public Optional<MediaType> contentType()
    {
        return CONTENT_TYPE;
    }


    @Override
    public Optional<Long> contentLength()
    {
        try
        {
            return new Present<>((long) toString().getBytes(ENCODING).length);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(String.format(Locale.ENGLISH, "%s encoding not supported by runtime", ENCODING));
        }
    }


    @Override
    public void writeContent(OutputStream out) throws IOException
    {
        out.write(toString().getBytes(ENCODING));
    }


    @Override
    public String toString()
    {
        return new XWwwFormUrlEncoded(mValues).toString();
    }
}
