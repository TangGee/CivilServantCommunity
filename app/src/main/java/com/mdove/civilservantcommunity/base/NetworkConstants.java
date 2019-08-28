package com.mdove.civilservantcommunity.base;

public class NetworkConstants {

    /*********************************************
     * Network Results
     *********************************************/
    public static final int MSG_OK                          = 10;
    public static final int MSG_ERROR                       = 11;

    public static final int OP_ERROR_NO_CONNECTION          = 12;
    public static final int OP_ERROR_CONNECT_TIMEOUT        = 13;
    public static final int OP_ERROR_NETWORK_TIMEOUT        = 14;
    public static final int OP_ERROR_NETWORK_ERROR          = 15;
    public static final int OP_ERROR_SERVER_ERROR           = 16;
    public static final int OP_ERROR_API_ERROR              = 17;
    public static final int OP_ERROR_UNKNOWN                = 18;
    public static final int OP_ERROR_SERVICE_UNAVAILABLE    = 19;
    public static final int OP_ERROR_RESPONSE_LENGTH_EXCEED = 20;
    public static final int OP_ERROR_SSL                    = 21;
    public static final int OP_ERROR_UNBIND_ACCOUNT         = 22;

    public final static int OP_ERROR_SESSION_EXPIRE = 105;
    public final static int OP_ERROR_PLATFORM_EXPIRE = 108;
    public final static int OP_ERROR_CONNECT_SWITCH = 111;
    public final static int OP_ERROR_HAS_CONNECTED_TO_OTHER_USER = 114;

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";
    public static final String SESSION_EXPIRED = "session_expired";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATA = "data";
    public static final String ERROR_MESSAGE = "error_message";
    public static final String DISCONNECT_LASTCONNECT_ERROR = "DisconnectLastConnectError";

    public static final int SC_UNKNOWN = 1;
    public static final int SC_CONNECT_TIMEOUT = 2;
    public static final int SC_SOCKET_TIMEOUT = 3;
    public static final int SC_IO_EXCEPTION = 4;
    public static final int SC_SOCKET_EXCEPTION = 5;
    public static final int SC_RESET_BY_PEER = 6;
    public static final int SC_BIND_EXCEPTION = 7;
    public static final int SC_CONNECT_EXCEPTION = 8;
    public static final int SC_NO_REOUTE_TO_HOST = 9;
    public static final int SC_PORT_UNREACHABLE = 10;
    public static final int SC_UNKNOWN_HOST = 11;
    public static final int SC_ECONNRESET = 12;
    public static final int SC_ECONNREFUSED = 13;
    public static final int SC_EHOSTUNREACH = 14;
    public static final int SC_ENETUNREACH = 15;
    public static final int SC_EADDRNOTAVAIL = 16;
    public static final int SC_EADDRINUSE = 17;
    public static final int SC_NO_HTTP_RESPONSE = 18;
    public static final int SC_CLIENT_PROTOCOL_EXCEPTION = 19;
    public static final int SC_FILE_TOO_LARGE = 20;
    public static final int SC_TOO_MANY_REDIRECT = 21;

    public static final int SC_UNKNOWN_CLIENT_ERROR = 31;
    public static final int SC_NO_SPACE = 32; // ENOSPC: no space left on device
    public static final int SC_ENOENT = 33; // ENOENT: no such file or directory
    public static final int SC_EDQUOT = 34; // EDQUOT: exceed disk quota
    public static final int SC_EROFS = 35; // EROFS: read-only file system
    public static final int SC_EACCES = 36; // EACCES: permission denyed
    public static final int SC_EIO = 37; // EIO (I/O error)

    public final static String THREAD_NAME_ACTIONREAPER = "ActionReaper";

    public final static int CONNECT_TIMEOUT = 20 * 1000;
    public final static int IO_TIMEOUT = 20 * 1000;
    public final static int SOCKET_BUFFER_SIZE = 8192;

    public static final String PNAME_REMOTE_ADDRESS = "x-snssdk.remoteaddr";

}
