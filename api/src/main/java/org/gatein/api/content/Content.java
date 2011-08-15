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

import org.gatein.api.id.Identifiable;
import org.gatein.api.util.Filter;
import org.gatein.api.util.Type;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Content extends Identifiable<Content>
{
   Type<Content> getType();

   String PORTLET_TYPE_NAME = "portlet";
   Type<Portlet> PORTLET = new Type<Portlet>(PORTLET_TYPE_NAME)
   {
   };

   Filter<Content> PORTLETS = new Filter<Content>()
   {
      @Override
      public boolean accept(Content item)
      {
         return PORTLET.equals(item.getType());
      }
   };

   String WSRP_TYPE_NAME = "wsrp";
   Type<WSRP> WSRP = new Type<WSRP>(WSRP_TYPE_NAME)
   {
   };

   Filter<Content> WSRPS = new Filter<Content>()
   {
      @Override
      public boolean accept(Content item)
      {
         return WSRP.equals(item.getType());
      }
   };

   String GADGET_TYPE_NAME = "gadget";
   Type<Gadget> GADGET = new Type<Gadget>(GADGET_TYPE_NAME)
   {
   };

   Filter<Content> GADGETS = new Filter<Content>()
   {
      @Override
      public boolean accept(Content item)
      {
         return GADGET.equals(item.getType());
      }
   };
}
