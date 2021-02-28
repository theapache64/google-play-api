package com.theapache64.gpa.core.net

import org.apache.http.HttpHost
import org.apache.http.conn.socket.LayeredConnectionSocketFactory
import org.apache.http.protocol.HttpContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException

class DroidConnectionSocketFactory : LayeredConnectionSocketFactory {

    @Throws(IOException::class)
    override fun createSocket(context: HttpContext): Socket {
        return Socket()
    }

    @Throws(IOException::class)
    override fun connectSocket(
        connectTimeout: Int,
        socket: Socket?,
        host: HttpHost,
        remoteAddress: InetSocketAddress,
        localAddress: InetSocketAddress?,
        context: HttpContext
    ): Socket {
        val sock = socket ?: createSocket(context)
        sock.connect(remoteAddress, connectTimeout)
        return createLayeredSocket(
            sock, host.hostName,
            remoteAddress.port, context
        )
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createLayeredSocket(
        socket: Socket, target: String, port: Int,
        context: HttpContext
    ): Socket {
        return DroidSocket(socket)
    }
}