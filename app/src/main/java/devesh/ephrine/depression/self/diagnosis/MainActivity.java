package devesh.ephrine.depression.self.diagnosis;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public final static String option = "com.example.myfirstapp.MESSAGE";
    public InterstitialAd mInterstitialAd;
    public String uid;
    public View HomeView;
    public View HistoryView;

    public String NetCheck;

    public String Total;
    public int graph1 = 0;
    public int graph2 = 0;
    public int graph3 = 0;
    public int graph4 = 0;
    public int graph5 = 0;
    public int graph6 = 0;
    public int graph7 = 0;
    public int graph8 = 0;
    public int graph9 = 0;
    public int graph10 = 0;
    public int graph11 = 0;
    public int graph12 = 0;
    public int graph13 = 0;
    public int graph14 = 0;
    public int graph15 = 0;
    public int graph16 = 0;
    public int graph17 = 0;
    public int graph18 = 0;
    public int graph19 = 0;
    public int graph20 = 0;
    public int graph21 = 0;
    public int graph22 = 0;
    public int graph23 = 0;
    public int graph24 = 0;
    public int graph25 = 0;
    public int graph26 = 0;
    public int graph27 = 0;
    public boolean installed = false;
    int L = 2;
    int T;
    private AdView mAdView;


    public CallbackManager mCallbackManager;
    public String UserFname;
    public String UserLname;
    public String UserEmailID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeView = (View) findViewById(R.id.HomeView);
                    HomeView.setVisibility(View.VISIBLE);

                    HistoryView = (View) findViewById(R.id.HistoryView);
                    HistoryView.setVisibility(View.INVISIBLE);

                    return true;
                case R.id.navigation_dashboard:

                    HomeView = (View) findViewById(R.id.HomeView);
                    HomeView.setVisibility(View.INVISIBLE);

                    HistoryView = (View) findViewById(R.id.HistoryView);
                    HistoryView.setVisibility(View.VISIBLE);
                    GetTotal();
                    BlankGraph();
                    GetProfile();
                    if(NetCheck.equals("y")){
                        Toast.makeText(MainActivity.this, "Downloading Data... Please Wait", Toast.LENGTH_LONG).show();

                    }
                    FbAds();
                    isNetworkAvailable();
                    return true;

            }
            return false;
        }

    };
    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
