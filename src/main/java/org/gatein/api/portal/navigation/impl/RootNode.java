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
package org.gatein.api.portal.navigation.impl;

import java.net.URI;

import org.gatein.api.ApiException;
import org.gatein.api.portal.Label;
import org.gatein.api.portal.navigation.PublicationDate;
import org.gatein.api.portal.navigation.Visibility;
import org.gatein.api.portal.page.PageId;
import org.gatein.api.portal.site.SiteId;

public class RootNode extends NodeImpl
{
   private SiteId siteId;

   public RootNode(SiteId siteId)
   {
      super();
      this.siteId = siteId;
   }

   public SiteId getSiteId()
   {
      return siteId;
   }

   @Override
   public String getResolvedLabel()
   {
      return null;
   }

   @Override
   public URI getResolvedURI()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setIconName(String iconName)
   {
      throw new ApiException("Cannot set iconName on root node");
   }

   @Override
   public void setLabel(Label label)
   {
      throw new ApiException("Cannot set label on root node");
   }

   @Override
   public void setPageId(PageId pageId)
   {
      throw new ApiException("Cannot set pageId on root node");
   }

   @Override
   public void setVisibility(boolean visible)
   {
      throw new ApiException("Cannot set visibility on root node");
   }

   @Override
   public void setVisibility(PublicationDate publicationDate)
   {
      throw new ApiException("Cannot set visibility on root node");
   }

   @Override
   public void setVisibility(Visibility visibility)
   {
      throw new ApiException("Cannot set visibility on root node");
   }
}
