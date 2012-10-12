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

import org.gatein.api.portal.Group;
import org.gatein.api.portal.Permission;
import org.gatein.api.portal.User;
import org.gatein.api.portal.site.SiteId;

import java.io.Serializable;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Page implements Serializable
{
   private final PageId id;
   private String title;
   private String description;
   private Permission accessPermission;
   private Permission editPermission;

   public Page(String siteName, String pageName)
   {
      this(new PageId(siteName, pageName));
   }

   public Page(Group group, String pageName)
   {
      this(new PageId(group, pageName));
   }

   public Page(User user, String pageName)
   {
      this(new PageId(user, pageName));
   }

   public Page(PageId id)
   {
      this.id = id;
   }

   public PageId getId()
   {
      return id;
   }

   public SiteId getSiteId()
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
}
