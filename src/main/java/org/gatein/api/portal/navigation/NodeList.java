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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
class NodeList extends ArrayList<Node>
{
   private boolean loaded;

   private final Node parent;

   public NodeList(Node parent)
   {
      this.parent = parent;
   }

   public NodeList(Node parent, int initialSize)
   {
      super(initialSize);
      this.parent = parent;
   }

   @Override
   public void add(int index, Node n)
   {
      checkAdd(n, true);
      super.add(index, n);
      n.setParent(parent);
   }

   @Override
   public boolean add(Node n)
   {
      checkAdd(n, true);

      if (super.add(n))
      {
         n.setParent(parent);
         return true;
      }
      else
      {
         return false;
      }
   }

   @Override
   public boolean addAll(Collection<? extends Node> c)
   {
      for (Node n : c)
      {
         checkAdd(n, true);
      }

      if (super.addAll(c))
      {
         for (Node n : c)
         {
            n.setParent(parent);
         }
         return true;
      }
      else
      {
         return false;
      }
   }

   @Override
   public boolean addAll(int index, Collection<? extends Node> c)
   {
      for (Node n : c)
      {
         checkAdd(n, true);
      }

      if (super.addAll(index, c))
      {
         for (Node n : c)
         {
            n.setParent(parent);
         }
         return true;
      }
      else
      {
         return false;
      }
   }

   private void checkAdd(Node n, boolean checkExists)
   {
      if (n == parent) throw new IllegalArgumentException("Cannot add itself as a child.");
      if (n.getParent() != null)
         throw new IllegalArgumentException(
               "Node being added is already associated with a parent. You can copy the node by passing it to the constructor which will remove the parent association.");

      if (checkExists && get(n.getName()) != null)
         throw new IllegalArgumentException("Child with name " + n.getName() + " already exists.");

      checkCyclic();
   }

   private void checkCyclic()
   {
      if (parent instanceof Node)
      {
         Node c = (Node) parent;
         Node current = (Node) c;
         while ((current = current.getParent()) != null)
         {
            if (c == current.getParent()) throw new RuntimeException("Circular reference detected for node " + c.getName());
         }
      }
   }

   public Node get(String name)
   {
      Iterator<Node> itr = iterator();
      while (itr.hasNext())
      {
         Node n = itr.next();
         if (n.getName().equals(name))
         {
            return n;
         }
      }
      return null;
   }

   public boolean isLoaded()
   {
      return loaded;
   }

   @Override
   public ListIterator<Node> listIterator()
   {
      return new ListItr(super.listIterator());
   }

   @Override
   public ListIterator<Node> listIterator(int index)
   {
      return new ListItr(super.listIterator(index));
   }

   public boolean remove(String name)
   {
      Iterator<Node> itr = iterator();
      while (itr.hasNext())
      {
         Node n = itr.next();
         if (n.getName().equals(name))
         {
            itr.remove();
            return true;
         }
      }
      return false;
   }

   public void setLoaded(boolean loaded)
   {
      this.loaded = loaded;
   }

   private class ListItr implements ListIterator<Node>
   {
      private ListIterator<Node> itr;

      public ListItr(ListIterator<Node> itr)
      {
         this.itr = itr;
      }

      @Override
      public void add(Node e)
      {
         checkAdd(e, true);
         itr.add(e);
      }

      @Override
      public boolean hasNext()
      {
         return itr.hasNext();
      }

      @Override
      public boolean hasPrevious()
      {
         return itr.hasPrevious();
      }

      @Override
      public Node next()
      {
         return itr.next();
      }

      @Override
      public int nextIndex()
      {
         return itr.nextIndex();
      }

      @Override
      public Node previous()
      {
         return itr.previous();
      }

      @Override
      public int previousIndex()
      {
         return itr.previousIndex();
      }

      @Override
      public void remove()
      {
         itr.remove();
      }

      @Override
      public void set(Node e)
      {
         int lastRet = itr.nextIndex() - 1;
         boolean checkExists = !get(lastRet).getName().equals(e.getName());
         checkAdd(e, checkExists);
         itr.set(e);
      }
   }
}
