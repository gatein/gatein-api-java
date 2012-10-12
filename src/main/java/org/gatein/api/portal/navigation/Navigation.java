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
import java.util.ArrayList;
import java.util.List;

/**
 * Navigation for a site.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Navigation implements Serializable
{
   private final SiteId siteId;
   private int priority;
   private List<Node> nodes;

   public Navigation(SiteId siteId, int priority)
   {
      if (siteId == null) throw new IllegalArgumentException("siteId cannot be null");

      this.siteId = siteId;
      this.priority = priority;
      this.nodes = new ArrayList<Node>();
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

   public List<Node> getNodes()
   {
      return nodes;
   }

   public void setNodes(List<Node> nodes)
   {
      this.nodes = new ArrayList<Node>(nodes);
   }

   public void addNodes(List<Node> nodes)
   {
      this.nodes.addAll(nodes);
   }

   public void addNode(Node node)
   {
      this.nodes.add(node);
   }

   public Node removeNode(String name)
   {
      Node found = null;
      for (Node node : nodes)
      {
         if (node.getName().equals(name))
         {
            found = node;
         }
      }

      nodes.remove(found);
      return found;
   }
}
