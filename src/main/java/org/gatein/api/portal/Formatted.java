/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

package org.gatein.api.portal;

import org.gatein.api.annotation.NotNull;

/**
 * Interface to represent something that supports {@link java.util.Formatter#format(String, Object...)} where
 * by supplying the formatted arguments (the Object array) one can customize the format of an object implementing
 * this interface.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Formatted
{
   /**
    * The arguments which can be formatted. This should be documented by the implementing class on how many and
    * which arguments are formatted.
    *
    * @return the formatter arguments
    */
   Object[] getFormatArguments();

   /**
    * The arguments which can be formatted. This should be documented by the implementing class on how many and
    * which arguments are formatted.
    *
    * @param adapter an adapter which can 'adapt' the argument to be used in the format method as apposed to the original.
    * @return the formatter arguments
    */
   Object[] getFormatArguments(@NotNull Adapter adapter);

   /**
    * An adapter which can be used to adapt an argument which will be used in the formatting method as apposed to the
    * original value produced by the <code>Formatted</code> implementation.
    */
   public static interface Adapter
   {
      /**
       * @param index    the index of the argument returned from {@link org.gatein.api.portal.Formatted#getFormatArguments()}}
       *                 method.
       * @param argument the original argument produced by the <code>Formatted</code> implementation.
       * @return the object to use for the formatting method {@link java.util.Formatter#format(String, Object...)}
       */
      @NotNull
      Object adapt(int index, @NotNull Object argument);
   }
}
