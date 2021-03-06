package ca.kylegray.vanvin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_1_JOB;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_2_VIN;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_3_TIMESTAMP;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_4_READING;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_5_TIRE;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_6_COMMENT;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_7_DEVID;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_ID;

public class BMWStorageActivity extends AppCompatActivity implements Dialog.NoticeDialogListener{
    private static final String TAG = "*** STORAGE ***";
    private EditText barcodeResult;
    private EditText commentInput;
//    private TextView readingInput;
    private TextView textResult;
    private TextView totalCount;
    private ProgressDialog progressDialog;


    //  TIME STAMP FROM DEVICE TO COMPARE AGAINST SERVER TIME STAMP
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
    Date date = new Date();
    String stamp = sdf.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bmwstorage);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        clearFields();
        myfunction();

        barcodeResult = findViewById(R.id.text_vin);
        commentInput = findViewById(R.id.text_comment);
//        readingInput = findViewById(R.id.text_reading);
        textResult = findViewById(R.id.text_result);
        totalCount = findViewById(R.id.textview_count);
        barcodeResult.requestFocus();

    }

    // DIALOG FOR SUBMITTING SCANS, VALIDATES VIN LENGTH
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String devid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String vin = barcodeResult.getText().toString();
        String comment = commentInput.getText().toString();
        String job = "storage";
        dbHelper dbh = new dbHelper(this);
        dbh.toDB(job, stamp, vin, comment, devid);
        Log.d(TAG, "unique ID: " + devid);
        clearFields();
        myfunction();
        dbh.close();
        barcodeResult.requestFocus();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        clearFields();
        myfunction();
    }


    // CREATES ACTION BAR AIRPLANE BUTTON
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // CREATES ACTION BAR AIRPLANE SEND BUTTON FUNCTION
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            // send function
            servesUp();
        }
        return super.onOptionsItemSelected(item);
    }



    // REWRITES ALL THE INFORMATION AFTER THE CLEAR FUNCTION RUNS
    public void myfunction() {
        dbHelper genDatabase = new dbHelper(this); //dbHelper with this context
        Cursor mCursor = genDatabase.getScans("'storage'");
        TextView genericTextResult = findViewById(R.id.text_result);
        TextView genericTotalCount = findViewById(R.id.textview_count);
        EditText barcodeResult = findViewById(R.id.text_vin);
        String scansText = "Scans: ";
        int i =0;

        if (mCursor.moveToFirst()) {
            while (!mCursor.isAfterLast()) {
                i++;
                String id = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
                String vin = mCursor.getString(mCursor.getColumnIndex(COLUMN_2_VIN));
                String reading = mCursor.getString(mCursor.getColumnIndex(COLUMN_4_READING));
                String comment = mCursor.getString(mCursor.getColumnIndex(COLUMN_6_COMMENT));
                String job = mCursor.getString(mCursor.getColumnIndex(COLUMN_1_JOB));
                String devid = mCursor.getString(mCursor.getColumnIndex(COLUMN_7_DEVID));
                genericTextResult.append(i + ") " + vin + " " + comment + "\n");

                mCursor.moveToNext();
                int countInt = mCursor.getCount();
                String CountString = Integer.toString(countInt);
                genericTotalCount.setText(scansText + CountString);
                barcodeResult.requestFocus();
            }
        }
        mCursor.close();

    }

    // HANDLES BARCODE SCAN
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CommonStatusCodes.SUCCESS) {
            if (data != null) {
                Barcode barcode = data.getParcelableExtra("barcode");
                barcodeResult.setText(barcode.displayValue);
            }
        } else {
            barcodeResult.setText(R.string.NoBarcode); // Not working when backing out of camera view
        }
        super.onActivityResult(requestCode, resultCode, data);
        //}
    }

    //Sends to Barcode Activity
    public void scanBarcode(View view) {
        Intent ScanIntent = new Intent(this, ScanActivity.class);
        startActivityForResult(ScanIntent, 0);
    }


    // CLEARS THE FIELDS FOR REGENERATION BY myfunction FUNCTION
    public void clearFields() {
        TextView textResult = findViewById(R.id.text_result);
        EditText barcodeResult = findViewById(R.id.text_vin);
        EditText genericCommentInput = findViewById(R.id.text_comment);
        TextView totalCount = findViewById(R.id.textview_count);
        EditText readingInput = findViewById(R.id.text_reading);
        totalCount.setText("Scans: 0");
        textResult.setText("");
//        readingInput.setText("12.");
        barcodeResult.getText().clear();
        Switch genericSwitchKeep = findViewById(R.id.generic_keep_switch);
        if (!genericSwitchKeep.isChecked()) {
            genericCommentInput.getText().clear();
        }
        Log.i(TAG, "Fields cleared");
    }


    // SUBMITS CURRENT VIN AND ITEMS TO THE QUEUE LIST OF PREVIOUS VINS
    public void submitToQueue(View view) {
        String devid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String vin = barcodeResult.getText().toString();
        String comment = commentInput.getText().toString();
        String job = "storage";
        dbHelper dbh = new dbHelper(this);

        // IF STATEMENT FOR VALIDATING VIN LENGTH
        if (vin.length() != 17) {
            lengthDialog();
            Log.d(TAG, "VIN LENGTH ERROR");
        }else{
            dbh.toDB(job, stamp, vin, comment, devid);
            Log.d(TAG, "unique ID: " + devid);
            clearFields();
            myfunction();
            dbh.close();

        }

    }

    // DIALOG FOR VIN 17 CHECK
    public void lengthDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    // SERVES JSON DATA TO THE SERVER
    public void servesUp(){
        JSONArray jobj = jsonCreator("'storage'");
        new BMWStorageActivity.JSONTask().execute(jobj); // was new JSONTask().execute(String.valueOf(jobj));
    }

    // CREATES JSON DATA FROM SCAN DATA
    public JSONArray jsonCreator(String Job) {
        // CONVERT GENERIC CURSOR RESULTS TO JSON OBJECT AND TO STRING
        dbHelper genDatabase = new dbHelper(this); // access database
        Cursor mCursor = genDatabase.getScans(Job); // access cursor
        JSONArray jsonArray = new JSONArray(); // create jason array
        JSONObject jsonObject = new JSONObject(); // create json obj
        JSONObject json = new JSONObject();

        if (mCursor.moveToFirst()) {
            while (!mCursor.isAfterLast()) {
                //String id = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
                String vin = mCursor.getString(mCursor.getColumnIndex(COLUMN_2_VIN));
                String comment = mCursor.getString(mCursor.getColumnIndex(COLUMN_6_COMMENT));
                String job = mCursor.getString(mCursor.getColumnIndex(COLUMN_1_JOB));
//                String reading = mCursor.getString(mCursor.getColumnIndex(COLUMN_4_READING));
                String tire = mCursor.getString(mCursor.getColumnIndex(COLUMN_5_TIRE));
                String stamp = mCursor.getString(mCursor.getColumnIndex(COLUMN_3_TIMESTAMP));
                String devid = mCursor.getString(mCursor.getColumnIndex(COLUMN_7_DEVID));

                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("vin", vin);
                    jsonObject.put("job", job);
                    jsonObject.put("comment", comment);
//                    jsonObject.put("reading", reading);
                    jsonObject.put("tire", tire);
                    jsonObject.put("stamp",stamp);
                    jsonObject.put("devid", devid);
                } catch (JSONException e) {
                    Log.d(TAG, "CURSOR/JSON exception: " + e);
                }
                jsonArray.put(jsonObject);

                mCursor.moveToNext();
            }
//                try{
//                    json.put("json",jsonArray);
//                } catch (JSONException e) {
//                    Log.d(TAG,"Exception" + e);
//                }
            //bgTask.doInBackground(jsonArray);// use background method to send json to server
            //Log.d(TAG, "json array to string:" + jsonArray.toString());
            //Log.d(TAG, "json creator log: " + json);
            mCursor.close();
            Log.d(TAG, "JSON STRING:  " + jsonArray.toString());
        }return jsonArray;

    }




    // JSON/ASYNC TASK DOES ALL THE BACKGROUND NETWORK STUFF??
    public class JSONTask extends AsyncTask<JSONArray, Integer, String> {
        URL url = null;
        HttpURLConnection connection;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(BMWStorageActivity.this);
            //progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.sendingMsg));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(JSONArray... data) {
            JSONArray json = data[0];

            try {
                //CREATING THE CONNECTION
                url = new URL(Globals.URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, "connection exception: " + e);
                return e.toString();
            }

            try {
                connection = (HttpURLConnection) url.openConnection(); //establish/opens connection
                connection.setRequestMethod("POST"); //establish method POST
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(10000);
                connection.setRequestProperty("Content-type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "httpurl exception: " + e);
                return e.toString();
            }

            try {
                OutputStreamWriter streamOut = new OutputStreamWriter(connection.getOutputStream());
                streamOut.write(json.toString()); // sends the params that was passed from servesUp method
                streamOut.flush();
                streamOut.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "output stream exception: " + e);
                return e.toString();
            }

            try {
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read Response
                    InputStream streamIn = connection.getInputStream();
                    BufferedReader breader = new BufferedReader(new InputStreamReader(streamIn));
                    StringBuilder response = new StringBuilder();
                    String output;
                    while ((output = breader.readLine()) != null) {
                        response.append(output);
                    }
                    // Pass data to onPostExecute method
                    return (response.toString());

                } else {
                    return ("did not receive response from server");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "final network exception: " + e);
            }finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            dbHelper dbh = new dbHelper(getApplicationContext());
            Log.d(TAG,"server response: " + response);
            super.onPostExecute(response);
            progressDialog.dismiss(); // DISMISS THE PROGRESS DIAGLOG

            // SERVER PHP IS SET UP TO RETURN "INSERT SUCCESSFUL" STRING IF ALL THE LINES ARE
            // INSERTED INTO THE DATABASE
            if(response.contains(Globals.SRESPONSE)){
                dbh.dumpScans("'storage'");
                clearFields();
                myfunction();
            }else{
            Toast.makeText(getApplicationContext(),"Something went wrong...",Toast.LENGTH_LONG).show();
            }
        }

    }
}
