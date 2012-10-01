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

import org.gatein.api.internal.Objects;
import org.gatein.api.internal.Strings;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Group
{
   private final String id;

   public Group(String... group)
   {
      if (group == null) throw new IllegalArgumentException("group cannot be null");

      this.id = Strings.joiner("/").leading().trimToNull().ignoreNulls().join(group);
   }

   public Group(String id)
   {
      this(Strings.splitter("/").trim().ignoreEmptyStrings().split(id));
   }

   public String getId()
   {
      return id;
   }

   @Override
   public String toString()
   {
      return Objects.toStringBuilder(getClass())
         .add("groupId", id)
         .toString();
   }

   public static class Membership
   {
      public static final String ANY = "*";

      private final String membershipType;
      private final Group group;

      public Membership(String membershipType, Group group)
      {
         if (membershipType == null) throw new IllegalArgumentException("membershipType cannot be null");
         if (group == null) throw new IllegalArgumentException("group cannot be null");

         this.membershipType = membershipType;
         this.group = group;
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
         return membershipType + ":" + group.getId();
      }

      public static Membership fromString(String membership)
      {
         if (membership == null) throw new IllegalArgumentException("membership cannot be null");

         String[] parts = Strings.splitter(":").split(membership);
         return new Membership(parts[0], new Group(parts[1]));
      }
   }
}
