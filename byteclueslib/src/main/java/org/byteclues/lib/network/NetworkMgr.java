package org.byteclues.lib.network;

/**
 * Created by admin on 19-07-2015..
 */

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.logger.Logger;
import org.byteclues.lib.shared_preferences.SharedPreferencesLib;
import org.byteclues.lib.utils.Util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

public class NetworkMgr {
    private static final String SPACE = " ";
    private static final String TAG = "NetworkMgr";

    private static boolean isFROYODevice = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1);
    private static String tag = "b2bazar";

    public static synchronized NetworkResponse httpPost(String httpUrl, String fieldName, String postData) {
        NetworkResponse netResp = httpPostNew(httpUrl, fieldName, postData);
        if (netResp != null && !netResp.isSuccess()) {
            netResp = httpPostNew(httpUrl, fieldName, postData);
            if (!netResp.isSuccess()) {
                netResp = httpPostNew(httpUrl, fieldName, postData);
            }
        }
        if (netResp != null && netResp.getStatusCode().equals(Util.STATUS_CODE_USER_LOGOUT)) {
            Intent intent = new Intent(Util.ACTION_USER_LOGOUT);
            intent.putExtra(Util.KEY_LOGOUT_MESSAGE, netResp.getMessageFromServer());
            Env.currentActivity.sendBroadcast(intent);
        }
        if (netResp != null && Util.checkValueForKey(netResp.getJsonObject(), Util.KEY_USER_TOKEN)) {
            try {
                SharedPreferencesLib.setUserToken(netResp.getJsonObject().getString(Util.KEY_USER_TOKEN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return netResp;
    }


    public static NetworkResponse httpPostNew(String httpUrl, String fieldName, String postData) {

        long stime = System.currentTimeMillis();
        NetworkResponse netResp = new NetworkResponse();
      /*  if (TextUtils.isEmpty(postData)) {
            if (Logger.IS_DEBUG) {
                Logger.error(TAG, "url [" + httpUrl + "] no data to post");
            }
            return netResp;
        }*/
        if (Logger.IS_DEBUG) {
            Logger.info(TAG, "url [" + httpUrl + "] field [" + fieldName + "] data [" + postData + "]");
        }

        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            String data;
            if (TextUtils.isEmpty(fieldName)) {
                data = URLEncoder.encode(postData, HttpConstants.UTF8);
            } else {
                data = URLEncoder.encode(fieldName, HttpConstants.UTF8) + "=" + URLEncoder.encode(postData, HttpConstants.UTF8);
            }
            data = "http://ec2-54-169-18-73.ap-southeast-1.compute.amazonaws.com//index.php?route=feed/rest_api/session";
            // constants
            URL url = new URL(data);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Oc-Merchant-Id", "b2bazar");
            conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty(HttpConstants.ACCEPT_CHARSET, HttpConstants.UTF8);
            conn.setRequestProperty(HttpConstants.CONNECTION, HttpConstants.KEEP_ALIVE);
            conn.setConnectTimeout(HttpConstants.HTTP_REQUEST_TIMEOUT);
            conn.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);


            conn.setFixedLengthStreamingMode(data.getBytes().length);
            // POST the data
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), HttpConstants.UTF8);
            osw.write(data);
            osw.close();
            osw = null;
            int statusCode = conn.getResponseCode();
            if (statusCode >= 200 && statusCode < 400) {

                // Create an InputStream in order to extract the response object
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }
            String response = readIt(is, conn.getContentEncoding());
            Log.d(tag, "response value is : " + response);
            if (conn.getResponseCode() != HttpConstants.SC_OK) {
                netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                return netResp;
            } else {
                netResp.respStr = response;
                netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            }
        } catch (SocketTimeoutException e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "url [" + httpUrl + "]\n data [" + postData + "]\n Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.TIMEOUT;
            // eLogger.fatal("httpPostNew : SocketTimeoutException : " + e);
            return netResp;
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "url [" + httpUrl + "]\n data [" + postData + "]\n Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
            // eLogger.fatal("httpPostNew : Network Exception" + e);
            return netResp;
        } finally {
            finallyClose(is, conn);
        }
        if (Logger.IS_DEBUG) {
            Logger.info(TAG, "url [" + httpUrl + "]\n data [" + postData + "]\n Response [" + netResp.respStr + "] status [" + netResp.netRespCode + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
        }

        return netResp;
    }

    private static void finallyClose(InputStream is, HttpURLConnection conn) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            conn.disconnect();
        }
    }

    private static String readIt(InputStream is, String encoding) throws IOException, UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder(
                HttpConstants.INPUT_STREAM_BUFFSIZE);
        BufferedReader r = null;
        if (HttpConstants.ENCODING_GZIP.equals(encoding)) {
            r = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(is), HttpConstants.UTF8),
                    HttpConstants.INPUT_STREAM_BUFFSIZE);
        } else {
            r = new BufferedReader(
                    new InputStreamReader(is, HttpConstants.UTF8),
                    HttpConstants.INPUT_STREAM_BUFFSIZE);
        }
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        r.close();
        return sb.toString();
    }

    public static NetworkResponse httpGet
            (String sourceFileURI, File outputFile) {

        long stime = System.currentTimeMillis();

        NetworkResponse netResp = new NetworkResponse();
        // file should not be download if exists.
        if (outputFile.exists()) {
            netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            if (Logger.IS_DEBUG) {
                Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] already exists");
            }
            return netResp;
        }
        if (TextUtils.isEmpty(sourceFileURI)) {
            netResp.netRespCode = NetworkResponse.ResponseCode.EMPTY_URL;
            if (Logger.IS_DEBUG) {
                Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] url null");
            }
            return netResp;
        }
        if (Logger.IS_DEBUG) {
            Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] already exists");
        }
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            // constants
            sourceFileURI = URLDecoder
                    .decode(sourceFileURI, HttpConstants.UTF8);
            URL url = new URL(sourceFileURI);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HttpConstants.ACCEPT_CHARSET, "UTF-8");
            conn.setRequestProperty(HttpConstants.CONNECTION,
                    HttpConstants.KEEP_ALIVE);
            conn.setConnectTimeout(HttpConstants.HTTP_REQUEST_TIMEOUT);
            conn.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);

            conn.setRequestProperty(HttpConstants.HEADER_ACCEPT_ENCODING,
                    HttpConstants.ENCODING_GZIP);
            is = conn.getInputStream();
            int statusCode = conn.getResponseCode();
            if (statusCode == HttpConstants.SC_OK) {
                InputStream inputStream = null;
                try {
                    inputStream = conn.getInputStream();
                } catch (IllegalStateException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                } catch (FileNotFoundException e) {
                    // No need to treat missing file on server side as an error
                    netResp.netRespCode = NetworkResponse.ResponseCode.OK;
                    return netResp;
                } catch (Exception e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outputFile);
                } catch (FileNotFoundException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
                int bytesRead = 0;
                try {
                    String encoding = conn.getContentEncoding();
                    // NOTE: Need to check how to get encoding info from
                    // Response.
                    BufferedInputStream bis;
                    if (HttpConstants.ENCODING_GZIP.equals(encoding)) {
                        // System.out.println("unzip file");
                        bis = new BufferedInputStream(new GZIPInputStream(
                                inputStream),
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    } else {
                        // System.out.println("no unzip file");
                        bis = new BufferedInputStream(inputStream,
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    }
                    byte[] buffer = new byte[HttpConstants.INPUT_STREAM_BUFFSIZE];
                    while ((bytesRead = bis.read(buffer)) > 0) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.close();
                    // Fix for zero byte file
                    if (outputFile.length() == 0) {
                        outputFile.delete();
                        // downloading 0 byte file is not an error. System
                        // should not try it again.
                        return netResp;
                    }
                } catch (IOException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    // eLogger.fatal("downloadFile : IOException" + e);
                    return netResp;
                }
            } else if (statusCode == HttpConstants.SC_NOT_FOUND) {
                netResp.netRespCode = NetworkResponse.ResponseCode.FILE_NOT_FOUND;
                return netResp;
            }
        } catch (SocketTimeoutException e) {
            netResp.netRespCode = NetworkResponse.ResponseCode.TIMEOUT;
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            return netResp;
        } catch (FileNotFoundException e) {
            // No need to treat missing file on server side as an error
            netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            return netResp;
        } catch (Exception e) {
            netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            return netResp;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // eLogger.fatal("httpPost : SocketTimeoutException : " +
                    // e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        if (Logger.IS_DEBUG)
            Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
        netResp.netRespCode = NetworkResponse.ResponseCode.OK;
        return netResp;
    }

    public static NetworkResponse httpGet(String sourceFileURI) {

        long stime = System.currentTimeMillis();

        NetworkResponse netResp = new NetworkResponse();

        if (TextUtils.isEmpty(sourceFileURI)) {
            netResp.netRespCode = NetworkResponse.ResponseCode.EMPTY_URL;
            return netResp;
        }
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            // constants
            sourceFileURI = URLDecoder
                    .decode(sourceFileURI, HttpConstants.UTF8);
            URL url = new URL(sourceFileURI);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HttpConstants.ACCEPT_CHARSET, "UTF-8");
            conn.setRequestProperty(HttpConstants.CONNECTION,
                    HttpConstants.KEEP_ALIVE);
            conn.setConnectTimeout(HttpConstants.HTTP_REQUEST_TIMEOUT);
            conn.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);
            // make some HTTP header nicety
            // conn.setRequestProperty("Content-Type",
            // "text/plain; charset=utf-8");
            conn.setRequestProperty(HttpConstants.HEADER_ACCEPT_ENCODING,
                    HttpConstants.ENCODING_GZIP);
            is = conn.getInputStream();
            int statusCode = conn.getResponseCode();
            if (statusCode == HttpConstants.SC_OK) {
                InputStream inputStream = null;
                try {
                    inputStream = conn.getInputStream();
                } catch (IllegalStateException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                } catch (FileNotFoundException e) {
                    // No need to treat missing file on server side as an error
                    netResp.netRespCode = NetworkResponse.ResponseCode.OK;
                    return netResp;
                } catch (Exception e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
                StringBuilder sb = new StringBuilder(1024);
                int bytesRead = 0;
                try {
                    String encoding = conn.getContentEncoding();
                    // NOTE: Need to check how to get encoding info from
                    // Response.
                    BufferedInputStream bis;
                    if (HttpConstants.ENCODING_GZIP.equals(encoding)) {
                        // System.out.println("unzip file");
                        bis = new BufferedInputStream(new GZIPInputStream(
                                inputStream),
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    } else {
                        // System.out.println("no unzip file");
                        bis = new BufferedInputStream(inputStream,
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    }
                    String response = readIt(bis, HttpConstants.UTF8);
                    netResp.netRespCode = NetworkResponse.ResponseCode.OK;
                    netResp.respStr = response;
                } catch (IOException e) {
                    e.printStackTrace();
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
            } else if (statusCode == HttpConstants.SC_NOT_FOUND) {
                netResp.netRespCode = NetworkResponse.ResponseCode.FILE_NOT_FOUND;
                return netResp;
            }
        } catch (SocketTimeoutException e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.TIMEOUT;
            return netResp;
        } catch (FileNotFoundException e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "]");
            }
            // No need to treat missing file on server side as an error
            netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            return netResp;
        } catch (Exception e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
            return netResp;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        if (Logger.IS_DEBUG)
            Logger.error(TAG, "get(): url [" + sourceFileURI + "] response [" + netResp.respStr + "] code [" + netResp.netRespCode + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
        return netResp;
    }
    public static NetworkResponse uploadFileToServer(String serverUrl, String filePath, String file_name) {
        NetworkResponse networkResponse = new NetworkResponse();
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourcefile = new File(filePath);

        if (sourcefile.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourcefile);
                URL url = new URL(serverUrl);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("connection", "close");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("file", file_name);
                conn.setRequestProperty("name", filePath);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"name\";filename=\"" + filePath + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                int statusCode = conn.getResponseCode();
                if (statusCode >= 200 && statusCode < 400) {
                    // Create an InputStream in order to extract the response object
                    is = conn.getInputStream();
                } else {
                    is = conn.getErrorStream();
                }
                String response = readIt(is, conn.getContentEncoding());
                Log.d(tag, "response value is : " + response);
                if (conn.getResponseCode() != HttpConstants.SC_OK) {
                    networkResponse.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return networkResponse;
                } else {
                    networkResponse.respStr = response;
                    networkResponse.netRespCode = NetworkResponse.ResponseCode.OK;
                }
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
        return networkResponse;
    }
}
