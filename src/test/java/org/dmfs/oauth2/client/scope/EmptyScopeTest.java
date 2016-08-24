package org.dmfs.oauth2.client.scope;

import org.junit.Test;

import static org.junit.Assert.*;


public class EmptyScopeTest
{

    @Test
    public void testIsEmpty()
    {
        assertTrue(new EmptyScope().isEmpty());
    }


    @Test
    public void testHasToken()
    {
        assertFalse(new EmptyScope().hasToken("test"));
    }


    @Test
    public void testToString()
    {
        assertEquals("", new EmptyScope().toString());
    }

}
