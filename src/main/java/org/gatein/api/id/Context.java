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

package org.gatein.api.id;


import org.gatein.api.ParameterValidation;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Context
{
   private final LinkedHashMap<String, ComponentIndex> namesToComponents;
   private final Component[] components;
   private final String format;
   private final String knownComponents;
   private final String defaultSeparator;

   public Context(String defaultSeparator, List<Component> componentList)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(componentList, "Component list");

      int size = componentList.size();
      components = new Component[size];
      namesToComponents = new LinkedHashMap<String, ComponentIndex>(size);

      StringBuilder formatSB = new StringBuilder();
      StringBuilder knownComponentsSB = new StringBuilder();

      int i = 1;
      for (Component component : componentList)
      {
         addComponent(component, i - 1);

         formatSB.append("%").append(i++).append("$s");
         knownComponentsSB.append(component.getName());
         if (i <= size)
         {
            formatSB.append(defaultSeparator);
            knownComponentsSB.append(", ");
         }
      }

      format = formatSB.toString();
      knownComponents = knownComponentsSB.toString();
      this.defaultSeparator = defaultSeparator;
   }

   private void addComponent(Component component, int i)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(component, "Component");
      components[i] = component;
      namesToComponents.put(component.getName(), new ComponentIndex(namesToComponents.size(), component));
   }

   public void setSeparator(String separator, int position)
   {
   }

   protected int getIndexFor(String component)
   {
      ComponentIndex index = namesToComponents.get(component);
      if (index == null)
      {
         throw new IllegalArgumentException("Unknown component: " + component + ". Known components are: " + knownComponents);
      }
      else
      {
         return index.index;
      }
   }

   public String toString(Id id)
   {
      return String.format(format, id.getComponents());
   }

   void validate(String[] componentValues)
   {
      if (componentValues.length != this.components.length)
      {
         throw new IllegalArgumentException("Wrong number of components: " + componentValues.length + ". Was expecting: "
            + this.components.length + ". Known components are: " + knownComponents);
      }

      boolean error = false;
      StringBuilder sb = new StringBuilder(111);

      for (int i = 0; i < components.length; i++)
      {
         try
         {
            components[i].validate(componentValues[i]);
         }
         catch (IllegalArgumentException e)
         {
            error = true;
            sb.append(e.getLocalizedMessage()).append("\n");
         }
      }

      if (error)
      {
         throw new IllegalArgumentException("Invalid components:\n" + sb.toString());
      }
   }

   public String[] extractComponents(String idAsString)
   {
      return idAsString.split(defaultSeparator);
   }

   private static class ComponentIndex
   {
      private final int index;
      private final Component component;

      private ComponentIndex(int index, Component component)
      {
         this.index = index;
         this.component = component;
      }
   }
}
