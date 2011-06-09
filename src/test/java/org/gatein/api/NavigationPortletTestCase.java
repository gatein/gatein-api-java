/*
 * JBoss, a division of Red Hat
 * Copyright 2011, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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

import org.gatein.api.id.Id;
import org.gatein.api.navigation.Node;
import org.gatein.api.navigation.Nodes;
import org.gatein.api.navigation.Page;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class NavigationPortletTestCase
{
   @Test(enabled = false)
   public void shouldListGroupPages()
   {
      Id groupId = Ids.groupId("platform", "administrators");

      IterableResult<Node> adminNodes = Nodes.getWhere(new Nodes.GroupNodeFilter(groupId));
      assert 2 == adminNodes.size();
      Iterator<Node> iterator = adminNodes.iterator();

      Node administrationNode = iterator.next();
      assert "Administration".equals(administrationNode.getDisplayName());
      assert 2 == administrationNode.getChildrenNumber();
      IterableResult<? extends Node> children = administrationNode.getChildren();
      for (Node child : children)
      {
         assert child.equals(administrationNode.getChild(child.getName(), child.getClass()));
         assert child.equals(Nodes.get(child.getId()));
      }

      Node<?> wsrp = iterator.next();
      assert "WSRP".equals(wsrp.getDisplayName());
      assert wsrp instanceof Page;
      assert 1 == wsrp.getChildrenNumber();

      assert !iterator.hasNext();
   }

   @Test(enabled = false)
   public void shouldListSitePages()
   {
      final Id id = Ids.userId("root");

      // with a Query
      IterableResult<Portal> fromNodes = Nodes.get(Query.<Portal>builder().where(new Filter<Portal>()
      {
         @Override
         public boolean accept(Portal item)
         {
            return item.accessAllowedFromUser(id, Permission.Type.ACCESS);
         }
      }).build());

      // with getForUser using type
      IterableResult<Portal> forUser = Nodes.getForUser(id, Node.Type.SITE);
      assert fromNodes.equals(forUser);

      for (Portal portal : fromNodes)
      {
         portal.getChildrenWhere(new Filter<Node>()
         {
            public boolean accept(Node item)
            {
               return item.accessAllowedFromUser(id, Permission.Type.ACCESS);
            }
         });
         // then display
      }
   }

   @Test(enabled = false)
   public void shouldListDashboardPages()
   {
      final Id id = Ids.userId("root");

      Filter<Node> filter = new Filter<Node>()
      {
         public boolean accept(Node item)
         {
            return item.accessAllowedFromUser(id, Permission.Type.ACCESS) && Node.Type.DASHBOARD.equals(item.getOwnerType());
         }
      };
      Iterable<Node> nodes = Nodes.getWhere(filter);
      Node dashboard = Nodes.getSingleOrFail(Query.<Node>builder().where(filter).build());
      assert dashboard.equals(nodes.iterator().next());

      // from Nodes
      Iterable<Node> fromNodes = Nodes.getForUser(id, Node.Type.DASHBOARD);
      assert nodes.equals(fromNodes);
      assert dashboard.equals(fromNodes.iterator().next());
   }
}
