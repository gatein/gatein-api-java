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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Context
{
   private static final String LIST_SEPARATOR = ", ";
   private final LinkedHashMap<String, ComponentIndex> namesToComponents;
   private final int requiredCardinality;
   private final String knownComponents;
   private final String requiredComponents;
   private final String defaultSeparator;

   public boolean isIgnoringRemainingAfterFirstMissingOptional()
   {
      return ignoreRemainingAfterFirstMissingOptional;
   }

   private final boolean ignoreRemainingAfterFirstMissingOptional;

   public Context(String defaultSeparator, List<Component> componentList, boolean ignoreRemainingAfterFirstMissingOptional)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(componentList, "Component list");

      int size = componentList.size();
      namesToComponents = new LinkedHashMap<String, ComponentIndex>(size);
      this.ignoreRemainingAfterFirstMissingOptional = ignoreRemainingAfterFirstMissingOptional;

      StringBuilder knownComponentsSB = new StringBuilder();

      int current = 1;
      int required = 0;
      for (Component component : componentList)
      {
         addComponent(component, current - 1);

         if (component.isRequired())
         {
            required++;
         }

         String name = component.getName();
         buildString(knownComponentsSB, name, current <= size, LIST_SEPARATOR);
      }

      this.requiredCardinality = required;

      // need to re-iterate over components now that we know which ones are required to build list of required components' name
      StringBuilder requiredComponentsSB = new StringBuilder();
      int requiredIndex = 0;
      for (Component component : componentList)
      {
         if (component.isRequired())
         {
            buildString(requiredComponentsSB, component.getName(), ++requiredIndex < requiredCardinality, LIST_SEPARATOR);
         }
      }

      knownComponents = knownComponentsSB.toString();
      requiredComponents = requiredComponentsSB.toString();
      this.defaultSeparator = defaultSeparator;
   }

   private void buildString(StringBuilder sb, String toAppend, boolean appendSeparator, String separator)
   {
      sb.append(toAppend);
      if (appendSeparator)
      {
         sb.append(separator);
      }
   }

   private void addComponent(Component component, int i)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(component, "Component");
      namesToComponents.put(component.getName(), new ComponentIndex(namesToComponents.size(), component));
   }

   public void setSeparator(String separator, int position)
   {
   }

   protected int getIndexFor(String component)
   {
      return getComponentOrFail(component).index;
   }

   protected void validate(String value, int index)
   {
      for (ComponentIndex componentIndex : namesToComponents.values())
      {
         if (index == componentIndex.index)
         {
            componentIndex.component.validate(value);
            return;
         }
      }

      throw new IllegalArgumentException("No component with index: " + index + ". Known components are: " + knownComponents);
   }

   private ComponentIndex getComponentOrFail(String component)
   {
      ComponentIndex index = namesToComponents.get(component);
      if (index == null)
      {
         throw new IllegalArgumentException("Unknown component: " + component + ". Known components are: " + knownComponents);
      }
      else
      {
         return index;
      }
   }

   public String toString(Id id)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(id, "Id to output as String");

      StringBuilder sb = new StringBuilder(111);
      int componentNumber = id.getComponentNumber();
      for (ComponentIndex componentIndex : namesToComponents.values())
      {
         String name = componentIndex.component.getName();
         int index = componentIndex.index;

         String value = id.getComponent(index, name, this);
         if (value != null)
         {
            sb.append(value);
         }
         else
         {
            if (componentIndex.component.isRequired())
            {
               throw new IllegalArgumentException("Missing value for required component '" + name + "'");
            }
            else if (ignoreRemainingAfterFirstMissingOptional)
            {
               break;
            }
         }

         if (index < componentNumber - 1 && index < namesToComponents.size())
         {
            sb.append(defaultSeparator);
         }
      }
      return sb.toString();
   }

   void validate(String[] componentValues)
   {
      int componentNumber = namesToComponents.size();
      if (componentValues.length < requiredCardinality)
      {
         throw new IllegalArgumentException("Wrong number of components: " + componentValues.length
            + ". Was expecting at most " + componentNumber + " values for components: "
            + knownComponents + "; among which " + requiredComponents + " are required.");
      }

      boolean error = false;
      StringBuilder sb = null;

      String[] nullPaddedValues = Arrays.copyOf(componentValues, componentNumber);

      int currentValue = 0;
      for (ComponentIndex index : namesToComponents.values())
      {
         try
         {
            index.component.validate(nullPaddedValues[currentValue++]);
         }
         catch (Exception e)
         {
            error = true;
            if (sb == null)
            {
               sb = new StringBuilder(111);
            }
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

   public boolean isComponentRequired(String component)
   {
      return getComponentOrFail(component).component.isRequired();
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
