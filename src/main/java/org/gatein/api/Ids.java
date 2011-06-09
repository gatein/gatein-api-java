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
import org.gatein.api.content.ManagedContent;
import org.gatein.api.id.Context;
import org.gatein.api.id.Id;
import org.gatein.api.organization.Group;
import org.gatein.api.organization.User;

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

   public static final Context PORTLET = Context.builder().withDefaultSeparator("=")
      .requiredComponent(INVOKER_COMPONENT_NAME, GateInObject.class, Pattern.compile(".*"))
      .requiredComponent(PORTLET_COMPONENT_NAME, GateInObject.class, Pattern.compile(".*"))
      .optionalComponent(INSTANCE_COMPONENT_NAME, GateInObject.class, Pattern.compile(".*Instance$"))
      .ignoreRemainingAfterFirstMissingOptional().build();

   private static final Pattern USER_NAME_PATTERN = Pattern.compile(".*");
   private static final String USER_COMPONENT_NAME = "userName";
   public final static Context USER = Context.builder().requiredComponent(USER_COMPONENT_NAME, User.class, USER_NAME_PATTERN).build();

   public static Id<User> userId(String userId)
   {
      return Id.create(USER, userId);
   }

   private static final Pattern GROUP_PATTERN = Pattern.compile(".*");
   public final static Context GROUP = Context.builder().withDefaultSeparator("/")
      .requiredUnboundedHierarchicalComponent("root", Group.class, GROUP_PATTERN).build();

   public static Id<Group> groupId(String root, String... children)
   {
      return Id.create(GROUP, root, children);
   }

   public static final Context APPLICATION = Context.builder().withDefaultSeparator("/")
      .requiredComponent("applicationName", Application.class, Pattern.compile(".*"))
      .requiredComponent("portletName", GateInObject.class, Pattern.compile(".*")).build();

   public static Id<Application> applicationId(String applicationName, String portletName)
   {
      return Id.create(APPLICATION, applicationName, portletName);
   }

   public static final Context WSRP = Context.builder().withDefaultSeparator(".")
      .requiredComponent("invokerId", GateInObject.class, Pattern.compile(".*"))
      .requiredComponent("portletId", GateInObject.class, Pattern.compile(".*")).build();

   public static Id<Content> wsrpPortletId(String invoker, String portlet)
   {
      return Id.create(WSRP, invoker, portlet);
   }

   public static final Context CONTENT = Context.builder()
      .requiredComponent("content", Content.class, Pattern.compile(".*"))
      .optionalComponent("managedContent", ManagedContent.class, Pattern.compile("[a-z0-9]*"))
      .withDefaultSeparator("_m:").build();
   private static long counter = 0;

   public static <T extends Content<T>> Id<ManagedContent<T>> managedContentId(Id<T> contentId)
   {
      return Id.getIdForChild(contentId, Long.toString(counter++));
   }
}
