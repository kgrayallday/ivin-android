package ca.kylegray.vanvin;

import android.annotation.SuppressLint;
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

import javax.net.ssl.HttpsURLConnection;

import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_1_JOB;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_2_VIN;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_3_TIMESTAMP;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_4_READING;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_5_TIRE;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_6_COMMENT;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_7_DEVID;
import static ca.kylegray.vanvin.ScanContract.ScanEntry.COLUMN_ID;


public class GenericActivity extends AppCompatActivity implements Dialog.NoticeDialogListener {
    private static final String TAG = "*** GENERIC ***";
    private EditText genericBarcodeResult;
    private EditText genericCommentInput;
    private TextView genericTextResult;
    private TextView genericTotalCount;
    private ProgressDialog progressDialog;


//    dbHelper genDatabase = new dbHelper(this); //create an instance (or reference to?) the database
//    Cursor genCursor;// create an instance of cursor containing query results?
//    ArrayList<Scan> genArrayList = new ArrayList<>(); // create array list to hold database query results?

    //  TIME STAMP FROM DEVICE TO COMPARE AGAINST SERVER TIME STAMP
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
    Date date = new Date();
    String stamp = sdf.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_generic);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        clearFields();
        rebuild();

        genericBarcodeResult = findViewById(R.id.text_vin);
        genericCommentInput = findViewById(R.id.text_comment);
        genericTextResult = findViewById(R.id.text_result);
        genericTotalCount = findViewById(R.id.textview_count);
        genericBarcodeResult.requestFocus();

    }

    // DIALOG FOR SUBMITTING SCANS, VALIDATES VIN LENGTH
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        @SuppressLint("HardwareIds") String devid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String vin = genericBarcodeResult.getText().toString();
        String comment = genericCommentInput.getText().toString();
        String job = "generic";
        dbHelper dbh = new dbHelper(this);
        dbh.toDB(job, stamp, vin, comment, devid);
        Log.d(TAG, "unique ID: " + devid);
        clearFields();
        rebuild();
        dbh.close();
    }


    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        clearFields();
        rebuild();
    }

    // CREATES ACTION BAR AIRPLANE BUTTON
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public void rebuild() {
        dbHelper genDatabase = new dbHelper(this); //dbHelper with this context
        Cursor mCursor = genDatabase.getScans("'generic'");
        TextView genericTextResult = findViewById(R.id.text_result);
        TextView genericTotalCount = findViewById(R.id.textview_count);
        EditText barcodeResult = findViewById(R.id.text_vin);
        String scansText = "Scans: ";
        int i = 0;

        if (mCursor.moveToFirst()) {
            while (!mCursor.isAfterLast()) {
                i++;
                String id = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
                String vin = mCursor.getString(mCursor.getColumnIndex(COLUMN_2_VIN));
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

    // GETS THE RESULTING BARCODE FROM THE SCAN ACTIVITY AND RETURNS THE RESULT HERE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    genericBarcodeResult.setText(barcode.displayValue);
                }
            } else if (resultCode == RESULT_CANCELED) {
                genericBarcodeResult.setText(R.string.NoBarcode); // Not working when backing out of camera view
            } else {
                genericBarcodeResult.setText(R.string.NoBarcode);
            }
        }
    }

    //Sends to actual Barcode Activity
    public void scanBarcode(View view) {
        Intent ScanIntent = new Intent(this, ScanActivity.class);
        startActivityForResult(ScanIntent, 0);
    }


    // CLEARS THE FIELDS FOR REGENERATION BY myfunction FUNCTION
    public void clearFields() {
        TextView genericTextResult = findViewById(R.id.text_result);
        EditText genericBarcodeResult = findViewById(R.id.text_vin);
        EditText genericCommentInput = findViewById(R.id.text_comment);
        TextView genericTotalCount = findViewById(R.id.textview_count);
        genericTotalCount.setText("Scans: 0");
        genericTextResult.setText("");
        genericBarcodeResult.getText().clear();
        Switch genericSwitchKeep = findViewById(R.id.generic_keep_switch);
        if (!genericSwitchKeep.isChecked()) {
            genericCommentInput.getText().clear();
        }
        Log.i(TAG, "Fields cleared");
    }


    // SUBMITS CURRENT VIN AND ITEMS TO THE QUEUE LIST OF PREVIOUS VINS
    public void submitToQueue(View view) {
        String devid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String vin = genericBarcodeResult.getText().toString();
        String comment = genericCommentInput.getText().toString();
        String job = "generic";
        dbHelper dbh = new dbHelper(this);

        // IF STATEMENT FOR VALIDATING VIN LENGTH
        if (vin.length() != 17) {
            lengthDialog();
            Log.d(TAG, "INCORRECT VIN LENGTH");
        } else {
            dbh.toDB(job, stamp, vin, comment, devid);
            Log.d(TAG, "unique ID: " + devid);
            clearFields();
            rebuild();
            dbh.close();
        }

    }

    // DIALOG FOR VIN 17 CHECK
    public void lengthDialog() {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    // SERVES JSON DATA TO THE SERVER
    public void servesUp() {
        JSONArray jobj = jsonCreator("'generic'");
        new JSONTask().execute(jobj); // was new JSONTask().execute(String.valueOf(jobj));
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
                String reading = mCursor.getString(mCursor.getColumnIndex(COLUMN_4_READING));
                String tire = mCursor.getString(mCursor.getColumnIndex(COLUMN_5_TIRE));
                String stamp = mCursor.getString(mCursor.getColumnIndex(COLUMN_3_TIMESTAMP));
                String devid = mCursor.getString(mCursor.getColumnIndex(COLUMN_7_DEVID));

                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("vin", vin);
                    jsonObject.put("job", job);
                    jsonObject.put("comment", comment);
                    jsonObject.put("reading", reading);
                    jsonObject.put("tire", tire);
                    jsonObject.put("stamp", stamp);
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
        }
        return jsonArray;

    }


    // JSON/ASYNC TASK DOES ALL THE BACKGROUND NETWORK STUFF??
    public class JSONTask extends AsyncTask<JSONArray, Integer, String> {
        URL url;
        HttpURLConnection connection;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(GenericActivity.this);
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
                connection = (HttpsURLConnection) url.openConnection(); //establish/opens connection
                connection.setRequestMethod("POST"); //establish http method POST
                connection.setReadTimeout(150000);
                connection.setConnectTimeout(100000);
                connection.setRequestProperty("Content-type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                Log.d(TAG, "URL AS STRING: " + url.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "http url exception: " + e);
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
                String responseCodeString = Integer.toString(responseCode);
                Log.d(TAG,  "RESPONSE CODE STRING: " + responseCodeString);

                if (responseCode == HttpsURLConnection.HTTP_OK) {
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
                    return ("did not receive correct response from server: " + responseCodeString + ".");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "final network exception: " + e);
            } finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            dbHelper dbh = new dbHelper(getApplicationContext());
            Log.d(TAG, "server response: " + response);
            super.onPostExecute(response);
            progressDialog.dismiss(); // DISMISS THE PROGRESS DIAGLOG

            // SERVER PHP IS SET UP TO RETURN "INSERT SUCCESSFUL" STRING IF ALL THE LINES ARE
            // INSERTED INTO THE DATABASE
            if (response == null) {
                Log.d(TAG, "RESPONSE IS NULL");
                Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
            } else {
                if (response.contains(Globals.SRESPONSE)) {
                    dbh.dumpScans("'generic'");
                    clearFields();
                    rebuild();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

    }
}


