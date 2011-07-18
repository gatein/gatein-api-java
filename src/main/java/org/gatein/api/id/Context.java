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


import org.gatein.api.util.ParameterValidation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

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
   private final boolean ignoreRemainingAfterFirstMissingOptional;
   private boolean hasHierarchicalComponents;

   private Context(String defaultSeparator, List<Component> componentList, boolean ignoreRemainingAfterFirstMissingOptional)
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

         if (!hasHierarchicalComponents && component.isHierarchical())
         {
            hasHierarchicalComponents = true;
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

      final String[] components = id.getComponents();

      // validate that the components for the specified Id are valid for this context
      validate(components);

      int index = 0;
      for (String component : components)
      {
         sb.append(component);

         if (index < componentNumber - 1)
         {
            sb.append(defaultSeparator);
         }

         index++;
      }

      return sb.toString();
   }

   void validate(String... componentValues)
   {
      int componentNumber = namesToComponents.size();
      if (componentValues.length < requiredCardinality || (!hasHierarchicalComponents && componentValues.length > componentNumber))
      {
         throw new IllegalArgumentException("Wrong number of components: " + componentValues.length
            + ". Was expecting at most " + componentNumber + " values for components: "
            + knownComponents + "; among which '" + requiredComponents + "' are required. Got: " + Arrays.toString(componentValues));
      }

      boolean error = false;
      StringBuilder sb = null;

      int testComponentNumber = Math.max(componentNumber, componentValues.length);
      String[] nullPaddedValues = Arrays.copyOf(componentValues, testComponentNumber);

      int currentComponent = 0;

      if (!hasHierarchicalComponents)
      {
         for (ComponentIndex index : namesToComponents.values())
         {
            try
            {
               index.component.validate(nullPaddedValues[currentComponent]);
            }
            catch (Exception e)
            {
               error = true;
               sb = createErrorMessage(sb, e.getLocalizedMessage());
            }
            currentComponent++;
         }
      }
      else
      {
         final List<ComponentIndex> components = new ArrayList<ComponentIndex>(namesToComponents.values());
         for (String value : nullPaddedValues)
         {
            // do not go past the number of known components
            if (currentComponent >= componentNumber)
            {
               currentComponent = componentNumber - 1;
            }

            final ComponentIndex index = components.get(currentComponent);
            try
            {
               index.component.validate(value);
            }
            catch (Exception e)
            {
               // if the previous component is hierarchical, check if we can validate the current value against it
               if (currentComponent > 1)
               {
                  final Component component = components.get(currentComponent - 1).component;
                  if (component.isHierarchical())
                  {
                     try
                     {
                        component.validate(value);
                        currentComponent--; // set the current component to the hierarchical one before processing the rest
                        error = false; // we're not in an error situation
                     }
                     catch (Exception e1)
                     {
                        error = true;
                        sb = createErrorMessage(sb, e1.getLocalizedMessage());
                     }
                  }
               }
               else
               {
                  error = true;
                  sb = createErrorMessage(sb, e.getLocalizedMessage());
               }
            }

            // by default assume that we're moving to the next component to give priority to non-hierarchical elements
            currentComponent++;
         }
      }

      if (error)
      {
         throw new IllegalArgumentException("Invalid components:\n" + sb.toString());
      }
   }

   private StringBuilder createErrorMessage(StringBuilder sb, String message)
   {
      if (sb == null)
      {
         sb = new StringBuilder(111);
      }
      sb.append(message).append("\n");
      return sb;
   }

   public String[] extractComponents(String idAsString)
   {
      return idAsString.split(defaultSeparator);
   }

   public boolean isComponentRequired(String component)
   {
      return getComponentOrFail(component).component.isRequired();
   }

   public boolean isComponentUnboundedHierarchical(String component)
   {
      return getComponentOrFail(component).component.isHierarchical();
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

   public static ContextBuilder builder()
   {
      return new ContextBuilder();
   }

   public static class ContextBuilder
   {
      private List<Component> components = new ArrayList<Component>(7);
      private String defaultSeparator;
      private boolean ignoreRemainingAfterFirstMissingOptional;

      private ContextBuilder()
      {
      }

      public <T extends Identifiable<T>> ContextBuilder requiredComponent(String name, Class<T> componentType, Pattern validationPattern)
      {
         components.add(new Component<T>(name, componentType, validationPattern, true, false));
         return this;
      }

      public <T extends Identifiable> ContextBuilder optionalComponent(String name, Class<T> componentType, Pattern validationPattern)
      {
         components.add(new Component<T>(name, componentType, validationPattern, false, false));
         return this;
      }

      public <T extends Identifiable> ContextBuilder requiredUnboundedHierarchicalComponent(String firstComponentName, Class<T> componentType, Pattern validationPattern)
      {
         components.add(new Component<T>(firstComponentName, componentType, validationPattern, true, true));
         return this;
      }

      public ContextBuilder withDefaultSeparator(String defaultSeparator)
      {
         this.defaultSeparator = defaultSeparator;
         return this;
      }

      public ContextBuilder ignoreRemainingAfterFirstMissingOptional()
      {
         this.ignoreRemainingAfterFirstMissingOptional = true;
         return this;
      }

      public Context build()
      {
         if (components.isEmpty())
         {
            throw new IllegalStateException("Cannot build a Context with empty components");
         }
         return new Context(defaultSeparator, components, ignoreRemainingAfterFirstMissingOptional);
      }
   }
}
