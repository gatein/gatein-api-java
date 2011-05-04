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

package org.gatein.api.organization;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Operation
{
   private final String name;

   // known operations
   private final static Map<String, Operation> OPERATIONS = new HashMap<String, Operation>();
   public static final Operation CREATE = new Operation("CREATE");
   public static final Operation READ = new Operation("READ");
   public static final Operation UPDATE = new Operation("UPDATE");
   public static final Operation DELETE = new Operation("DELETE");

   private Operation(String name)
   {
      this.name = name;
      OPERATIONS.put(name, this);
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      Operation operation = (Operation)o;

      if (name != null ? !name.equals(operation.name) : operation.name != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      return name != null ? name.hashCode() : 0;
   }
}
