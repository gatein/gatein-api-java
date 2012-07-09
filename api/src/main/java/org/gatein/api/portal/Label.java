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

import org.gatein.api.i18n.Localized;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Label extends Localized<String>
{
   /**
    * Used to return the value of the label. If the label is localized it will use the default locale of the request.
    * If it's not localized it can still be a resource bundle expression in which case the resolve parameter can be used
    * to resolve the value from the resource bundle.
    *
    * @param resolve true if the label is to be resolved. If it's a localized or a non resource bundle expression, this
    *                parameter is ignore.
    * @return the value of the label
    */
   String getValue(boolean resolve);

   /**
    * Used to set the value w/out specifying the locale. If it's localized it will use the
    * default locale of the request.
    *
    * @param value the value of the label
    */
   void setValue(String value);

   /**
    * Indicates if the label is localized or not.
    *
    * @return true if the label is NOT localized
    */
   boolean isLocalized();
}
