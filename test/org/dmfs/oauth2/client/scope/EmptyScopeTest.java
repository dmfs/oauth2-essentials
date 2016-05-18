package org.dmfs.oauth2.client.scope;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


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
