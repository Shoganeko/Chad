package org.woahoverflow.chad.framework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The Utility class for Chad
 *
 * @author sho, codebasepw
 * @since 0.6.3 B2
 */
public final class Util
{

    /**
     * The user agent
     */
    private static final String USER_AGENT = "Mozilla/5.0";

    /**
     * Gets the current timestamp
     *
     * @return The current timestamp
     */
    public static synchronized String getTimeStamp()
    {
        return new SimpleDateFormat("MM/dd/yyyy hh:mm").format(Calendar.getInstance().getTime());
    }

    /**
     * Gets a String from an http
     *
     * @param url The URL to request
     * @return The gotten String
     */
    public static synchronized String httpGet(String url) {
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            if (responseCode != 200)
            {
                System.out.println("\nThere was an error sending a request to url : " + url);
                System.out.println("Response Code : " + responseCode);
                return "";
            }
            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String response = in.lines().collect(Collectors.joining());
            in.close();
            return response;
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public static synchronized String fixEnumString(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Flips a coin
     *
     * @return Returns either true or false, it's randomized
     */
    public static synchronized boolean coinFlip()
    {
        Random random = new Random();
        int flip = -1;
        for (int i = 0; i < 100; i++)
            flip = random.nextInt(2);
        return flip == 0;
    }
}
