package devesh.ephrine.depression.self.diagnosis;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "Jinx";
    public InterstitialAd mInterstitialAd;
    public String uid;

    public boolean installed = false;
    private AdView mAdView;

    public boolean AdLoaded;

    public String UserFname;
    public String UserProfileURL;
    public String UserEmailID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public String NetCheck;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        AdLoaded=false;
isNetworkAvailable();
        isAppInstalled("devesh.ephrine.depression.self.diagnosis.pro");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            uid = user.getUid();
            UserEmailID = user.getEmail();
            UserFname = user.getDisplayName();
            UserProfileURL=user.getPhotoUrl().toString();
            Log.d(TAG, "onCreate:-------------------\n"+uid+"\n"+UserEmailID+"\n"+UserFname+"\n"+UserProfileURL);

            FirebaseDatabase database1 = FirebaseDatabase.getInstance();

            //First Name
            DatabaseReference AddUser = database1.getReference("Jinx/users/" + uid );
            AddUser.child("FirstName").setValue(UserFname);
            AddUser.child("emailID").setValue(UserEmailID);

            ImageView profilePicimageView = (ImageView) findViewById(R.id.ProfilePicCircularImageView);
            TextView UserNameTextView=(TextView)findViewById(R.id.textViewUserName);
            TextView UserEmailTextView=(TextView)findViewById(R.id.textViewUserEmail);

            if(UserProfileURL!=null){
                if(UserProfileURL.equals(null)||UserProfileURL.equals("null")){

                }else {
                    AddUser.child("ProfilePicURL").setValue(UserProfileURL);

                    Glide.with(this).load(UserProfileURL).into(profilePicimageView);
                }
            }

           UserNameTextView.setText(UserFname);
           UserEmailTextView.setText(UserEmailID);

           LoadHistory();


        }


        if(installed) {

        } else {


            //ADS------------------
            MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.intadd));

            //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
  /*          mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                    finish();
                }
            });
*/
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                    AdLoaded=true;
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                    //finish();
                    AdLoaded=true;

                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                   // finish();
                    AdLoaded=true;
                }
            });
            requestNewInterstitial();
        }



        isNetworkAvailable();

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.



    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onBackPressed() {

        if(AdLoaded){
            finish();
        }else {

            if (!installed) {
                if (mInterstitialAd.isLoaded()) {
                   // AdLoaded=true;
                    mInterstitialAd.show();
                } else {
                    finish();
                }

            } else if (installed) {
                finish();
            } else {
                setContentView(R.layout.activity_pay);
//            finish();
            }

        }
    }

    public void sq(View v) {
        Intent we = new Intent(this, QuestionnaireActivity.class);
        startActivity(we);
        finish();
    }

    public void LoadHistory(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference GetHistoryData = database.getReference("Jinx/users/" + uid + "/ScoreData/");

        final LinearLayout LLHistroyView = (LinearLayout) findViewById(R.id.LLHistoryView);

        GetHistoryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //  String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Total Value is: ");

                LLHistroyView.removeAllViewsInLayout();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String Score = "x";
                    String Date = "x";
                    String result = "x";

                    Button ClearBT=(Button)findViewById(R.id.ClearHistoryButton);

                    if (postSnapshot.getKey() != null) {



                   Score= postSnapshot.child("score").getValue(String.class);
                   Date=postSnapshot.child("date").getValue(String.class);
                   result=postSnapshot.child("result").getValue(String.class);
                        final LinearLayout LL1 = new LinearLayout(MainActivity.this);
                        LL1.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        LL1.setOrientation(LinearLayout.VERTICAL);
                        LL1.setPadding(10, 10, 10, 10);

                        LLHistroyView.addView(LL1);

                        final TextView txDate = new TextView(MainActivity.this);
                        final TextView txScore = new TextView(MainActivity.this);
                        final TextView txResults = new TextView(MainActivity.this);




                            //Date
                            txDate.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            txDate.setTypeface(null, Typeface.BOLD);
                           // txDate.setTextSize(18);
                            txDate.setText(Date);  // Name
                            txDate.setVisibility(View.VISIBLE);
                            //txUserName.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            LL1.addView(txDate);


                            //Score
                            txScore.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            txScore.setText("Score: "+Score);
                           // txScore.setTypeface(null, Typeface.ITALIC);
                        //    txPaymentDesc.setText("Amount: "+PaymentAMT+"₹ \nTransaction id:"+TransactionTXD);   // msg

                            LL1.addView(txScore);


                            //Results
                        txResults.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        //txResults.setTextSize(10);
                        txResults.setText("Note: "+result);
                        LL1.addView(txResults);


                        ImageView imgLine = new ImageView(MainActivity.this);
                        Drawable DOCDrawableLine = getResources().getDrawable(R.color.BlackColor);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                        imgLine.setLayoutParams(layoutParams1);
                        imgLine.setImageDrawable(DOCDrawableLine);
                        LL1.addView(imgLine);


                        ClearBT.setVisibility(View.VISIBLE);
                    }else {

                        ClearBT.setVisibility(View.GONE);

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read Total value.", error.toException());
            }
        });
        GetHistoryData.keepSynced(true);
    }


    public void DeleteHistory(View v){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DeleteData = database.getReference("Jinx/users/" + uid + "/ScoreData/");
        DeleteData.removeValue();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //    Inflate the menu items for use in the action bar

        if (installed) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menupro, menu);

        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);

        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected

            case R.id.set:
                Intent ab = new Intent(this, AboutActivity.class);
                startActivity(ab);

                break;

            case R.id.up:
                Intent up = new Intent(this, PayActivity.class);
                startActivity(up);
                //  setContentView(R.layout.activity_pay);

                break;

            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "App to self diagnose depression. Download Jinx for FREE @ https://play.google.com/store/apps/details?id=devesh.ephrine.depression.self.diagnosis");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            default:
                break;
        }
        return true;


    }

    public void pay(View v) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=devesh.ephrine.depression.self.diagnosis.pro")); //Google play store
        startActivity(intent);

    }

    public void exit(View v) {
        finish();
    }

    public void bg(View v) {

    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null) {

            Toast.makeText(MainActivity.this, "Please Connect to Internet !!", Toast.LENGTH_LONG).show();
NetCheck="no";

        }else {
            NetCheck="y";
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
