package org.jcows.test.system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.jcows.JCowsException;
import org.jcows.system.JCowsHelper;
import org.jcows.system.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJCowsHelper {
  
  private static final String TEST_JAR_FILENAME="dist/win32/jcows_0.2_win32.jar";
  
  /**
   * Sets up the fixture, for example, open a network connection.
   * This method is called before a test is executed.
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    Properties.init();
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
 
  @Test
  public void testGetFileStringStringBoolean() throws Exception {
    
    File testJar=new File(TEST_JAR_FILENAME);
    if(!testJar.exists())
      fail(new FileNotFoundException(testJar.getAbsolutePath()).toString());
    
    try {
      JCowsHelper.getFile(null,null,true);
    }
    catch(JCowsException e) {
      assertEquals(e.getNestedException().getClass(),FileNotFoundException.class);
    }

    try {
      JCowsHelper.getFile(null,"org/jcows/JCows.class",true);
    }
    catch(JCowsException e) {
      assertEquals(e.getNestedException().getClass(),FileNotFoundException.class);
    }

    if(!testJar.exists())
      fail(new FileNotFoundException(testJar.getAbsolutePath()).toString());
    try {
      JCowsHelper.getFile(TEST_JAR_FILENAME,null,true);
    }
    catch(JCowsException e) {
      assertEquals(e.getNestedException().getClass(),FileNotFoundException.class);
    }

    try {
      JCowsHelper.getFile("wrongPath",null,true);
    }
    catch(JCowsException e) {
      assertEquals(e.getNestedException().getClass(),FileNotFoundException.class);
    }

    try {
      JCowsHelper.getFile(TEST_JAR_FILENAME,"org/jcows/JCows.clas",true);
    }
    catch(JCowsException e) {
      assertEquals(e.getNestedException().getClass(),ZipException.class);
    }
    
    File file=null;
    try {
      file=JCowsHelper.getFile(TEST_JAR_FILENAME,"org/jcows/JCows.class",false);
    }
    catch(JCowsException e) {

    }
    assertTrue(file.length()>0);
    file.delete();
  }
 
  @Test
  public void testGetFileStringBoolean() throws Exception {
    File testJar=new File(TEST_JAR_FILENAME);
    if(!testJar.exists())
      fail(new FileNotFoundException(testJar.getAbsolutePath()).toString());
    
    try {
      File first=JCowsHelper.getFile(TEST_JAR_FILENAME,"org/jcows/JCows.class",false);
      long timestamp1=first.lastModified();
      File second=JCowsHelper.getFile(TEST_JAR_FILENAME,"org/jcows/JCows.class",false);
      long timestamp2=second.lastModified();
      assertEquals(timestamp1,timestamp2);
      /*
       * Override existing file.
       */
      second=JCowsHelper.getFile(TEST_JAR_FILENAME,"org/jcows/JCows.class",true);
      timestamp2=second.lastModified();
      assertNotSame(timestamp1,timestamp2);
      
      first.delete();
      second.delete();
    }
    catch(JCowsException e) {
      throw e.getNestedException();
    }
  }

  @Test
  public void testPreprocessSWT() {
    try {
      JCowsHelper.preprocessSWT();
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
  }

  @Test
  public void testPreloadClassList() {
    
    File testJar=new File(TEST_JAR_FILENAME);
    if(!testJar.exists())
      fail(new FileNotFoundException(testJar.getAbsolutePath()).toString());
    
    try {
      List<String> list=JCowsHelper.preloadClassList(new URL(""));
      assertNull(list);
      list=JCowsHelper.preloadClassList(testJar.toURL());
      assertNotNull(list);
      assertTrue(list.size()==0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    catch(MalformedURLException e) {
      /*
       * Do nothing.
       */
    }
  }

  @Test
  public void testGetColor() {
    Display display=new Display();

    Color colorWhite1=JCowsHelper.getColor("ffffff");
    Color colorWhite2=new Color(display,new RGB(255,255,255));
      
    Color colorBlack1=JCowsHelper.getColor("000000");
    Color colorBlack2=new Color(display,new RGB(0,0,0));  

    assertNotNull(colorWhite1);
    assertNotNull(colorWhite2);
    assertNotNull(colorBlack1);
    assertNotNull(colorBlack2);
    assertEquals(colorWhite1,colorWhite2);
    assertEquals(colorBlack1,colorBlack2);

    Color invalidColor=JCowsHelper.getColor("abc");
    assertEquals(invalidColor,colorBlack1);
  }

}
