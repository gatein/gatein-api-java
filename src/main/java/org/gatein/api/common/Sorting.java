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

package org.gatein.api.common;

import org.gatein.api.internal.ObjectToStringBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A sorting object defining order (ASC, DESC) or a custom comparator, but not both.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Sorting<T> implements Serializable {
    private final Order order;
    private final Comparator<T> comparator;

    /**
     * A sorting object with an order, i.e. ascending or descending. T must be comparable.
     *
     * @param order the order to sort.
     */
    public Sorting(Order order) {
        this(order, null);
    }

    /**
     * A sorting object which will use a {@link Comparator} to sort.
     *
     * @param comparator the comparator
     * @see Comparator
     */
    public Sorting(Comparator<T> comparator) {
        this(null, comparator);
    }

    private Sorting(Order order, Comparator<T> comparator) {
        this.order = order;
        this.comparator = comparator;
    }

    /**
     * The order to sort, i.e. ascending or descending.
     *
     * @return the order, which can be null.
     */
    public Order getOrder() {
        return order;
    }

    /**
     * The comparator used to sort.
     *
     * @return the comparator, which can be null.
     */
    public Comparator<T> getComparator() {
        return comparator;
    }

    @Override
    public String toString() {
        return ObjectToStringBuilder.toStringBuilder().add("order", order).add("comparator", comparator).toString();
    }

    public static enum Order {
        ascending, descending
    }
}
