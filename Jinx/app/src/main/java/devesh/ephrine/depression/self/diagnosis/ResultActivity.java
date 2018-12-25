package devesh.ephrine.depression.self.diagnosis;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    public String uid;
    public String MState;

    public boolean installed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        isNetworkAvailable();
        isAppInstalled("devesh.ephrine.depression.self.diagnosis.pro");


///        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
   //     setSupportActionBar(toolbar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            uid = user.getUid();

        }

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        if (installed == true) {

        } else {
            MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }

        Intent intent = getIntent();
        String message = intent.getStringExtra(QuestionnaireActivity.results);
        TextView tx = (TextView) findViewById(R.id.ResultTextView);
        tx.setText(message);
        TextView mindstat = (TextView) findViewById(R.id.MindStatus);
        String mstat = "null";
        final String total = message;

        // total = Integer.valueOf(message.toString());
        //total = Integer.parseInt(message.toString());


        if (total.equals("5") || total.equals("6") || total.equals("7") || total.equals("8") || total.equals("9")) {
            mstat = "Mild Depression";
            MState = "Mild Depression";
            mindstat.setTextColor(Color.parseColor("#00a0d1"));
            mindstat.setText(mstat);
        } else if (total.equals("10") || total.equals("11") || total.equals("12") || total.equals("13") || total.equals("14")) {
            mstat = "Moderate Depression";
            MState = "Moderate Depression";

            mindstat.setTextColor(Color.parseColor("#a100ff"));

            mindstat.setText(mstat);

        } else if (total.equals("15") || total.equals("16") || total.equals("17") || total.equals("18") || total.equals("19")) {
            mstat = "Moderate to Severe Depression";
            MState = "Moderate to Severe Depression";

            mindstat.setTextColor(Color.parseColor("#ffb710"));

            mindstat.setText(mstat);
        } else if (total.equals("0") || total.equals("1") || total.equals("2") || total.equals("3") || total.equals("4")) {
            mstat = "Healthy Mindset and not depressed";
            MState = "Healthy Mindset and not depressed";
            mindstat.setTextColor(Color.parseColor("#0dc900"));

            mindstat.setText(mstat);
        } else {
            mindstat.setTextColor(Color.parseColor("#ff0000"));

            mstat = "Severe Depression";
            MState = "Severe Depression";

            mindstat.setText(mstat);
        }


        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int date = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        int AMPMint =c.get(Calendar.AM_PM);
        String AMPM="AM";

        if(hour==0){
            hour=12;
        }

        if(AMPMint==1){
            AMPM="PM";

        }

        if(min<=9){
            String o="0"+String.valueOf(min);
            min=Integer.valueOf(o);
        }



        final String Hour = String.valueOf(hour);
        final String Min = String.valueOf(min);
        final String Date = String.valueOf(date);
        final String Month = String.valueOf(month + 1);
        final String Year = String.valueOf(year);




        FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        //Score
        DatabaseReference DataScore = database1.getReference("Jinx/users/" + uid + "/ScoreData/").push();
        DataScore.child("score").setValue(total);
        DataScore.child("date").setValue(Date + "/" + Month + "/" + Year + ", " + Hour + ":" + Min+" "+AMPM);
        DataScore.child("result").setValue(MState);

        //Add Date & time
       // DatabaseReference DataDate = database1.getReference("users/" + uid + "/data/" + tot + "/");


        //Add state
        //DatabaseReference DataResult = database1.getReference("users/" + uid + "/data/" + tot + "/");



  /*      // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DataTotal = database.getReference("users/" + uid + "/data/total");
        // Read from the database
        DataTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String tot = dataSnapshot.getValue(String.class);
                Log.d("Jinx ", "Value is: " + tot);

                if(tot==null || tot.equals(null) || tot.equals("null") || tot=="null"){
                    tot="1";
                }
                int NTotal = Integer.parseInt(tot.toString());
                int NwTotal = NTotal + 1;
                String NewTotal = String.valueOf(NwTotal);

                //Add total
                DatabaseReference DataTotal = database1.getReference("users/" + uid + "/data/total");
                DataTotal.setValue(NewTotal);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Jinx", "Failed to read value.", error.toException());
            }
        });

        //  myRef.setValue("Hello, World!");

*/

    }

    @Override
    public void onBackPressed() {

        Intent we = new Intent(this, MainActivity.class);
        startActivity(we);
        finish();
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

            Toast.makeText(ResultActivity.this, "Sorry!! We are unable to Save your Score because you are not connected to Internet", Toast.LENGTH_LONG).show();


        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }
}
