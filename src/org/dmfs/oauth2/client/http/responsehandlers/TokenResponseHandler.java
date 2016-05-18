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

package org.dmfs.oauth2.client.http.responsehandlers;

import java.io.IOException;

import org.dmfs.httpclient.HttpResponse;
import org.dmfs.httpclient.HttpResponseHandler;
import org.dmfs.httpclient.exceptions.ProtocolError;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.responsehandlers.StringResponseHandler;
import org.dmfs.httpclient.types.MediaType;
import org.dmfs.httpclient.types.StructuredMediaType;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.tokens.JsonAccessToken;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * {@link HttpResponseHandler} for OAuth2 token responses.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class TokenResponseHandler implements HttpResponseHandler<OAuth2AccessToken>
{
	private final static MediaType APPLICATION_JSON = new StructuredMediaType("application", "json");

	private final OAuth2Scope mScope;


	public TokenResponseHandler(OAuth2Scope scope)
	{
		mScope = scope;
	}


	@Override
	public OAuth2AccessToken handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
	{
		if (!APPLICATION_JSON.equals(response.responseEntity().contentType()))
		{
			throw new ProtocolException(
				String.format("Illegal response content-type %s, exected %s", response.responseEntity().contentType(), APPLICATION_JSON));
		}

		String responseString = new StringResponseHandler("UTF-8").handleResponse(response);
		try
		{
			return new JsonAccessToken(new JSONObject(responseString), mScope);
		}
		catch (JSONException e)
		{
			throw new ProtocolException(String.format("Can't decode JSON response %s", responseString), e);
		}
	}
}