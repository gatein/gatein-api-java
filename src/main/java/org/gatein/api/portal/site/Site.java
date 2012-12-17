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

package org.gatein.api.portal.site;

import org.gatein.api.common.Attributes;
import org.gatein.api.common.Describable;
import org.gatein.api.common.Displayable;
import org.gatein.api.security.Permission;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Site extends Displayable, Describable, Comparable<Site>, Serializable
{
   SiteId getId();

   SiteType getType();

   String getName();

   Locale getLocale();

   public void setLocale(Locale locale);

   public String getSkin();

   public void setSkin(String skin);

   public Attributes getAttributes();

   public Permission getAccessPermission();

   public void setAccessPermission(Permission permission);

   public Permission getEditPermission();

   public void setEditPermission(Permission permission);

   public static final class AttributeKeys
   {
      public static final Attributes.Key<String> SESSION_BEHAVIOR = Attributes.key("org.gatein.api.portal.session_behavior", String.class);
      public static final Attributes.Key<Boolean> SHOW_PORTLET_INFO_BAR = Attributes.key("org.gatein.api.portal.show_info_bar", Boolean.class);

      private AttributeKeys()
      {
      }
   }
}
