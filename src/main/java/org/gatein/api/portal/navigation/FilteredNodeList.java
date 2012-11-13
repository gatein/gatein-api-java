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

import org.gatein.api.internal.DelegateList;
import org.gatein.api.util.Filter;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
class FilteredNodeList extends DelegateList<Node> implements List<Node>
{
   private final Filter<Node> filter;

   public FilteredNodeList(Filter<Node> filter, NodeList list)
   {
      super(list);
      this.filter = filter;
   }

   @Override
   public Node get(int index)
   {
      int size = delegate.size();
      for (int i=index; i<size; i++)
      {
         Node child = delegate.get(i);
         if (filter.accept(child)) return new FilteredNode(filter, child);
      }

      return null;
   }

   @Override
   public int indexOf(Object o)
   {
      int index = -1;
      for (Node node : delegate)
      {
         if (filter.accept(node))
         {
            index++;
            if (o instanceof FilteredNode)
            {
               o = ((FilteredNode) o).delegate;
            }
            if (node == o)
            {
               return index;
            }
         }
      }

      return -1;
   }

   @Override
   public int lastIndexOf(Object o)
   {
      return indexOf(o);
   }

   @Override
   public int size()
   {
      int size = 0;
      for (Node node : delegate)
      {
         if (filter.accept(node))
         {
            size++;
         }
      }

      return size;
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

   @Override
   public Iterator<Node> iterator()
   {
      return new Itr(super.iterator());
   }

   private class Itr implements Iterator<Node>
   {
      final Iterator<Node> iterator;
      Node next;
      boolean findNext;

      private Itr(Iterator<Node> iterator)
      {
         this.iterator = iterator;
         findNext = true;
      }

      @Override
      public boolean hasNext()
      {
         if (findNext)
         {
            next = findNext();
            findNext = false;
         }

         return next != null;
      }

      @Override
      public Node next()
      {
         if (findNext)
         {
            next = findNext();
         }
         findNext = true;
         return next;
      }

      @Override
      public void remove()
      {
         iterator.remove();
      }

      Node findNext()
      {
         while (iterator.hasNext())
         {
            Node node = iterator.next();
            if (filter.accept(node))
            {
               return new FilteredNode(filter, node);
            }
         }

         return null;
      }
   }

   private class ListItr extends Itr implements ListIterator<Node>
   {
      ListIterator<Node> listIterator;
      Node previous;
      boolean findPrevious;

      private ListItr(ListIterator<Node> iterator)
      {
         super(iterator);
         this.listIterator = iterator;
         findPrevious = true;
      }

      @Override
      public boolean hasNext()
      {
         boolean hasNext = super.hasNext();
         if (hasNext)
         {
            findPrevious = false;
         }

         return hasNext;
      }

      @Override
      public boolean hasPrevious()
      {
         if (findPrevious)
         {
            previous = findPrevious();
            findPrevious = false;
         }

         return previous != null;
      }

      @Override
      public Node next()
      {
         findPrevious = true;
         return super.next();
      }

      @Override
      public Node previous()
      {
         if (findPrevious)
         {
            previous = findPrevious();
         }

         findPrevious = true;
         return previous;
      }

      @Override
      public int nextIndex()
      {
         if (findNext)
         {
            next = findNext();
            findNext = false;
         }

         return (next == null) ? size() : indexOf(next);
      }

      @Override
      public int previousIndex()
      {
         if (findPrevious)
         {
            previous = findPrevious();
            findPrevious = false;
         }

         return indexOf(previous);
      }

      @Override
      public void set(Node node)
      {
         listIterator.set(node);
      }

      @Override
      public void add(Node node)
      {
         findPrevious = true;
         listIterator.add(node);
      }

      private Node findPrevious()
      {
         while (listIterator.hasPrevious())
         {
            Node node = listIterator.previous();
            if (filter.accept(node))
            {
               return new FilteredNode(filter, node);
            }
         }

         return null;
      }
   }
}
