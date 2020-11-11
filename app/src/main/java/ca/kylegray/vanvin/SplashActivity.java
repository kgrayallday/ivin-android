package ca.kylegray.vanvin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

public class SplashActivity extends AppCompatActivity {
    String TAG = "*** SPLASH ACTIVITY ***";
    VideoView videoView;
    private static int SPLASH_TIME_OUT = 3000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        Log.d(TAG, "Running onCreate");

            new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, MainMenuActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);

            try {
                videoView = (VideoView) findViewById(R.id.video_view);
                getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ivinwebm));
                videoView.setZOrderOnTop(true); // Solved black video load flicker issue ✅
                //videoView.requestFocus();
//                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    public void onCompletion(MediaPlayer mp) {
//                        //GoToMain();
//                    }
//
//                });
                videoView.start();

            }catch (Exception e) {
                //GoToMain();
        }
    }


//    public void GoToMain() {
//        Log.d(TAG, "Running GoToMain function");
//        if (isFinishing()){
//            Log.d(TAG, "Never Made it to Start Activity");
//        }else{
//        startActivity(new Intent(this, MainMenuActivity.class));
//        finish();
//        }
//    }

}



