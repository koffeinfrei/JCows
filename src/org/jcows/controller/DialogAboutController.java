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
package org.jcows.controller;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.jcows.view.core.DialogAbout;
/**
 * The <code>DialogAboutController</code> controls the about dialog.
 * The about dialog contains application information (version number, release date etc.)
 * about the application.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
 */
public class DialogAboutController extends JCowsController{
  
  private static final Logger LOGGER = Logger.getLogger(DialogAboutController.class);
  
  protected DialogAbout m_dialogAbout;
  
  public DialogAboutController() {
    m_dialogAbout = new DialogAbout(m_shell,SWT.NONE);
    attachViewListenersDialogAbout();
  }
  
  /**
   * Attaches the listeners for the about dialog.
   */  
  public void attachViewListenersDialogAbout() {
    m_dialogAbout.addDialogAboutCloseListener(getDialogAboutCloseListener());
  }
  
  /**
   * Listener that listens for any mouse events over the dialog.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */
  private class DialogAboutCloseListener implements MouseListener {

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDoubleClick(MouseEvent evt) {
      LOGGER.debug(evt);
      /*
       * Do nothing.
       */      
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDown(MouseEvent evt) {
      LOGGER.debug(evt);
      /*
       * Do nothing.
       */
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseUp(MouseEvent evt) {
      LOGGER.debug(evt);
      m_dialogAbout.setVisible(false);
    }
  }
  
  /**
   * Returns a mouse listener for the Close button of the about dialog.
   * 
   * @return a mouse listener.
   */
  private MouseListener getDialogAboutCloseListener() {
    return new DialogAboutCloseListener();
  }

}
