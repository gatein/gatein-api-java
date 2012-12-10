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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Permission
{
   private static final Permission EVERYONE = new Permission();

   public static Permission everyone()
   {
      return EVERYONE;
   }

   public static Permission any(String... group)
   {
      return new Permission(Membership.any(group));
   }

   private final Set<Membership> memberships;

   public Permission()
   {
      memberships = Collections.emptySet();
   }

   public Permission(String membershipType, Group group)
   {
      this(new Membership(membershipType, group));
   }

   public Permission(User user)
   {
      this(new Membership(user));
   }

   public Permission(Membership membership)
   {
      this(Collections.singleton(membership));
   }

   public Permission(Set<Membership> memberships)
   {
      this(new LinkedHashSet<Membership>(memberships));
   }

   private Permission(LinkedHashSet<Membership> memberships)
   {
      this.memberships = memberships;
   }

   public Set<Membership> getMemberships()
   {
      return Collections.unmodifiableSet(memberships);
   }

   public Permission addMembership(Membership membership)
   {
      LinkedHashSet<Membership> newMemberships = new LinkedHashSet<Membership>(memberships.size() + 1);
      newMemberships.addAll(memberships);
      newMemberships.add(membership);

      return new Permission(newMemberships);
   }

   public boolean isAccessibleToEveryone()
   {
      return memberships.isEmpty();
   }

   @Override
   public String toString()
   {
      return Objects.toStringBuilder()
         .add("memberships", memberships.isEmpty() ? "Everyone" : memberships)
         .toString();
   }
}
