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

import java.io.IOException;

import org.dmfs.httpclient.HttpMethod;
import org.dmfs.httpclient.HttpRequest;
import org.dmfs.httpclient.HttpRequestEntity;
import org.dmfs.httpclient.HttpResponse;
import org.dmfs.httpclient.HttpResponseHandler;
import org.dmfs.httpclient.converters.PlainStringHeaderConverter;
import org.dmfs.httpclient.exceptions.ProtocolError;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.headers.BasicSingletonHeaderType;
import org.dmfs.httpclient.headers.Headers;
import org.dmfs.oauth2.client.OAuth2AccessToken;


/**
 * An {@link HttpRequest} decorator that adds a Bearer authorization header.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 * 
 * @param <T>
 *            The type of the expected response.
 */
public final class BearerAuthRequestDecorator<T> implements HttpRequest<T>
{
	// TODO: use a generic authorization header instead (once we have one)
	private final static BasicSingletonHeaderType<String> AUTHORIZATION_HEADER = new BasicSingletonHeaderType<String>("Authorization",
		new PlainStringHeaderConverter());

	private final OAuth2AccessToken mAccessToken;
	private final HttpRequest<T> mDecorated;


	public BearerAuthRequestDecorator(HttpRequest<T> decorated, OAuth2AccessToken accessToken)
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
			return mDecorated.headers().withHeader(AUTHORIZATION_HEADER.entityFromString("Bearer " + mAccessToken.accessToken()));
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
