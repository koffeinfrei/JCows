package org.jcows.controller;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jcows.view.core.DialogSSL;

public class DialogSSLController extends JCowsController {
  
  private static final Logger LOGGER = 
    Logger.getLogger(DialogSSLController.class);
  
  public static int MODE_NONE = 0;
  public static int MODE_ACCEPT_TEMPORARILY = 1;
  public static int MODE_ACCEPT_PERMANENTLY = 2;
  public static int MODE_REJECT = 3;
  
  public DialogSSL m_dialogSSL;
  
  private int m_mode;
  
  public DialogSSLController(){
    
    m_dialogSSL = new DialogSSL(m_shell,SWT.NONE);
    
    attachViewListenersDialogSSL();
  }
  
  /**
   * Attaches the listeners for the SSL dialog.
   */
  public void attachViewListenersDialogSSL() {
    m_dialogSSL.addDialogSSLButtonAcceptListener(getDialogSSLButtonAcceptListener());
    m_dialogSSL.addDialogSSLButtonAcceptPermanentlyListener(getDialogSSLButtonAcceptPermanentlyListener());
    m_dialogSSL.addDialogSSLButtonCancelListener(getDialogSSLButtonCancelListener());
  }
  
  /** 
   * Listener for the Accept button of the SSL dialog.
   * This accepts the certificate onyl for this session.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class DialogSSLButtonAcceptListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      m_mode=DialogSSLController.MODE_ACCEPT_TEMPORARILY;
    }
  }

  /** 
   * Listener for the Accept Permanently button of the SSL dialog.
   * This accepts the certificate even if it is not certified by a known authority.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */ 
  private class DialogSSLButtonAcceptPermanentlyListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      m_mode=DialogSSLController.MODE_ACCEPT_PERMANENTLY;
    }
  }
  
  /** 
   * Listener for the Cancel button of the SSL dialog.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class DialogSSLButtonCancelListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      m_mode=DialogSSLController.MODE_REJECT;
    }
  }
  
  /**
   * Returns a listener for the SSL Accept button of the SSL dialog.
   * 
   * @return a listener.
   */   
  private SelectionAdapter getDialogSSLButtonAcceptListener() {
    return new DialogSSLButtonAcceptListener();
  }
  
  /**
   * Returns a listener for the SSL Accept Permanently button of the SSL dialog.
   * 
   * @return a listener.
   */   
  private SelectionAdapter getDialogSSLButtonAcceptPermanentlyListener() {
    return new DialogSSLButtonAcceptPermanentlyListener();
  }
  
  /**
   * Returns a listener for the SSL Cancel button of the SSL dialog.
   * 
   * @return a listener.
   */  
  private SelectionAdapter getDialogSSLButtonCancelListener() {
    return new DialogSSLButtonCancelListener();
  }

  public void setText(String text){
    m_dialogSSL.setCertificateDetails(text);
  }
  
  /**
   * Shows the SSL dialog and returns the code
   * that indicates whether the certificate was
   * accepted or not.
   * 
   * @return the return code.
   */
  public int evaluateSSLDialog(){
    m_mode = MODE_NONE;
    Shell shell=m_dialogSSL.getShell();
    Display display=shell.getDisplay();
    
    m_dialogSSL.setVisible(true);
    while(m_mode == MODE_NONE){
      if(!display.readAndDispatch())
        display.sleep();
    }
    m_dialogSSL.setVisible(false);
    return m_mode;
  }
}
