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
package org.jcows.model.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.jcows.JCowsException;
import org.jcows.controller.DialogSSLController;
import org.jcows.system.Properties;

/**
 * This is the trustmanager for the JCows application. Additionally to the 
 * Java trustmanager it provides the possibility to accept untrusted certificates too.
 * 
 * @author Alexis Reigel (alexis.reigel@jcows.org)
 * @version $LastChangedRevision: 165 $, $LastChangedDate: 2006-07-05 21:51:21 +0000 (Wed, 05 Jul 2006) $
 *  
 */
class JCowsX509TrustManager implements X509TrustManager {

  protected static final Logger LOGGER = 
    Logger.getLogger(JCowsX509TrustManager.class);
  
  private KeyStore m_keystore;
  private String m_keystorePath;
  private String m_passphrase;
  private DialogSSLController m_dialogSSLController;

  private static boolean m_isTemporary;
  /*
   * The default X509TrustManager returned by SunX509.  We'll delegate
   * decisions to it, and fall back to the logic in this class if the
   * default X509TrustManager doesn't trust it.
   */
  X509TrustManager sunJSSEX509TrustManager;

  /**
   * Creates a new instance of the this class with a controller instance
   * that is needed for displaying the SSL warning dialog.
   *
   * @param controller the controller for the SSL dialog.
   * @throws JCowsException
   */
  public JCowsX509TrustManager(DialogSSLController controller) throws JCowsException{
    m_dialogSSLController = controller;
    init();
  }

  /**
   * Initializes the keystore and the trustmanager. Creates a new keystore file
   * if there isn't any yet and handles whether the trusted key's trust scope
   * is temporarily or permanent.
   * 
   * @throws JCowsException
   */
  private void init() throws JCowsException{
    try {
      m_keystorePath = Properties.getConfig("network.keyStore");
      m_passphrase = "59_jcows@5932"; //TODO: to config.properties?
      
      // temporary keystore (if temporarily accepted cert)
      if (m_isTemporary)
        m_keystorePath = System.getProperty("java.io.tmpdir") 
          + File.separator + m_keystorePath;
        
      // create a "default" JSSE X509TrustManager.
      m_keystore = KeyStore.getInstance("JKS");
      
      // create keystore file if not existant
      if (!(new File(m_keystorePath)).exists()){
        m_keystore.load(null, m_passphrase.toCharArray());
        m_keystore.store(new FileOutputStream(new File(m_keystorePath)), 
            m_passphrase.toCharArray());
      }
      m_keystore.load(new FileInputStream(m_keystorePath), m_passphrase.toCharArray());      
      
      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509",
          "SunJSSE");
      tmf.init(m_keystore);

      TrustManager tms[] = tmf.getTrustManagers();
      LOGGER.info("Number of Trustmanagers: " + tms.length);
      /*
       * Iterate over the returned trustmanagers, look
       * for an instance of X509TrustManager.  If found,
       * use that as our "default" trust manager.
       */
      for (int i = 0; i < tms.length; i++) {
        if (tms[i] instanceof X509TrustManager) {
          sunJSSEX509TrustManager = (X509TrustManager) tms[i];
          return;
        }
      }
    }
    catch (KeyStoreException e) {
      throw new JCowsException(
          Properties.getMessage("error.KeyStoreException"), e);
    }
    catch (NoSuchAlgorithmException e) {
      throw new JCowsException(
          Properties.getMessage("error.NoSuchAlgorithmException"), e);
    }
    catch (CertificateException e) {
      throw new JCowsException(
          Properties.getMessage("error.CertificateException"), e);
    }
    catch (NoSuchProviderException e) {
      throw new JCowsException(
          Properties.getMessage("error.NoSuchProviderException"), e);
    }
    catch (IOException e) {
      throw new JCowsException(Properties.getMessage("error.IOException"), e);
    }

