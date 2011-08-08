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

import java.util.Arrays;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public abstract class GenericId<T extends Identifiable> implements Id<T>
{
   protected final GenericContext originalContext;
   private final Class<T> identifiableType;

   private GenericId(GenericContext context, Class<T> identifiableType)
   {
      this.originalContext = context;
      this.identifiableType = identifiableType;
   }

   public String toString(Context context)
   {
      return context.toString(this);
   }

   public Class<T> getIdentifiableType()
   {
      return identifiableType;
   }

   public String toString()
   {
      return originalContext.toString(this);
   }

   public Id getIdForChild(String childId)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(this, "Parent resource");
      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(childId, "child identifier", null);

      String[] components = getComponents();
      int childIndex = components.length;

      GenericContext context = getOriginalContext();
      context.validate(childId, childIndex);

      String[] newComponents = new String[childIndex + 1];
      System.arraycopy(components, 0, newComponents, 0, childIndex);
      newComponents[childIndex] = childId;

      return context.internalCreate(Identifiable.class, false, newComponents);
   }

   public String getComponent(String component)
   {
      int index = originalContext.getIndexFor(component);
      return getComponent(index, component, originalContext);
   }

   public boolean knowsComponent(String name)
   {
      return originalContext.hasComponent(name);
   }

   protected abstract String getComponent(int index, String component, Context context);

   public GenericContext getOriginalContext()
   {
      return originalContext;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof Id))
      {
         return false;
      }

      GenericId id = (GenericId)o;

      return Arrays.equals(getComponents(), id.getComponents());
   }

   @Override
   public int hashCode()
   {
      return Arrays.hashCode(getComponents());
   }

   public int compareTo(Id o)
   {
      if (this.equals(o))
      {
         return 0;
      }
      else
      {
         return toString().compareTo(o.toString());
      }
   }

   abstract String getComponentNameFor(int currentComponent);

   public Id getParent()
   {
      int componentNumber = getComponentNumber();
      if (componentNumber > 1)
      {
         int parent = componentNumber - 1;
         return originalContext.internalCreate(originalContext.getComponent(getComponentNameFor(parent)).getIdentifiedComponentClass(), true, Arrays.copyOf(getComponents(), parent));
      }
      else
      {
         return null;
      }
   }

   static class SimpleGenericId<T extends Identifiable> extends GenericId<T>
   {
      private final String root;
      private String componentName;

      SimpleGenericId(GenericContext context, Class<T> identifiableType, String rootComponent)
      {
         super(context, identifiableType);
         this.root = rootComponent;
      }

      @Override
      protected String getComponent(int index, String component, Context context)
      {
         if (index != 0)
         {
            if (context.isComponentRequired(component))
            {
               throw new IllegalArgumentException("Unknown component: " + component);
            }
            else
            {
               return null;
            }
         }
         else
         {
            return root;
         }
      }

      public String[] getComponents()
      {
         return new String[]{root};
      }

      public int getComponentNumber()
      {
         return 1;
      }

      public String getRootComponent()
      {
         return root;
      }

      public void associateComponentWith(int componentIndex, String componentName)
      {
         if (componentIndex != 0)
         {
            throw new IllegalStateException("Shouldn't be possible");
         }
         this.componentName = componentName;
      }

      @Override
      String getComponentNameFor(int componentIndex)
      {
         if (componentIndex != 0)
         {
            throw new IllegalArgumentException("Invalid component index: " + componentIndex);
         }
         else
         {
            return componentName;
         }
      }
   }

   static class ComplexGenericId<T extends Identifiable> extends GenericId<T>
   {
      private final String[] components;
      private final String[] associatedComponentName;

      ComplexGenericId(GenericContext context, Class<T> identifiableType, String[] components)
      {
         super(context, identifiableType);
         this.components = components;
         associatedComponentName = new String[components.length];
      }

      @Override
      protected String getComponent(int index, String component, Context context)
      {
         if (index < 0 || index >= components.length)
         {
            if (context.isComponentRequired(component))
            {
               throw new IllegalArgumentException("Unknown component: " + component);
            }
            else
            {
               return null;
            }
         }
         else
         {
            return components[index];
         }
      }

      public String[] getComponents()
      {
         return components;
      }

      public int getComponentNumber()
      {
         return components.length;
      }

      public String getRootComponent()
      {
         return components[0];
      }

      public void associateComponentWith(int componentIndex, String componentName)
      {
         associatedComponentName[componentIndex] = componentName;
      }

      @Override
      String getComponentNameFor(int currentComponent)
      {
         return associatedComponentName[currentComponent];
      }
   }
}
