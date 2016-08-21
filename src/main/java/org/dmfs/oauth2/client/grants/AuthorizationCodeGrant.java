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

package org.dmfs.oauth2.client.grants;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.oauth2.client.*;
import org.dmfs.oauth2.client.http.requests.AuthorizationCodeTokenRequest;
import org.dmfs.oauth2.client.scope.StringScope;

import java.io.IOException;
import java.net.URI;


/**
 * Implements the OAuth2 Authorization Code Grant as specified in <a href="https://tools.ietf.org/html/rfc6749#section-4.1">RFC
 * 6749, Section 4.1</a>.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class AuthorizationCodeGrant implements OAuth2InteractiveGrant
{
    private final OAuth2Client mClient;
    private final OAuth2Scope mScope;
    private final String mState;


    /**
     * Launches a new Authorization Code Grant for the given {@link OAuth2Client} and {@link OAuth2Scope}.
     *
     * @param client
     *         The {@link OAuth2Client}.
     * @param scope
     *         An {@link OAuth2Scope}.
     */
    public AuthorizationCodeGrant(OAuth2Client client, OAuth2Scope scope)
    {
        this(client, scope, client.generatedRandomState());
    }


    private AuthorizationCodeGrant(final OAuth2Client client, final OAuth2Scope scope, String state)
    {
        mClient = client;
        mScope = scope;
        mState = client.generatedRandomState();
    }


    @Override
    public URI authorizationUrl()
    {
        if (mScope.isEmpty())
        {
            return mClient.authorizationUrl(new BasicOAuth2AuthorizationRequest("code", mState));
        }
        else
        {
            return mClient.authorizationUrl(new BasicOAuth2AuthorizationRequest("code", mScope, mState));
        }
    }


    @Override
    public OAuth2InteractiveGrant withRedirect(final URI redirectUri) throws ProtocolError
    {
        return new AuthorizedAuthorizationCodeGrant(mClient, redirectUri, mScope, mState);
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
    {
        throw new IllegalStateException(
                "first use withRedirectUri(URI) to pass the redirect URI returned by the authorization endpoint.");
    }


    @Override
    public OAuth2InteractiveGrant.OAuth2GrantState state()
    {
        return new InitialAuthorizationCodeGrantState(mScope, mState);
    }


    /**
     * An {@link OAuth2InteractiveGrant} that represents the authorized state of an Authorization Code Grant. That
     * means, the user has granted access and an auth token was issued by the server.
     * <p>
     * The next step is to retrieve the {@link OAuth2AccessToken}.
     */
    private final static class AuthorizedAuthorizationCodeGrant implements OAuth2InteractiveGrant
    {
        private final OAuth2Client mClient;
        private final URI mRedirectUri;
        private final OAuth2Scope mScope;
        private final String mState;


        private AuthorizedAuthorizationCodeGrant(OAuth2Client client, URI redirectUri, OAuth2Scope scope, String state)
        {
            mClient = client;
            mRedirectUri = redirectUri;
            mScope = scope;
            mState = state;
        }


        @Override
        public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
        {
            return mClient.accessToken(
                    new AuthorizationCodeTokenRequest(
                            new BasicOAuth2AuthCodeAuthorization(mRedirectUri, mScope, mState), mClient.redirectUri()),
                    executor);
        }


        @Override
        public URI authorizationUrl()
        {
            throw new IllegalStateException("This grant has already been completed. You can't start it again.");
        }


        @Override
        public OAuth2InteractiveGrant withRedirect(URI redirectUri)
        {
            throw new IllegalStateException(
                    "This grant has already been completed. You can't feed another redirect URI.");
        }


        @Override
        public OAuth2GrantState state()
        {
            return new AuthorizedAuthorizationCodeGrantState(mScope, mRedirectUri, mState);
        }
    }


    /**
     * An {@link OAuth2GrantState} that represents the state of an Authorization Code Grant that was not confirmed by
     * the user so far.
     */
    private final static class InitialAuthorizationCodeGrantState implements OAuth2InteractiveGrant.OAuth2GrantState
    {

        private static final long serialVersionUID = 1L;

        private final String mScopeString;
        private final String mState;


        public InitialAuthorizationCodeGrantState(OAuth2Scope scope, String state)
        {
            mScopeString = scope.toString();
            mState = state;
        }


        @Override
        public AuthorizationCodeGrant grant(OAuth2Client client)
        {
            return new AuthorizationCodeGrant(client, new StringScope(mScopeString), mState);
        }

    }


    /**
     * An {@link OAuth2GrantState} that represents the state of an Authorization Code Grant that got user consent.
     */
    private final static class AuthorizedAuthorizationCodeGrantState implements OAuth2InteractiveGrant.OAuth2GrantState
    {

        private static final long serialVersionUID = 1L;

        private final String mScopeString;
        private final URI mRedirectUri;
        private final String mState;


        public AuthorizedAuthorizationCodeGrantState(OAuth2Scope scope, URI redirectUri, String state)
        {
            mScopeString = scope.toString();
            mRedirectUri = redirectUri;
            mState = state;
        }


        @Override
        public OAuth2InteractiveGrant grant(final OAuth2Client client)
        {
            return new AuthorizedAuthorizationCodeGrant(client, mRedirectUri, new StringScope(mScopeString), mState);
        }
    }
}
