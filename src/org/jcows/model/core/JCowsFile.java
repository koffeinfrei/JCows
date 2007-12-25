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
package org.jcows.model.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.log4j.Logger;
import org.jcows.JCowsException;
import org.jcows.system.Properties;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JCowsFile {

  private static final Logger LOGGER = Logger.getLogger(JCowsFile.class);
  
  private String m_wsdlUrl;
  
  /**
   * Constructs a new instance of this class with the specified URL of 
   * the WSDL document.
   * 
   * @param wsdlUrl the URL of the WSDL document.
   */
  public JCowsFile(String wsdlUrl){
    m_wsdlUrl = wsdlUrl;
  }
  
  /**
   * Gets the local name of the WSDL document name.<br/>
   * This is taken from {@link org.apache.axis.wsdl.toJava.JavaBuildFileWriter} such that
   * we get the same name as the WSDL2Java parser generates.
   * 
   * @return the local name of the WSDL document.
   */
  public String getWsdlLocalName() {
    int index = 0;
    String wsdlFile = m_wsdlUrl;
    if ((index = wsdlFile.lastIndexOf("/")) > 0) {
        wsdlFile = wsdlFile.substring(index + 1);
    }
    if ((index = wsdlFile.lastIndexOf("?")) > 0) {
        wsdlFile = wsdlFile.substring(0, index);
    }
    if ((index = wsdlFile.indexOf('.')) != -1) {
        return wsdlFile.substring(0, index);
    } 
    else {
        return wsdlFile;
    }
  }
  
  /**
   * Trims the node (remove whitespace)
   * and normalizes it.
   * 
   * @param node the {@link org.w3c.dom.Node Node} to be normalized.
   */
  public void normalizeDomNode(Node node){
    if (node.getNodeType() == Node.TEXT_NODE){
      node.setNodeValue(node.getNodeValue().trim());
      return;
    }
    NodeList nodes = node.getChildNodes();
    for (int i = 0; i < nodes.getLength(); ++i)
      normalizeDomNode(nodes.item(i));
    //node.normalize();//TODO: can't use this for soapenvelope
  }
  
  /**
   * Generates a crc32 checksum of a string.
   * 
   * @param str the string to be checksummed.
   * @return the checksum value.
   */
  public long generateChecksum(String str){
    byte[] bytes = str.getBytes();
    Checksum checksumEngine = new CRC32();
    checksumEngine.update(bytes, 0, bytes.length);
    long checksum = checksumEngine.getValue();
    return checksum;
  }
  
  /**
   * Searches for the specified checksum in the JCows file
   * of the current web service and returns it.<br/>
   * Returns 0 if there is no entry.<br/>
   * 
   * @return the checksum value if found, 0 otherwise.
   * @throws JCowsException
   */
  public long getChecksum() throws JCowsException{
    try{
      File file = new File(Properties.getConfig("parser.outputDirName") 
          + getWsdlLocalName() + Properties.getConfig("parser.JCowsFileEnding"));
      if (file.exists()){
        BufferedReader in = new BufferedReader(new FileReader(file.getCanonicalPath()));
        in.readLine(); // skip first line, it contains the WSDL URL
        String buf = in.readLine(); // read checksum
        long oldChecksum = Long.parseLong(buf);
        return oldChecksum;
      }
    } 
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
    return 0;
  }
  
  /**
   * Returns a {@link java.util.List List} containing the names of the 
   * generated classes from the jcows file.
   * 
   * @return a {@link java.util.List List} of class names.
   * @throws JCowsException
   */
  public List getGeneratedClassNames() throws JCowsException{
    List<String> classnames = new ArrayList<String>();
    try {
      File file = new File(Properties.getConfig("parser.outputDirName") 
          + getWsdlLocalName() + Properties.getConfig("parser.JCowsFileEnding"));
      BufferedReader in = new BufferedReader(new FileReader(file.getCanonicalPath()));
      String buf;
      in.readLine(); // skip first line, as it contains the WSDL URL
      in.readLine(); // skip second line, as it contains the checksum
      while((buf = in.readLine()) != null){
        if (!buf.equals("")) classnames.add(buf); // skip empty lines
      }
      LOGGER.info("Loaded class names from " + file.getName());
      
      return classnames;
    }
    catch (FileNotFoundException e) {
      throw new JCowsException(Properties.getMessage("error.FileNotFoundException"), e);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }
  
  /**
   * Creates a new JCows file that stores information about the
   * current web service.
   * 
   * @param generatedClasses a {@link java.util.List List} of the generated classes.
   * @param newChecksum the value of the checksum of the WSDL document.
   * @throws JCowsException
   */
  public void createJCowsFile(List generatedClasses, long newChecksum)
      throws JCowsException{
    try {
      File file = new File(Properties.getConfig("parser.outputDirName") 
          + getWsdlLocalName() + Properties.getConfig("parser.JCowsFileEnding"));
      file.createNewFile();
      PrintWriter out = new PrintWriter(new FileWriter(file.getCanonicalPath()), true);
      out.println(m_wsdlUrl);
      out.println(newChecksum);
      for (Object o : generatedClasses)
        if (o != null) out.println(o);
      out.close();
      LOGGER.info("Created attributes file: " + file.getName());
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }
  }
  
  /**
   * Removes all Java source files from the output directory.
   */
  public void cleanOutputDir() {
    File outputDir = new File(Properties.getConfig("parser.outputDirName"));
    for (File file : outputDir.listFiles()){
      if (file.isDirectory()) {
        deleteDir(file);
      }
      else if (file.getName().endsWith(".java")){
        file.delete();
      }
    }
  }
  
  /**
   * Removes recursively the contents of a directory.
   * 
   * @param dir the directory to be removed.
   * @return true on success, false on failure.
   */
  public boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    // The directory is now empty so delete it
    return dir.delete();
  }
}
