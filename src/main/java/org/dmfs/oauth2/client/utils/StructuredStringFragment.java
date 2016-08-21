/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
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

import org.dmfs.httpessentials.parameters.Parameter;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.parameters.Parametrized;
import org.dmfs.iterables.CsvIterable;
import org.dmfs.iterators.AbstractConvertedIterator.Converter;
import org.dmfs.iterators.AbstractFilteredIterator.IteratorFilter;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.iterators.FilteredIterator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;


/**
 * Provides access to individual fields of x-www-form-url-encoded fragments.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class StructuredStringFragment implements Parametrized
{

    private final String mFragment;
    private final Iterable<String> mParts;


    public StructuredStringFragment(String fragment)
    {
        mFragment = fragment;
        mParts = new CsvIterable(fragment, '&');
    }


    @Override
    public <T> Parameter<T> firstParameter(final ParameterType<T> parameterType, final T defaultValue)
    {
        Iterator<Parameter<T>> iterator = parameters(parameterType);
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return parameterType.entity(defaultValue);
    }


    @Override
    public <T> Iterator<Parameter<T>> parameters(final ParameterType<T> parameterType)
    {
        return new ConvertedIterator<Parameter<T>, String>(
                new FilteredIterator<String>(mParts.iterator(), new IteratorFilter<String>()
                {
                    public boolean iterate(String element)
                    {
                        String paramName = parameterType.name();
                        int paramNameLen = paramName.length();
                        return element.startsWith(paramName)
                                && (element.length() == paramNameLen || element.length() > paramNameLen && element.charAt(
                                paramNameLen) == '=');
                    }
                }), new Converter<Parameter<T>, String>()
        {

            @Override
            public Parameter<T> convert(String element)
            {
                String value = element.substring(parameterType.name().length());
                if (value.length() > 0)
                {
                    try
                    {
                        return parameterType.entityFromString(URLDecoder.decode(value.substring(1), "UTF-8"));
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        throw new RuntimeException("UTF-8 encoding not supported!", e);
                    }
                }
                return parameterType.entityFromString("");
            }
        });
    }


    @Override
    public <T> boolean hasParameter(final ParameterType<T> parameterType)
    {
        return parameters(parameterType).hasNext();
    }


    @Override
    public String toString()
    {
        return mFragment;
    }

}
