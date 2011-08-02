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

package org.gatein.api.util;

import java.lang.reflect.ParameterizedType;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public abstract class Type<T>
{
   private final String name;
   private final Class<T> valueType;

   @SuppressWarnings("unchecked")
   public Type(String name)
   {
      this.name = name;
      final java.lang.reflect.Type genericSuperclass = getClass().getGenericSuperclass();
      if (genericSuperclass instanceof ParameterizedType)
      {
         ParameterizedType superclass = (ParameterizedType)genericSuperclass;

         valueType = (Class<T>)superclass.getActualTypeArguments()[0];
      }
      else
      {
         throw new IllegalArgumentException("Must instantiate Type with a specific Java type parameter");
      }
   }

   @Override
   public String toString()
   {
      final StringBuilder sb = new StringBuilder();
      sb.append("Type");
      sb.append("{name='").append(name).append('\'');
      sb.append(", valueType=").append(valueType);
      sb.append('}');
      return sb.toString();
   }

   public String getName()
   {
      return name;
   }

   public Class<T> getValueType()
   {
      return valueType;
   }
}