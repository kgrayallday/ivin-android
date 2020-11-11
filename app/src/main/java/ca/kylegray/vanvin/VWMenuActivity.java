package ca.kylegray.vanvin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class VWMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vw);
    }

    //THIS IS - - - - - ONCLICK INTENTS FOR BUTTONS LEADING TO OTHER VIEWS - - - - -

    public void UpfitOnClick(View view){
        Intent UpfitIntent =new Intent(this,VWUpfitActivity.class);
        startActivity(UpfitIntent);
    }

    public void ServiceOnClick(View view){
        Intent ServiceIntent = new Intent(this,VWServiceActivity.class);
        startActivity(ServiceIntent);
    }

}
