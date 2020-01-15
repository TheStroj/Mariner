package dev.simonmezgec.mariner.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

import dev.simonmezgec.mariner.Constant;

/** Class containing network utilities. */
public class NetworkUtils {

    /** Builds the URL based on the sorting parameter. Method taken from one of the public exercises
      * by Udacity. */
    public static URL buildUrl(String query) {
        Uri builtUri = Uri.parse(Constant.BASE_SEARCH_URL).buildUpon()
                .appendQueryParameter(Constant.TEXT_PARAMETER, "\"" + query + "\"")
                .appendQueryParameter(Constant.OFFSET_PARAMETER, Constant.OFFSET_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /** Returns the result from an HTTP response. Method taken from one of the public exercises
      * by Udacity. */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /** Performs a check if there is a connection to the internet. Method taken from the following
      *  Stack Overflow question (accepted answer, user: Levit, date: 06.08.2018):
      *  https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out */
    public static boolean isNotOnline() {
        try {
            int timeoutMs = Constant.TIMEOUT;
            Socket sock = new Socket();
            SocketAddress sockAdd = new InetSocketAddress(Constant.HOSTNAME, Constant.PORT);
            sock.connect(sockAdd, timeoutMs);
            sock.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}