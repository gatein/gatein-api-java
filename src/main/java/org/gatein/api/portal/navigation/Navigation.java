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

import org.gatein.api.portal.site.SiteId;

import java.io.Serializable;
import java.util.List;

/**
 * Navigation for a site.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Navigation implements NodeContainer, Serializable
{
   private final SiteId siteId;
   private int priority;
   private Node rootNode;

   public Navigation(SiteId siteId, int priority)
   {
      if (siteId == null) throw new IllegalArgumentException("siteId cannot be null");

      this.siteId = siteId;
      this.priority = priority;
      this.rootNode = Node.rootNode(siteId);
   }

   public SiteId getSiteId()
   {
      return siteId;
   }

   public int getPriority()
   {
      return priority;
   }

   public void setPriority(int priority)
   {
      this.priority = priority;
   }

   @Override
   public boolean isChildrenLoaded()
   {
      return rootNode.isChildrenLoaded();
   }

   @Override
   public void addChild(Node node)
   {
      rootNode.addChild(node);
   }

   @Override
   public Node getChild(String name)
   {
      return rootNode.getChild(name);
   }

   @Override
   public boolean removeChild(String name)
   {
      return rootNode.removeChild(name);
   }

   @Override
   public boolean removeChild(Node node)
   {
      return rootNode.removeChild(node);
   }

   @Override
   public List<Node> getChildren()
   {
      return rootNode.getChildren();
   }
}
