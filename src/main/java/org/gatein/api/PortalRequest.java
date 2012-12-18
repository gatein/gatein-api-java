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

package org.gatein.api;

import org.gatein.api.security.User;
import org.gatein.api.navigation.Navigation;
import org.gatein.api.navigation.Node;
import org.gatein.api.navigation.NodePath;
import org.gatein.api.navigation.Nodes;
import org.gatein.api.page.Page;
import org.gatein.api.page.PageId;
import org.gatein.api.site.Site;
import org.gatein.api.site.SiteId;
import org.gatein.api.common.Filter;

import java.util.Locale;

/**
 * The PortalRequest object represents the current request of the portal. This object is available in the portal simply
 * by invoking the static {@link org.gatein.api.PortalRequest#getInstance()} method.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public abstract class PortalRequest
{
   /**
    * The user of the request. If this request is for an unauthenticated user then {@link User#anonymous()}
    * is returned.
    *
    * @return the user of the current portal request. This should never be null.
    */
   public abstract User getUser();

   /**
    * The <code>SiteId</code> of the request.
    *
    * @return the site id of the current portal request. This should never be null.
    */
   public abstract SiteId getSiteId();

   /**
    * The <code>NodePath</code> of the request.
    *
    * @return the node path of the current portal request. This should never be null.
    */
   public abstract NodePath getNodePath();

   /**
    * The <code>Locale</code> of the request.
    *
    * @return the locale of the current portal request.
    */
   public abstract Locale getLocale();

   /**
    * The site represented by the <code>SiteId</code> of the request.
    *
    * @return the site of the current portal request
    */
   public Site getSite()
   {
      return getPortal().getSite(getSiteId());
   }

   /**
    * The page currently being accessed by the current portal request.
    *
    * @return the page of the current portal request.
    */
   public Page getPage()
   {
      PageId pageId = getNode().getPageId();

      return (pageId == null) ? null : getPortal().getPage(pageId);
   }

   /**
    * The navigation of the current portal request.
    *
    * @return the navigation represented by the current portal request.
    */
   public Navigation getNavigation()
   {
      return getPortal().getNavigation(getSiteId());
   }

   /**
    * The node of the current portal request, filtered based on the current user's access rights.
    *
    * @return the node of the current portal request
    */
   public Node getNode()
   {
      Node node = getNavigation().getNode(getNodePath());
      if (node == null)
         throw new ApiException("Node could not be found for current request path " + getNodePath());

      return node.filter(getUserFilter());
   }

   /**
    * Returns the filter that can be used to filter based on the current user's access rights.
    *
    * @return the user filter
    * @see Nodes#userFilter(org.gatein.api.security.User, Portal)
    */
   public Filter<Node> getUserFilter()
   {
      return Nodes.userFilter(getUser(), getPortal());
   }

   /**
    * Access to the portal interface
    *
    * @return the portal interface
    */
   public abstract Portal getPortal();

   /**
    * Obtain the current instance of a <code>PortalRequest</code>
    *
    * @return the portal request
    */
   public static PortalRequest getInstance()
   {
      return instance.get();
   }

   protected static void setInstance(PortalRequest request)
   {
      if (request == null)
      {
         instance.remove();
      }
      else
      {
         instance.set(request);
      }
   }

   private static ThreadLocal<PortalRequest> instance = new ThreadLocal<PortalRequest>()
   {
      @Override
      protected PortalRequest initialValue()
      {
         return null;
      }
   };
}
