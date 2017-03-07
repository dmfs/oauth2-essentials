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

package org.dmfs.oauth2.client.grants;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.oauth2.client.BasicOAuth2AuthCodeAuthorization;
import org.dmfs.oauth2.client.BasicOAuth2AuthorizationRequest;
import org.dmfs.oauth2.client.OAuth2AccessToken;
import org.dmfs.oauth2.client.OAuth2AuthorizationRequest;
import org.dmfs.oauth2.client.OAuth2Client;
import org.dmfs.oauth2.client.OAuth2InteractiveGrant;
import org.dmfs.oauth2.client.OAuth2Scope;
import org.dmfs.oauth2.client.http.requests.AuthorizationCodeTokenRequest;
import org.dmfs.oauth2.client.pkce.S256CodeChallenge;
import org.dmfs.oauth2.client.scope.StringScope;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.encoding.XWwwFormUrlEncoded;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.adapters.XwfueParameterList;
import org.dmfs.rfc3986.parameters.parametersets.EmptyParameterList;

import java.io.IOException;
import java.net.URI;


/**
 * Implements the OAuth2 Authorization Code Grant as specified in <a href="https://tools.ietf.org/html/rfc6749#section-4.1">RFC 6749, Section 4.1</a>.
 * <p>
 * Note this automatically uses the PKCE extension as specified in <a href="https://tools.ietf.org/html/rfc7636">RFC 7636</a>
 *
 * @author Marten Gajda
 */
