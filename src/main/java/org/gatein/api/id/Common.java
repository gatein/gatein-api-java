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

import org.gatein.api.organization.Group;
import org.gatein.api.organization.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Common
{
   public static final String CONTAINER_COMPONENT_NAME = "containerComponent";
   public static final String PORTAL_COMPONENT_NAME = "portalComponent";
   public static final String INVOKER_COMPONENT_NAME = "invokerComponent";
   public static final String PORTLET_COMPONENT_NAME = "portletComponent";
   public static final String INSTANCE_COMPONENT_NAME = "instanceComponent";

   private static List<Component> components;

   static
   {
      components = new ArrayList<Component>(5);
      components.add(new Component(CONTAINER_COMPONENT_NAME, Pattern.compile("container"), true));
      components.add(new Component(PORTAL_COMPONENT_NAME, Pattern.compile("portal"), true));
      components.add(new Component(INVOKER_COMPONENT_NAME, Pattern.compile(".*"), false));
      components.add(new Component(PORTLET_COMPONENT_NAME, Pattern.compile(".*"), false));
      components.add(new Component(INSTANCE_COMPONENT_NAME, Pattern.compile(".*Instance$"), false));
   }

   public static final Context PORTLET = new Context("=", components, true);

   private static final Pattern USER_NAME_PATTERN = Pattern.compile(".*"); // todo
   public final static Context USER = new Context(null, Collections.singletonList(new Component("userName", USER_NAME_PATTERN, true)), true);

   public static Id<User> getUserId(String userId)
   {
      return Id.create(USER, userId);
   }

   private static final Pattern GROUP_PATTERN = Pattern.compile(".*"); // todo
   public final static Context GROUP = new Context(null, Collections.singletonList(new Component("userName", GROUP_PATTERN, true)), true); // todo: need concept of unbounded hierarchical context

   public static Id<Group> getGroupId(String root, String... children)
   {
      return Id.create(GROUP, root, children);
   }
}
