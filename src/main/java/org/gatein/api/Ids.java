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

import org.gatein.api.content.Application;
import org.gatein.api.content.Content;
import org.gatein.api.content.Gadget;
import org.gatein.api.content.ManagedContent;
import org.gatein.api.id.Context;
import org.gatein.api.id.Id;
import org.gatein.api.navigation.Site;

import java.net.URL;
import java.util.regex.Pattern;

/**
 * todo: need appropriate classes to represent PortletInvoker, Portlet and Instances
 * todo: need appropriate validation patterns
 *
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Ids
{
   public static final String CONTAINER_COMPONENT_NAME = "containerComponent";
   public static final String PORTAL_COMPONENT_NAME = "portalComponent";
   public static final String INVOKER_COMPONENT_NAME = "invokerComponent";
   public static final String PORTLET_COMPONENT_NAME = "portletComponent";
   public static final String INSTANCE_COMPONENT_NAME = "instanceComponent";

   private static final Pattern USER_NAME_PATTERN = Pattern.compile(".*");
   private static final String USER_COMPONENT_NAME = "userName";
   public final static Context USER = Context.builder().requiredComponent(USER_COMPONENT_NAME, Object.class, USER_NAME_PATTERN).build();

   public static Id userId(String userId)
   {
      return Id.create(USER, userId);
   }

   private static final Pattern GROUP_PATTERN = Pattern.compile(".*");
   public final static Context GROUP = Context.builder().withDefaultSeparator("/")
      .requiredUnboundedHierarchicalComponent("root", Object.class, GROUP_PATTERN).build();

   public static Id groupId(String root, String... children)
   {
      return Id.create(GROUP, root, children);
   }

   public static final Context APPLICATION = Context.builder().withDefaultSeparator("/")
      .requiredComponent("applicationName", Application.class, Pattern.compile(".*"))
      .requiredComponent("portletName", Object.class, Pattern.compile(".*")).build();

   public static Id<Application> applicationId(String applicationName, String portletName)
   {
      return Id.create(APPLICATION, applicationName, portletName);
   }

   public static final Context WSRP = Context.builder().withDefaultSeparator(".")
      .requiredComponent("invokerId", Object.class, Pattern.compile(".*"))
      .requiredComponent("portletId", Object.class, Pattern.compile(".*")).build();

   public static Id<Content> wsrpPortletId(String invoker, String portlet)
   {
      return Id.create(WSRP, invoker, portlet);
   }

   private static long counter = 0;

   public static <T extends Content<T>> Id<ManagedContent<T>> managedContentId(Id<T> contentId)
   {
      return Id.getIdForChild(contentId, Long.toString(counter++));
   }

   public static Id<Gadget> gadgetId(String name)
   {
      return null; // fix-me
   }

   public static Id<Gadget> gadgetId(URL url)
   {
      return null; // fix-me
   }

   public static Id<Portal> portalId(String id)
   {
      return null;
   }

   public static Id<Site> siteId(String siteId)
   {
      return null;
   }
}
