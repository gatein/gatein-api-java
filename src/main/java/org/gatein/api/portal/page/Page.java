/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.api.portal.page;

import org.gatein.api.portal.Describable;
import org.gatein.api.portal.Displayable;
import org.gatein.api.portal.LocalizedString;
import org.gatein.api.portal.Permission;
import org.gatein.api.portal.site.SiteId;

import java.io.Serializable;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Page extends Displayable, Describable, Comparable<Page>, Serializable
{
   PageId getId();

   SiteId getSiteId();

   String getName();

   Permission getAccessPermission();

   void setAccessPermission(Permission permission);

   Permission getEditPermission();

   void setEditPermission(Permission permission);
}
