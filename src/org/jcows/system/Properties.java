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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.jcows.JCowsException;

/**
 * This class manages the access to the JCows config, messages and history files.
 * The config setting file config.properties contains program settings. Information and error messages
 * are stored in the message.properties file. Program history (entered URLs etc.) are stored
 * in a history.jcows file. This file is created at first start.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 *
 */
public class Properties {
  public static final String KEY_HISTORY_URL = "URLHistory";
  public static final String KEY_JDK_PATH = "java.jdk.path";
  
  private static final Logger LOGGER = Logger.getLogger(Properties.class);

  private static String CONFIG_FILE="config.properties";
  private static String MESSAGES_FILE="messages";
  private static String HISTORY_FILE="history.jcows";
  
  private static final java.util.Properties config = new java.util.Properties();
  private static final java.util.Properties history = new java.util.Properties();
  private static ResourceBundle message;

  /**
   * Initializes the <code>Property</code> class.
   * 
   * @throws JCowsException if files are not found or not accessible.
   */
  public static void init() throws JCowsException {
    try {
      File configFile=new File(CONFIG_FILE);
      FileInputStream inputstream=new FileInputStream(configFile);
      config.load(inputstream);
      inputstream.close();
      
      File historyFile=new File(HISTORY_FILE);
      /* Create new history if it does not exist. */
      if(!historyFile.exists()){
        historyFile.createNewFile();
        setHistory(KEY_HISTORY_URL, "");
      }
      
      inputstream=new FileInputStream(historyFile);
      history.load(inputstream);
      inputstream.close();
      
      message=ResourceBundle.getBundle(MESSAGES_FILE);
    }
    catch(FileNotFoundException e) {
      throw new JCowsException("Input/Output error;The specified file was not found.\n", e);  
    }
    catch(IOException e) {
      throw new JCowsException("Input/Output Error;Could not read/write to/from file.\n", e);
    }
    catch(MissingResourceException e) {
      throw new JCowsException("Input/Output Error;The specified resource could not be found.\n", e);
    }
  }

  /**
   * Returns the message value corresponding to the specified message key, 
   * with the placeholders in the message substituted.
   * 
   * @param key the key that identifies the message entry.
   * @param substitutes text that replaces the {0...n} placeholders in the message.
   * @return the message string.
   */
  public static String getMessage(String key, String[] substitutes){
    String msg = getMessage(key);
    for (int i = 0; i < substitutes.length; ++i){
      String replace = "\\{" + i + "\\}";
      //if (!msg.contains(replace)) TODO: exception?
      msg = msg.replaceAll(replace, substitutes[i]);
    }
    return msg;
  }

  /**
   * Returns the message value corresponding to the specified message key.
   * 
   * @param key the key that identifies the message entry.
   * @return the message string.
   */
  public static String getMessage(String key) {
    try {
      return message.getString(key);
    }
    catch (MissingResourceException e) {
      LOGGER.error("The specified resource key \"" + key + "\" could not be found.");
      return '!' + key + '!';
    }
  }
  
  /**
   * Returns the configuration value corresponding to the specified key.
   * 
   * @param key the key that identifies the config entry.
   * @return the configuration string.
   */
  public static String getConfig(String key) {
    String value = config.getProperty(key);
    if (value == null){
      LOGGER.error("The specified resource key \"" + key + "\" could not be found.");
      return '!' + key + '!';
    }
    return value;
  }
  
  /**
   * Returns the history value corresponding to the specified key.
   * 
   * @param key the key that identifies the history entry.
   * @return the history string.
   */
  public static String getHistory(String key) {
    String value = history.getProperty(key);
    if (value == null){
      LOGGER.error("The specified resource key \"" + key + "\" could not be found.");
      return '!' + key + '!';
    }
    return value;
  }
  
  /**
   * Saves a configuration key and its value in the corresponding property file
   * and updates the value in memory too.
   * 
   * @param key the key that identifies the entry.
   * @param value the corresponding value.
   */
  public static void setConfig(String key,String value){
    try {
      String linesep = System.getProperty("line.separator");
      File configFile=new File(CONFIG_FILE);
      BufferedReader reader=new BufferedReader(new FileReader(configFile));
      String line=reader.readLine();
      StringBuffer newContent= new StringBuffer();
      boolean keyFound = false;
      while(line!=null) {
        if(line.startsWith(key)){
          newContent.append(key + "=" + value + linesep);
          keyFound = true;
        }
        else
          newContent.append(line + linesep);
        line=reader.readLine();
      }
      reader.close();
      
      // create new entry if key was not found
      if (!keyFound)
        newContent.append(key + "=" + value + linesep);
      
      BufferedWriter writer=new BufferedWriter(new FileWriter(configFile));
      writer.write(newContent.toString());
      writer.close();
      
      // update in memory too
      config.setProperty(key, value);
    }
    catch(FileNotFoundException e) {
      new JCowsException("Input/Output error;The specified file was not found.\n", e);
    }
    catch(IOException e) {
      new JCowsException("Input/Output Error;Could not read/write to/from file.\n", e);
    }
  }
  
  /**
   * Saves a history key and its value in the corresponding property file
   * and update the value in memory too.
   * 
   * @param key the key that identifies the entry.
   * @param value the corresponding value.
   */
  public static void setHistory(String key,String value){
    try {
      String linesep = System.getProperty("line.separator");
      File historyFile=new File(HISTORY_FILE);
      BufferedReader reader=new BufferedReader(new FileReader(historyFile));
      String line=reader.readLine();
      StringBuffer newContent= new StringBuffer();
      boolean keyFound = false;
      while(line!=null) {
        if(line.startsWith(key)){
          newContent.append(key + "=" + value + linesep);
          keyFound = true;
        }
        else
          newContent.append(line + linesep);
        line=reader.readLine();
      }
      reader.close();
      
      // create new entry if key was not found
      if (!keyFound)
        newContent.append(key + "=" + value + linesep);
      
      BufferedWriter writer=new BufferedWriter(new FileWriter(historyFile));
      writer.write(newContent.toString());
      writer.close();
      
      // update in memory too
      history.setProperty(key, value);
    }
    catch(FileNotFoundException e) {
      new JCowsException("Input/Output error;The specified file was not found.\n", e);
    }
    catch(IOException e) {
      new JCowsException("Input/Output Error;Could not read/write to/from file.\n", e);
    }
  }  
}
