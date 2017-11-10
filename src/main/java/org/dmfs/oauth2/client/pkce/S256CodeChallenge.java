/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.oauth2.client.pkce;

import net.iharder.Base64;
import org.dmfs.httpessentials.types.CharToken;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.jems.messagedigest.elementary.Sha256;
import org.dmfs.jems.single.elementary.Digest;

import java.io.IOException;


/**
 * A {@link PkceCodeChallenge} that uses SHA-256 to protect the verifier.
 *
 * @author Marten Gajda
 */
public final class S256CodeChallenge implements PkceCodeChallenge
{
    private final CharSequence mCodeVerifier;


    public S256CodeChallenge(CharSequence codeVerifier)
    {
        mCodeVerifier = codeVerifier;
    }


    @Override
    public Token method()
    {
        return new CharToken("S256");
    }


    @Override
    public CharSequence challenge()
    {
        try
        {
            String result = Base64.encodeBytes(new Digest(new Sha256(), mCodeVerifier).value(), Base64.URL_SAFE);
            // Note, the code challenge parameter doesn't support equals chars, so we have to remove any padding
            if (result.endsWith("=="))
            {
                return result.substring(0, result.length() - 2);
            }
            if (result.endsWith("="))
            {
                return result.substring(0, result.length() - 1);
            }
            return result;
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException while operating on strings");
        }
    }
}
