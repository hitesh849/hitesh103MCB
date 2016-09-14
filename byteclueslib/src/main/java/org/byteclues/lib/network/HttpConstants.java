package org.byteclues.lib.network;

/**
 * Created by admin on 19-07-2015.
 */
public class HttpConstants {
    public final static int HTTP_GET = 0;
    public final static int HTTP_POST = 1;
    public final static int HTTP_MULTIPART_POST = 2;
    public final static int HTTP_REQUEST_TIMEOUT = 30000; // 180000;
    public final static int SOCKET_REQUEST_TIMEOUT = 30000;
    public final static int OUTPUT_BUFFER_SIZE = 16 * 1024;
    public static final int HTTP_READ_TIMEOUT = 30000;
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ENCODING_GZIP = "gzip";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final int INPUT_STREAM_BUFFSIZE = 16 * 1024;
    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String FLD_MEDIA_FORMAT = "media_format";
    public static final int SC_OK = 200;
    public static final int SC_NOT_FOUND = 404;
    public static String UTF8 = "UTF-8";
    public static String urlencoded = "application/x-www-form-urlencoded;charset=utf-8";
    public static String ACCEPT_CHARSET = "Accept-Charset";
}
