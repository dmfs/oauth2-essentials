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

import net.iharder.Base64;

import org.dmfs.express.json.elementary.JsonText;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.oauth2.client.*;
import org.dmfs.oauth2.client.scope.StringScope;
import org.dmfs.oauth2.client.tokens.ImplicitGrantAccessToken;
import org.dmfs.oauth2.client.utils.GrantState;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc3986.uris.Text;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;


/**
 * Implements the OAuth2 Implicit Grant as specified in <a href="https://tools.ietf.org/html/rfc6749#section-4.2">RFC 6749, Section 4.2</a>.
 *
 * @author Marten Gajda
 */
public final class ImplicitGrant implements OAuth2InteractiveGrant
{
    private final OAuth2Client mClient;
    private final OAuth2Scope mScope;
    private final CharSequence mState;


    /**
     * Creates a new {@link ImplicitGrant} for the given {@link OAuth2Client} and {@link OAuth2Scope}.
     *
     * @param client
     *     The {@link OAuth2Client} that requests access.
     * @param scope
     *     The {@link OAuth2Scope} to request access to.
     */
    public ImplicitGrant(OAuth2Client client, OAuth2Scope scope)
    {
        this(client, scope, client.randomChars());
    }


    private ImplicitGrant(OAuth2Client client, OAuth2Scope scope, CharSequence state)
    {
        mClient = client;
        mScope = scope;
        mState = state;
    }


    @Override
    public URI authorizationUrl()
    {
        if (mScope.isEmpty())
        {
            return mClient.authorizationUrl(new BasicOAuth2AuthorizationRequest("token", mState));
        }
        else
        {
            return mClient.authorizationUrl(new BasicOAuth2AuthorizationRequest("token", mScope, mState));
        }
    }


    @Override
    public OAuth2InteractiveGrant withRedirect(final Uri redirectUri)
    {
        return new AuthorizedImplicitGrant(mClient, redirectUri, mScope, mState);
    }


    @Override
    public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
    {
        throw new IllegalStateException("first use withRedirectUri(URI) to pass the redirect URI returned by the authorization endpoint.");
    }


    @Deprecated
    @Override
    public OAuth2InteractiveGrant.OAuth2GrantState state()
    {
        return new InitialImplicitGrantState(mScope, mState);
    }


    @Override
    public String encodedState()
    {
        return Base64.encodeBytes(
            new JsonText(new GrantState(InitialImplicitGrantFactory.class, mScope.toString(), mState.toString()))
                .value()
                .getBytes(StandardCharsets.UTF_8));
    }


    private final static class InitialImplicitGrantFactory implements OAuth2InteractiveGrantFactory
    {

        @Override
        public OAuth2InteractiveGrant grant(OAuth2Client client, JSONArray arguments)
        {
            if (arguments.length() != 2)
            {
                throw new IllegalArgumentException("Can't restore grant from invalid state.");
            }
            return new ImplicitGrant(client, new StringScope(arguments.getString(0)), arguments.getString(1));

        }
    }


    /**
     * An {@link OAuth2InteractiveGrant} that represents the authorized state of an Implicit Grant. That means, the user has granted access and an access token
     * was issued by the server.
     * <p>
     * The next step is to retrieve the {@link OAuth2AccessToken}.
     */
    private final static class AuthorizedImplicitGrant implements OAuth2InteractiveGrant
    {
        private final OAuth2Client mClient;
        private final Uri mRedirectUri;
        private final OAuth2Scope mScope;
        private final CharSequence mState;


        private AuthorizedImplicitGrant(OAuth2Client client, Uri redirectUri, OAuth2Scope scope, CharSequence state)
        {
            mClient = client;
            mRedirectUri = redirectUri;
            mScope = scope;
            mState = state;
        }


        @Override
        public OAuth2AccessToken accessToken(HttpRequestExecutor executor) throws IOException, ProtocolError, ProtocolException
        {
            return new ImplicitGrantAccessToken(mRedirectUri, mScope, mState, mClient.defaultTokenTtl());
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


        @Deprecated
        @Override
        public OAuth2GrantState state()
        {
            return new AuthorizedImplicitGrantState(mRedirectUri, mScope, mState);
        }


        @Override
        public String encodedState()
        {
            return Base64.encodeBytes(
                new JsonText(new GrantState(AuthenticatedImplicitGrantFactory.class, new Text(mRedirectUri).toString(), mScope.toString(), mState.toString()))
                    .value()
                    .getBytes(StandardCharsets.UTF_8));
        }


        private final static class AuthenticatedImplicitGrantFactory implements OAuth2InteractiveGrantFactory
        {

            @Override
            public OAuth2InteractiveGrant grant(OAuth2Client client, JSONArray arguments)
            {
                if (arguments.length() != 3)
                {
                    throw new IllegalArgumentException("Can't restore grant from invalid state.");
                }
                return new AuthorizedImplicitGrant(client,
                    new LazyUri(new Precoded(arguments.getString(0))),
                    new StringScope(arguments.getString(1)),
                    arguments.getString(2));
            }
        }

    }


    /**
     * An {@link OAuth2GrantState} that represents the state of an Implicit Grant that was not confirmed by the user so far.
     *
     * @deprecated
     */
    @Deprecated
    private final static class InitialImplicitGrantState implements OAuth2InteractiveGrant.OAuth2GrantState
    {

        private static final long serialVersionUID = 1L;

        private final String mScopeString;
        private final String mState;


        public InitialImplicitGrantState(OAuth2Scope scope, CharSequence state)
        {
            mScopeString = scope.toString();
            // convert state to String, because a CharSequence may not be serializable
            mState = state.toString();
        }


        @Override
        public ImplicitGrant grant(OAuth2Client client)
        {
            return new ImplicitGrant(client, new StringScope(mScopeString), mState);
        }
    }


    /**
     * An {@link OAuth2GrantState} that represents the state of an authorized Implicit Grant.
     *
     * @deprecated
     */
    @Deprecated
    private final static class AuthorizedImplicitGrantState implements OAuth2InteractiveGrant.OAuth2GrantState
    {
        private static final long serialVersionUID = 1L;

        private final Uri mRedirectUri;
        private final String mScopeString;
        private final String mState;


        private AuthorizedImplicitGrantState(Uri redirectUri, OAuth2Scope scope, CharSequence state)
        {
            mRedirectUri = redirectUri;
            mScopeString = scope.toString();
            // convert state to String, because a CharSequence may not be serializable
            mState = state.toString();
        }


        @Override
        public OAuth2InteractiveGrant grant(OAuth2Client client)
        {
            return new AuthorizedImplicitGrant(client, mRedirectUri, new StringScope(mScopeString), mState);
        }
    }
}
