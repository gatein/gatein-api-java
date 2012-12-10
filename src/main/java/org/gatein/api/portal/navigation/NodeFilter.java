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

package org.gatein.api.portal.navigation;

import org.gatein.api.Portal;
import org.gatein.api.portal.User;
import org.gatein.api.portal.page.Page;
import org.gatein.api.portal.page.PageId;
import org.gatein.api.util.Filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class NodeFilter implements Filter<Node>
{
   private final boolean accessPermission;
   private final boolean editPermission;
   private final boolean showAll;
   private final Set<Visibility> visibilities;

   private final Portal portal;
   private final User user;

   private NodeFilter(boolean showAll, Set<Visibility> visibilities, boolean accessPermission, boolean editPermission, Portal portal, User user)
   {
      this.showAll = showAll;
      this.visibilities = (visibilities == null) ? Collections.<Visibility>emptySet() : new HashSet<Visibility>(visibilities);

      this.accessPermission = accessPermission;
      this.editPermission = editPermission;

      // If security check, make sure portal and user are not null
      if (editPermission || accessPermission && (portal == null || user == null))
      {
         throw new IllegalArgumentException("Neither user nor portal can be null when access or edit permissions are enabled for this filter.");
      }
      this.portal = portal;
      this.user = user;
   }

   @Override
   public boolean accept(Node node)
   {
      if (!showAll && !node.isVisible()) return false;

      for (Visibility visibility : visibilities)
      {
         if (!visibility.equals(node.getVisibility())) return false;
      }

      PageId pageId = node.getPageId();
      if (editPermission || accessPermission)
      {
         if (pageId == null) return false;

         Page page = portal.getPage(pageId);
         if (editPermission) // check edit permission
         {
            return portal.hasPermission(user, page.getEditPermission());
         }
         else // check access permission
         {
            return portal.hasPermission(user, page.getAccessPermission());
         }
      }

      return true;
   }

   public static final class Builder
   {
      private boolean showAll;
      private Set<Visibility> visibilities = new HashSet<Visibility>();

      private boolean accessPermission;
      private boolean editPermission;

      private Portal portal;
      private User user;

      public Builder showAll(boolean showAll)
      {
         this.showAll = showAll;
         return this;
      }

      public Builder withVisibilityFlag(Visibility.Flag flag)
      {
         return withVisibility(new Visibility(flag));
      }

      public Builder withVisibility(Visibility visibility)
      {
         visibilities.add(visibility);
         return this;
      }

      public Builder withNoAccess()
      {
         this.accessPermission = false;
         return this;
      }

      public Builder withAccess(User user, Portal portal)
      {
         this.accessPermission = true;
         return set(user, portal);
      }

      public Builder withEdit(User user, Portal portal)
      {
         this.editPermission = true;
         return set(user, portal);
      }

      public Builder withNoEdit()
      {
         this.editPermission = false;
         return this;
      }

      private Builder set(User user, Portal portal)
      {
         this.user = user;
         this.portal = portal;

         return this;
      }

      public Filter<Node> build() throws IllegalStateException
      {
         return new NodeFilter(showAll, visibilities, accessPermission, editPermission, portal, user);
      }
   }
}