package org.dmfs.oauth2.client.state;

import net.iharder.Base64;

import org.dmfs.jems2.Function;
import org.dmfs.oauth2.client.OAuth2Client;
import org.dmfs.oauth2.client.OAuth2InteractiveGrant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import static org.dmfs.oauth2.client.OAuth2InteractiveGrant.OAuth2InteractiveGrantFactory;


/**
 * A Function to return an {@link OAuth2InteractiveGrant} for a {@link String} that was previously retrieved from
 * {@link OAuth2InteractiveGrant#encodedState()}.
 */
public final class InteractiveGrantFactory implements Function<String, OAuth2InteractiveGrant>, java.util.function.Function<String, OAuth2InteractiveGrant>
{
    private final OAuth2Client mOAuth2Client;


    /**
     * Creates an {@link OAuth2InteractiveGrantFactory} for the given {@link OAuth2Client}.
     */
    public InteractiveGrantFactory(OAuth2Client oAuth2Client)
    {
        mOAuth2Client = oAuth2Client;
    }


    @Override
    public OAuth2InteractiveGrant value(String state)
    {
        JSONObject object;
        try
        {
            object = new JSONObject(new String(Base64.decode(state), StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to decode state", e);
        }
        JSONArray args = object.getJSONArray("args");
        String grantClass = object.getString("class");

        try
        {
            Constructor<OAuth2InteractiveGrantFactory> constructor = (Constructor<OAuth2InteractiveGrantFactory>) Class.forName(grantClass)
                .getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance().grant(mOAuth2Client, args);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
               ClassCastException exception)
        {
            throw new IllegalArgumentException("Can't Instantiate OAuth2InteractiveGrantFactory implementation " + grantClass, exception);
        }
    }


    @Override
    public OAuth2InteractiveGrant apply(String encodedState)
    {
        return value(encodedState);
    }
}
