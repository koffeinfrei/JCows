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
 * <code>Parser</code> class for the L1 parser system
 * that provides syntax highlighting for
 * the JCows editor.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision:234 $, $LastChangedDate:2006-11-09 09:58:57 +0000 (Thu, 09 Nov 2006) $
 */
public class Parser {

  //private static final Logger LOGGER = Logger.getLogger(Parser.class);
  
  private Lexer m_lex; // lexer used by this parser

  private char m_token; // current token
  
  
  /**
   * Constructs a new <code>Parser</code> instance with 
   * the specified source string.
   * 
   * @param src the source string to be parsed.
   */
  public Parser(String src) {
    m_lex = new Lexer(src);
  }

  /**
   * Starts the parsing process.
   * 
   * @return the result string of the parsing.
   */
  public String parse() {
    m_token = m_lex.nextToken();
    node();
    return m_lex.getResult();
  }
  
  /**
   * Represents a Node.
   */
  private void node(){
    if (!m_lex.isEof()){
      if (m_token == '<'){
        m_lex.startRange();
        m_token = m_lex.nextToken();
        if (m_token == '/')
          endTag();
        else 
          startTag();
      }
      else{
        m_lex.startRange();
        m_token = m_lex.nextToken();
        textNode();
      }
    }
  }
  
  /**
   * Represents a text node.
   */
  private void textNode(){
    while (m_token != '<' && !m_lex.isEof()){ 
      m_token = m_lex.nextToken(); 
    }
    m_lex.endRangeTextNode();
    node();
  }
  
  /**
   * Represents a start tag ("<...>").
   */
  private void startTag(){
    startTagBegin();
  }

  /**
   * Represents the beginning of a start tag.
   */
  private void startTagBegin(){
    while (m_token != ' ' && m_token != '>' && !m_lex.isEof()){ 
      m_token = m_lex.nextToken(); 
    }
    if (m_token == ' ' && !m_lex.isEof()) m_token = m_lex.nextToken();
    m_lex.endRangeElement();
    attributeName();
    startTagEnd();
  }
  
  /**
   * Represents the end of a start tag.
   */
  private void startTagEnd(){
    if (m_token == '>' && !m_lex.isEof()){
      m_lex.startRange();
      m_token = m_lex.nextToken();
      m_lex.endRangeElement();
      node();
    }
    else if (m_token == '/' && !m_lex.isEof()){
      m_lex.startRange();
      m_token = m_lex.nextToken();
      //TODO: if next not ">" -> error
      m_token = m_lex.nextToken();
      m_lex.endRangeElement();
      node();
    }
  }
  
  /**
   * Represents an attribute name.
   */
  private void attributeName(){
    if (m_token != '>' && m_token != '/' && !m_lex.isEof()){
      m_lex.startRange();
      while (m_token != '\'' && m_token != '"' && !m_lex.isEof()){ 
        m_token = m_lex.nextToken();
      }
      m_lex.endRangeAttributeName();
      attributeValue();
      attributeName();
    }
  }
  
  /**
   * Represents an attribute value.
   */
  private void attributeValue(){
    m_lex.startRange();
    m_token = m_lex.nextToken();
    while (m_token != '\'' && m_token != '"' && !m_lex.isEof()){
      m_token = m_lex.nextToken();
    }
    m_token = m_lex.nextToken();
    if (m_token == ' ' && !m_lex.isEof()) m_token = m_lex.nextToken();
    m_lex.endRangeAttributeValue();
  }
  
  /**
   * Represents an end tag ("</...>").
   */
  private void endTag(){
    while (m_token != '>' && !m_lex.isEof()){ 
      m_token = m_lex.nextToken();
    }
    m_token = m_lex.nextToken();
    m_lex.endRangeElement();
    node();
  }

  /**
   * Returns all ranges for elements.
   * 
   * @return a {@link java.util.Vector Vector} of ranges.
   */
  public Vector<int[]> getRangeElement(){
    return m_lex.getRangeElement();
  }
  
  /**
   * Returns all ranges for attribute names.
   * 
   * @return a {@link java.util.Vector Vector} of ranges.
   */
  public Vector<int[]> getRangeAttributeName(){
    return m_lex.getRangeAttributeName();
  }
  
  /**
   * Returns all ranges for attribute values.
   * 
   * @return a {@link java.util.Vector Vector} of ranges.
   */
  public Vector<int[]> getRangeAttributeValue(){
    return m_lex.getRangeAttributeValue();
  }
  
  /**
   * Returns all ranges for text nodes.
   * 
   * @return a {@link java.util.Vector Vector} of ranges.
   */
  public Vector<int[]> getRangeTextNode(){
    return m_lex.getRangeTextNode();
  }
  
  
}
