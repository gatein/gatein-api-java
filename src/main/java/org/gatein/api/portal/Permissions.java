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

package org.gatein.api.portal;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Permissions
{
   private static final Permission EVERYONE = new Permission();

   public static Permission any(String... group)
   {
      return new Permission(new Group.Membership(Group.Membership.ANY, new Group(group)));
   }

   public static Permission everyone()
   {
      return EVERYONE;
   }

   public static Permission membership(String membershipType, String... group)
   {
      return new Permission(new Group.Membership(membershipType, new Group(group)));
   }

   public static Permission memberships(String... memberships)
   {
      if (memberships == null) throw new IllegalArgumentException("memberships cannot be null");

      Set<Group.Membership> groupMemberships = new LinkedHashSet<Group.Membership>(memberships.length);
      for (String membership : memberships)
      {
         groupMemberships.add(Group.Membership.fromString(membership));
      }

      return new Permission(groupMemberships);
   }
}
