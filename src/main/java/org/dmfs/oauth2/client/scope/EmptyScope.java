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
 * An {@link OAuth2Scope} that contains no scope tokens.
 *
 * @author Marten Gajda
 */
public final class EmptyScope implements OAuth2Scope
{
    public final static EmptyScope INSTANCE = new EmptyScope();


    @Override
    public boolean isEmpty()
    {
        return true;
    }


    @Override
    public boolean hasToken(String token)
    {
        // no tokens in here
        return false;
    }


    @Override
    public int tokenCount()
    {
        return 0;
    }


    @Override
    public String toString()
    {
        return "";
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof OAuth2Scope))
        {
            return false;
        }

        return ((OAuth2Scope) obj).isEmpty();
    }
}
