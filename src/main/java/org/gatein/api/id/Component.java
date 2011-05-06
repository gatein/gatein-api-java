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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Component<T>
{
   private final String name;
   private final Matcher matcher;
   private final String validationPattern;
   private final boolean required;
   private final boolean hierarchical;
   private final Class<T> identifiedComponentClass;

   public Component(String name, Class<T> identifiedComponentClass, Pattern validationPattern, boolean required, boolean hierarchical)
   {
      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(name, "Component name", null);
      ParameterValidation.throwIllegalArgExceptionIfNull(validationPattern, "Validation pattern");
      ParameterValidation.throwIllegalArgExceptionIfNull(identifiedComponentClass, "Class of the objects identified by this component");
      this.name = name;
      matcher = validationPattern.matcher("");
      this.validationPattern = validationPattern.toString();
      this.required = required;
      this.identifiedComponentClass = identifiedComponentClass;
      this.hierarchical = hierarchical;
   }

   public String getName()
   {
      return name;
   }

   public void validate(String componentValue)
   {
      if (!ParameterValidation.isNullOrEmpty(componentValue))
      {
         matcher.reset(componentValue);
         if (!matcher.matches())
         {
            throw new IllegalArgumentException("Invalid value '" + componentValue + "'. Valid values should match: '" + validationPattern + "'");
         }
      }
      else
      {
         if (required)
         {
            throw new IllegalArgumentException("Component '" + name + "' is required.");
         }
      }
   }

   public boolean isRequired()
   {
      return required;
   }

   public boolean isHierarchical()
   {
      return hierarchical;
   }
}