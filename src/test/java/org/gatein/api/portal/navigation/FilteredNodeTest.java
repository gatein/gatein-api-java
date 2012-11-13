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

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class FilteredNodeTest
{


   @Test
   public void testFilter()
   {
      Node parent = new Node("parent");
      parent.addChild("child1").addChild("child1-1");
      parent.getChild("child1").addChild("child2-1");
      parent.getChild("child1").setVisibility(false);

      parent.addChild("child2").addChild("child2-1").addChild("child2-1-1");
      parent.getChild("child2").getChild("child2-1").addChild("child2-1-2").setVisibility(false);

      FilteredNode filtered = new FilteredNode(new Filter<Node>()
      {
         @Override
         public boolean accept(Node object)
         {
            return object.isVisible();
         }
      }, parent);

      assertFalse(filtered.hasChild("child1"));
      assertNull(filtered.getChild("child1"));

      assertEquals(1, filtered.size());
      assertNotNull(filtered.getChild("child2"));
      assertEquals(1, filtered.getChild("child2").size());
      assertNotNull(filtered.getChild("child2").getChild("child2-1"));
      assertEquals(1, filtered.getChild("child2").getChild("child2-1").size());
      assertNotNull("child2-1-1", filtered.getChild("child2").getChild("child2-1").getChild("child2-1-1"));

      assertEquals(-1, filtered.indexOf("child1"));
      assertEquals(0, filtered.indexOf("child2"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testFilter_SameChild()
   {
      Node parent = new Node("parent");
      parent.addChild("child1");
      parent.getChild("child1").addChild("child1-1");
      parent.getChild("child1").addChild("child1-2").setVisibility(false);

      FilteredNode filtered = new FilteredNode(new Filter<Node>()
      {
         @Override
         public boolean accept(Node object)
         {
            return object.isVisible();
         }
      }, parent);

      assertNull(filtered.getChild("child1").getChild("child1-2"));
      filtered.getChild("child1").addChild("child1-2");
   }
}
