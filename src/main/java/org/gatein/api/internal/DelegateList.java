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

package org.gatein.api.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class DelegateList<E> implements List<E>
{
   protected final List<E> delegate;

   public DelegateList(List<E> delegate)
   {
      this.delegate = delegate;
   }

   @Override
   public int size()
   {
      return delegate.size();
   }

   @Override
   public boolean isEmpty()
   {
      return delegate.isEmpty();
   }

   @Override
   public boolean contains(Object o)
   {
      return delegate.contains(o);
   }

   @Override
   public Iterator<E> iterator()
   {
      return delegate.iterator();
   }

   @Override
   public Object[] toArray()
   {
      return delegate.toArray();
   }

   @Override
   public <T> T[] toArray(T[] a)
   {
      return delegate.toArray(a);
   }

   public boolean add(E e)
   {
      return delegate.add(e);
   }

   @Override
   public boolean remove(Object o)
   {
      return delegate.remove(o);
   }

   @Override
   public boolean containsAll(Collection<?> c)
   {
      return delegate.containsAll(c);
   }

   public boolean addAll(Collection<? extends E> c)
   {
      return delegate.addAll(c);
   }

   public boolean addAll(int index, Collection<? extends E> c)
   {
      return delegate.addAll(index, c);
   }

   @Override
   public boolean removeAll(Collection<?> c)
   {
      return delegate.removeAll(c);
   }

   @Override
   public boolean retainAll(Collection<?> c)
   {
      return delegate.retainAll(c);
   }

   @Override
   public void clear()
   {
      delegate.clear();
   }

   @Override
   public boolean equals(Object o)
   {
      return delegate.equals(o);
   }

   @Override
   public int hashCode()
   {
      return delegate.hashCode();
   }

   @Override
   public E get(int index)
   {
      return delegate.get(index);
   }

   public E set(int index, E element)
   {
      return delegate.set(index, element);
   }

   public void add(int index, E element)
   {
      delegate.add(index, element);
   }

   @Override
   public E remove(int index)
   {
      return delegate.remove(index);
   }

   @Override
   public int indexOf(Object o)
   {
      return delegate.indexOf(o);
   }

   @Override
   public int lastIndexOf(Object o)
   {
      return delegate.lastIndexOf(o);
   }

   @Override
   public ListIterator<E> listIterator()
   {
      return delegate.listIterator();
   }

   @Override
   public ListIterator<E> listIterator(int index)
   {
      return delegate.listIterator(index);
   }

   @Override
   public List<E> subList(int fromIndex, int toIndex)
   {
      return delegate.subList(fromIndex, toIndex);
   }
}
