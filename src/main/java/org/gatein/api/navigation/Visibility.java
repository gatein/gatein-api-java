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

package org.gatein.api.navigation;

import org.gatein.api.Parameters;
import org.gatein.api.internal.ObjectToStringBuilder;

import java.io.Serializable;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Visibility implements Serializable {
    private final Status status;
    private final PublicationDate publicationDate;

    public Visibility() {
        this(Status.VISIBLE, null);
    }

    public Visibility(Status flag) {
        this(flag, null);
    }

    public Visibility(PublicationDate publicationDate) {
        this(Status.PUBLICATION, publicationDate);
    }

    public Visibility(Status status, PublicationDate publicationDate) {
        Parameters.requireNonNull(status, "status");
        if (status == Status.PUBLICATION && publicationDate == null)
            throw new IllegalArgumentException("publicationDate cannot be null when the status is " + Status.PUBLICATION);

        this.status = status;
        this.publicationDate = publicationDate;
    }

    public boolean isVisible() {
        switch (status) {
            case VISIBLE:
                return true;
            case PUBLICATION:
                return (publicationDate != null) && publicationDate.within(System.currentTimeMillis());
            case SYSTEM:
                return true;
            case HIDDEN:
                return false;
            default:
                return false;
        }
    }

    public Status getStatus() {
        return status;
    }

    public PublicationDate getPublicationDate() {
        return publicationDate;
    }

    @Override
    public String toString() {
        return ObjectToStringBuilder.toStringBuilder().add("flag", status).add("publicationDate", publicationDate).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Visibility))
            return false;

        Visibility that = (Visibility) o;
        if (status != that.status)
            return false;

        if (publicationDate == null)
            return that.publicationDate == null;
        return that.publicationDate != null && publicationDate.equals(that.publicationDate);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        return result;
    }

    public static enum Status {
        VISIBLE, SYSTEM, PUBLICATION, HIDDEN
    }
}
