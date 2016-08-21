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

import org.dmfs.oauth2.client.http.entities.XWwwFormUrlEncodedEntity;
import org.dmfs.oauth2.client.utils.ImmutableEntry;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * A basic {@link OAuth2AuthorizationRequest} implementation.
 * <p/>
 * Note: Usually you don't need to instantiate this directly.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class BasicOAuth2AuthorizationRequest implements OAuth2AuthorizationRequest
{
    private final ImmutableEntry[] mParameters;


    public BasicOAuth2AuthorizationRequest(String responseType, String state)
    {
        this(new ImmutableEntry("response_type", responseType), new ImmutableEntry("state", state));
    }


    public BasicOAuth2AuthorizationRequest(String responseType, OAuth2Scope scope, String state)
    {
        this(new ImmutableEntry("response_type", responseType), new ImmutableEntry("scope", scope.toString()),
                new ImmutableEntry("state", state));
    }


    private BasicOAuth2AuthorizationRequest(ImmutableEntry... parameters)
    {
        mParameters = parameters;
    }


    @Override
    public OAuth2AuthorizationRequest withClientId(String clientId)
    {
        return withEntry(new ImmutableEntry("client_id", clientId));
    }


    @Override
    public OAuth2AuthorizationRequest withRedirectUri(URI redirectUri)
    {
        return withEntry(new ImmutableEntry("redirect_uri", redirectUri.toASCIIString()));
    }


    private OAuth2AuthorizationRequest withEntry(ImmutableEntry entry)
    {
        // prepare a result that can hold an additional value
        final ImmutableEntry[] result = new ImmutableEntry[mParameters.length + 1];

        for (int i = 0, count = mParameters.length; i < count; ++i)
        {
            ImmutableEntry parameter = mParameters[i];
            if (parameter.getKey().equals(entry.getKey()))
            {
                // the key already exists, take the shortcut and update a cloned array
                ImmutableEntry[] clonedResult = mParameters.clone();
                clonedResult[i] = entry;
                return new BasicOAuth2AuthorizationRequest(clonedResult);
            }
            // copy the value
            result[i] = mParameters[i];
        }

        result[mParameters.length] = entry;
        return new BasicOAuth2AuthorizationRequest(result);
    }


    @Override
    public URI authorizationUri(URI authorizationEndpoint)
    {
        // TODO: refuse to return a URI without a client id.
        try
        {
            // this is not optimal, but we need to create two URIs to make sure we don't append our query to an existing query. That's probably the easiest way
            // for now.
            return URI.create(String.format("%s?%s",
                    new URI(authorizationEndpoint.getScheme(), authorizationEndpoint.getAuthority(),
                            authorizationEndpoint.getPath(), null, null).toASCIIString(),
                    new XWwwFormUrlEncodedEntity(mParameters).toString()));
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException("Can't create valid authorization URI", e);
        }
    }

}
