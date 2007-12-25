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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.jcows.JCowsException;
import org.jcows.view.core.DialogError;
/** 
 * The <code>DialogErrorController</code> controls the dialog for errors.
 * This dialog is showed when application errors occurs.
 * 
 * @author Marco Schmid (marco.schmid@jcows.org)
 * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
 */
public class DialogErrorController extends JCowsController {

  private static final Logger LOGGER = 
    Logger.getLogger(DialogErrorController.class);
  
  protected DialogError m_dialogError;

  /**
   * Constructs a new instance of this class.
   * 
   * @throws JCowsException
   */
  public DialogErrorController() {
    m_dialogError = new DialogError(m_shell,SWT.NONE);
    attachViewListenersDialogError();
  }

  /**
   * Attaches the listeners for the error dialog.
   */  
  public void attachViewListenersDialogError() {
    m_dialogError.addDialogErrorCloseListener(getDialogErrorCloseListener());
  }
  
  /** 
   * Listener for the Cancel button of the error dialog.
   * Closes the error dialog.
   * 
   * @author Marco Schmid (marco.schmid@jcows.org)
   * @version $LastChangedRevision: 225 $, $LastChangedDate: 2006-11-09 08:19:55 +0000 (Thu, 09 Nov 2006) $
   */  
  private class DialogErrorCloseListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent evt) {
      LOGGER.debug(evt);
      m_dialogError.setVisible(false);
    }
  }

  /**
   * Returns a listener for the Cancel button of the error dialog.
   * 
   * @return a listener.
   */
  private SelectionAdapter getDialogErrorCloseListener() {
    return new DialogErrorCloseListener();
  }

}
