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

import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class ComponentTestCase
{
   @Test(expectedExceptions = IllegalArgumentException.class)
   public void aComponentShouldHaveANonNullName()
   {
      new Component<Object>(null, Object.class, null, true, false);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void aComponentShouldHaveANonEmptyName()
   {
      new Component<Object>("", Object.class, null, true, false);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void aComponentShouldHaveAValidationPattern()
   {
      new Component<Object>("name", Object.class, null, true, false);
   }

   @Test
   public void isRequiredShouldWork()
   {
      Component foo = new Component<Object>("foo", Object.class, Pattern.compile(".*"), true, false);

      assert "foo".equals(foo.getName());
      assert foo.isRequired();

      foo = new Component<Object>("foo", Object.class, Pattern.compile(".*"), false, false);
      assert !foo.isRequired();
   }

   @Test
   public void isHierarchicalShouldWork()
   {
      Component foo = new Component<Object>("foo", Object.class, Pattern.compile(".*"), true, true);

      assert "foo".equals(foo.getName());
      assert foo.isHierarchical();

      foo = new Component<Object>("foo", Object.class, Pattern.compile(".*"), false, false);
      assert !foo.isHierarchical();
   }
}
