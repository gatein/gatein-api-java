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

package org.gatein.api.content;

import org.gatein.api.util.Type;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Portlet extends Content<Portlet>
{
   Portlet.Id getId();

   Type<Portlet> getType();

   public final class Id extends Content.Id<Portlet>
   {

      /** . */
      private final String applicationId;

      /** . */
      private final String portletName;

      /**
       * Create a new portlet id.
       *
       * @param applicationId the application id
       * @param portletName the portlet name
       * @throws NullPointerException if any argument is null
       */
      public Id(String applicationId, String portletName) throws NullPointerException
      {
         if (applicationId == null)
         {
            throw new NullPointerException();
         }
         if (portletName == null)
         {
            throw new NullPointerException();
         }

         this.applicationId = applicationId;
         this.portletName = portletName;
      }

      public Class<Portlet> getIdentifiableType()
      {
         return Portlet.class;
      }

      public String getApplicationId()
      {
         return applicationId;
      }

      public String getPortletName()
      {
         return portletName;
      }

      @Override
      public int hashCode()
      {
         return applicationId.hashCode() ^ portletName.hashCode();
      }

      @Override
      public boolean equals(Object obj)
      {
         if (obj == this)
         {
            return true;
         }
         if (obj instanceof Id)
         {
            Id that = (Id)obj;
            return applicationId.equals(that.applicationId) && portletName.equals(that.portletName);
         }
         return false;
      }

      @Override
      public String toString()
      {
         return "Portlet.Id[applicationId=" + applicationId + ",portletName=" + portletName + "]";
      }
   }
}
