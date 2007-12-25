/*
 * Copyright 2006 Project JCows.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jcows.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.Library;
import org.jcows.JCowsException;

import com.cloudgarden.resource.SWTResourceManager;
/**
 * This class contains some static methods used from several JCows classes.
 * Theses methods are "helper" methods and support the main classes.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class JCowsHelper {
	
  //private static final Logger LOGGER = Logger.getLogger(JCowsHelper.class);
	
  /** 
   * Extracts a file from the Jar file.
   * 
   * @param jarname the name of the Jar file.
   * @param filename the name of the file.
   * @param override overrides the file even if it exists.
   * @return File the file from the Jar file.
   * @throws IOException
   * @throws JCowsException if file was not found or could not extracted.
   */
  public static File getFile(String jarname,String filename,boolean override) throws JCowsException  {

    JarFile jarfile=null;
    ZipEntry entry=null;

    /*
     * Throw FileNotFoundException if some variables
     * are null.
     */
    if(jarname==null || filename==null || jarname.equals("") || filename.equals(""))
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"),new FileNotFoundException());

    File outputFile = new File(filename);
    if(outputFile.exists()==true && override==false)
      return outputFile;

    File jarFile=new File(jarname);
    if(!jarFile.exists())
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"),new FileNotFoundException(jarname));

    try {
      jarfile = new JarFile(jarFile);
      entry = jarfile.getEntry(filename);
      if (entry == null)
        throw new JCowsException(Properties.getMessage("error.FileNotFoundException"),new ZipException(filename));
      outputFile=writeFile(jarfile.getInputStream(entry),filename);
    }
    catch(IOException e) {
      try {
        jarfile.close();
      }
      catch(IOException e2) {
        /*
         * Do nothing.
         */
      }
      throw new JCowsException(Properties.getMessage("error.IOException"),e);      
    }

    return outputFile;
  }
  
  /**
   * Writes a file from an input stream.
   * 
   * @param is the input stream to write.
   * @param filename name of the file.
   * @return the written file.
   * @throws JCowsException
   */
  private static File writeFile(InputStream is,String filename) throws JCowsException {

    OutputStream os=null;  
    File outputFile=new File(filename);
    
    /*
     * Create directory if it does not already exists.
     */
    File parentFile=outputFile.getParentFile();
    if(parentFile!=null)
      if(!parentFile.exists())
        parentFile.mkdirs();
    
    try {
      os=new FileOutputStream(outputFile);
      byte[] buffer=new byte[4096];
      int cnt=is.read(buffer);
      while(cnt>0) {
        os.write(buffer,0,cnt);
        cnt=is.read(buffer);
      }
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
    finally {
      try {
        if(os!=null)
          os.close();
        if(is!=null)
          is.close();
      }
      catch(IOException e) {
        /*
         * Do nothing.
         */
      }
    }
    return outputFile;
  }
 
  /**
   * Extracts a file from the Jar file.
   * 
   * @param filename the name of the file.
   * @param override overrides the file even if it exists.
   * @return the file from the Jar file.
   * @throws JCowsException if file was not found or could not extracted.
   */
  public static File getFile(String filename,boolean override) throws JCowsException {
    
    InputStream is=null;
    OutputStream os=null;
    
    /*
     * Basic check.
     */
    if(filename==null)
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"),new FileNotFoundException());
  
    /*
     * Check, if file exists already and doesn't need
     * to be extracted.
     */
    File outputFile=new File(filename);
    if(outputFile.exists() && override==false)
      return outputFile;
  
    /*
     * Get URL of the file by the Jar ClassLoader.
     */
    URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
    if(url==null)
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"),new FileNotFoundException(filename));

    try {
      outputFile=writeFile(url.openStream(),filename);
    }
    catch(IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"),e);      
    }
    
    return outputFile;
  }
  
	/**
   * SWT needs OS dependent libraries to create native widgets (windows, buttons
   * etc.). This method extracts these libraries from the Jar file where
   * the application is running from.
   * 
   * @throws JCowsException if extracting of libraries failed.
   */
	public static void preprocessSWT() throws JCowsException{
	  try {
	    Library.loadLibrary("swt");
	  }
	  catch (IllegalArgumentException ignore) {
	    // ignore
	  }
		catch(Throwable e) {
		  /*
		   * Get file name from the error message.
		   */
		  String filename = e.getMessage().replaceAll("no ([-_a-zA-Z0-9]+) in java.library.path","$1");
		  String os=System.getProperty("os.name");
		  /*
		   * Do OS test because libraries have different names and prefixes.
		   */
		  if(os.startsWith("Windows")) {
		    filename+=".dll";
		    /*
		     * Extract file.
		     */
		    getFile(filename,false);
		  }
		  else if(os.startsWith("Linux")) {
		    filename+=".so";
		    if(filename.startsWith("swt-gtk-")) {
		      filename=filename.replaceAll("swt-gtk-","libswt-gtk-");
		      /*
		       * Extract file.
		       */
		      getFile(filename,false);   	
		      filename=filename.replaceAll("libswt-gtk-","libswt-pi-gtk-");
		      /*
		       * Extract file.
		       */
		      getFile(filename,false);
		    }
		  }
		}
	}
  
  /**
   * Returns a list of Java classes (inner classes are excluded) inside the Jar file.
   * Every class entry contains the full qualified class. For example org.jcows.JCows
   * 
   * @param jarFileURL the Jar file that contains Java classes.
   * @return list of strings that contains class paths.
   * @throws JCowsException if creating of a list failed.
   */
  public static List<String> preloadClassList(URL jarFileURL) throws JCowsException {
    
    List<String> list=new LinkedList<String>();
    JarFile jarFile=null;
    File file=null;
    
    try {
      file=new File(jarFileURL.toURI());
    }
    catch(URISyntaxException e) {
      throw new JCowsException(Properties.getMessage("error.URISyntaxException"),e);
    }

    if(!file.exists())
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"),new FileNotFoundException(file.getAbsolutePath()));

    try {
      jarFile=new JarFile(file);
    }
    catch(IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"),e);
    }
    
    Enumeration<JarEntry> entries=jarFile.entries();
    while(entries.hasMoreElements()) {
      String filename=entries.nextElement().getName();
      if(filename.startsWith("org/jcows/") && filename.endsWith(".class") && !filename.contains("$")) {
        String clazz=filename.replace(".class","");
        clazz=clazz.replace("/",".");
        list.add(clazz);
      }
    }
    
    return list;
  }
  
  /**
   * Returns a {@link org.eclipse.swt.graphics.Color} object from a RGB value in hexadecimal
   * format.
   * 
   * @param hex the RGB value in hexadecimal format.
   * @return a {@link org.eclipse.swt.graphics.Color} object.
   */
  public static Color getColor(String hex) {
    int r,g,b;
    try {
      r = Integer.parseInt( hex.substring(0,2).trim(), 16);
      g = Integer.parseInt( hex.substring(2,4).trim(), 16);
      b = Integer.parseInt( hex.substring(4,6).trim(), 16);
    }
    catch(NumberFormatException e) {
      /*
       * Return black as default color.
       */
      return SWTResourceManager.getColor(255,255,255);
    }
    catch(StringIndexOutOfBoundsException e) {
      /*
       * Return black as default color.
       */
      return SWTResourceManager.getColor(0,0,0); 
    }
    return SWTResourceManager.getColor(r,g,b);
  }
  
}
