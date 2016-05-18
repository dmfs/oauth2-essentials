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

package org.dmfs.oauth2.client.http.requests;

import java.io.IOException;

import org.dmfs.httpclient.HttpMethod;
import org.dmfs.httpclient.HttpRequest;
import org.dmfs.httpclient.HttpResponse;
import org.dmfs.httpclient.HttpResponseHandler;
import org.dmfs.httpclient.HttpStatus;
import org.dmfs.httpclient.exceptions.ProtocolError;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.headers.EmptyHeaders;
import org.dmfs.httpclient.headers.Headers;
import org.dmfs.httpclient.responsehandlers.FailResponseHandler;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.responsehandlers.TokenResponseHandler;


/**
 * An abstract {@link AccessTokenRequest} to retrieve an {@link OAuth2AccessToken} from a token endpoint.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public abstract class AbstractAccessTokenRequest implements HttpRequest<OAuth2AccessToken>
{
	private final OAuth2Scope mScope;


	public AbstractAccessTokenRequest(OAuth2Scope scope)
	{
		mScope = scope;
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
	public final HttpResponseHandler<OAuth2AccessToken> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
	{
		if (!HttpStatus.OK.equals(response.status()))
		{
			// there was an error, let the fail handler handle this
			return FailResponseHandler.getInstance();
		}
		return new TokenResponseHandler(mScope);
	}
}
