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

import org.gatein.api.util.Filter;
import org.junit.Test;

import java.util.Iterator;
import java.util.ListIterator;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class FilteredNodeListTest
{
   @Test
   public void testFilter_Iterator()
   {
      Node parent = new Node("parent");
      parent.addChild("child1").setVisibility(false);
      parent.addChild("child2");
      parent.addChild("child3").setVisibility(false);
      parent.addChild("child4");
      parent.addChild("child5").setVisibility(false);

      FilteredNodeList list = new FilteredNodeList(new Filter<Node>()
      {
         @Override
         public boolean accept(Node object)
         {
            return object.isVisible();
         }
      }, parent.nodeList());

      Iterator<Node> itr = list.iterator();

      assertTrue(itr.hasNext());
      Node next = itr.next();
      assertNotNull(next);
      assertEquals("child2", next.getName());

      assertTrue(itr.hasNext());
      next = itr.next();
      assertNotNull(next);
      assertEquals("child4", next.getName());

      assertFalse(itr.hasNext());

      parent.getChild("child5").setVisibility(true);

      Node last = null;
      int count = 0;
      for (Node child : list)
      {
         last = child;
         count++;
      }
      assertEquals(3, count);
      assertNotNull(last);
      assertEquals("child5", last.getName());
   }

   @Test
   public void testFilter_ListIterator()
   {
      Node parent = new Node("parent");
      parent.addChild("child1").setVisibility(false);
      parent.addChild("child2");
      parent.addChild("child3");
      parent.addChild("child4").setVisibility(false);
      parent.addChild("child5");
      parent.addChild("child6").setVisibility(false);
      parent.addChild("child7").setVisibility(false);

      FilteredNodeList list = new FilteredNodeList(new Filter<Node>()
      {
         @Override
         public boolean accept(Node object)
         {
            return object.isVisible();
         }
      }, parent.nodeList());

      ListIterator<Node> itr = list.listIterator();
      int index = 0;
      while (itr.hasNext())
      {
         assertEquals(index, itr.nextIndex());
         Node node = itr.next();
         assertNotNull(node);
         if (index == 0)
         {
            assertEquals("child2", node.getName());
            index++;
         }
         else if (index == 1)
         {
            assertEquals("child3", node.getName());
            index++;
         }
         else if (index == 2)
         {
            assertEquals("child5", node.getName());
            index++;
         }
         else
         {
            fail("Unknown index " + index);
         }
      }

      assertEquals(3, itr.nextIndex());

      while (itr.hasPrevious())
      {
         assertEquals(--index, itr.previousIndex());
         Node node = itr.previous();
         if (index == 2)
         {
            assertEquals("child5", node.getName());
         }
         else if (index == 1)
         {
            assertEquals("child3", node.getName());
         }
         else if (index == 0)
         {
            assertEquals("child2", node.getName());
         }
         else
         {
            fail("Unknown index " + index);
         }
      }

      assertEquals(-1, itr.previousIndex());
   }
}
