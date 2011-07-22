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
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public abstract class Type<T, C>
{
   private final String name;
   private final Class<T> valueType;
   private final Class<C> originatingClass;
   private static final Map<Class, Map<String, Type>> registeredTypes = new HashMap<Class, Map<String, Type>>(7);

   @SuppressWarnings("unchecked")
   public Type(String name)
   {
      this.name = name;
      valueType = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
      originatingClass = (Class<C>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];

      register(name);
   }

   private void register(String name)
   {
      Map<String, Type> typeMap = getTypeMapFor(originatingClass);

      typeMap.put(name, this);
   }

   private static Map<String, Type> getTypeMapFor(Class originatingClass)
   {
      Map<String, Type> typeMap = registeredTypes.get(originatingClass);
      if (typeMap == null)
      {
         typeMap = new HashMap<String, Type>(7);
         registeredTypes.put(originatingClass, typeMap);
      }
      return typeMap;
   }


   public static Type forName(String name, Class originatingClass)
   {
      Map<String, Type> typeMap = getTypeMapFor(originatingClass);
      Type type = typeMap.get(name);
      if (type != null)
      {
         return type;
      }
      else
      {
         throw new IllegalArgumentException("Unknown Type: " + name + " for originating class " + originatingClass.getCanonicalName());
      }
   }

   public String getName()
   {
      return name;
   }

   public Class<T> getValueType()
   {
      return valueType;
   }

   public Class<C> getOriginatingClass()
   {
      return originatingClass;
   }
}