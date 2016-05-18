package org.dmfs.oauth2.client.scope;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class StringScopeTest
{

	@Test
	public void testIsEmpty()
	{
		assertTrue(new StringScope("").isEmpty());
		assertFalse(new StringScope("test").isEmpty());
		assertFalse(new StringScope("test calendar").isEmpty());
	}


	@Test
	public void testHasToken()
	{
		assertFalse(new StringScope("").hasToken("test"));
		assertTrue(new StringScope("test").hasToken("test"));
		assertFalse(new StringScope("test").hasToken("calendar"));
		assertTrue(new StringScope("test calendar").hasToken("test"));
		assertTrue(new StringScope("test calendar").hasToken("calendar"));
		assertFalse(new StringScope("test calendar").hasToken("foo"));
	}


	@Test
	public void testToString()
	{
		assertEquals("", new StringScope("").toString());
		assertEquals("test", new StringScope("test").toString());
		assertEquals("test calendar", new StringScope("test calendar").toString());
	}

}
