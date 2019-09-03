package com.mdove.dependent.networkprovider.base

object NetworkConstants {

    /*********************************************
     * Network Results
     */
    val MSG_OK = 10
    val MSG_ERROR = 11

    val OP_ERROR_NO_CONNECTION = 12
    val OP_ERROR_CONNECT_TIMEOUT = 13
    val OP_ERROR_NETWORK_TIMEOUT = 14
    val OP_ERROR_NETWORK_ERROR = 15
    val OP_ERROR_SERVER_ERROR = 16
    val OP_ERROR_API_ERROR = 17
    val OP_ERROR_UNKNOWN = 18
    val OP_ERROR_SERVICE_UNAVAILABLE = 19
    val OP_ERROR_RESPONSE_LENGTH_EXCEED = 20
    val OP_ERROR_SSL = 21
    val OP_ERROR_UNBIND_ACCOUNT = 22

    val OP_ERROR_SESSION_EXPIRE = 105
    val OP_ERROR_PLATFORM_EXPIRE = 108
    val OP_ERROR_CONNECT_SWITCH = 111
    val OP_ERROR_HAS_CONNECTED_TO_OTHER_USER = 114

    val STATUS_SUCCESS = "success"
    val STATUS_ERROR = "error"
    val SESSION_EXPIRED = "session_expired"
    val KEY_MESSAGE = "message"
    val KEY_DATA = "data"
    val ERROR_MESSAGE = "error_message"
    val DISCONNECT_LASTCONNECT_ERROR = "DisconnectLastConnectError"

    val SC_UNKNOWN = 1
    val SC_CONNECT_TIMEOUT = 2
    val SC_SOCKET_TIMEOUT = 3
    val SC_IO_EXCEPTION = 4
    val SC_SOCKET_EXCEPTION = 5
    val SC_RESET_BY_PEER = 6
    val SC_BIND_EXCEPTION = 7
    val SC_CONNECT_EXCEPTION = 8
    val SC_NO_REOUTE_TO_HOST = 9
    val SC_PORT_UNREACHABLE = 10
    val SC_UNKNOWN_HOST = 11
    val SC_ECONNRESET = 12
    val SC_ECONNREFUSED = 13
    val SC_EHOSTUNREACH = 14
    val SC_ENETUNREACH = 15
    val SC_EADDRNOTAVAIL = 16
    val SC_EADDRINUSE = 17
    val SC_NO_HTTP_RESPONSE = 18
    val SC_CLIENT_PROTOCOL_EXCEPTION = 19
    val SC_FILE_TOO_LARGE = 20
    val SC_TOO_MANY_REDIRECT = 21

    val SC_UNKNOWN_CLIENT_ERROR = 31
    val SC_NO_SPACE = 32 // ENOSPC: no space left on device
    val SC_ENOENT = 33 // ENOENT: no such file or directory
    val SC_EDQUOT = 34 // EDQUOT: exceed disk quota
    val SC_EROFS = 35 // EROFS: read-only file system
    val SC_EACCES = 36 // EACCES: permission denyed
    val SC_EIO = 37 // EIO (I/O error)

    val THREAD_NAME_ACTIONREAPER = "ActionReaper"

    val CONNECT_TIMEOUT = 20 * 1000
    val IO_TIMEOUT = 20 * 1000
    val SOCKET_BUFFER_SIZE = 8192

    val PNAME_REMOTE_ADDRESS = "x-snssdk.remoteaddr"

}
