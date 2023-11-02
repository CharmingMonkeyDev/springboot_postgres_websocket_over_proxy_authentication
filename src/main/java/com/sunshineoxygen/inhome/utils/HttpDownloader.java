package com.sunshineoxygen.inhome.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;


public final class HttpDownloader{

    private static final int BUFFER_SIZE = 4096;

    public static String getContent(String downloadUrl) {
        try {
            return getContent(downloadUrl, null, null);
        } catch (Exception e) {}
        return null;
    }

    public static String getContent(String downloadUrl, String userName, String password) throws Exception {
        StringBuilder content= new StringBuilder();

        HttpClient client = new HttpClient ();
        client.getParams().setParameter("http.socket.timeout", Integer.valueOf(1000));

        if (userName!=null && password!=null)
        {
            Credentials defaultcreds = new UsernamePasswordCredentials (userName, password);
            client.getState().setCredentials(AuthScope.ANY, defaultcreds);
        }

        GetMethod method = new GetMethod (downloadUrl);

        if (userName!=null && password!=null)
        {
            method.setDoAuthentication ( true );
        }
        method.getParams().setParameter("http.socket.timeout", Integer.valueOf(5000));
        method.setFollowRedirects (false);

        int statusCode = client.executeMethod (method);
        if (statusCode == HttpStatus.SC_OK)
        {
            BufferedReader reader = new BufferedReader( new java.io.InputStreamReader(method.getResponseBodyAsStream (),"UTF-8"));
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null )
            {
                content.append(tempLine).append("\n");
                //response+=line+"\n";
            }
            reader.close();
            tempLine = null;
        }

        method.releaseConnection ();
        return content.toString();
    }


    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @param newFileName name for downloaded file, without extension
     * @throws IOException
     */
    public static void downloadFile(String fileURL, String saveDir, String newFileName)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            //String fileExtension = FileUtil.getSuffix(fileName);

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
            System.out.println("saveDir = " + saveDir);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            File saveFile = new File(saveDir, (newFileName==null?fileName:newFileName));

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFile);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
}

