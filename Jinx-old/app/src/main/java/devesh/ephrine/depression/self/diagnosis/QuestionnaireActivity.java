package devesh.ephrine.depression.self.diagnosis;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class QuestionnaireActivity extends AppCompatActivity {
    // public String c1t="0";
    // static int c2t;
    //  static int c3t;

    public final static String results = "com.example.myfirstapp.MESSAGE";
    public final static String totalscore = "com.example.myfirstapp.MESSAGE";
    public boolean installed = false;
    int c2;
    int c3;
    int c4;
    int total;
    String q;
    String Q1 = "1. Little interest or pleasure in doing things";
    String Q2 = "2. Feeling down, depressed or hopeless";
    String Q3 = "3. Trouble falling or staying asleep, or sleeping too much";
    String Q4 = "4. Feeling tired or having little energy";
    String Q5 = "5. Poor appetite or Over eating";
    String Q6 = "6. Feeling bad about yourself or that you are a failure or have let yourself or your family down";
    String Q7 = "7. Trouble concentrating on thing, such as reading the newspaper or watching television";
    String Q8 = "8. Moving or speaking so slowly that other people could have noticed? or the opposite - being so fidgety or restless that you have been moving around a lot more than usual?";
    String Q9 = "9. Thoughts that you would be better off dead or of hurting yourself in some way?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phq9questionnaire);
        isNetworkAvailable();
        isAppInstalled("devesh.ephrine.depression.self.diagnosis.pro");

        q = "1";
        TextView question = (TextView) findViewById(R.id.QuestionTextView);

        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
        TextView Qpg = (TextView) findViewById(R.id.QtextView16);
        if (q.equals(null) || q.equals("1")) {
            question.setText(Q1);
            p.setProgress(10);

        }

        c2 = 0;
        c3 = 0;
        c4 = 0;

        if (installed == true) {

        } else {
            MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }

    public void a1(View v) {
        TextView question = (TextView) findViewById(R.id.QuestionTextView);
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
        TextView pgno = (TextView) findViewById(R.id.QtextView16);

        LinearLayout ll1 = (LinearLayout) findViewById(R.id.LinearViewQuestion);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        ll1.startAnimation(animation1);


        if (q.equals("1")) {
            q = "2";
            question.setText(Q2);
            p.setProgress(20);
            pgno.setText(q + "/9");


        } else if (q.equals("2")) {
            q = "3";
            question.setText(Q3);
            p.setProgress(30);
            pgno.setText(q + "/9");


        } else if (q.equals("3")) {
            q = "4";
            question.setText(Q4);
            p.setProgress(40);
            pgno.setText(q + "/9");


        } else if (q.equals("4")) {
            q = "5";
            question.setText(Q5);
            p.setProgress(50);
            pgno.setText(q + "/9");


        } else if (q.equals("5")) {
            q = "6";
            question.setText(Q6);
            p.setProgress(60);
            pgno.setText(q + "/9");


        } else if (q.equals("6")) {
            q = "7";
            question.setText(Q7);
            p.setProgress(70);
            pgno.setText(q + "/9");


        } else if (q.equals("7")) {
            q = "8";
            question.setText(Q8);
            p.setProgress(80);
            pgno.setText(q + "/9");


        } else if (q.equals("8")) {
            q = "9";
            question.setText(Q9);
            p.setProgress(90);
            pgno.setText(q + "/9");


        } else if (q.equals("9")) {
            p.setProgress(100);
            pgno.setText(q + "/9");

            total = c2 + c3 + c4;
            String t = String.valueOf(total);
            Intent intent = new Intent(this, ResultActivity.class);


            String totalphq9 = t;
            //  String resultof = "phq9";
            //   intent.putExtra(results, resultof);
            intent.putExtra(totalscore, totalphq9);

            startActivity(intent);

            finish();
        }
    }


    public void a2(View v) {
        TextView question = (TextView) findViewById(R.id.QuestionTextView);
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
        TextView pgno = (TextView) findViewById(R.id.QtextView16);

        LinearLayout ll1 = (LinearLayout) findViewById(R.id.LinearViewQuestion);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        ll1.startAnimation(animation1);


        if (q.equals("1")) {
            q = "2";
            question.setText(Q2);
            p.setProgress(20);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("2")) {
            q = "3";
            question.setText(Q3);
            p.setProgress(30);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("3")) {
            q = "4";
            question.setText(Q4);
            p.setProgress(40);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("4")) {
            q = "5";
            question.setText(Q5);
            p.setProgress(50);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("5")) {
            q = "6";
            question.setText(Q6);
            p.setProgress(60);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("6")) {
            q = "7";
            question.setText(Q7);
            p.setProgress(70);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("7")) {
            q = "8";
            question.setText(Q8);
            p.setProgress(80);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("8")) {
            q = "9";
            question.setText(Q9);
            p.setProgress(90);
            pgno.setText(q + "/9");
            c2 = c2 + 1;

        } else if (q.equals("9")) {
            p.setProgress(100);
            c2 = c2 + 1;

            total = c2 + c3 + c4;
            String t = String.valueOf(total);
            Intent intent = new Intent(this, ResultActivity.class);


            String totalphq9 = t;
            //  String resultof = "phq9";
            //   intent.putExtra(results, resultof);
            intent.putExtra(totalscore, totalphq9);

            startActivity(intent);

            finish();

        }

    }

    public void a3(View v) {

        TextView question = (TextView) findViewById(R.id.QuestionTextView);
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
        TextView pgno = (TextView) findViewById(R.id.QtextView16);

        LinearLayout ll1 = (LinearLayout) findViewById(R.id.LinearViewQuestion);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        ll1.startAnimation(animation1);

        if (q.equals("1")) {
            q = "2";
            question.setText(Q2);
            p.setProgress(20);
            pgno.setText(q + "/9");
            c3 = c3 + 2;

        } else if (q.equals("2")) {
            q = "3";
            question.setText(Q3);
            p.setProgress(30);
            pgno.setText(q + "/9");
            c3 = c3 + 2;

        } else if (q.equals("3")) {
            q = "4";
            question.setText(Q4);
            p.setProgress(40);
            pgno.setText(q + "/9");
            c3 = c3 + 2;
        } else if (q.equals("4")) {
            q = "5";
            question.setText(Q5);
            p.setProgress(50);
            pgno.setText(q + "/9");
            c3 = c3 + 2;
        } else if (q.equals("5")) {
            q = "6";
            question.setText(Q6);
            p.setProgress(60);
            pgno.setText(q + "/9");
            c3 = c3 + 2;
        } else if (q.equals("6")) {
            q = "7";
            question.setText(Q7);
            p.setProgress(70);
            pgno.setText(q + "/9");
            c3 = c3 + 2;
        } else if (q.equals("7")) {
            q = "8";
            question.setText(Q8);
            p.setProgress(80);
            pgno.setText(q + "/9");
            c3 = c3 + 2;
        } else if (q.equals("8")) {
            q = "9";
            question.setText(Q9);
            p.setProgress(90);
            pgno.setText(q + "/9");
            c3 = c3 + 2;
        } else if (q.equals("9")) {
            p.setProgress(100);
            c3 = c3 + 2;
            total = c2 + c3 + c4;
            String t = String.valueOf(total);
            Intent intent = new Intent(this, ResultActivity.class);


            String totalphq9 = t;
            //  String resultof = "phq9";
            //   intent.putExtra(results, resultof);
            intent.putExtra(totalscore, totalphq9);

            startActivity(intent);

            finish();
        }
    }

    public void a4(View v) {
        TextView question = (TextView) findViewById(R.id.QuestionTextView);
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
        TextView pgno = (TextView) findViewById(R.id.QtextView16);

        LinearLayout ll1 = (LinearLayout) findViewById(R.id.LinearViewQuestion);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        ll1.startAnimation(animation1);

        if (q.equals("1")) {
            q = "2";
            question.setText(Q2);
            p.setProgress(20);
            pgno.setText(q + "/9");
            c4 = c4 + 3;

        } else if (q.equals("2")) {
            q = "3";
            question.setText(Q3);
            p.setProgress(30);
            pgno.setText(q + "/9");
            c4 = c4 + 3;
        } else if (q.equals("3")) {
            q = "4";
            question.setText(Q4);
            p.setProgress(40);
            pgno.setText(q + "/9");
            c4 = c4 + 3;

        } else if (q.equals("4")) {
            q = "5";
            question.setText(Q5);
            p.setProgress(50);
            pgno.setText(q + "/9");
            c4 = c4 + 3;

        } else if (q.equals("5")) {
            q = "6";
            question.setText(Q6);
            p.setProgress(60);
            pgno.setText(q + "/9");
            c4 = c4 + 3;

        } else if (q.equals("6")) {
            q = "7";
            question.setText(Q7);
            p.setProgress(70);
            pgno.setText(q + "/9");
            c4 = c4 + 3;

        } else if (q.equals("7")) {
            q = "8";
            question.setText(Q8);
            p.setProgress(80);
            pgno.setText(q + "/9");
            c4 = c4 + 3;

        } else if (q.equals("8")) {
            q = "9";
            question.setText(Q9);
            p.setProgress(90);
            pgno.setText(q + "/9");
            c4 = c4 + 3;

        } else if (q.equals("9")) {
            p.setProgress(100);
            pgno.setText(q + "/9");

            c4 = c4 + 3;

            total = c2 + c3 + c4;
            String t = String.valueOf(total);
            Intent intent = new Intent(this, ResultActivity.class);


            String totalphq9 = t;
            //  String resultof = "phq9";
            //   intent.putExtra(results, resultof);
            intent.putExtra(totalscore, totalphq9);

            startActivity(intent);
            finish();


        }
    }


    // Question 1 buttons
    /*
public void q1c1(View v){
    LinearLayout ll1 =(LinearLayout)findViewById(R.id.ll1);

      //ImageView image = (ImageView)findViewById(R.id.imageView);
     Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
     ll1.startAnimation(animation1);


    }
    public void q1c2(View v){

        c2t=c2t+1;
    }
    public void q1c3(View v){
      c3t=c3t+2;


    }
    public void q1c4(View v){
        c4t=c4t+3;
    }

    // Question 2 buttons
    public void q2c1(View v){

    }
    public void q2c2(View v){
        c2t=c2t+1;
    }
    public void q2c3(View v){
       c3t=c3t+2;
    }
    public void q2c4(View v){
        c4t=c4t+3;
    }

    // Question 3 buttons
    public void q3c1(View v){

    }
    public void q3c2(View v){
        c2t=c2t+1;
    }
    public void q3c3(View v){
        c3t=c3t+2;
    }
    public void q3c4(View v){
        c4t=c4t+3;
    }


    // Question 4 buttons
    public void q4c1(View v){

    }
    public void q4c2(View v){
        c2t=c2t+1;
    }
    public void q4c3(View v){
        c3t=c3t+2;
    }
    public void q4c4(View v){
        c4t=c4t+3;
    }


    // Question 5 buttons
    public void q5c1(View v){

    }
    public void q5c2(View v){
        c2t=c2t+1;
    }
    public void q5c3(View v){
        c3t=c3t+2;
    }
    public void q5c4(View v){
        c4t=c4t+3;
    }

    // Question 6 buttons
    public void q6c1(View v){

    }
    public void q6c2(View v){
        c2t=c2t+1;
    }
    public void q6c3(View v){
        c3t=c3t+2;
    }
    public void q6c4(View v){
        c4t=c4t+3;
    }

    // Question 7 buttons
    public void q7c1(View v){

    }
    public void q7c2(View v){
        c2t=c2t+1;
    }
    public void q7c3(View v){
        c3t=c3t+2;
    }
    public void q7c4(View v){
        c4t=c4t+3;
    }


    // Question 8 buttons
    public void q8c1(View v){

    }
    public void q8c2(View v){
        c2t=c2t+1;
    }
    public void q8c3(View v){
        c3t=c3t+2;
    }
    public void q8c4(View v){
        c4t=c4t+3;
    }


    // Question 9 buttons
    public void q9c1(View v){

    }
    public void q9c2(View v){
        c2t=c2t+1;
    }
    public void q9c3(View v){
        c3t=c3t+2;
    }
    public void q9c4(View v){
        c4t=c4t+3;
    }


    String stat;

public void submit(View v){
    TextView tx= (TextView)findViewById(R.id.textView2);
    double total=c2t+c3t+c4t;
String t=String.valueOf(total);

    if(total<9 && total>4){
        stat="Mild"+t;
    }else if(total<14 && total >8){
        stat="mod"+t;
    }else if(total<19 && total >14){
        stat="mod to sever"+t;
    }else if(total>21){
        stat="severe"+t;
    }else if(total <6){
        stat="none"+t;
    }
    tx.setText(stat);

}
    */


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

            Toast.makeText(QuestionnaireActivity.this, "Please Connect to Internet, Otherwise we will unable to Save your Score", Toast.LENGTH_LONG).show();


        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }
}
