package ca.kylegray.vanvin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {
    String TAG = "MAIN MENU ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        Log.d(TAG, "Main Activity Created");
    }

    //THESE HANDLE ON-CLICK FOR FOR ACTIVITY/INTENT PORTAL
    public void BMWOnClick(View view){
        Intent BMWIntent = new Intent (this, BMWMenuActivity.class);
        startActivity(BMWIntent);
        Log.d(TAG, "BMW Activity Button Clicked");
    }

    public void VWOnClick(View view){
        Intent VWIntent = new Intent (this, VWMenuActivity.class);
        startActivity(VWIntent);
        Log.d(TAG, "VW Activity Button Clicked");
    }

    public void GenericOnClick(View view){
        Intent GenericIntent = new Intent (this, GenericActivity.class);
        startActivity(GenericIntent);
        Log.d(TAG, "Generic Activity Button Clicked");

    }
}


/*

        ______          ___  ___   ___
        |  ___|         |  \/  |  |_  |
        | |_ ___  _ __  | .  . |    | |
        |  _/ _ \| '__| | |\/| |    | |
        | || (_) | |    | |  | |/\__/ /
        \_| \___/|_|    \_|  |_/\____/


*/