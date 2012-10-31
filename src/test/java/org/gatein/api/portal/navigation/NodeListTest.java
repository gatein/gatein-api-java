/*
 * JBoss, a division of Red Hat
 * Copyright 2012, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

import java.util.ListIterator;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class NodeListTest extends TestCase
{
   public void testListIterator()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      nodeList.add(new Node("2"));
      nodeList.add(new Node("3"));

      ListIterator<Node> itr = nodeList.listIterator();
      itr.next();

      try
      {
         itr.add(new Node("2"));
         fail("Expected error");
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         itr.set(new Node("2"));
         fail("Expected error");
      }
      catch (IllegalArgumentException e)
      {
      }

      itr.set(new Node("1"));
   }
}
