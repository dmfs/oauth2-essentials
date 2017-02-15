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
import org.dmfs.rfc3986.encoding.XWwwFormUrlEncoded;
import org.dmfs.rfc3986.parameters.Parameter;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;


/**
 * An {@link HttpRequestEntity} that encodes key-value pairs using <code>application/x-www-form-urlencoded</code> encoding.
 *
 * @author Marten Gajda
 */
public final class XWwwFormUrlEncodedEntity implements HttpRequestEntity
{
    private static final MediaType CONTENT_TYPE = new StructuredMediaType("application", "x-www-form-urlencoded");
    private static final String ENCODING = "UTF-8";

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
    public MediaType contentType()
    {
        return CONTENT_TYPE;
    }


    @Override
    public long contentLength() throws IOException
    {
        return toString().getBytes(ENCODING).length;
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