public final class AuthorizationCodeGrant implements OAuth2InteractiveGrant
{
    private final OAuth2Client mClient;
    private final OAuth2Scope mScope;
    private final CharSequence mState;
    private final CharSequence mCodeVerifier;
    private final ParameterList mCustomParameters;


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
        this(client, scope, client.randomChars(), client.randomChars(), EmptyParameterList.INSTANCE);
    }


    /**
     * Launches a new Authorization Code Grant for the given {@link OAuth2Client}, {@link OAuth2Scope} and custom {@link ParameterList}. The given {@link
     * ParameterList} must not contain any OAuth2 specific parameters. Any such parameter might be overridden by this grant.
     *
     * @param client
     *         The {@link OAuth2Client}.
     * @param scope
     *         An {@link OAuth2Scope}.
     * @param customParameters
     *         Custom parameters to send to the authorization endpoint.
     */
    public AuthorizationCodeGrant(OAuth2Client client, OAuth2Scope scope, ParameterList customParameters)
    {
        this(client, scope, client.randomChars(), client.randomChars(), customParameters);
    }


    private AuthorizationCodeGrant(final OAuth2Client client, final OAuth2Scope scope, CharSequence state, CharSequence codeVerifier, ParameterList customParameters)
    {
        mClient = client;
        mScope = scope;
        mState = state;
        mCodeVerifier = codeVerifier;
        mCustomParameters = customParameters;
    }


    @Override
    public URI authorizationUrl()
    {
        OAuth2AuthorizationRequest authorizationRequest;
        if (mScope.isEmpty())
        {
            authorizationRequest = new BasicOAuth2AuthorizationRequest("code", mState, mCustomParameters);
        }
        else
        {
            authorizationRequest = new BasicOAuth2AuthorizationRequest("code", mScope, mState, mCustomParameters);
        }
        return mClient.authorizationUrl(authorizationRequest.withCodeChallenge(new S256CodeChallenge(mCodeVerifier)));
    }


    @Override
    public OAuth2InteractiveGrant withRedirect(final Uri redirectUri) throws ProtocolError
    {
        return new AuthorizedAuthorizationCodeGrant(mClient, redirectUri, mScope, mState, mCodeVerifier);
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
    {
        throw new IllegalStateException("first use withRedirectUri(URI) to pass the redirect URI returned by the authorization endpoint.");
    }


    @Override
    public OAuth2InteractiveGrant.OAuth2GrantState state()
    {
        return new InitialAuthorizationCodeGrantState(mScope, mState, mCodeVerifier, new XWwwFormUrlEncoded(mCustomParameters));
    }


    /**
     * An {@link OAuth2InteractiveGrant} that represents the authorized state of an Authorization Code Grant. That means, the user has granted access and an
     * auth token was issued by the server.
     * <p>
     * The next step is to retrieve the {@link OAuth2AccessToken}.
     */
    private final static class AuthorizedAuthorizationCodeGrant implements OAuth2InteractiveGrant
    {
        private final OAuth2Client mClient;
        private final Uri mRedirectUri;
        private final OAuth2Scope mScope;
        private final CharSequence mState;
        private final CharSequence mCodeVerifier;


        private AuthorizedAuthorizationCodeGrant(OAuth2Client client, Uri redirectUri, OAuth2Scope scope, CharSequence state, CharSequence codeVerifier)
        {
            mClient = client;
            mRedirectUri = redirectUri;
            mScope = scope;
            mState = state;
            mCodeVerifier = codeVerifier;
        }


        @Override
        public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
        {
            return mClient.accessToken(
                    new AuthorizationCodeTokenRequest(
                            new BasicOAuth2AuthCodeAuthorization(mRedirectUri, mScope, mState), mClient.redirectUri(), mCodeVerifier),
                    executor);
        }


        @Override
        public URI authorizationUrl()
        {
            throw new IllegalStateException("This grant has already been completed. You can't start it again.");
        }


        @Override
        public OAuth2InteractiveGrant withRedirect(Uri redirectUri)
        {
            throw new IllegalStateException("This grant has already been completed. You can't feed another redirect URI.");
        }


        @Override
        public OAuth2GrantState state()
        {
            return new AuthorizedAuthorizationCodeGrantState(mScope, mRedirectUri, mState, mCodeVerifier);
        }
    }


    /**
     * An {@link OAuth2GrantState} that represents the state of an Authorization Code Grant that was not confirmed by the user so far.
     */
    private final static class InitialAuthorizationCodeGrantState implements OAuth2InteractiveGrant.OAuth2GrantState
    {

        private static final long serialVersionUID = 1L;

        private final String mScopeString;
        private final String mState;
        private final String mCodeVerifier;
        private final String mCustomQueryParams;


        public InitialAuthorizationCodeGrantState(OAuth2Scope scope, CharSequence state, CharSequence codeVerifier, CharSequence customQuery)
        {
            mScopeString = scope.toString();
            // convert state and codeVerifier to String, because a CharSequence may not be serializable
            mState = state.toString();
            mCodeVerifier = codeVerifier.toString();
            mCustomQueryParams = customQuery.toString();
        }


        @Override
        public AuthorizationCodeGrant grant(OAuth2Client client)
        {
            return new AuthorizationCodeGrant(client,
                    new StringScope(mScopeString),
                    mState,
                    mCodeVerifier,
                    new XwfueParameterList(new Precoded(mCustomQueryParams)));
        }

    }


    /**
     * An {@link OAuth2GrantState} that represents the state of an Authorization Code Grant that got user consent.
     */
    private final static class AuthorizedAuthorizationCodeGrantState implements OAuth2InteractiveGrant.OAuth2GrantState
    {
        private static final long serialVersionUID = 1L;

        private final String mScopeString;
        private final Uri mRedirectUri;
        private final String mState;
        private final String mCodeVerifier;


        public AuthorizedAuthorizationCodeGrantState(OAuth2Scope scope, Uri redirectUri, CharSequence state, CharSequence codeVerifier)
        {
            mScopeString = scope.toString();
            mRedirectUri = redirectUri;
            // convert state and codeVerifier to String, because a CharSequence may not be serializable
            mState = state.toString();
            mCodeVerifier = codeVerifier.toString();
        }


        @Override
        public OAuth2InteractiveGrant grant(final OAuth2Client client)
        {
            return new AuthorizedAuthorizationCodeGrant(client, mRedirectUri, new StringScope(mScopeString), mState, mCodeVerifier);
        }
    }
}
