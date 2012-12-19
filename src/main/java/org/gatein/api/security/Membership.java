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

package org.gatein.api.security;

import org.gatein.api.internal.StringSplitter;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Membership
{
   public static final String ANY = "*";

   public static Membership any(String... group)
   {
      return new Membership(ANY, new Group(group));
   }

   private final String membershipType;
   private final Group group;

   public Membership(String membershipType, Group group)
   {
      if (membershipType == null) throw new IllegalArgumentException("membershipType cannot be null");
      if (group == null) throw new IllegalArgumentException("group cannot be null");

      this.membershipType = membershipType;
      this.group = group;
   }

   public Membership(User user)
   {
      this.membershipType = user.getId();
      this.group = null;
   }

   public String getMembershipType()
   {
      return membershipType;
   }

   public Group getGroup()
   {
      return group;
   }

   @Override
   public String toString()
   {
      if (group == null)
      {
         return membershipType;
      }
      else
      {
         return membershipType + ":" + group.getId();
      }
   }

   public static Membership fromString(String membership)
   {
      if (membership == null) throw new IllegalArgumentException("membership cannot be null");

      String[] parts = StringSplitter.splitter(":").split(membership);
      if (parts.length == 1)
      {
         return new Membership(new User(parts[0]));
      }
      else if (parts.length == 2)
      {
         return new Membership(parts[0], new Group(parts[1]));
      }
      else
      {
         throw new IllegalArgumentException("Invalid membership string " + membership);
      }
   }
}
