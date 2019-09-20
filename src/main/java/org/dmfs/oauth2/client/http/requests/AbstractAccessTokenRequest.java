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

package org.dmfs.oauth2.client.http.requests;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.entities.XWwwFormUrlEncodedEntity;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.responsehandlers.FailResponseHandler;
import org.dmfs.jems.pair.Pair;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.responsehandlers.TokenErrorResponseHandler;
import org.dmfs.oauth2.client.http.responsehandlers.TokenResponseHandler;


/**
 * An abstract {@link HttpRequest} to retrieve an {@link OAuth2AccessToken} from a token endpoint.
 *
 * @author Marten Gajda
 */
public abstract class AbstractAccessTokenRequest implements HttpRequest<OAuth2AccessToken>
{
    private final OAuth2Scope mScope;
    private final HttpRequestEntity mEntity;


    public AbstractAccessTokenRequest(OAuth2Scope scope, Iterable<Pair<CharSequence, CharSequence>> parameters)
    {
        this(scope, new XWwwFormUrlEncodedEntity(parameters, "UTF-8"));
    }


    public AbstractAccessTokenRequest(OAuth2Scope scope, HttpRequestEntity entityGenerator)
    {
        mScope = scope;
        mEntity = entityGenerator;
    }


    @Override
    public final HttpMethod method()
    {
        return HttpMethod.POST;
    }


    @Override
    public final Headers headers()
    {
        return EmptyHeaders.INSTANCE;
    }


    @Override
    public final HttpRequestEntity requestEntity()
    {
        return mEntity;
    }


    @Override
    public final HttpResponseHandler<OAuth2AccessToken> responseHandler(HttpResponse response)
    {
        if (!HttpStatus.OK.equals(response.status()))
        {
            if (!HttpStatus.BAD_REQUEST.equals(response.status()))
            {
                // there was an unexpected error, let the fail handler handle this
                return FailResponseHandler.getInstance();
            }
            // the server returned a Bad Request
            return new TokenErrorResponseHandler();
        }
        return new TokenResponseHandler(mScope);
    }
}
