package org.jcows.test.view.vc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jcows.JCowsException;
import org.jcows.model.vc.DoubleValidator;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.model.vc.RegexValidator;
import org.jcows.system.Properties;
import org.jcows.view.vc.VCdouble;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestVCdouble {
  
  private static Shell m_shell;
  private static Display m_display;
  
  private VCdouble m_vcPrimitive;
  private VCdouble m_vcWrapped;
  private ParamListItem m_paramListItemPrimitive;
  private ParamListItem m_paramListItemWrapped;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Properties.init();
    m_display=Display.getCurrent();
    m_shell=new Shell(m_display);
    m_shell.setLayout(new FillLayout());
  }
  
  @AfterClass
  public static void setUpAfterClass() {
    m_shell.open();
    m_shell.layout();
  }
  
  @Before
  public void setUp() throws JCowsException {
    m_paramListItemPrimitive=new ParamListItem("double",true,"double value",(double)5,false);
    m_paramListItemWrapped=new ParamListItem("double",true,"double value",new Double(5),false);
  }
  
  @Test
  public void testVCdoubleParamListItemComposite() {   
    try {
      m_vcPrimitive=new VCdouble(m_paramListItemPrimitive,m_shell);
      m_vcWrapped=new VCdouble(m_paramListItemWrapped,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
  }

  @Test
  public void testVCdoubleParamListItemCompositeInt() {   
    try {
      m_vcPrimitive=new VCdouble(m_paramListItemPrimitive,m_shell,0);
      m_vcWrapped=new VCdouble(m_paramListItemWrapped,m_shell,0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    try {
      m_vcPrimitive=new VCdouble(m_paramListItemPrimitive,m_shell,5);
      fail("Should have raised an ArrayIndexOutOfBoundsException.");
    }
    catch(JCowsException e) {
      
    }

    try {
      m_vcWrapped=new VCdouble(m_paramListItemWrapped,m_shell,5);
      fail("Should have raised an ArrayIndexOutOfBoundsException.");
    }
    catch(JCowsException e) {
      
    }
  }

  @Test
  public void testValidate() {
    IValidator validator1=new DoubleValidator();
    IValidator validator2=new RegexValidator("[a-z]+");
    
    try {
      m_vcPrimitive=new VCdouble(m_paramListItemPrimitive,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    try {
      m_vcWrapped=new VCdouble(m_paramListItemWrapped,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    m_vcPrimitive.addValidator(validator1);
    assertTrue(m_vcPrimitive.validate());
    m_vcPrimitive.addValidator(validator2);
    assertFalse(m_vcPrimitive.validate());
    
    m_vcWrapped.addValidator(validator1);
    assertTrue(m_vcWrapped.validate());
    m_vcWrapped.addValidator(validator2);
    assertFalse(m_vcWrapped.validate());
  }

}
