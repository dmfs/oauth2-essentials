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

package org.dmfs.oauth2.client.http.decorators;

import net.iharder.Base64;
import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.HeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.UpdatedHeaders;

import java.io.UnsupportedEncodingException;


/**
 * Header decoration for adding a Basic Authorization header.
 * <p>
 * Null username or password is treated as empty strings.
 *
 * @author Marten Gajda
 * @author Gabor Keszthelyi
 * @see <a href="https://tools.ietf.org/html/rfc2617#section-2">RFC2617</a>
 */
public final class BasicAuthHeaderDecoration implements Decoration<Headers>
{
    // TODO: use a generic authorization header instead (once we have one)
    private final HeaderType<String> AUTHORIZATION_HEADER_TYPE = new BasicSingletonHeaderType<String>("Authorization", new PlainStringHeaderConverter());

    private final String mUsername;
    private final String mPassword;


    public BasicAuthHeaderDecoration(String username, String password)
    {
        if (username == null)
        {
            throw new IllegalArgumentException("username must not be null");
        }
        if (password == null)
        {
            throw new IllegalArgumentException("password must not be null");
        }
        mUsername = username;
        mPassword = password;
    }


    @Override
    public Headers decorated(Headers original)
    {
        String authHeaderValue = "Basic " + Base64.encodeBytes(usernameAndPasswordBytes());
        return new UpdatedHeaders(original, AUTHORIZATION_HEADER_TYPE.entity(authHeaderValue));
    }


    private byte[] usernameAndPasswordBytes()
    {
        try
        {
            return String.format("%s:%s", mUsername, mPassword).getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Charset UTF-8 not supported by runtime!", e);
        }
    }
}
