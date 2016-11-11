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

package org.dmfs.oauth2.client.scope;

import org.dmfs.oauth2.client.OAuth2Scope;


/**
 * A basic {@link OAuth2Scope} that gets the scope tokens in an array.
 *
 * @author Marten Gajda
 */
public final class BasicScope implements OAuth2Scope
{
    private final String[] mTokens;


    /**
     * Creates a new {@link OAuth2Scope} that contains the given tokens.
     *
     * @param tokens
     *         The scope tokens in this scope. Must not contain <code>null</code> or empty {@link String}s.
     */
    public BasicScope(String... tokens)
    {
        mTokens = tokens.clone();
    }


    @Override
    public boolean isEmpty()
    {
        return mTokens.length == 0;
    }


    @Override
    public boolean hasToken(String token)
    {
        for (String scopeToken : mTokens)
        {
            if (scopeToken.equals(token))
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public int tokenCount()
    {
        return mTokens.length;
    }


    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder(mTokens.length * 30);
        boolean first = true;

        for (String token : mTokens)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                result.append(' ');
            }
            result.append(token);
        }
        return result.toString();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof OAuth2Scope))
        {
            return false;
        }

        OAuth2Scope other = (OAuth2Scope) obj;
        if (isEmpty() && other.isEmpty())
        {
            return true;
        }

        if (tokenCount() != other.tokenCount())
        {
            return false;
        }

        for (String token : mTokens)
        {
            if (!other.hasToken(token))
            {
                return false;
            }
        }
        return true;
    }
}
