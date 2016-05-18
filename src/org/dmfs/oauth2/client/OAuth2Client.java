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

package org.dmfs.oauth2.client;

import java.io.IOException;
import java.net.URI;

import org.dmfs.httpclient.HttpRequest;
import org.dmfs.httpclient.HttpRequestExecutor;
import org.dmfs.httpclient.exceptions.ProtocolError;
import org.dmfs.httpclient.exceptions.ProtocolException;
import org.dmfs.httpclient.exceptions.RedirectionException;
import org.dmfs.httpclient.exceptions.UnexpectedStatusException;
import org.dmfs.rfc5545.Duration;


/**
 * Represents an OAuth2 client to a specific {@link OAuth2AuthorizationProvider}.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public interface OAuth2Client
{

	/**
	 * Executes the given {@link HttpRequest} and returns an {@link OAuth2AccessToken}.
	 * 
	 * @param tokenRequest
	 *            An {@link HttpRequest} to execute.
	 * @param executor
	 *            An {@link HttpRequestExecutor} to execute the request.
	 * @return An {@link OAuth2AccessToken}
	 * @throws RedirectionException
	 * @throws UnexpectedStatusException
	 * @throws IOException
	 * @throws ProtocolError
	 * @throws ProtocolException
	 */
	public OAuth2AccessToken accessToken(HttpRequest<OAuth2AccessToken> tokenRequest, HttpRequestExecutor executor) throws RedirectionException,
		UnexpectedStatusException, IOException, ProtocolError, ProtocolException;


	/**
	 * Constructs the initial URL for an interactive authorization grant.
	 * 
	 * @param authorizationRequest
	 *            The {@link OAuth2AuthorizationRequest} to launch.
	 * @return The URL that needs to be opened in a user agent.
	 */
	public URI authorizationUrl(OAuth2AuthorizationRequest authorizationRequest);


	/**
	 * Generates a random state String to be used in interactive grants.
	 * 
	 * @return A random {@link String}.
	 */
	public String generatedRandomState();


	/**
	 * The redirect URI of this client as registered with the server.
	 * 
	 * @return A {@link URI}.
	 */
	public URI redirectUri();


	/**
	 * Returns the default lifetime of an access token.
	 * 
	 * @return A {@link Duration}.
	 */
	public Duration defaultTokenTtl();
}
