package it.univaq.findervelox.utility;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Requests {

    public static void asyncRequest(OnRequestListener listener) {

        new Thread(() -> {
            byte[] data = doRequest("GET", "http://www.datiopen.it/export/json/Mappa-degli-autovelox-in-italia.json", listener);
            if(listener != null)
                listener.onRequestCompleted(data);
        }).start();
    }

    private static byte[] doRequest(String method, String address, OnRequestListener listener) {

        HttpURLConnection connection = null;
        try{
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream in = connection.getInputStream();
                int length = Integer.parseInt(connection.getHeaderField("Content-Length"));

                byte[] data = new byte[length], buffer = new byte[1024];
                int read, counter = 0;

                while((read = in.read(buffer)) != -1) {
                    System.arraycopy(buffer, 0, data, counter, read);
                    counter += read;

                    int percentage = counter * 100 / length;
                    if(listener != null)
                        listener.onRequestUpdate(percentage);
                }

                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null) connection.disconnect();
        }

        return null;
    }
}
