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
import org.gatein.api.Portal;
import org.gatein.api.id.Id;
import org.gatein.api.organization.Group;
import org.gatein.api.organization.User;
import org.gatein.api.traits.Titled;

import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Nodes
{
   public static <T extends Node> List<T> get(Filter<T> filter)
   {
      return null;
   }

   public static <T extends Node> T getSingleOrFail(Filter<T> filter)
   {
      List<T> nodes = get(filter);
      if (nodes.size() != 1)
      {
         throw new IllegalStateException("Excepted filtered output to only return one result. Got " + nodes.size());
      }

      return nodes.get(0);
   }

   public static List<Node> forGroup(Id<Group> groupId)
   {
      return get(new GroupNodeFilter(groupId));
   }

   public static Node get(Id id)
   {
      return null;
   }

   public static <T extends Node> Collection<T> getForUser(Id<User> userId, Class<T> nodeClass)
   {
      return null;
   }

   public static class GroupNodeFilter extends Filter<Node>
   {
      private final Id<Group> group;

      public GroupNodeFilter(Id<Group> groupId)
      {
         this.group = groupId;
      }

      public boolean accept(Node item)
      {
         return item.hasOwner(group);
      }
   }
}
