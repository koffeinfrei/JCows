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
package org.jcows;

import org.apache.log4j.Logger;

/** 
 * <code>JCowsException</code> is a wrapper class for all other exceptions that are thrown
 * within the JCows application.<br/>
 * This design concept allows that all methods throws only one type
 * of exception.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 411 $, $LastChangedDate: 2006-12-15 12:17:47 +0000 (Fri, 15 Dec 2006) $
 */
public class JCowsException extends Exception {

  private Exception m_nestedException;
  
  /*
   * Needed to log all exceptions.
   */
  private static final Logger LOGGER=Logger.getLogger(JCowsException.class);
  
  /**
   * Constructs a new instance of this class.
   * 
   * @param message the exception message.
   */
  public JCowsException(String message){
    super(message);
    LOGGER.error(getMessage());
    //LOGGER.error(getStackTrace());
    printStackTrace();
  }

  /**
   * Constructs a new instance of this class.
   * 
   * @param message the exception message.
   * @param nestedException the nested exception.
   */
  public JCowsException(String message, Exception nestedException){
    super(message);
    m_nestedException = nestedException;
    LOGGER.error(getMessage());
    printStackTrace();
  }

  /* (non-Javadoc)
   * @see java.lang.Throwable#getCause()
   */
  @Override
  public Throwable getCause() {
    if (m_nestedException == null)
      return super.getCause();
    return m_nestedException.getCause();
  }

  /* (non-Javadoc)
   * @see java.lang.Throwable#getMessage()
   */
  @Override
  public String getMessage() {
    if (m_nestedException != null && m_nestedException.getMessage() != null)
      return super.getMessage() 
        + "\nDetails --\n" 
        + m_nestedException.getMessage();
    
    return super.getMessage();
  }
  
  /**
   * This method returns the nested exception.
   * @return the nested exception.
   */
  public Exception getNestedException(){
    return m_nestedException;
  }

}
