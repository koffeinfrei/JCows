package org.jcows.test.view.vc;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jcows.JCowsException;
import org.jcows.model.vc.CharValidator;
import org.jcows.model.vc.DoubleValidator;
import org.jcows.model.vc.IValidator;
import org.jcows.model.vc.IntValidator;
import org.jcows.model.vc.ParamListItem;
import org.jcows.model.vc.RegexValidator;
import org.jcows.model.vc.ShortValidator;
import org.jcows.system.Properties;
import org.jcows.view.vc.VC;
import org.jcows.view.vc.VCchar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

public class TestVCchar {
  
  private static Shell m_shell;
  private static Display m_display;
  
  private VCchar m_vcPrimitive;
  private VCchar m_vcWrapped;
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
    m_paramListItemPrimitive=new ParamListItem("char",true,"char value",(char)64,false);
    m_paramListItemWrapped=new ParamListItem("char",true,"char value",new Character((char)64),false);
  }
  
  @Test
  public void testVCcharParamListItemComposite() {   
    try {
      m_vcPrimitive=new VCchar(m_paramListItemPrimitive,m_shell);
      m_vcWrapped=new VCchar(m_paramListItemWrapped,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
  }

  @Test
  public void testVCcharParamListItemCompositeInt() {   
    try {
      m_vcPrimitive=new VCchar(m_paramListItemPrimitive,m_shell,0);
      m_vcWrapped=new VCchar(m_paramListItemWrapped,m_shell,0);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
 
    try {
      m_vcPrimitive=new VCchar(m_paramListItemPrimitive,m_shell,5);
      fail("Should have raised an ArrayIndexOutOfBoundsException.");
    }
    catch(JCowsException e) {
      
    }

    try {
      m_vcWrapped=new VCchar(m_paramListItemWrapped,m_shell,5);
      fail("Should have raised an ArrayIndexOutOfBoundsException.");
    }
    catch(JCowsException e) {
      
    }
  }

  @Test
  public void testValidate() {
    IValidator validator1=new CharValidator();
    IValidator validator2=new RegexValidator("[a-z]+");
    
    try {
      m_vcPrimitive=new VCchar(m_paramListItemPrimitive,m_shell);
    }
    catch(JCowsException e) {
      fail(e.toString());
    }
    
    try {
      m_vcWrapped=new VCchar(m_paramListItemWrapped,m_shell);
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
