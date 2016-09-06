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
 * @author Marten Gajda <marten@dmfs.org>
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
        mAccessToken = accessToken;
        mDecorated = decorated;
    }


    @Override
    public HttpMethod method()
    {
        return mDecorated.method();
    }


    @Override
    public Headers headers()
    {
        try
        {
            return mDecorated.headers()
                    .withHeader(AUTHORIZATION_HEADER.entityFromString("Bearer " + mAccessToken.accessToken()));
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
