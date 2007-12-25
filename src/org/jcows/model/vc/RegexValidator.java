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
package org.jcows.model.vc;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
/**
 * Class that matches a string to a regular expression. 
 * This class allows to define own, complex rules to validate input strings.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class RegexValidator implements IValidator  {
	
  private Pattern m_pattern;
  /**
   * Constructs a new instance of this class.
   * 
   * @param regex a regular expression.
   */
	public RegexValidator(String regex) throws PatternSyntaxException {
      m_pattern=Pattern.compile(regex);
	}
  
  public boolean validate(String value) {
    if(value==null)
      return false;
    return m_pattern.matcher(value).matches();
  }
  
}
