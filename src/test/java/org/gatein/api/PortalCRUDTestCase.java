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

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class PortalCRUDTestCase
{
   @Test(enabled = false)
   public void createAPortal()
   {
      /*
        <portal-config>
           <portal-name>classic</portal-name>
           <locale>en</locale>
           <factory-id>office</factory-id>
           <access-permissions>Everyone</access-permissions>
           <edit-permission>*:/platform/administrators</edit-permission>
           <creator>root</creator>
           <portal-layout>
              <application>
                 <instance-id>portal#classic:/web/BannerPortlet/banner</instance-id>
                 <show-info-bar>false</show-info-bar>
                 </application>
                 <application>
                    <instance-id>portal#classic:/web/NavigationPortlet/toolbar</instance-id>
                    <show-info-bar>false</show-info-bar>
                    </application>
                    <application>
                       <instance-id>portal#classic:/web/BreadcumbsPortlet/breadcumbs</instance-id>
                       <show-info-bar>false</show-info-bar>
                       </application>
       <page-body> </page-body>
       <application>
       <instance-id>portal#classic:/web/FooterPortlet/footer</instance-id>
       <show-info-bar>false</show-info-bar>
       </application>
       </portal-layout>
       </portal-config>
       */

      PortalContainer container = GateIn.getPortalContainer("container");
      Portal portal = container.createPortal("portal");
      assert Ids.getPortalId("container", "portal").equals(portal.getId());
      assert "portal".equals(portal.getName());
      assert portal.getContentRegistry() != null;
   }
}
