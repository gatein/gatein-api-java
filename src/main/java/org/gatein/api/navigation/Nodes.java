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

package org.gatein.api.navigation;

import org.gatein.api.Filter;
import org.gatein.api.IterableResult;
import org.gatein.api.Query;
import org.gatein.api.id.Id;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Nodes
{
   public static <T extends Node<T>> IterableResult<T> get(Query<T> query)
   {
      return null;
   }

   public static <T extends Node<T>> IterableResult<T> getWhere(Filter<T> filter)
   {
      return get(Query.<T>builder().where(filter).build());
   }

   public static <T extends Node<T>> T getSingleOrFail(Query<T> query)
   {
      IterableResult<T> nodes = get(query);
      int size = nodes.size();
      if (size == 1)
      {
         return nodes.iterator().next();
      }
      else
      {
         throw new IllegalStateException("You expected the " + query + " to return exactly one result but it returned " + size);
      }
   }

   public static <T extends Node<T>> T get(Id<T> id)
   {
      return null;
   }

   public static <T extends Node> IterableResult<T> getForUser(Id userId, Class<T> nodeClass)
   {
      return null;
   }

   public static <T extends Node> IterableResult<T> getForUser(Id userId, Node.Type<T> type)
   {
      return null;
   }

   public static class GroupNodeFilter extends Filter<Node>
   {
      private final Id group;

      public GroupNodeFilter(Id groupId)
      {
         this.group = groupId;
      }

      public boolean accept(Node item)
      {
         return item.hasOwner(group);
      }
   }
}
