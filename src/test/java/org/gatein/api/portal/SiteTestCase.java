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

package org.gatein.api.portal;

import org.gatein.api.util.Type;
import org.testng.annotations.Test;

/** @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a> */
public class SiteTestCase
{
   @Test
   public void forNameShouldProperlyResolveTypes()
   {
      assert Site.PORTAL.equals(Type.forName(Site.PORTAL_NAME, Site.class));
      assert Site.DASHBOARD.equals(Type.forName(Site.DASHBOARD_NAME, Site.class));
      assert Site.GROUP.equals(Type.forName(Site.GROUP_NAME, Site.class));
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void forNameShouldThrowAnExceptionOnUnknownTypeName()
   {
      Type.forName("foo", Site.class);
   }

   @Test
   public void shouldProperlyReturnValueType()
   {
      assert Portal.class.equals(Site.PORTAL.getValueType());
      assert Site.class.equals(Site.GROUP.getValueType());
      assert Site.class.equals(Site.DASHBOARD.getValueType());
   }
}
