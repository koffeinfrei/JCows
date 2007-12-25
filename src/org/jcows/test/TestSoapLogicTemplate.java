
package org.jcows.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.jcows.system.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/*
 * Needed by the TestSoapLogicDynamic class.
 */
import org.jcows.model.core.ISoapLogic;

public class TestSoapLogicTemplate {
  
  private static final String FILENAME_SOURCE="src/org/jcows/test/TestSoapLogicTemplate.java";
  private static final String FILENAME_DEST="src/org/jcows/test/TestSoapLogicDynamic.java";
  
  private static final String CLASSNAME_SOURCE="TestSoapLogicTemplate";
  private static final String CLASSNAME_DEST="TestSoapLogicDynamic";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    
    Properties.init();
    
    String classname=this.getClass().getSimpleName();
    
    if(classname.equals(CLASSNAME_SOURCE))
      createNewTest();
  }
  
  public void createNewTest() throws Exception {
    
    XMethods xmethods=new XMethods();
    String[] urls=xmethods.getWsdlUrlsFromServer();
    
    File file=new File(FILENAME_SOURCE);
    BufferedReader in=new BufferedReader(new FileReader(file));
    String code="";
    for (String line;(line=in.readLine())!=null;)
      code+="\n"+line;
    
    in.close();
    
    int last=0;
    String[] lines=code.split("\n");
    for(int i=lines.length-1;i>=0;i--) {
      if(lines[i].contains("}")) {
        last=i;
        break;
      }
    }
    
    PrintWriter out=new PrintWriter(FILENAME_DEST);
    for(int i=0;i<last;i++) {
      if(lines[i].contains("class "+CLASSNAME_SOURCE))
        lines[i]=lines[i].replace("class "+CLASSNAME_SOURCE,"class "+CLASSNAME_DEST);
      out.println(lines[i]);
    }

    for(int i=0;i<urls.length;i++)
      out.println("@Test\n public void testWebService"+i+"() { try { ISoapLogic logic=new SoapLogic(\""+urls[i]+"\"); } catch(JCowsException e) { fail(\""+urls[i]+"\"); } }\n");
    
    out.println("}");
    out.close();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDummy() {

  }

}
