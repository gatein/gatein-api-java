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

import java.util.ArrayList;
import java.util.Collection;
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
      // Parent should never be null for a NodeList
      if (parent == null) throw new IllegalArgumentException("parent cannot be null");
      this.parent = parent;
   }

   public NodeList(Node parent, NodeList original)
   {
      super(original.size());

      // Parent should never be null for a NodeList
      if (parent == null) throw new IllegalArgumentException("parent cannot be null");
      this.parent = parent;

      // Copying NodeList by copying all nodes recursively
      for (Node node : original)
      {
         if (add(new Node(node)))
         {
            node.setParent(parent);
         }
      }
   }

   public Node get(String name)
   {
      return findNode(name);
   }

   public boolean remove(String name)
   {
      Node node = findNode(name);
      return (node != null) && super.remove(node);
   }

   public boolean isLoaded()
   {
      return loaded;
   }

   public void setLoaded(boolean loaded)
   {
      this.loaded = loaded;
   }

   @Override
   public void add(int index, Node node)
   {
      _add(index, node);
   }

   @Override
   public boolean add(Node node)
   {
      return _add(null, node);
   }

   @Override
   public boolean addAll(Collection<? extends Node> nodes)
   {
      return _addAll(null, nodes);
   }

   @Override
   public boolean addAll(int index, Collection<? extends Node> nodes)
   {
      return _addAll(index, nodes);
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

   private Node findNode(String name)
   {
      for (Node node : this)
      {
         if (node.getName().equals(name)) return node;
      }

      return null;
   }

   private boolean _add(Integer index, Node node)
   {
      checkAdd(node, true);

      boolean added;
      if (index == null)
      {
         added = super.add(node);
      }
      else
      {
         super.add(index, node);
         added = true;
      }

      if (added)
      {
         node.setParent(parent);
      }

      return added;
   }

   private boolean _addAll(Integer index, Collection<? extends Node> nodes)
   {
      for (Node node : nodes)
      {
         checkAdd(node, true);
      }

      boolean added;
      if (index == null)
      {
         added = super.addAll(nodes);
      }
      else
      {
         added = super.addAll(index, nodes);
      }

      if (added)
      {
         for (Node node : nodes)
         {
            node.setParent(parent);
         }
      }

      return added;
   }

   private void checkAdd(Node node, boolean checkExists)
   {
      if (node == parent) throw new IllegalArgumentException("Cannot add itself as a child.");
      if (node.getParent() != null)
         throw new IllegalArgumentException(
            "Node being added is already associated with a parent. You can copy the node by passing it to the constructor which will remove the parent association.");

      if (checkExists && get(node.getName()) != null)
         throw new IllegalArgumentException("Child with name " + node.getName() + " already exists.");

      checkCyclic(node);
   }

   private void checkCyclic(Node node)
   {
      Node current = parent;
      while ((current = current.getParent()) != null)
      {
         if (node == current) throw new IllegalArgumentException("Cannot add '" + node.getName() +"' to '" + parent.getName() + "', circular reference detected.");
      }
   }

   private class ListItr implements ListIterator<Node>
   {
      private ListIterator<Node> itr;

      public ListItr(ListIterator<Node> itr)
      {
         this.itr = itr;
      }

      @Override
      public void add(Node node)
      {
         checkAdd(node, true);
         itr.add(node);
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
      public void set(Node node)
      {
         int lastRet = itr.nextIndex() - 1;
         boolean checkExists = !get(lastRet).getName().equals(node.getName());
         checkAdd(node, checkExists);
         itr.set(node);
      }
   }
}
