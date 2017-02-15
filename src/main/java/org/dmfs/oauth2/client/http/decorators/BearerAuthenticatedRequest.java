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

package org.dmfs.oauth2.client.http.decorators;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.oauth2.client.OAuth2AccessToken;

import java.io.IOException;


/**
 * An {@link HttpRequest} decorator that adds a Bearer authorization header.
 *
 * @param <T>
 *         The type of the expected response.
 *
 * @author Marten Gajda
 */
public final class BearerAuthenticatedRequest<T> implements HttpRequest<T>
{
    // TODO: use a generic authorization header instead (once we have one)
    private final static BasicSingletonHeaderType<String> AUTHORIZATION_HEADER = new BasicSingletonHeaderType<String>(
            "Authorization",
            new PlainStringHeaderConverter());

    private final OAuth2AccessToken mAccessToken;
    private final HttpRequest<T> mDecorated;


    public BearerAuthenticatedRequest(HttpRequest<T> decorated, OAuth2AccessToken accessToken)
    {
        mDecorated = decorated;
        mAccessToken = accessToken;
    }


    @Override
    public HttpMethod method()
    {
        return mDecorated.method();
    }


    @Override
    public Headers headers()
    {
        return mDecorated.headers().withHeader(AUTHORIZATION_HEADER.entityFromString("Bearer " + getAccessToken().toString()));
    }


    private CharSequence getAccessToken()
    {
        try
        {
            return mAccessToken.accessToken();
        }
        catch (ProtocolException e)
        {
            throw new RuntimeException("Can't authenticate request", e);
        }
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return mDecorated.requestEntity();
    }


    @Override
    public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        return mDecorated.responseHandler(response);
    }

}
