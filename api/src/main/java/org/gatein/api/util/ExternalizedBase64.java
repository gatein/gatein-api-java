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

package org.gatein.api.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Base64 Externalized functionality.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public abstract class ExternalizedBase64
{
   private final byte[] header;
   protected ExternalizedBase64(byte[] header)
   {
      this.header = header;
   }

   protected abstract void writeExternal(DataOutput out) throws IOException;

   protected abstract void readExternal(DataInput in) throws IOException;

   public void writeBase64(OutputStream stream) throws IOException
   {
      writeExternalBase64(stream);
   }

   public String toBase64String()
   {
      try
      {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         writeBase64(baos);
         return new String(baos.toByteArray(), Charset.forName("UTF-8"));
      }
      catch (IOException e)
      {
         final RuntimeException re = new RuntimeException(e.getMessage());
         re.setStackTrace(e.getStackTrace());
         throw re;
      }
   }

   protected void writeExternalBase64(final OutputStream out) throws IOException
   {
      Base64.OutputStream bstream = new Base64.OutputStream(out, Base64.ENCODE | Base64.URL_SAFE);
      DataOutputStream dos = new DataOutputStream(bstream);

      // write header so concrete classes can put identify their data stream.
      dos.write(header);

      //
      writeExternal(dos);
      bstream.flushBase64(); // Required to ensure last block is written to stream.
   }

   protected void readExternalBase64(final InputStream in) throws IOException
   {
      Base64.InputStream bstream = new Base64.InputStream(in, Base64.URL_SAFE);
      DataInputStream dis = new DataInputStream(bstream);

      // verify header
      boolean verify = verifyHeader(dis);
      if (!verify) throw new IOException("Invalid header detected for externalized data stream for " + getClass());

      //
      readExternal(dis);
      bstream.close();
   }

   private boolean verifyHeader(DataInputStream stream)
   {
      byte[] buffer = new byte[header.length];
      try
      {
         int read = stream.read(buffer);
         if (read == buffer.length)
         {
            for (int i=0; i<buffer.length; i++)
            {
               if (buffer[i] != header[i]) return false;
            }
         }
         else
         {
            return false;
         }
      }
      catch (IOException e)
      {
         return false;
      }

      return true;
   }
}
