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

import java.util.Iterator;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
class FilteredNodeList implements Iterable<Node>
{
   private final Filter<Node> filter;
   private final NodeList list;

   public FilteredNodeList(Filter<Node> filter, NodeList list)
   {
      this.filter = filter;
      this.list = list;
   }

   @Override
   public Iterator<Node> iterator()
   {
      return new FilteredNodeListIterator();
   }

   private class FilteredNodeListIterator implements Iterator<Node>
   {
      private Node next;
      private int cursor;
      private boolean findNext;

      private FilteredNodeListIterator()
      {
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
         throw new UnsupportedOperationException();
      }

      private Node findNext()
      {
         while (cursor < list.size())
         {
            Node node = list.get(cursor++);
            if (filter.accept(node))
            {
               return new FilteredNode(filter, node);
            }
         }

         return null;
      }
   }
}
