/*******************************************************************************
 * Copyright 2020 Patrick Ahlbrecht
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.theapache64.gpa.core.net

import org.bouncycastle.tls.*
import java.io.ByteArrayInputStream
import java.io.IOException
import java.security.KeyStore
import java.security.cert.*
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class DefaultTlsAuthentication(selectedCipherSuite: Int) : ServerOnlyTlsAuthentication() {
    private var trustManagers: Array<TrustManager>? = null
    private var certificateFactory: CertificateFactory? = null
    private var authType: String? = null

    @Throws(IOException::class)
    override fun notifyServerCertificate(serverCertificate: org.bouncycastle.tls.Certificate?) {
        if (serverCertificate == null || serverCertificate.isEmpty) {
            throw TlsFatalAlert(AlertDescription.handshake_failure)
        }
        if (trustManagers == null || certificateFactory == null) {
            throw TlsFatalAlert(AlertDescription.unknown_ca)
        }
        if (authType == null) {
            throw TlsFatalAlert(AlertDescription.internal_error)
        }
        val certificates = serverCertificate.certificateList
        val chain = arrayOfNulls<X509Certificate>(certificates.size)
        var bis: ByteArrayInputStream? = null
        for (i in chain.indices) {
            bis = ByteArrayInputStream(certificates[i].encoded)
            try {
                chain[i] = certificateFactory!!.generateCertificate(bis) as X509Certificate
                chain[i]!!.checkValidity()
            } catch (e: CertificateExpiredException) {
                throw TlsFatalAlert(AlertDescription.certificate_expired)
            } catch (e: CertificateNotYetValidException) {
                throw TlsFatalAlert(AlertDescription.certificate_expired)
            } catch (e: CertificateException) {
                throw TlsFatalAlert(AlertDescription.decode_error, e)
            }
        }
        for (trustManager in trustManagers!!) {
            if (trustManager is X509TrustManager) {
                try {
                    trustManager.checkServerTrusted(chain, authType)
                } catch (e: Exception) {
                    throw IOException(e.cause)
                }
            }
        }
    }

    private fun getAuthTypeServer(keyExchangeAlgorithm: Int): String? {
        return when (keyExchangeAlgorithm) {
            KeyExchangeAlgorithm.DH_anon -> "DH_anon"
            KeyExchangeAlgorithm.DH_DSS -> "DH_DSS"
            KeyExchangeAlgorithm.DH_RSA -> "DH_RSA"
            KeyExchangeAlgorithm.DHE_DSS -> "DHE_DSS"
            KeyExchangeAlgorithm.DHE_PSK -> "DHE_PSK"
            KeyExchangeAlgorithm.DHE_RSA -> "DHE_RSA"
            KeyExchangeAlgorithm.ECDH_anon -> "ECDH_anon"
            KeyExchangeAlgorithm.ECDH_ECDSA -> "ECDH_ECDSA"
            KeyExchangeAlgorithm.ECDH_RSA -> "ECDH_RSA"
            KeyExchangeAlgorithm.ECDHE_ECDSA -> "ECDHE_ECDSA"
            KeyExchangeAlgorithm.ECDHE_PSK -> "ECDHE_PSK"
            KeyExchangeAlgorithm.ECDHE_RSA -> "ECDHE_RSA"
            KeyExchangeAlgorithm.RSA -> "RSA"
            KeyExchangeAlgorithm.RSA_PSK -> "RSA_PSK"
            KeyExchangeAlgorithm.SRP -> "SRP"
            KeyExchangeAlgorithm.SRP_DSS -> "SRP_DSS"
            KeyExchangeAlgorithm.SRP_RSA -> "SRP_RSA"
            else -> null
        }
    }

    init {
        try {
            val trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            trustManagers = trustManagerFactory.trustManagers
            certificateFactory = CertificateFactory.getInstance("X.509")
            val keyExchangeAlgorithm = TlsUtils
                .getKeyExchangeAlgorithm(selectedCipherSuite)
            authType = getAuthTypeServer(keyExchangeAlgorithm)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}