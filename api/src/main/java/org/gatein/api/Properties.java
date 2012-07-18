/*
* JBoss, a division of Red Hat
* Copyright 2012, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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

import org.gatein.api.commons.PropertyType;

import java.util.List;

/** @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a> */
public interface Properties
{
   PropertyType<String> SESSION_BEHAVIOR = new PropertyType<String>("org.gatein.api.portal.session_behavior")
   {
   };

   List<String> getSessionBehaviorValues();

   PropertyType<Boolean> SHOW_PORTLET_INFO_BAR = new PropertyType<Boolean>("org.gatein.api.portal.show_info_bar")
   {
   };

   boolean isKnown(PropertyType propertyType);
}
