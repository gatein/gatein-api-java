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

import org.gatein.api.internal.ObjectToStringBuilder;
import org.gatein.api.internal.Parameters;

import java.io.Serializable;

/**
 * Represents the visiblity of a {@link Node}
 * 
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class Visibility implements Serializable {
    private final Status status;
    private final PublicationDate publicationDate;

    /**
     * Creates a new instance with the status set to {@link Status#VISIBLE}
     */
    public Visibility() {
        this(Status.VISIBLE);
    }

    /**
     * Creates a new instance with the specified status
     * 
     * @param status the status
     * @throws IllegalArgumentException if the status is null, or the status is {@link Status#PUBLICATION}
     */
    public Visibility(Status status) {
        this.status = Parameters.requireNonNull(status, "status");
        this.publicationDate = null;
    }

    /**
     * Creates a new instance with the status set to {@link Status#PUBLICATION} which is visible at the time specified by the
     * publication date
     * 
     * @param publicationDate the publication date
     */
    public Visibility(PublicationDate publicationDate) {
        this.status = Status.PUBLICATION;
        this.publicationDate = Parameters.requireNonNull(publicationDate, "publicationDate");
    }

    /**
     * Returns true if the status is {@link Status#VISIBLE} or if the status is {@link Status#PUBLICATION} and the
     * publicationDate is within the current time.
     * 
     * @return true if visible
     */
    public boolean isVisible() {
        switch (status) {
            case VISIBLE:
                return true;
            case PUBLICATION:
                return publicationDate.within(System.currentTimeMillis());
            default:
                return false;
        }
    }

    /**
     * Returns the status
     * 
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the publication date or null if the status is not {@link Status#PUBLICATION}
     * 
     * @return the publication date or null
     */
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
