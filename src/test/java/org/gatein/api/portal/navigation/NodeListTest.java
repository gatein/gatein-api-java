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

import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class NodeListTest
{
   @Test
   public void testAdd()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      assertEquals(1, nodeList.size());
      assertEquals("1", nodeList.get(0).getName());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAdd_SameChild()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      nodeList.add(new Node("1"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAdd_NullChild()
   {
      new NodeList(new Node("parent")).add(null);
   }

   @Test
   public void testSet()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      nodeList.set(0, new Node("1"));
      assertEquals(1, nodeList.size());
      assertEquals("1", nodeList.get(0).getName());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSet_SameChild()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      nodeList.add(new Node("2"));
      nodeList.set(1, new Node("1"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSet_NullChild()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      nodeList.set(0, null);
   }

   @Test
   public void testSort()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("3"));
      nodeList.add(new Node("2"));
      nodeList.add(new Node("1"));

      nodeList.sort(new Comparator<Node>()
      {
         @Override
         public int compare(Node o1, Node o2)
         {
            return o1.getName().compareTo(o2.getName());
         }
      });

      assertEquals(3, nodeList.size());
      assertEquals("1", nodeList.get(0).getName());
      assertEquals("2", nodeList.get(1).getName());
      assertEquals("3", nodeList.get(2).getName());
   }

   @Test
   public void testIterator()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      nodeList.add(new Node("2"));
      nodeList.add(new Node("3"));

      Iterator<Node> itr = nodeList.iterator();
      assertEquals("1", itr.next().getName());
      assertEquals("2", itr.next().getName());
      assertEquals("3", itr.next().getName());
   }

   @Test
   public void testIterator_Remove()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      Node remove = new Node("remove");
      nodeList.add(remove);
      assertNotNull(remove.getParent());

      Iterator<Node> itr = nodeList.iterator();
      itr.next();
      itr.remove();

      assertEquals(0, nodeList.size());
      assertNull(remove.getParent());
   }

   @Test
   public void testListIterator_Add()
   {
      NodeList nodeList = new NodeList(new Node("parent"));

      ListIterator<Node> itr = nodeList.listIterator();
      itr.add(new Node("1"));
      assertEquals(1, nodeList.size());
      assertEquals("1", nodeList.get(0).getName());

      nodeList.clear();
      nodeList.add(new Node("2"));
      itr = nodeList.listIterator();
      itr.add(new Node("1"));
      assertEquals(2, nodeList.size());
      assertEquals("1", nodeList.get(0).getName());
      assertEquals("2", nodeList.get(1).getName());

      nodeList.clear();
      nodeList.add(new Node("1"));
      itr = nodeList.listIterator();
      itr.next();
      itr.add(new Node("2"));
      assertEquals(2, nodeList.size());
      assertEquals("1", nodeList.get(0).getName());
      assertEquals("2", nodeList.get(1).getName());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testListIterator_Add_SameChild()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));

      nodeList.listIterator().add(new Node("1"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testListIterator_Add_NullChild()
   {
      new NodeList(new Node("parent")).listIterator().add(null);
   }

   @Test
   public void testListIterator_Set()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));

      ListIterator<Node> itr = nodeList.listIterator();
      itr.next();
      itr.set(new Node("1"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testListIterator_Set_SameChild()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));
      nodeList.add(new Node("2"));

      ListIterator<Node> itr = nodeList.listIterator();
      itr.next();
      itr.next();
      itr.set(new Node("1"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testListIterator_Set_NullChild()
   {
      NodeList nodeList = new NodeList(new Node("parent"));
      nodeList.add(new Node("1"));

      ListIterator<Node> itr = nodeList.listIterator();
      itr.next();
      itr.set(null);
   }
}
