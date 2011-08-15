package org.gatein.api.id;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class BaseId<T extends Identifiable<T>> implements Id<T>
{

   public boolean knowsComponent(String name)
   {
      throw new UnsupportedOperationException();
   }

   public String[] getComponents()
   {
      throw new UnsupportedOperationException();
   }

   public Id<?> getParent()
   {
      throw new UnsupportedOperationException();
   }

   public int getComponentNumber()
   {
      throw new UnsupportedOperationException();
   }

   public Context getOriginalContext()
   {
      throw new UnsupportedOperationException();
   }

   public String getComponent(String component)
   {
      throw new UnsupportedOperationException();
   }

   public Id getIdForChild(String childId)
   {
      throw new UnsupportedOperationException();
   }

   public String toString(RenderingContext context)
   {
      throw new UnsupportedOperationException();
   }

   public String getRootComponent()
   {
      throw new UnsupportedOperationException();
   }

   public int compareTo(Id<T> o)
   {
      throw new UnsupportedOperationException();
   }
}