isNetworkAvailable();
        isAppInstalled("devesh.ephrine.depression.self.diagnosis.pro");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            uid = user.getUid();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //-----------
        LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.home);
        l1.startAnimation(animation1);
        l2.startAnimation(animation1);

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
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                    finish();
                }
            });

            requestNewInterstitial();
        }

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
        FirebaseUser currentUser = mAuth.getCurrentUser();

        uid=currentUser.getUid();

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());
                        try {

                            String email = object.getString("email");
                            UserEmailID = email;

                            String UserGender = object.getString("gender");

                            String FBlink = object.getString("link");

                            String FBFname = object.getString("first_name");
                            UserFname = FBFname;

                            String FBLname = object.getString("last_name");
                            UserLname = FBLname;

                            //  FBDOB = object.getString("birthday"); // 01/31/1980 format

                            Log.d("FB Email", email);
                            Log.d("FB Gender", UserGender);
                            Log.d("FB link", FBlink);
                            Log.d("FB name", FBFname + " " + FBLname);

                            //  Log.d("FB DOB", FBDOB);
                            // emailid.setText(email);


                        } catch (JSONException e) {
                            Log.e("MYAPP", "unexpected JSON exception", e);
                            // Do something to recover ... or kill the app.
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,id,name,email,gender,link");
        request.setParameters(parameters);
        request.executeAsync();

        Profile profile = Profile.getCurrentProfile();
        System.out.println(profile.getFirstName());
        //System.out.println(profile.getId());
        Log.d("Jinx: FB id", profile.getId());

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        //First Name
        DatabaseReference Fname = database1.getReference("users/" + uid + "/FirstName");
        Fname.setValue(UserFname);

        //Last Name
        DatabaseReference Lname = database1.getReference("users/" + uid + "/LastName");
        Lname.setValue(UserLname);

        //Email ID
        DatabaseReference emailID = database1.getReference("users/" + uid + "/emailID");
        emailID.setValue(UserEmailID);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
  /*      user.updateEmail(UserEmailID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
*/

        DatabaseReference newuser = database1.getReference("users/" + uid + "/new");
        newuser.setValue("1");




    }
    @Override
    public void onStop() {
        super.onStop();
    //    if (mAuthListener != null) {
      //      mAuth.removeAuthStateListener(mAuthListener);
        //}
    }

    @Override
    public void onBackPressed() {

        if (!installed) {
            if (mInterstitialAd.isLoaded()) {
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

    public void sq(View v) {
        Intent we = new Intent(this, QuestionnaireActivity.class);
        startActivity(we);
        finish();
    }

    public void what(View v) {
        Intent intent = new Intent(this, WebActivity.class);
        String optionSel = "w";
        intent.putExtra(option, optionSel);
        startActivity(intent);
    }

    public void fight(View v) {
        Intent intent = new Intent(this, WebActivity.class);
        String optionSel = "f";
        intent.putExtra(option, optionSel);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //    Inflate the menu items for use in the action bar

        if (installed == true) {
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Friends!!! Check out a Awesome app to self diagnose depression. Download Jinx for FREE @ https://play.google.com/store/apps/details?id=devesh.ephrine.depression.self.diagnosis");
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

    public void GetTotal() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DataTotal = database.getReference("users/" + uid + "/data/total");

        // Read from the database
        DataTotal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    Total = value;
                    T = Integer.parseInt(Total.toString());

                    GetTotalArrange();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

    }

    public void GetTotalArrange() {
        int T = Integer.parseInt(Total.toString());
        int T1 = T - 1;
        int T2 = T - 2;
        int T3 = T - 3;
        int T4 = T - 4;
        int T5 = T - 5;
        int T6 = T - 6;
        int T7 = T - 7;
        int T8 = T - 8;
        int T9 = T - 9;
        int T10 = T - 10;
        int T11 = T - 11;
        int T12 = T - 12;
        int T13 = T - 13;
        int T14 = T - 14;
        int T15 = T - 15;
        int T16 = T - 16;
        int T17 = T - 17;
        int T18 = T - 18;
        int T19 = T - 19;
        int T20 = T - 20;
        int T21 = T - 21;
        int T22 = T - 22;
        int T23 = T - 23;
        int T24 = T - 24;
        int T25 = T - 25;
        int T26 = T - 26;
        int T27 = T - 27;

        String t1 = String.valueOf(T1);
        String t2 = String.valueOf(T2);
        String t3 = String.valueOf(T3);
        String t4 = String.valueOf(T4);
        String t5 = String.valueOf(T5);
        String t6 = String.valueOf(T6);
        String t7 = String.valueOf(T7);
        String t8 = String.valueOf(T8);
        String t9 = String.valueOf(T9);
        String t10 = String.valueOf(T10);
        String t11 = String.valueOf(T11);
        String t12 = String.valueOf(T12);
        String t13 = String.valueOf(T13);
        String t14 = String.valueOf(T14);
        String t15 = String.valueOf(T15);
        String t16 = String.valueOf(T16);
        String t17 = String.valueOf(T17);
        String t18 = String.valueOf(T18);
        String t19 = String.valueOf(T19);
        String t20 = String.valueOf(T20);
        String t21 = String.valueOf(T21);
        String t22 = String.valueOf(T22);
        String t23 = String.valueOf(T23);
        String t24 = String.valueOf(T24);
        String t25 = String.valueOf(T25);
        String t26 = String.valueOf(T26);
        String t27 = String.valueOf(T27);

        // Max Score No 27
        // 190/27 = 7.03703

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Value 1  -------------------------
        //Score
        DatabaseReference V1Score = database.getReference("users/" + uid + "/data/" + t1 + "/score");
        // Read from the database
        V1Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore12);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                    graph1 = Integer.parseInt(value.toString());
                    GraphDraw();
                }else {

                    View loading=(View)findViewById(R.id.loading);
                    loading.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V1DataDate = database.getReference("users/" + uid + "/data/" + t1 + "/date");
        V1DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewDate11);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V1DataResult = database.getReference("users/" + uid + "/data/" + t1 + "/result");
        V1DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus13);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        // Value 2  -------------------------
        //Score
        DatabaseReference V2Score = database.getReference("users/" + uid + "/data/" + t2 + "/score");
        // Read from the database
        V2Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewScore22);
                    Tx.setText(value);
                    graph2 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V2DataDate = database.getReference("users/" + uid + "/data/" + t2 + "/date");
        V2DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewDate21);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V2DataResult = database.getReference("users/" + uid + "/data/" + t2 + "/result");
        V2DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus23);
                    Tx.setText(value);
                    Tx.setSelected(true);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 3  -------------------------
        //Score
        DatabaseReference V3Score = database.getReference("users/" + uid + "/data/" + t3 + "/score");
        // Read from the database
        V3Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore32);
                    Tx.setText(value);
                    graph3 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V3DataDate = database.getReference("users/" + uid + "/data/" + t3 + "/date");
        V3DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {


                    TextView Tx = (TextView) findViewById(R.id.TextViewDate31);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V3DataResult = database.getReference("users/" + uid + "/data/" + t3 + "/result");
        V3DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value1 = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value1);

                if (value1 != null) {
                    TextView Tx1 = (TextView) findViewById(R.id.TextViewStatus33x);
                    Tx1.setText(value1);
                    Tx1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 4  -------------------------
        //Score
        DatabaseReference V4Score = database.getReference("users/" + uid + "/data/" + t4 + "/score");
        // Read from the database
        V4Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewScore42);
                    Tx.setText(value);
                    graph4 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V4DataDate = database.getReference("users/" + uid + "/data/" + t4 + "/date");
        V4DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate41);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V4DataResult = database.getReference("users/" + uid + "/data/" + t4 + "/result");
        V4DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus43);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        // Value 5  -------------------------
        //Score
        DatabaseReference V5Score = database.getReference("users/" + uid + "/data/" + t5 + "/score");
        // Read from the database
        V5Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore52);
                    Tx.setText(value);
                    graph5 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V5DataDate = database.getReference("users/" + uid + "/data/" + t5 + "/date");
        V5DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewDate51);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V5DataResult = database.getReference("users/" + uid + "/data/" + t5 + "/result");
        V5DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus53);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 6  -------------------------
        //Score
        DatabaseReference V6Score = database.getReference("users/" + uid + "/data/" + t6 + "/score");
        // Read from the database
        V6Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewScore62);
                    Tx.setText(value);
                    graph6 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V6DataDate = database.getReference("users/" + uid + "/data/" + t6 + "/date");
        V6DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate61);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V6DataResult = database.getReference("users/" + uid + "/data/" + t6 + "/result");
        V6DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus63);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 7  -------------------------
        //Score
        DatabaseReference V7Score = database.getReference("users/" + uid + "/data/" + t7 + "/score");
        // Read from the database
        V7Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewScore72);
                    Tx.setText(value);
                    graph7 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V7DataDate = database.getReference("users/" + uid + "/data/" + t7 + "/date");
        V7DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewDate71);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V7DataResult = database.getReference("users/" + uid + "/data/" + t7 + "/result");
        V7DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus73);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 8  -------------------------
        //Score
        DatabaseReference V8Score = database.getReference("users/" + uid + "/data/" + t8 + "/score");
        // Read from the database
        V8Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewScore82);
                    Tx.setText(value);
                    graph8 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V8DataDate = database.getReference("users/" + uid + "/data/" + t8 + "/date");
        V8DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewDate81);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V8DataResult = database.getReference("users/" + uid + "/data/" + t8 + "/result");
        V8DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus83);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 9  -------------------------
        //Score
        DatabaseReference V9Score = database.getReference("users/" + uid + "/data/" + t9 + "/score");
        // Read from the database
        V9Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewScore92);
                    Tx.setText(value);
                    graph9 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V9DataDate = database.getReference("users/" + uid + "/data/" + t9 + "/date");
        V9DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewDate91);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V9DataResult = database.getReference("users/" + uid + "/data/" + t9 + "/result");
        V9DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus93);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 10  -------------------------
        //Score
        DatabaseReference V10Score = database.getReference("users/" + uid + "/data/" + t10 + "/score");
        // Read from the database
        V10Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {
                    TextView Tx = (TextView) findViewById(R.id.TextViewScore10_2);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                    graph10 = Integer.parseInt(value.toString());
                    GraphDraw();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V10DataDate = database.getReference("users/" + uid + "/data/" + t10 + "/date");
        V10DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate10_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V10DataResult = database.getReference("users/" + uid + "/data/" + t10 + "/result");
        V10DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus10_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 11  -------------------------
        //Score
        DatabaseReference V11Score = database.getReference("users/" + uid + "/data/" + t11 + "/score");
        // Read from the database
        V11Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore11_2);
                    Tx.setText(value);
                    graph11 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V11DataDate = database.getReference("users/" + uid + "/data/" + t11 + "/date");
        V11DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate11_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V11DataResult = database.getReference("users/" + uid + "/data/" + t11 + "/result");
        V11DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus11_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        // Value 12  -------------------------
        //Score
        DatabaseReference V12Score = database.getReference("users/" + uid + "/data/" + t12 + "/score");
        // Read from the database
        V12Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore12_2);
                    Tx.setText(value);
                    graph12 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V12DataDate = database.getReference("users/" + uid + "/data/" + t12 + "/date");
        V12DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate12_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V12DataResult = database.getReference("users/" + uid + "/data/" + t12 + "/result");
        V12DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus12_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 13  -------------------------
        //Score
        DatabaseReference V13Score = database.getReference("users/" + uid + "/data/" + t13 + "/score");
        // Read from the database
        V13Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore13_2);
                    Tx.setText(value);
                    graph13 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V13DataDate = database.getReference("users/" + uid + "/data/" + t13 + "/date");
        V13DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate13_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V13DataResult = database.getReference("users/" + uid + "/data/" + t13 + "/result");
        V13DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus13_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 14  -------------------------
        //Score
        DatabaseReference V14Score = database.getReference("users/" + uid + "/data/" + t14 + "/score");
        // Read from the database
        V14Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore14_2);
                    Tx.setText(value);
                    graph14 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V14DataDate = database.getReference("users/" + uid + "/data/" + t14 + "/date");
        V14DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate14_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V14DataResult = database.getReference("users/" + uid + "/data/" + t14 + "/result");
        V14DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus14_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        // Value 15  -------------------------
        //Score
        DatabaseReference V15Score = database.getReference("users/" + uid + "/data/" + t15 + "/score");
        // Read from the database
        V15Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore15_2);
                    Tx.setText(value);
                    graph15 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V15DataDate = database.getReference("users/" + uid + "/data/" + t15 + "/date");
        V15DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate15_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V15DataResult = database.getReference("users/" + uid + "/data/" + t15 + "/result");
        V15DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus15_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 16  -------------------------
        //Score
        DatabaseReference V16Score = database.getReference("users/" + uid + "/data/" + t16 + "/score");
        // Read from the database
        V16Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore16_2);
                    Tx.setText(value);
                    graph16 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V16DataDate = database.getReference("users/" + uid + "/data/" + t16 + "/date");
        V16DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate16_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V16DataResult = database.getReference("users/" + uid + "/data/" + t16 + "/result");
        V16DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus16_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 17  -------------------------
        //Score
        DatabaseReference V17Score = database.getReference("users/" + uid + "/data/" + t17 + "/score");
        // Read from the database
        V17Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore17_2);
                    Tx.setText(value);
                    graph17 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V17DataDate = database.getReference("users/" + uid + "/data/" + t17 + "/date");
        V17DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate17_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V17DataResult = database.getReference("users/" + uid + "/data/" + t17 + "/result");
        V17DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus17_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        // Value 18  -------------------------
        //Score
        DatabaseReference V18Score = database.getReference("users/" + uid + "/data/" + t18 + "/score");
        // Read from the database
        V18Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore18_2);
                    Tx.setText(value);
                    graph18 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V18DataDate = database.getReference("users/" + uid + "/data/" + t18 + "/date");
        V18DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate18_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V18DataResult = database.getReference("users/" + uid + "/data/" + t18 + "/result");
        V18DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus18_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 19  -------------------------
        //Score
        DatabaseReference V19Score = database.getReference("users/" + uid + "/data/" + t19 + "/score");
        // Read from the database
        V19Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore19_2);
                    Tx.setText(value);
                    graph19 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V19DataDate = database.getReference("users/" + uid + "/data/" + t19 + "/date");
        V19DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate19_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V19DataResult = database.getReference("users/" + uid + "/data/" + t19 + "/result");
        V19DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus19_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 20  -------------------------
        //Score
        DatabaseReference V20Score = database.getReference("users/" + uid + "/data/" + t20 + "/score");
        // Read from the database
        V20Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore20_2);
                    Tx.setText(value);
                    graph20 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V20DataDate = database.getReference("users/" + uid + "/data/" + t20 + "/date");
        V20DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);

                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate20_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V20DataResult = database.getReference("users/" + uid + "/data/" + t20 + "/result");
        V20DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus20_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        // Value 21  -------------------------
        //Score
        DatabaseReference V21Score = database.getReference("users/" + uid + "/data/" + t21 + "/score");
        // Read from the database
        V21Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore21_2);
                    Tx.setText(value);
                    graph21 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V21DataDate = database.getReference("users/" + uid + "/data/" + t21 + "/date");
        V21DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate21_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V21DataResult = database.getReference("users/" + uid + "/data/" + t21 + "/result");
        V21DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus21_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 22  -------------------------
        //Score
        DatabaseReference V22Score = database.getReference("users/" + uid + "/data/" + t22 + "/score");
        // Read from the database
        V22Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore22_2);
                    Tx.setText(value);
                    graph22 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V22DataDate = database.getReference("users/" + uid + "/data/" + t22 + "/date");
        V22DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate22_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V22DataResult = database.getReference("users/" + uid + "/data/" + t22 + "/result");
        V22DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus22_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 23  -------------------------
        //Score
        DatabaseReference V23Score = database.getReference("users/" + uid + "/data/" + t23 + "/score");
        // Read from the database
        V23Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore23_2);
                    Tx.setText(value);
                    graph23 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V23DataDate = database.getReference("users/" + uid + "/data/" + t23 + "/date");
        V23DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate23_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V23DataResult = database.getReference("users/" + uid + "/data/" + t23 + "/result");
        V23DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus23_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 24  -------------------------
        //Score
        DatabaseReference V24Score = database.getReference("users/" + uid + "/data/" + t24 + "/score");
        // Read from the database
        V24Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore24_2);
                    Tx.setText(value);
                    graph24 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V24DataDate = database.getReference("users/" + uid + "/data/" + t24 + "/date");
        V24DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate24_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V24DataResult = database.getReference("users/" + uid + "/data/" + t24 + "/result");
        V24DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus24_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        // Value 25  -------------------------
        //Score
        DatabaseReference V25Score = database.getReference("users/" + uid + "/data/" + t25 + "/score");
        // Read from the database
        V25Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore25_2);
                    Tx.setText(value);
                    graph25 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V25DataDate = database.getReference("users/" + uid + "/data/" + t25 + "/date");
        V25DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate25_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V25DataResult = database.getReference("users/" + uid + "/data/" + t25 + "/result");
        V25DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus25_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 26  -------------------------
        //Score
        DatabaseReference V26Score = database.getReference("users/" + uid + "/data/" + t26 + "/score");
        // Read from the database
        V26Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore26_2);
                    Tx.setText(value);
                    graph26 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V26DataDate = database.getReference("users/" + uid + "/data/" + t26 + "/date");
        V26DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate26_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V26DataResult = database.getReference("users/" + uid + "/data/" + t26 + "/result");
        V26DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus26_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


        // Value 27  -------------------------
        //Score
        DatabaseReference V27Score = database.getReference("users/" + uid + "/data/" + t27 + "/score");
        // Read from the database
        V27Score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewScore27_2);
                    Tx.setText(value);
                    graph27 = Integer.parseInt(value.toString());
                    GraphDraw();
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add Date & time
        DatabaseReference V27DataDate = database.getReference("users/" + uid + "/data/" + t27 + "/date");
        V27DataDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewDate27_1);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //Add state
        DatabaseReference V27DataResult = database.getReference("users/" + uid + "/data/" + t27 + "/result");
        V27DataResult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Jinx", "Value is: " + value);
                if (value != null) {

                    TextView Tx = (TextView) findViewById(R.id.TextViewStatus27_3);
                    Tx.setText(value);
                    Tx.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });


    }

    public void GraphDraw() {

        if (L == T) {
            GraphView graph = (GraphView) findViewById(R.id.graph);

            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, graph1),
                    new DataPoint(1, graph2),
                    new DataPoint(2, graph3),
                    new DataPoint(3, graph4),
                    new DataPoint(4, graph5),
                    new DataPoint(5, graph6),
                    new DataPoint(6, graph7),
                    new DataPoint(7, graph8),
                    new DataPoint(8, graph9),
                    new DataPoint(9, graph10),
                    new DataPoint(10, graph11),
                    new DataPoint(11, graph12),
                    new DataPoint(12, graph13),
                    new DataPoint(13, graph14),
                    new DataPoint(14, graph15),
                    new DataPoint(15, graph16),
                    new DataPoint(16, graph17),
                    new DataPoint(17, graph18),
                    new DataPoint(18, graph19),
                    new DataPoint(19, graph20),
                    new DataPoint(20, graph21),
                    new DataPoint(21, graph22),
                    new DataPoint(22, graph23),
                    new DataPoint(23, graph24),
                    new DataPoint(24, graph25),
                    new DataPoint(25, graph26),
                    new DataPoint(26, graph27),
                    new DataPoint(27, 0),
                    new DataPoint(28, 0)


            });


            //   series.setThickness(10);
            // first series is a line
            DataPoint[] points = new DataPoint[100];
            for (int i = 0; i < points.length; i++) {
                points[i] = new DataPoint(i, Math.sin(i * 0.5) * 20 * (Math.random() * 10 + 1));
            }

            // set manual X bounds
            //   graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(28);

            //    graph.getViewport().setXAxisBoundsManual(true);
            //  graph.getViewport().setMinX(4);
            // graph.getViewport().setMaxX(80);

            // enable scaling and scrolling
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);

            graph.addSeries(series);
            // L = 0;
            Log.d("Jinx: L 1:", String.valueOf(L));

            View loading=(View)findViewById(R.id.loading);
            loading.setVisibility(View.GONE);

        } else {
            L = L + 1;
            Log.d("Jinx: L 2:", String.valueOf(L));


        }
    }

    public void BlankGraph() {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, graph1),
                new DataPoint(1, graph2),
                new DataPoint(2, graph3),
                new DataPoint(3, graph4),
                new DataPoint(4, graph5),
                new DataPoint(5, graph6),
                new DataPoint(6, graph7),
                new DataPoint(7, graph8),
                new DataPoint(8, graph9),
                new DataPoint(9, graph10),
                new DataPoint(10, graph11),
                new DataPoint(11, graph12),
                new DataPoint(12, graph13),
                new DataPoint(13, graph14),
                new DataPoint(14, graph15),
                new DataPoint(15, graph16),
                new DataPoint(16, graph17),
                new DataPoint(17, graph18),
                new DataPoint(18, graph19),
                new DataPoint(19, graph20),
                new DataPoint(20, graph21),
                new DataPoint(21, graph22),
                new DataPoint(22, graph23),
                new DataPoint(23, graph24),
                new DataPoint(24, graph25),
                new DataPoint(25, graph26),
                new DataPoint(26, graph27),
                new DataPoint(30, 0),
                new DataPoint(31, 0)


        });

        graph.addSeries(series);

    }

    public void GetProfile() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                //    String providerId = profile.getProviderId();

                // UID specific to the provider
                //    String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                uid = user.getUid();

                TextView TXname = (TextView) findViewById(R.id.textViewUserName);
                ImageView IMGprofile = (ImageView) findViewById(R.id.imageViewProfilePic);
                //   final TextView userID = (TextView) findViewById(R.id.textViewUSERID);

                Glide.with(this).load(photoUrl).into(IMGprofile);
                TXname.setText(name);

                Log.d("Jinx", "Email ID: " + email);
                Log.d("Jinx", "Name: " + name);
                Log.d("Jinx", "Name: " + name);


            }

            if(NetCheck.equals("no")){

            }else{
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {

                                    String email = object.getString("email");
                                    String UserGender = object.getString("gender");
                                    String FBlink = object.getString("link");
                                    //  FBDOB = object.getString("birthday"); // 01/31/1980 format

                                    Log.d("FB Email", email);
                                    Log.d("FB Gender", UserGender);
                                    Log.d("FB link", FBlink);
                                    //  Log.d("FB DOB", FBDOB);


                                } catch (JSONException e) {
                                    Log.e("MYAPP", "unexpected JSON exception", e);
                                    // Do something to recover ... or kill the app.
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,link");
                request.setParameters(parameters);
                request.executeAsync();

                Profile profile = Profile.getCurrentProfile();
                System.out.println(profile.getFirstName());
                //System.out.println(profile.getId());
                Log.d("NoteScape: FB id", profile.getId());

            }


        }
    }

    public void delete(View V) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference data = database.getReference("users/" + uid + "/data/");
        data.removeValue();

        DatabaseReference tot = database.getReference("users/" + uid + "/data/total");
        tot.setValue("1");

        finish();
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "Successfully Deleted Data", Toast.LENGTH_SHORT).show();

    }

    public void SignOut(View v) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        finish();
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

    public void FbAds() {


        if (installed) {
            LinearLayout LLAd = (LinearLayout) findViewById(R.id.LLAdview);
            LLAd.setVisibility(View.GONE);

        } else {
              MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));

            mAdView = (AdView) findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


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
