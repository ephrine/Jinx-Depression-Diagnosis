package devesh.ephrine.depression.self.diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    public String Login;
    public String Start;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

      /*  mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                   // Login="y";
                    //  if(Start.equals("y")){
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);

                    // Remove activity
                    //                 finish();
                    //  }
                    // User is signed in
                    Log.d("Jinx FB", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                   // Login="n";

                    //  if(Start.equals("y")){
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);

                    // Remove activity
                    //               finish();
                    //   }
                    // User is signed out
                    Log.d("Jinx FB", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        */

        //TextView tx1 =(TextView)findViewById(R.id.textView18);
        // TextView tx2 =(TextView)findViewById(R.id.textView19);

        // Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        // Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        //tx1.startAnimation(animation1);
        //tx2.startAnimation(animation2);
        LoginCheck();

        ImageView img = (ImageView) findViewById(R.id.imageView4);
        ImageView ep = (ImageView) findViewById(R.id.imageView5);

        ImageView jlogo = (ImageView) findViewById(R.id.logo);
        ImageView jinxlogo = (ImageView) findViewById(R.id.jinxlogo);
        TextView jinxdesc = (TextView) findViewById(R.id.jinxdesc);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splashbg);
        Animation j = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);

        img.startAnimation(anim);
        jlogo.startAnimation(j);
        jinxlogo.startAnimation(j);
        jinxdesc.startAnimation(j);
        ep.startAnimation(j);


        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5 * 1000);
                    if (Login.equals("y")) {
                        // After 5 seconds redirect to another intent
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);

                        // Remove activity
                        finish();
                    } else if (Login.equals("n")) {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);

                        // Remove activity
                        finish();
                    }


                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

    }

    public void LoginCheck() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Login = "y";
                    //  if(Start.equals("y")){
                    //         Intent i = new Intent(getBaseContext(), MainActivity.class);
                    //         startActivity(i);

                    // Remove activity
                    //                 finish();
                    //  }
                    // User is signed in
                    Log.d("Jinx FB", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Login = "n";

                    //  if(Start.equals("y")){
                    //      Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    //        startActivity(i);

                    // Remove activity
                    //               finish();
                    //   }
                    // User is signed out
                    Log.d("Jinx FB", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
