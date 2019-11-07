package com.chen.idea.ui;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author chen
 */
public class HttpTool {

    private static final String TAG = "HttpTool";

    public static byte[] doPost(String path, byte[] data) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.getOutputStream().write(data);
            int len = conn.getContentLength();
            if (len < 0) {
                return null;
            }
            InputStream in = conn.getInputStream();
            conn.setReadTimeout(10000);
            byte[] b = new byte[len];
            boolean ok = readFully(in, b);
            in.close();
            conn.disconnect();
            if (ok) {
                return b;
            }
        } catch (Throwable ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }

    public static boolean readFully(InputStream in, byte[] b, int offset, int size) {
        int len;
        for (; size > 0; ) {
            try {
                len = in.read(b, offset, size);
                if (len == -1) {
                    return false;
                }
                offset += len;
                size -= len;
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                return false;
            }
        }
        return true;
    }

    public static boolean readFully(InputStream in, byte[] b) {
        return readFully(in, b, 0, b.length);
    }

}
