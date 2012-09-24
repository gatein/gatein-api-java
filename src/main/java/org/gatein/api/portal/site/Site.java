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

package org.gatein.api.portal.site;

import org.gatein.api.annotation.Immutable;
import org.gatein.api.annotation.NotNull;
import org.gatein.api.internal.Objects;
import org.gatein.api.portal.Attributes;
import org.gatein.api.portal.Formatted;
import org.gatein.api.portal.Group;
import org.gatein.api.portal.Ids;
import org.gatein.api.portal.Permission;
import org.gatein.api.portal.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Site implements Comparable<Site>, Serializable
{
   private final Id id;

   private String title;
   private String description;
   private Locale locale;
   private String skin;
   private Attributes attributes;
   private Permission accessPermission;
   private Permission editPermission;

   public Site(String name)
   {
      this(Ids.siteId(name));
   }

   public Site(Group group)
   {
      this(Ids.siteId(group));
   }

   public Site(User user)
   {
      this(Ids.siteId(user));
   }

   public Site(@NotNull Id id)
   {
      if (id == null) throw new IllegalArgumentException("id cannot be null");

      this.id = id;
      this.attributes = new Attributes();
   }

   public Id getId()
   {
      return id;
   }

   public Type getType()
   {
      return id.getType();
   }

   public String getName()
   {
      return id.getName();
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public Locale getLocale()
   {
      return locale;
   }

   public void setLocale(Locale locale)
   {
      this.locale = locale;
   }

   public String getSkin()
   {
      return skin;
   }

   public void setSkin(String skin)
   {
      this.skin = skin;
   }

   public Attributes getAttributes()
   {
      return attributes;
   }

   public void setAttributes(Attributes attributes)
   {
      this.attributes = attributes;
   }

   public Permission getAccessPermission()
   {
      return accessPermission;
   }

   public void setAccessPermission(Permission permission)
   {
      this.accessPermission = permission;
   }

   public Permission getEditPermission()
   {
      return editPermission;
   }

   public void setEditPermission(Permission permission)
   {
      this.editPermission = permission;
   }

   @Override
   public int compareTo(Site other)
   {
      return getName().compareTo(other.getName());
   }

   @Override
   public String toString()
   {
      return Objects.toStringBuilder(getClass())
         .add("type", getType().getName())
         .add("name", getName())
         .add("title", getTitle())
         .add("description", getDescription())
         .add("locale", getLocale())
         .add("skin", getSkin())
         .add("attributes", getAttributes())
         .add("editPermission", getEditPermission())
         .add("accessPermission", getAccessPermission())
         .toString();
   }

   @Immutable
   public static class Id implements Formatted, Serializable
   {
      private final Type type;
      private final String name;

      public Id(@NotNull Type type, @NotNull String name)
      {
         if (type == null) throw new IllegalArgumentException("type cannot be null");
         if (name == null) throw new IllegalArgumentException("name cannot be null");

         this.type = type;
         this.name = name;
      }

      public Type getType()
      {
         return type;
      }

      public String getName()
      {
         return name;
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         Id id = (Id) o;

         return name.equals(id.name) && type == id.type;
      }

      @Override
      public int hashCode()
      {
         int result = type.hashCode();
         result = 31 * result + name.hashCode();
         return result;
      }

      @Override
      public String toString()
      {
         return Ids.format(this, "Site.Id[type=%s, name=%s]");
      }

      @Override
      public Object[] getFormatArguments()
      {
         return getFormatArguments(null);
      }

      @Override
      public Object[] getFormatArguments(@NotNull Adapter adapter)
      {
         Object[] args = new Object[2];
         args[0] = adapt(0, type.getName(), adapter);
         args[1] = adapt(1, name, adapter);

         return args;
      }

      private Object adapt(int index, Object original, Adapter adapter)
      {
         return (adapter == null) ? original : adapter.adapt(index, original);
      }
   }

   public static enum Type
   {
      SITE("site"), SPACE("space"), DASHBOARD("dashboard");

      private final String name;

      private Type(String name)
      {
         this.name = name;
      }

      public String getName()
      {
         return name;
      }

      public static Type forName(String name)
      {
         return MAP.get(name);
      }

      private static final Map<String, Type> MAP;

      static
      {
         final Map<String, Type> map = new HashMap<String, Type>();
         for (Type type : values())
         {
            final String name = type.getName();
            if (name != null) map.put(name, type);
         }
         MAP = map;
      }
   }

   public static final class AttributeKeys
   {
      public static final Attributes.Key<String> SESSION_BEHAVIOR = Attributes.key("org.gatein.api.portal.session_behavior", String.class);
      public static final Attributes.Key<Boolean> SHOW_PORTLET_INFO_BAR = Attributes.key("org.gatein.api.portal.show_info_bar", Boolean.class);

      private AttributeKeys()
      {
      }
   }
}
