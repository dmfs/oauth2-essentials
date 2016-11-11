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

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.decoration.HeaderDecorated;
import org.dmfs.oauth2.client.http.decorators.BasicAuthHeaderDecoration;


/**
 * Basic implementation of {@link OAuth2ClientCredentials}.
 *
 * @author Marten Gajda
 */
public final class BasicOAuth2ClientCredentials implements OAuth2ClientCredentials
{
    private final String mClientId;
    private final String mClientSecret;


    public BasicOAuth2ClientCredentials(String clientId, String clientSecret)
    {
        mClientId = clientId;
        mClientSecret = clientSecret;
    }


    @Override
    public <T> HttpRequest<T> authenticatedRequest(HttpRequest<T> request)
    {
        return new HeaderDecorated<>(request, new BasicAuthHeaderDecoration(mClientId, mClientSecret));
    }


    @Override
    public String clientId()
    {
        return mClientId;
    }

}
