package com.example.marlo.voteapp.Helpers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marlo on 23/10/2016.
 */

public class HttpHandler
{

    //region [ Public Methods ]

    public String makeServiceCall(String requestUrl)
    {
        String response = null;
        try
        {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(in);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    //endregion

    //region [ Private Methods ]

    private String convertStreamToString(InputStream stream)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;

        try
        {
            while((line = reader.readLine()) != null)
                sb.append(line).append('\n');
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //endregion

}
