/*package ca.kylegray.vanvin;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackgroundTask extends AsyncTask {
    public final String TAG = " 👻 Background Task ";

    @Override
    protected Object doInBackground(Object[] jArray) {

        try{
            //CREATING THE CONNECTION
            URL url = new URL(Globals.URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); // Uses Post Protocol
            connection.setRequestProperty("Accept", "application/json"); // JSON data
            connection.setDoOutput(true); // Accepts output
            connection.setDoInput(true); // Accepts Input

            // WRITE OUT JSON DATA
            String dataToSend = jArray.toString(); // Converts Json Array data to string for sending
            OutputStream streamOut = connection.getOutputStream();  // Opens connection for output stream
            BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(streamOut, "utf-8")); // Buffers the output stream
            bWriter.write(dataToSend);
            bWriter.flush();
            bWriter.close();

            // Input data? Response from server?
            BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = bReader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            Log.d(TAG, response.toString());

        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
     return true;
    }


}
*/