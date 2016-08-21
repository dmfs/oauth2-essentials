/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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
 * 
 */

package org.dmfs.oauth2.client.http.entities;

import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StructuredMediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map.Entry;


/**
 * An {@link HttpRequestEntity} that encodes key-value pairs using <code>application/x-www-form-urlencoded</code>
 * encoding.
 * <p/>
 * This implementation will just ignore any {@link Entry}s with a <code>null</code> key or value.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class XWwwFormUrlEncodedEntity implements HttpRequestEntity
{
    private final static MediaType CONTENT_TYPE = new StructuredMediaType("application", "x-www-form-urlencoded");

    private final Entry<String, String>[] mValues;


    public XWwwFormUrlEncodedEntity(Entry<String, String>[] values)
    {
        mValues = values.clone();
    }


    @Override
    public MediaType contentType()
    {
        return CONTENT_TYPE;
    }


    @Override
    public long contentLength() throws IOException
    {
        return toString().getBytes("UTF-8").length;
    }


    @Override
    public void writeContent(OutputStream out) throws IOException
    {
        out.write(toString().getBytes("UTF-8"));
    }


    @Override
    public String toString()
    {
        try
        {
            final StringBuilder builder = new StringBuilder(mValues.length * 32);
            boolean first = true;
            for (Entry<String, String> value : mValues)
            {
                if (value != null && value.getKey() != null && value.getValue() != null)
                {
                    if (first)
                    {
                        first = false;
                    }
                    else
                    {
                        builder.append('&');
                    }

                    builder.append(URLEncoder.encode(value.getKey(), "UTF-8"));
                    builder.append('=');
                    builder.append(URLEncoder.encode(value.getValue(), "UTF-8"));
                }
            }
            return builder.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Runtime doesn't support required encoding UTF-8", e);
        }
    }
}
