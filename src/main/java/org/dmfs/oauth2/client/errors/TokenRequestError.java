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

package org.dmfs.oauth2.client.errors;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URI;


/**
 * A {@link ProtocolError} thrown when there was something wrong with a token request.
 *
 * @author Marten Gajda
 */
public final class TokenRequestError extends ProtocolError
{
    private static final long serialVersionUID = 1L;

    private final String mErrorResponse;

    /**
     * {@link JSONObject} is not a {@link Serializable}, so we mark it transient and restore it from String if necessary.
     */
    private transient JSONObject mErrorObject;


    public TokenRequestError(JSONObject errorObject) throws JSONException
    {
        super(errorObject.getString("error"));
        mErrorObject = errorObject;
        mErrorResponse = errorObject.toString();
    }


    /**
     * Returns the error code token that was returned by the server.
     *
     * @return A String containing the error code token.
     */
    public String error()
    {
        return errorObject().optString("error", "unknown");
    }


    /**
     * Returns the error description that was returned by the server.
     *
     * @return A String containing the error description.
     */
    public String description()
    {
        return errorObject().optString("error_description", "");
    }


    /**
     * Returns the optional error URI returned by the server.
     *
     * @return A URI pointing to a more descriptive error page or <code>null</code>.
     */
    public URI uri()
    {
        return errorObject().has("error_uri") ? URI.create(errorObject().optString("error_uri")) : null;
    }


    private JSONObject errorObject()
    {
        if (mErrorObject == null)
        {
            try
            {
                mErrorObject = new JSONObject(mErrorResponse);
            }
            catch (JSONException e)
            {
                throw new RuntimeException(String.format("Can't restore JSONObject from String", mErrorResponse), e);
            }
        }
        return mErrorObject;
    }
}
