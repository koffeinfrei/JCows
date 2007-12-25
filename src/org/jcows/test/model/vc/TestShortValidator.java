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
package org.jcows.test.model.vc;

import junit.framework.TestCase;

import org.jcows.model.vc.ShortValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 * This test case tests the Short validator.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 222 $, $LastChangedDate: 2006-11-07 07:35:44 +0000 (Tue, 07 Nov 2006) $
 */
public class TestShortValidator extends TestCase {
  
  private ShortValidator m_validator;

  /**
   * Sets up the fixture, for example, open a network connection.
   * This method is called before a test is executed.
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    m_validator=new ShortValidator();
  }

  /**
   * Tears down the fixture, for example, close a network connection.
   * This method is called after a test is executed.
   * 
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
  }
  /**
   * Tests the validator. There is known in advantage which
   * values the validator has to recognise as valid and invalid.
   *
   */
  @Test
  public void testValidate() {
    
    String true1="5";
    
    String false1="5.0";
    String false2="abc";
    String false3=null;
    
    assertTrue(m_validator.validate(true1));
    
    assertFalse(m_validator.validate(false1));
    assertFalse(m_validator.validate(false2));
    assertFalse(m_validator.validate(false3));
    
  }

}
