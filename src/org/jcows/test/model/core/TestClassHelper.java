package org.jcows.test.model.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.jcows.JCowsException;
import org.jcows.model.core.ClassHelper;
import org.jcows.model.vc.ParamListItem;
import org.jcows.system.Properties;

import junit.framework.TestCase;

public class TestClassHelper extends TestCase {

  private ClassHelper m_classHelper;
  private File m_jarFile;
  private URLClassLoader m_jarLoader;
  
  List<ParamListItem> m_list;
  ParamListItem m_item;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    // init the JCows properties
    Properties.init();
  
    m_jarFile = new File(Properties.getConfig("parser.outputDirName") 
        + "TestService.jar");
    
    m_jarLoader = new URLClassLoader(new URL[] { m_jarFile.toURL() }); 
    
    m_classHelper = new ClassHelper(m_jarLoader);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.hasInterface(Class, String)'
   */
  public void testHasInterface() {
    try {
      boolean res = m_classHelper.hasInterface(ArrayList.class, "java.util.List");
      assertTrue(res);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.hasSuperclass(Class, String)'
   */
  public void testHasSuperclass() {
    try {
      boolean res = m_classHelper.hasSuperclass(String.class, "java.lang.Object");
      assertTrue(res);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getParamNames(Class, String, String[])'
   */
  public void testGetParamNames() {
    /* precondition: needs the compiled web service (jar) 
       "TestService" in the JCows output dir */
    
    String methodName = "getBooleanArray";
    String[] paramTypes = {"boolean[]"};
    try {
      String[] paramNames = m_classHelper.getParamNames(
          m_jarLoader.loadClass("com.marcoschmid.www.axis.TestService_jws.TestServiceSoapBindingStub"), 
          methodName, paramTypes);
      assertNotNull(paramNames);
      assertTrue(paramNames.length > 0);
      assertEquals(paramNames[0], "values");
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
    catch (ClassNotFoundException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getParamListFromFields(Class[], String[], Object[])'
   */
  public void testGetParamListFromFields() {
    Class[] arguments = {Boolean[].class};
    String[] labels = {"values"};
    Object[] values = {new Boolean[]{true}};
    try {
      m_list = 
        m_classHelper.getParamListFromFields(arguments, labels, values);
      assertNotNull(m_list);
      assertTrue(m_list.size() > 0);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getParamListItemFromFields(Class, String, Object)'
   */
  public void testGetParamListItemFromFields() {
    Class argument = Boolean[].class;
    String label = "values";
    Object value = new Boolean[]{true};
    try {
      m_item = 
        m_classHelper.getParamListItemFromFields(argument, label, value);
      assertNotNull(m_item);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getParamListFromConstructor(Class[], String[])'
   */
  public void testGetParamListFromConstructor() {
    Class[] arguments = {Boolean[].class};
    String[] labels = {"values"};
    Object[] values = {new Boolean[]{true}};
    try {
      List<ParamListItem> list = 
        m_classHelper.getParamListFromFields(arguments, labels, values);
      assertNotNull(list);
      assertTrue(list.size() > 0);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getParamListItemFromConstructor(Class, String)'
   */
  public void testGetParamListItemFromConstructor() {
    Class argument = Boolean[].class;
    String label = "values";
    Object value = new Boolean[]{true};
    try {
      ParamListItem item = 
        m_classHelper.getParamListItemFromFields(argument, label, value);
      assertNotNull(item);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.constructArguments(List<ParamListItem>)'
   */
  public void testConstructArgumentsListOfParamListItem() {
    // precondition
    testGetParamListFromFields();
    
    try {
      Object[] args = m_classHelper.constructArguments(m_list);
      assertNotNull(args);
      assertTrue(args.length > 0);
      // args should be a Object[] containing a Boolean[] with value true at index 0
      assertEquals(((Object[])args[0])[0], true);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.constructArguments(Class[])'
   */
  public void testConstructArgumentsClassArray() {
    try {
      Object[] args = 
        m_classHelper.constructArguments(new Class[]{String.class, int.class});
      assertEquals(args[0], "");
      assertEquals(args[1], 1);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getNewInstance(Class, Object[])'
   */
  public void testGetNewInstanceClassObjectArray() {
    try {
      Object obj = m_classHelper.getNewInstance(Boolean.class, new Object[]{true});
      assertTrue(obj instanceof Boolean);
      assertTrue((Boolean)obj);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getNewInstance(Class)'
   */
  public void testGetNewInstanceClass() {
    try {
      Object obj = m_classHelper.getNewInstance(Boolean.class);
      assertTrue(obj instanceof Boolean);
      // defaultvalue for boolean is false
      assertFalse((Boolean)obj);
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getConstructorIndex(Class, Constructor[])'
   */
  public void testGetConstructorIndexClassConstructorArray() {
    try {
      int index = m_classHelper.getConstructorIndex(
          String.class, String.class.getDeclaredConstructors());
      assertEquals(index, 2);
    }
    catch (SecurityException e) {
      fail(e.getMessage());
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

  /*
   * Test method for 'org.jcows.model.core.ClassHelper.getConstructorIndex(Class, Constructor[], int)'
   */
  public void testGetConstructorIndexClassConstructorArrayInt() {
    try {
      int index1 = m_classHelper.getConstructorIndex(
          String.class, String.class.getDeclaredConstructors());
      
      int index2 = m_classHelper.getConstructorIndex(
          String.class, String.class.getDeclaredConstructors(), index1);
      
      assertEquals(index1, index2);
    }
    catch (SecurityException e) {
      fail(e.getMessage());
    }
    catch (JCowsException e) {
      fail(e.getMessage());
    }
  }

}
