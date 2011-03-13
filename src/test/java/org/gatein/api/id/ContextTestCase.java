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

package org.gatein.api.id;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class ContextTestCase
{
   private static final String CONTAINER_COMPONENT = "containerComponent";
   private static final String PORTAL_COMPONENT = "portalComponent";
   private static final String INVOKER_COMPONENT = "invokerComponent";
   private static final String PORTLET_COMPONENT = "portletComponent";
   private static final String INSTANCE_COMPONENT = "instanceComponent";

   @Test
   public void simpleContext()
   {
      List<Component> components = new ArrayList<Component>(1);
      components.add(new Component("component", Pattern.compile(".*"), true));
      Context context = new Context("-", components, false);

      assert 0 == context.getIndexFor("component");
      assert context.isComponentRequired("component");
      context.validate(new String[]{"foo"});
   }

   @Test
   public void testPortletCase()
   {
      List<Component> components = new ArrayList<Component>(2);
      components.add(new Component(CONTAINER_COMPONENT, Pattern.compile("container"), true));
      components.add(new Component(PORTAL_COMPONENT, Pattern.compile("portal"), true));
      components.add(new Component(INVOKER_COMPONENT, Pattern.compile(".*"), false));
      components.add(new Component(PORTLET_COMPONENT, Pattern.compile(".*"), false));
      components.add(new Component(INSTANCE_COMPONENT, Pattern.compile(".*Instance$"), false));
      Context context = new Context("=", components, true);

      context.validate(new String[]{"container", "portal"});
      context.validate(new String[]{"container", "portal", "foo"});
      context.validate(new String[]{"container", "portal", "foo", "bar"});
      context.validate(new String[]{"container", "portal", "foo", "bar", "barInstance"});
   }
}
