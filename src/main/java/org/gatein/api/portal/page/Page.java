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

package org.gatein.api.portal.page;

import org.gatein.api.annotation.Immutable;
import org.gatein.api.internal.ArraysExt;
import org.gatein.api.portal.Formatted;
import org.gatein.api.portal.Group;
import org.gatein.api.portal.Ids;
import org.gatein.api.portal.Permission;
import org.gatein.api.portal.User;
import org.gatein.api.portal.site.Site;

import java.io.Serializable;

import static org.gatein.api.portal.Ids.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Page implements Serializable
{
   private final Id id;
   private String title;
   private String description;
   private Permission accessPermission;
   private Permission editPermission;

   public Page(String siteName, String pageName)
   {
      this(pageId(siteName, pageName));
   }

   public Page(Group group, String pageName)
   {
      this(pageId(group, pageName));
   }

   public Page(User user, String pageName)
   {
      this(pageId(user, pageName));
   }

   public Page(Id id)
   {
      this.id = id;
   }

   public Id getId()
   {
      return id;
   }

   public Site.Id getSiteId()
   {
      return id.getSiteId();
   }

   public String getName()
   {
      return id.getPageName();
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

   @Immutable
   public static class Id implements Formatted, Serializable
   {
      private final Site.Id siteId;
      private final String pageName;

      public Id(Site.Id siteId, String pageName)
      {
         if (siteId == null) throw new IllegalArgumentException("siteId cannot be null");
         if (pageName == null) throw new IllegalArgumentException("pageName cannot be null");

         this.siteId = siteId;
         this.pageName = pageName;
      }

      /**
       * @return Id of the site
       */
      public Site.Id getSiteId()
      {
         return siteId;
      }

      /**
       * @return Name of the page
       */
      public String getPageName()
      {
         return pageName;
      }

      @Override
      public String toString()
      {
         return Ids.format(this, "Page.Id[siteType=%s, siteName=%s, pageName=%s]");
      }

      @Override
      public Object[] getFormatArguments()
      {
         return ArraysExt.concat(siteId.getFormatArguments(), pageName);
      }

      @Override
      public Object[] getFormatArguments(Adapter adapter)
      {
         return ArraysExt.concat(siteId.getFormatArguments(adapter), adapter.adapt(2, pageName));
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         Id id = (Id) o;

         return pageName.equals(id.pageName) && siteId.equals(id.siteId);
      }

      @Override
      public int hashCode()
      {
         int result = siteId.hashCode();
         result = 31 * result + pageName.hashCode();
         return result;
      }
   }
}
