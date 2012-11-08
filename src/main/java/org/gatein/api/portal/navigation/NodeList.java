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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
class NodeList extends ArrayList<Node>
{
   private boolean loaded;

   final Node parent;

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
   public Node set(int index, Node node)
   {
      if (node == null) throw new IllegalArgumentException("node cannot be null");

//      //TODO: Would we rather do this hack job, or just implement our own List like data structure and have a Nodes.sort() or something.
//      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//      for (StackTraceElement ste : stackTrace)
//      {
//         if (ste.getClassName().equals(Collections.class.getName()) && ste.getMethodName().equals("sort"))
//         {
//            return super.set(index, node);
//         }
//      }
      boolean checkExists = !get(index).getName().equals(node.getName());
      checkAdd(node, checkExists);
      return super.set(index, node);
   }

   @Override
   public Node remove(int index)
   {
      Node removed = super.remove(index);
      removed.setParent(null);

      return removed;
   }

   @Override
   public boolean remove(Object o)
   {
      boolean removed = super.remove(o);
      if (removed && o instanceof Node)
      {
         ((Node) o).setParent(null);
      }

      return removed;
   }

   @Override
   protected void removeRange(int fromIndex, int toIndex)
   {
      List<Node> removed = new ArrayList<Node>(subList(fromIndex, toIndex));
      super.removeRange(fromIndex, toIndex);
      for (Node node : removed)
      {
         node.setParent(null);
      }
   }

   @Override
   public void clear()
   {
      for (Node node : this)
      {
         node.setParent(null);
      }
      super.clear();
   }

   public void sort(Comparator<Node> comparator)
   {
      Node[] nodes = toArray(new Node[size()]);
      Arrays.sort(nodes, comparator);
      for (int i=0; i<nodes.length; i++)
      {
         super.set(i, nodes[i]);
      }
   }

   public Iterable<Node> filter(Filter<Node> filter)
   {
      return new FilteredNodeList(filter, this);
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
      if (node == null) throw new IllegalArgumentException("Node cannot be null");
      if (node == parent) throw new IllegalArgumentException("Cannot add itself as a child.");
      if (node.getParent() != null && parent != node.getParent())
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
}
