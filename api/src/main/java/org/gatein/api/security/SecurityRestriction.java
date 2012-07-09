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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Access restriction. Represent type of restriction (edit or just access) and list of groups with matching
 * membership type based on which access will be granted
 *
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public class SecurityRestriction
{

   private final List<Entry> entries;

   private final Type type;

   private SecurityRestriction(Type type)
   {
      this.type = type;
      this.entries = new ArrayList<Entry>();
   }

   private SecurityRestriction(Type type, Entry entry)
   {
      this.type = type;
      this.entries = Arrays.asList(entry);
   }

   /**
    * @return New AccessRestriction object with Type.ACCESS
    */
   public static SecurityRestriction access()
   {
      return new SecurityRestriction(Type.ACCESS);
   }

   /**
    * @param entry the edit membership
    * @return New AccessRestriction object with Type.EDIT
    */
   public static SecurityRestriction edit(Entry entry)
   {
      if (entry == null) throw new IllegalArgumentException("entry cannot be null");

      return new SecurityRestriction(Type.EDIT, entry);
   }

   /**
    * @return Type of restriction
    */
   public Type getType()
   {
      return type;
   }

   /**
    * @return List of entries matching this restriction
    */
   public List<Entry> getEntries()
   {
      return entries;
   }

   /**
    * @param entry The entry to add to the restriction
    * @return SecurityRestriction object with added entry
    */
   public SecurityRestriction addEntry(Entry entry)
   {
      entries.add(entry);
      return this;
   }

   /**
    * @return true if there are no restriction entries
    */
   public boolean isPublic()
   {
      return entries.isEmpty() && type != Type.EDIT;
   }

   /**
    * Clears all current restriction entries, making the restriction accessible to anyone (public). <i>Note</i> this
    * only applies to {@link SecurityRestriction.Type#ACCESS} types. Meaning EDIT types cannot be set to public.
    *
    * @return SecurityRestriction object with applied change
    */
   public SecurityRestriction setPublic()
   {
      if (type == Type.EDIT) throw new IllegalStateException("Cannot set restriction type " + Type.EDIT + " public.");

      entries.clear();
      return this;
   }

   /**
    * Access type
    */
   public enum Type
   {
      ACCESS, EDIT
   }

   /**
    * Represents pair of Group Id and MembershipType name.
    */
   public static class Entry
   {
      private final String groupId;
      private final String membershipType;

      private Entry(String membership, String groupId)
      {
         if (membership == null)
         {
            throw new IllegalArgumentException("membership cannot be null");
         }
         if (groupId == null)
         {
            throw new IllegalArgumentException("groupId cannot be null");
         }

         this.groupId = groupId;
         this.membershipType = membership;
      }

      public static Entry create(String membershipType, String... group)
      {
         StringBuilder sb = new StringBuilder();
         for (String s : group)
         {
            sb.append("/").append(s);
         }

         return new Entry(membershipType, sb.toString());
      }

      public static Entry create(String membershipType, String groupId)
      {

         return new Entry(membershipType, groupId);
      }

      /**
       * Creates entry with ANY/* membership type
       *
       * @param group the group
       * @return an entry with any membership type for the specified group
       */
      public static Entry any(String... group)
      {
         return create("*", group);
      }

      /**
       * Creates entry with ANY/* membership type
       *
       * @param groupId the groupId
       * @return an entry with any membership type for the specified group
       */
      public static Entry any(String groupId)
      {
         return create("*", groupId);
      }

      /**
       * @return true if membership type is equals to "*" (ANY membership"
       */
      public boolean isAny()
      {
         return membershipType.equals("*");
      }

      public String toString()
      {
         return membershipType + ":" + groupId;
      }

      public String getGroupId()
      {
         return groupId;
      }

      public String getMembershipType()
      {
         return membershipType;
      }
   }
}