    /* Fail the constructor if none of the above was successful */
    throw new JCowsException(
        Properties.getMessage("error.x509InitializationError"));
  }
  
  /*
   * Delegate to the default trust manager.
   */
  /* (non-Javadoc)
   * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
   */
  public void checkClientTrusted(X509Certificate[] chain, String authType)
      throws CertificateException {
    try {
      sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
    }
    catch (CertificateException e) {
      // do any special handling here, or rethrow exception.
      throw e;
    }
  }

  /*
   * Delegate to the default trust manager.
   */
  /* (non-Javadoc)
   * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
   */
  public void checkServerTrusted(X509Certificate[] chain, String authType)
      throws CertificateException {
    try {
      sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
    }
    catch (CertificateException e) {
      /* Ask the user whether to accept or not */
      X509Certificate certificateAuthorityCert = getCACert(chain);
      
      /* construct the text for the certificate to be displayed */
      StringBuffer certText = new StringBuffer("Certificate Subject\n");
      String[] certSubject = certificateAuthorityCert.getSubjectX500Principal().toString().split(", ");
      for (String str : certSubject){
        certText.append(str);
        certText.append("\n");
      }
      certText.append("\nCertificate Issuer\n");
      String[] certIssuer = certificateAuthorityCert.getIssuerX500Principal().toString().split(", ");
      for (String str : certIssuer){
        certText.append(str);
        certText.append("\n");
      }
      certText.append("\n\nSignature Algorithm: ");
      certText.append(certificateAuthorityCert.getSigAlgName());
      certText.append("\n\nKey: ");
      String publicKey = certificateAuthorityCert.getPublicKey().toString();
      certText.append(publicKey.substring(0, publicKey.indexOf("modulus")).trim());
      certText.append("\n\nIssued On: ");
      certText.append(certificateAuthorityCert.getNotBefore());
      certText.append("\n\nExpires On: ");
      certText.append(certificateAuthorityCert.getNotAfter());
      
      m_dialogSSLController.setText(certText.toString());
      int evalSSL = m_dialogSSLController.evaluateSSLDialog();
      // when the mode (temp/perm) change, we need to reinitialize the keystore
      boolean needToReinitialize;
      // temporarily
      if (evalSSL == DialogSSLController.MODE_ACCEPT_TEMPORARILY){
        LOGGER.debug("Temporary Accepted Certificate");
        // if changed from perm to temp then reinitialize 
        needToReinitialize = !m_isTemporary;
        m_isTemporary = true;
      }
      // permanently
      else if (evalSSL == DialogSSLController.MODE_ACCEPT_PERMANENTLY){
        LOGGER.debug("Permanently Accepted Certificate");
        // if changed from temp to perm then reinitialize
        needToReinitialize = m_isTemporary;
        m_isTemporary = false;
      }
      // if declined, rethrow exception (abort)
      else throw e;
      try{
        if (needToReinitialize) 
          try {
            init();
          }
          catch (JCowsException e1) {
            throw new CertificateException(e1);
          }
        saveStore(certificateAuthorityCert);
      }
      catch (KeyStoreException e1){
        throw new CertificateException(
            Properties.getMessage("error.saveKeystoreFailed") + e1);
      }
    }
  }

  /*
   * Merely pass this through.
   */
  /* (non-Javadoc)
   * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
   */
  public X509Certificate[] getAcceptedIssuers() {
    return sunJSSEX509TrustManager.getAcceptedIssuers();
  }
  
  
  /**
   * Gets the root (ca) certificate of the current session.
   * 
   * @param chain an array of certificates.
   * @return the ca certificate if found, null otherwise
   */
  private X509Certificate getCACert(X509Certificate chain[]) {
    X509Certificate ca = chain[chain.length - 1];
    // check that root certificate is self-signed.
    if (ca.getSubjectDN().equals(ca.getIssuerDN())) 
      return ca;
    else 
      return null;
  }
  
  /**
   * Save the current certificate to the keystore file.
   * 
   * @param cert the certificate.
   * @throws KeyStoreException
   */
  private void saveStore(X509Certificate cert)
      throws KeyStoreException{   
    try{
      String alias = cert.getSubjectX500Principal() + " (" + cert.getSerialNumber().toString() + ")";
      m_keystore.setCertificateEntry(alias, cert);
      FileOutputStream fos = new FileOutputStream(m_keystorePath);
      m_keystore.store(fos, m_passphrase.toCharArray());
      fos.close();
    }
    catch (IOException e) {
      KeyStoreException kse = new KeyStoreException(
          "unable to access keystore file");
      kse.initCause(e);
      throw kse;
    }
    catch (GeneralSecurityException e) {
      if (e instanceof KeyStoreException) 
        throw (KeyStoreException) e;
      else throw new KeyStoreException("unable to save keystore "
          + m_keystorePath + " error was: " + e);
    } 
  }
  
  /**
   * Returns the path of the keystore. This is needed because we can have
   * two different keystores, i.e. the permanent and the temporary one.
   * 
   * @return the path of the currently used keystore.
   */
  public String getkeystorePath(){
    return m_keystorePath;
  }
}
