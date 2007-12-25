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

import javax.xml.soap.SOAPEnvelope;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;

/**
 * This class provides the syntax highlighting for the 
 * JCows editor.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision:234 $, $LastChangedDate:2006-11-09 09:58:57 +0000 (Thu, 09 Nov 2006) $
 */
public class SyntaxHighlighter {
  
  //private static final Logger LOGGER = Logger.getLogger(SyntaxHighlighter.class);
  
  private Parser m_parser;
  private String m_output;
  
  /**
   * Constructs a new instance with the specified 
   * {@link javax.xml.soap.SOAPEnvelope SOAPEnvelope}.
   * 
   * @param env the {@link javax.xml.soap.SOAPEnvelope SOAPEnvelope}
   *            to be parsed by the <code>SyntaxHighlighter</code>.
   */
  public SyntaxHighlighter(SOAPEnvelope env){    
    env.normalize(); // TODO fischlis normalize
    m_parser = new Parser(env.toString());
    m_output = m_parser.parse();
  }
  
  /**
   * Constructs a new instance with the specified 
   * {@link java.lang.String String} that represents a SOAP message.
   * 
   * @param message the {@link java.lang.String String}
   *            to be parsed by the <code>SyntaxHighlighter</code>.
   */
  public SyntaxHighlighter(String message){
    m_parser = new Parser(message);
    m_output = m_parser.parse();
  }
  
  /**
   * Returns all {@link org.eclipse.swt.custom.StyleRange StyleRange} 
   * objects that specify the ranges
   * that should be highlighted in the editor. 

   * @return a {@link java.util.Vector Vector} of 
   *           {@link org.eclipse.swt.custom.StyleRange StyleRange}
   */
  public Vector<StyleRange> getStyleRanges() {
    Vector<StyleRange> styles = new Vector<StyleRange>();
    
    /* element */
    Color color = JCowsHelper.getColor(Properties.getConfig("syntax.color.element"));
    //Color color = m_display.getSystemColor(SWT.COLOR_BLUE);
    for (int[] range : m_parser.getRangeElement()){
      styles.add(getStyleRange(range[0], range[1], color));
    }
    
    /* attribute name */
    color = JCowsHelper.getColor(Properties.getConfig("syntax.color.attributeName"));
    for (int[] range : m_parser.getRangeAttributeName()){
      styles.add(getStyleRange(range[0], range[1], color));
    }

    /* attribute value */
    color = JCowsHelper.getColor(Properties.getConfig("syntax.color.attributeValue"));
    for (int[] range : m_parser.getRangeAttributeValue()){
      styles.add(getStyleRange(range[0], range[1], color));
    }

    /* text nodes */
    color = JCowsHelper.getColor(Properties.getConfig("syntax.color.textNode"));
    for (int[] range : m_parser.getRangeTextNode()){
      styles.add(getStyleRange(range[0], range[1], color));
    }
    
    return styles;
  }
  
  /**
   * Returns the output text.
   * 
   * @return the output string.
   */
  public String getText(){
    return m_output;
  }
  
  /**
   * Returns a new {@link org.eclipse.swt.custom.StyleRange StyleRange} 
   * object for the specified parameters.
   * 
   * @param start the start index of the range.
   * @param length the length of the range.
   * @param color the color of the text of this range.
   * @return a new {@link org.eclipse.swt.custom.StyleRange StyleRange} instance
   *         corresponding to the arguments provided
   */
  private StyleRange getStyleRange(int start, int length, Color color){
    StyleRange styleRange = new StyleRange();
    styleRange.start  = start;
    styleRange.length = length;
    styleRange.fontStyle = SWT.BOLD;
    styleRange.foreground = color;
    return styleRange;
  }

}
