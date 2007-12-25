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
package org.jcows.format;

import java.util.Vector;

/**
 * <code>Lexer</code> class for the L1 parser system
 * that provides syntax highlighting for
 * the JCows editor.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision:234 $, $LastChangedDate:2006-11-09 09:58:57 +0000 (Thu, 09 Nov 2006) $
 */
public class Lexer {
  
  //private static final Logger LOGGER = Logger.getLogger(Lexer.class);
  
  private String m_src;  // source string for lexical analysis
  private int m_curr;     // current index in source
  private int m_len;     // length of source
  private StringBuffer m_res; // result strin
  private String m_indentation; // current indentation
  
  private boolean m_eof; // if file is at the end
  private boolean m_lastWasStartTag;
  
  private Vector<int[]> m_rangeElement;
  private Vector<int[]> m_rangeAttributeName;
  private Vector<int[]> m_rangeAttributeValue;
  private Vector<int[]> m_rangeTextNode;
  private int m_currentStart;

  /**
   * Constructs a new <code>Lexer</code> instance
   * with the specified source string.
   * 
   * @param src the string to be parsed.
   */
  public Lexer(String src) {
    // remove all newlines/whitespace 
    m_src = src.trim().replaceAll("[\n\r]", "");
    m_curr = -1;
    m_len = m_src.length();
    m_res = new StringBuffer();
    m_indentation = "";
    
    m_eof = false;
    m_lastWasStartTag = true;
    
    m_rangeElement = new Vector<int[]>();
    m_rangeAttributeName = new Vector<int[]>();
    m_rangeAttributeValue = new Vector<int[]>();
    m_rangeTextNode = new Vector<int[]>();
  }
  
  /**
   * Returns the next token in the 
   * source string.
   * 
   * @return the next character
   */
  public char nextToken(){
    char currChar;
    ++m_curr;

    if (m_curr < m_len) 
      currChar = m_src.charAt(m_curr);
    else{
      currChar = '0';
      m_eof = true;
    }
    /* skip whitespaces following a whitespace */
    if (currChar == ' '){
      while (m_src.charAt(m_curr + 1) == ' ') ++m_curr;
    }
    /* handle indent/unindent */
    else if (currChar == '<'){ 
      if (m_src.charAt(m_curr + 1) == '/'){ // endtag
        if (!m_lastWasStartTag) unindent();
        m_lastWasStartTag = false;
      }
      else if (m_curr > 0){ // starttag
        if (m_lastWasStartTag) indent();
        m_lastWasStartTag = true;
      }
      setNewline();
    }
    else if (currChar == '/'){
      if (m_src.charAt(m_curr + 1) == '>'){ // empty tag
        if (!m_lastWasStartTag) unindent();
        m_lastWasStartTag = false;
      }
    }
    
    // if we're at the end, append " " for correct range counting
    m_res.append(m_eof? " ": currChar); 
    
    return currChar;
  }
  
  /**
   * Returns the result of the parsing.
   * 
   * @return the result string
   */
  public String getResult(){
    // omit last (it is " ")
    return m_res.substring(0, m_res.length() - 1).toString();
  } 
  
  public void setNewline(){
    // don't newline if we are at the beginning
    // and if the current node is a text node (previous is neither > nor " ")
    if (m_curr > 1 
        && ( m_src.charAt(m_curr - 1) == '>' || m_src.charAt(m_curr - 1) == ' ')){ 
      m_res.append(System.getProperty("line.separator"));
      m_res.append(m_indentation);
    }
  }
  
  private void indent(){
    m_indentation += "  ";
  }
  
  private void unindent(){
    if (m_indentation.length() >= 2)
      m_indentation = m_indentation.substring(0, m_indentation.length() - 2);
  }
  
  
  /**
  * Returns the current position in the output string.
  *  
  * @return the current position
  */
  private int getPosition(){
   return m_res.length() - 1;
  }
  
  /**
   * Returns if we have reached the end of the source string.
   * 
   * @return true if we are at the end, false otherwise
   */
  public boolean isEof(){
    return m_eof;
  }
  
  /**
   * Indicates that a range should be started.
   * 
   */
  public void startRange(){
    m_currentStart = getPosition();
  }
  
  /**
   * Indicates that an element range should be ended.
   */
  public void endRangeElement(){
    m_rangeElement.add(new int[]{m_currentStart, getPosition() - m_currentStart});
  }
  
  /**
   * Indicates that an attribute name range should be ended.
   */
  public void endRangeAttributeName(){
    m_rangeAttributeName.add(new int[]{m_currentStart, getPosition() - m_currentStart});
  }
  
  /**
   * Indicates that an attribute value range should be ended.
   */
  public void endRangeAttributeValue(){
    m_rangeAttributeValue.add(new int[]{m_currentStart, getPosition() - m_currentStart});
  }
  
  /**
   * Indicates that a text node range should be ended.
   */
  public void endRangeTextNode(){
    m_rangeTextNode.add(new int[]{m_currentStart, getPosition() - m_currentStart});
  }

  
  /**
   * Returns all ranges for attribute names.
   * 
   * @return a {@link java.util.Vector Vector} of ranges
   */
  public Vector<int[]> getRangeAttributeName() {
    return m_rangeAttributeName;
  }

  /**
   * Returns all ranges for attribute values.
   * 
   * @return a {@link java.util.Vector Vector} of ranges
   */
  public Vector<int[]> getRangeAttributeValue() {
    return m_rangeAttributeValue;
  }

  /**
   * Returns all ranges for elements.
   * 
   * @return a {@link java.util.Vector Vector} of ranges
   */
  public Vector<int[]> getRangeElement() {
    return m_rangeElement;
  }

  /**
   * Returns all ranges for text nodes.
   * 
   * @return a {@link java.util.Vector Vector} of ranges
   */
  public Vector<int[]> getRangeTextNode() {
    return m_rangeTextNode;
  }
  
}
