/*
 * JBoss, a division of Red Hat
 * Copyright 2011, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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

package org.gatein.api.util;

import org.gatein.api.id.Id;
import org.gatein.api.id.Identifiable;
import org.testng.annotations.Test;

/** @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a> */
public abstract class HierarchicalContainerTestCase
{

   static interface Identified extends Identifiable<Identified> { }

   protected HierarchicalContainer<String, Identified> container;

   @Test
   public void createAndAddShouldBeIdempotent()
   {
      String foo = "foo";
      assert container.createAndAdd(foo).equals(container.createAndAdd(container.getIdForChild(foo)));

      String bar = "bar";
      assert container.createAndAdd(container.getIdForChild(bar)).equals(container.createAndAdd(bar));
   }

   @Test
   public void keyAndIdEquivalenceShouldWorkFine()
   {
      String fooName = "foo";
      Identifiable foo = container.createAndAdd(fooName);

      Id fooId = foo.getId();
      assert fooId.equals(container.getIdForChild(fooName));

      assert container.contains(fooName);
      assert container.contains(fooId);

      assert foo.equals(container.get(fooName));
      assert foo.equals(container.get(fooId));
   }
}
