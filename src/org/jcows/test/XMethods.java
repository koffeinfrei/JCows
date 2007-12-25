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
package org.jcows.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcows.JCowsException;
import org.jcows.system.Properties;

/**
 * This class provides the functionality to interact with the
 * xmethods web service repository.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 *
 */
public class XMethods{
  private URL m_url;
  private String[] m_wsdlUrls;
  
  private boolean m_enableTimeout = true;
  
  private static final String XMETHODS_URL = "http://www.xmethods.net/ve2/Directory.po";
  private static final String WSDL_URLS_FILE_PREFIX = "xmethods_";
  
  /**
   * Creates an instance with a default URL where the WSDL URLs can be found. 
   * 
   * @throws JCowsException
   */
  public XMethods() throws JCowsException{
    try{
      m_url = new URL(XMETHODS_URL);
    }
    catch(MalformedURLException e){
      throw new JCowsException(Properties.getMessage("error.MalformedURLException"), e);
    }
  }
  
  /**
   * Creates an instance with a specified URL where the WSDL URLs can be found.
   * 
   * @param url The URL as a String.
   * @throws JCowsException
   */
  public XMethods(String url) throws JCowsException{
    try {
      m_url = new URL(url);
    }
    catch(MalformedURLException e){
      throw new JCowsException(Properties.getMessage("error.MalformedURLException"), e);
    }
  }
  
  /**
   * Creates an instance with a specified URL where the WSDL urls can be found.
   * 
   * @param url The URL as an URL Object.
   */
  public XMethods(URL url){
    m_url = url;
  }
  
  
  /**
   * Enables or disable the server timeout<br/>
   * Default is set to true.
   * 
   * @param enable indicates whether to enable timeouts or not. 
   */
  public void enableServerTimeout(boolean enable){
    m_enableTimeout = enable;
  }
  
  /**
   * Returns a String array containing all WSDL URLs fetched from
   * the latest WSDL URL file.
   * 
   * @return an array of WSDL URLs.
   * @throws JCowsException
   */
  public String[] getWsdlUrlsFromFile() throws JCowsException{
    if (m_wsdlUrls == null){
      System.out.println("Getting WSDL URLs from file...");
      if (!openWsdlUrls()){
        System.out.println("No file found. Getting WSDL URLs from server...");
        getWsdlUrlsFromServer();
      }
    }
    return m_wsdlUrls;
  }
  
  /**
   * Returns a String array containing all WSDL URLs fetched from
   * the xmethods server.
   * 
   * @return an array of WSDL URLs.
   * @throws JCowsException
   */
  public String[] getWsdlUrlsFromServer() throws JCowsException{
    if (m_wsdlUrls == null){
      System.out.println("Getting WSDL URLs from server...");
      getWsdlUrls();
      System.out.println("Saving WSDL URLs to file...");
      String fileName = saveWsdlUrls();
      System.out.println("Saved to file '" + fileName + "'.");
    }
    return m_wsdlUrls;
  }
  
  /**
   * Fetches all WSDL URLs from the xmethods page.
   * 
   * @throws JCowsException
   */
  private void getWsdlUrls() throws JCowsException{
    try {
      Vector<String> wsdlUrls = new Vector<String>();
      URLConnection conn = (HttpURLConnection) m_url.openConnection();
      // timeout values in seconds
      if (m_enableTimeout){
        int connect_timeout = Integer.parseInt(Properties.getConfig("network.connectTimeout"));
        int read_timeout = Integer.parseInt(Properties.getConfig("network.readTimeout"));
      
        // timeout values needed in milliseconds
        conn.setConnectTimeout(connect_timeout * 1000);
        conn.setReadTimeout(read_timeout * 1000);
      }
      
      BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getInputStream()));
      
      Pattern pattern = Pattern.compile(
          "<a href=\"http:\\/\\/www.xmethods.net\\/tryit.html\\?url=([^\"]+)", 
          Pattern.CASE_INSENSITIVE);
      
      String line;
      while ((line = in.readLine()) != null) {
        // find line on which the URLs are
        if (line.toLowerCase().startsWith("</script>")){
          line = URLDecoder.decode(line, "UTF-8");
          
          Matcher matcher = pattern.matcher(line);
          while(matcher.find()){
            //System.out.print(".");
            wsdlUrls.add(matcher.group(1));
          }
          break;
        }
      }
      //System.out.println();
      in.close();
      
      m_wsdlUrls = wsdlUrls.toArray(new String[0]);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }
  
  /**
   * Saves all WSDL URLs to a file.
   *
   * @return the name of the file
   * @throws JCowsException
   */
  private String saveWsdlUrls() throws JCowsException{
    try {
      // get current date and time
      Date date = new Date();
      Format format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
      String dateStr = format.format(date);
      
      String fileName = WSDL_URLS_FILE_PREFIX + dateStr;
      
      File file = new File(fileName);
      PrintWriter out = new PrintWriter(new FileWriter(file.getCanonicalPath()), true);
      for (String line : m_wsdlUrls)
        out.println(line);
      
      out.close();
      
      return fileName;
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }
  
  /**
   * Imports all WSDL URLs from the latest WSDL URL file.
   * 
   * @return whether a file could be found or not.
   * @throws JCowsException
   */
  private boolean openWsdlUrls() throws JCowsException{
    Vector<String> wsdlUrls = new Vector<String>();
    
    // only get WSDL URL files
    FileFilter fileFilter = new FileFilter() {
      public boolean accept(File file) {
        return file.isFile() 
          && file.getName().startsWith(WSDL_URLS_FILE_PREFIX);
      }
    };
    
    File dir = new File(".");
    File[] fileList = dir.listFiles(fileFilter);
    
    if (fileList.length == 0) return false;
    
    // get latest (newest) file
    File latestFile = null;
    long latestMod = 0;
    for (File file : fileList)
      if (file.lastModified() > latestMod){
        latestFile = file;
        latestMod = file.lastModified();
      }
    System.out.println(latestFile.getAbsolutePath());
    try {
      BufferedReader in = new BufferedReader(new FileReader(latestFile.getAbsolutePath()));
      String str;
      while ((str = in.readLine()) != null){
        //System.out.print(".");
        if (!str.equals("") && !str.startsWith("#"))
          wsdlUrls.add(str.trim());
      }
      //System.out.println();
      in.close();
    }
    catch (FileNotFoundException e) {
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"), e);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
    
    m_wsdlUrls = wsdlUrls.toArray(new String[0]);
    return true;
  }
  
  
  
  // ####################################################### //
  
  /**
   * This method is for testing purpose only.
   * 
   * @param args not used.
   * @throws JCowsException
   */
  /*
  public static void main(String args[]) throws JCowsException{
    Properties.init();
    
    XMethods xmeth = new XMethods();
    xmeth.enableServerTimeout(false);
    //System.out.println("Getting WSDL URLs...");
    String[] wsdlUrls = xmeth.getWsdlUrlsFromServer();
    System.out.println("Got " + wsdlUrls.length + " URLs.\nFinished.");
  }
  */
}
