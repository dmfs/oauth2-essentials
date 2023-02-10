package org.dmfs.oauth2.client.utils;

import org.dmfs.express.json.elementary.Array;
import org.dmfs.express.json.elementary.DelegatingJsonValue;
import org.dmfs.express.json.elementary.Member;
import org.dmfs.express.json.elementary.Object;
import org.dmfs.jems2.iterable.Mapped;
import org.dmfs.jems2.iterable.Seq;
import org.dmfs.oauth2.client.OAuth2InteractiveGrant;


public final class GrantState extends DelegatingJsonValue
{
    public GrantState(Class<? extends OAuth2InteractiveGrant.OAuth2InteractiveGrantFactory> factoryClass, String... arguments)
    {
        this(factoryClass, new Seq<>(arguments));
    }


    public GrantState(Class<? extends OAuth2InteractiveGrant.OAuth2InteractiveGrantFactory> factoryClass, Iterable<String> arguments)
    {
        super(new Object(
            new Member("class", factoryClass.getName()),
            new Member("args", new Array(new Mapped<>(org.dmfs.express.json.elementary.String::new, arguments)))));
    }
}
