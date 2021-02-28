package com.theapache64.gpa.core.net

import org.bouncycastle.tls.TlsClient
import org.bouncycastle.tls.TlsClientProtocol
import java.net.Socket
import javax.net.ssl.SSLSocket
import kotlin.Throws
import java.io.IOException
import org.bouncycastle.tls.crypto.TlsCrypto
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto
import java.io.InputStream
import java.io.OutputStream
import java.lang.UnsupportedOperationException
import java.security.SecureRandom
import javax.net.ssl.SSLSession
import javax.net.ssl.HandshakeCompletedListener

/**
 * An encrypted layer on top of a plain socket.
 *
 * @original_author patrick
 * @kotlin_author theapache64
 */
internal class DroidSocket(private val base: Socket) : SSLSocket() {

    private var client: TlsClient? = null
    private var protocol: TlsClientProtocol? = null
    @Throws(IOException::class)
    override fun startHandshake() {
        if (protocol == null) {
            protocol = TlsClientProtocol(
                base.getInputStream(),
                base.getOutputStream()
            )
            val crypto: TlsCrypto = BcTlsCrypto(SecureRandom())
            client = JellyBeanTlsClient(crypto)
            protocol!!.connect(client)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        base.close()
    }

    override fun isClosed(): Boolean {
        return base.isClosed
    }

    @Throws(IOException::class)
    override fun getInputStream(): InputStream {
        if (protocol == null) {
            startHandshake()
        }
        return protocol!!.inputStream
    }

    @Throws(IOException::class)
    override fun getOutputStream(): OutputStream {
        if (protocol == null) {
            startHandshake()
        }
        return protocol!!.outputStream
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return arrayOf(
            "SSL_RSA_WITH_RC4_128_MD5",
            "SSL_RSA_WITH_RC4_128_SHA", "TLS_RSA_WITH_AES_128_CBC_SHA",
            "TLS_RSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_RC4_128_SHA",
            "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA", "TLS_ECDH_RSA_WITH_RC4_128_SHA",
            "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_RC4_128_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
            "TLS_DHE_DSS_WITH_AES_128_CBC_SHA", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
            "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA", "SSL_RSA_WITH_DES_CBC_SHA",
            "SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA",
            "SSL_RSA_EXPORT_WITH_RC4_40_MD5", "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA",
            "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"
        )
    }

    override fun getEnabledCipherSuites(): Array<String> {
        return supportedCipherSuites
    }

    override fun setEnabledCipherSuites(suites: Array<String>) {
        throw UnsupportedOperationException(
            "This would change the SSL fingerprint"
        )
    }

    override fun getSupportedProtocols(): Array<String> {
        return arrayOf("SSLv3")
    }

    override fun getEnabledProtocols(): Array<String> {
        return supportedProtocols
    }

    override fun setEnabledProtocols(protocols: Array<String>) {
        throw UnsupportedOperationException(
            "This would change the SSL fingerprint"
        )
    }

    override fun getSession(): SSLSession? {
        return null
    }

    override fun addHandshakeCompletedListener(listener: HandshakeCompletedListener) {
        throw UnsupportedOperationException("Do we need this?")
    }

    override fun removeHandshakeCompletedListener(
        listener: HandshakeCompletedListener
    ) {
        throw UnsupportedOperationException("Do we need this?")
    }

    override fun setUseClientMode(mode: Boolean) {
        if (!mode) {
            throw UnsupportedOperationException("This socket is client mode only")
        }
    }

    override fun getUseClientMode(): Boolean {
        return true
    }

    override fun setNeedClientAuth(need: Boolean) {
        if (need) {
            throw UnsupportedOperationException("Nope!")
        }
    }

    override fun getNeedClientAuth(): Boolean {
        return false
    }

    override fun setWantClientAuth(want: Boolean) {
        if (want) {
            throw UnsupportedOperationException("Don't care")
        }
    }

    override fun getWantClientAuth(): Boolean {
        return false
    }

    override fun setEnableSessionCreation(flag: Boolean) {}
    override fun getEnableSessionCreation(): Boolean {
        return false
    }
}