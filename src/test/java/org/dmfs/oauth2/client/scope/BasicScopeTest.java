package org.dmfs.oauth2.client.scope;

import org.junit.Test;

import static org.junit.Assert.*;


public class BasicScopeTest
{

    @Test
    public void testIsEmpty()
    {
        assertTrue(new BasicScope().isEmpty());
        assertFalse(new BasicScope("test").isEmpty());
        assertFalse(new BasicScope("test", "calendar").isEmpty());
    }


    @Test
    public void testHasToken()
    {
        assertFalse(new BasicScope().hasToken("test"));
        assertTrue(new BasicScope("test").hasToken("test"));
        assertFalse(new BasicScope("test").hasToken("calendar"));
        assertTrue(new BasicScope("test", "calendar").hasToken("test"));
        assertTrue(new BasicScope("test", "calendar").hasToken("calendar"));
        assertFalse(new BasicScope("test", "calendar").hasToken("foo"));
    }


    @Test
    public void testToString()
    {
        assertEquals("", new BasicScope().toString());
        assertEquals("test", new BasicScope("test").toString());
        assertEquals("test calendar", new BasicScope("test", "calendar").toString());
    }

}
