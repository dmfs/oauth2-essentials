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

package org.dmfs.oauth2.client.http.decorators;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.iharder.Base64;

import org.dmfs.httpclient.HttpMethod;
import org.dmfs.httpclient.HttpRequest;
import org.dmfs.httpclient.HttpRequestEntity;
import org.dmfs.httpclient.HttpResponse;
import org.dmfs.httpclient.HttpResponseHandler;
import org.dmfs.httpclient.converters.PlainStringHeaderConverter;
import org.dmfs.httpclient.exceptions.ProtocolError;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.headers.BasicSingletonHeaderType;
import org.dmfs.httpclient.headers.HeaderType;
import org.dmfs.httpclient.headers.Headers;
import org.dmfs.httpclient.headers.UpdatedHeaders;


/**
 * {@link HttpRequest} decorator that adds a Basic Authorization header.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 * 
 * @param <T>
 *            The expected response type.
 */
public final class BasicAuthRequestDecorator<T> implements HttpRequest<T>
{
	private final HeaderType<String> STRING_HEADER_TYPE = new BasicSingletonHeaderType<String>("Authorization", new PlainStringHeaderConverter());
	private final HttpRequest<T> mDecorated;
	private final String mUsername;
	private final String mPassword;


	public BasicAuthRequestDecorator(HttpRequest<T> decoratedRequest, String username, String password)
	{
		mDecorated = decoratedRequest;
		mUsername = username;
		mPassword = password;
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
			return new UpdatedHeaders(mDecorated.headers(), STRING_HEADER_TYPE.entity("Basic "
				+ Base64.encodeBytes(String.format("%s:%s", mUsername, mPassword).getBytes("UTF-8"))));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("Charset UTF-8 not supported by rumtime!", e);
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
