package org.gatein.api.id;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class BaseId<T extends Identifiable<T>> implements Id<T>
{

   @Override
   public String toString()
   {
      try
      {
         StringBuilder sb = new StringBuilder();
         toString(sb);
         return sb.toString();
      }
      catch (IOException e)
      {
         throw new UndeclaredThrowableException(e);
      }
   }

   public void toString(Appendable appendable) throws IOException
   {
      throw new UnsupportedOperationException("todo");
   }

   public int compareTo(Id<T> o)
   {
      throw new UnsupportedOperationException();
   }
}
