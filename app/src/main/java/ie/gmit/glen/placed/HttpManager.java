package ie.gmit.glen.placed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by glen on 17/02/15.
 *
 * my method to get data from server
 */
public class HttpManager {



    //my get data method to stream json string from server and build for parser
    public static String getData(String myString) {

        BufferedReader reader = null;

        try {
            URL url = new URL(myString);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(3000);

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            Log.d("String ",sb.toString());


            return sb.toString();



        } catch (Exception e)
        {
            Log.d("Help","helper");



            e.printStackTrace();
            return null;
        } finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {
                    e.printStackTrace();

                    return null;
                }
            }
        }

    }

}
