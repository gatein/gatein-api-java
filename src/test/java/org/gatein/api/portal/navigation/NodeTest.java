/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.api.portal.navigation;

import org.gatein.api.portal.navigation.Node;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class NodeTest
{
   //TODO: More tests

   @Test(expected = IllegalArgumentException.class)
   public void testnewNode_NullName()
   {
      new Node((String) null);
   }

   @Test(expected = NullPointerException.class)
   public void testNewNode_NullNode()
   {
      new Node((Node) null);
   }

   @Test
   public void testNode_Equals()
   {
      Node foo = new Node("foo");
      Node foo2 = new Node("foo");
      Node bar = new Node("bar");

      assertFalse(foo.equals(bar));
      assertTrue(foo.equals(foo2));
   }

   @Test
   public void testNode_Copy()
   {
      Node original = new Node("foo");
      Node copy = new Node(original);
      assertEquals(original, copy);
   }
}
