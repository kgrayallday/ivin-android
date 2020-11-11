package ca.kylegray.vanvin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class BMWMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bmw);
        Log.d("BMW Menu Activity", "BMW Menu Activity Created");
    }

    //THIS IS - - - - - ONCLICK INTENTS FOR BUTTONS LEADING TO OTHER VIEWS - - - - -

    public void InitialOnClick(View view){
        Intent InitialIntent = new Intent (this, BMWInitialActivity.class);
        startActivity(InitialIntent);
    }

    public void MaintenanceOnClick(View view){
        Intent MaintenanceIntent = new Intent (this, BMWMaintenanceActivity.class);
        startActivity(MaintenanceIntent);
    }

    public void StorageOnClick(View view){
        Intent StorageIntent = new Intent(this,BMWStorageActivity.class);
        startActivity(StorageIntent);
    }

    public void FinalOnClick(View view){
        Intent FinalIntent = new Intent(this,BMWFinalActivity.class);
        startActivity(FinalIntent);
    }

}
