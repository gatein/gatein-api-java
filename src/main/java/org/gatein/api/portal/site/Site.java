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

import org.gatein.api.internal.Objects;
import org.gatein.api.portal.Attributes;
import org.gatein.api.portal.Group;
import org.gatein.api.portal.Permission;
import org.gatein.api.portal.User;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Site implements Comparable<Site>, Serializable
{
   private final SiteId id;

   private String title;
   private String description;
   private Locale locale;
   private String skin;
   private Attributes attributes;
   private Permission accessPermission;
   private Permission editPermission;

   public Site(String name)
   {
      this(new SiteId(name));
   }

   public Site(Group group)
   {
      this(new SiteId(group));
   }

   public Site(User user)
   {
      this(new SiteId(user));
   }

   public Site(SiteId id)
   {
      if (id == null) throw new IllegalArgumentException("id cannot be null");

      this.id = id;
      this.attributes = new Attributes();
   }

   public SiteId getId()
   {
      return id;
   }

   public SiteType getType()
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

   public static final class AttributeKeys
   {
      public static final Attributes.Key<String> SESSION_BEHAVIOR = Attributes.key("org.gatein.api.portal.session_behavior", String.class);
      public static final Attributes.Key<Boolean> SHOW_PORTLET_INFO_BAR = Attributes.key("org.gatein.api.portal.show_info_bar", Boolean.class);

      private AttributeKeys()
      {
      }
   }
}
