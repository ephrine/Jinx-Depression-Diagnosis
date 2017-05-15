package devesh.ephrine.depression.self.diagnosis;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    public final static String option = "com.example.myfirstapp.MESSAGE";
    public boolean installed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        isAppInstalled("devesh.ephrine.depression.self.diagnosis.pro");

        //ImageView jinxlogo =(ImageView)findViewById(R.id.imageView8);
        //TextView jinxname =(TextView)findViewById(R.id.imageView6);

        RelativeLayout rr = (RelativeLayout) findViewById(R.id.rr);
        Animation j = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);

        rr.startAnimation(j);
        //jinxlogo.startAnimation(j);

        if (installed == true) {
            Button Bt = (Button) findViewById(R.id.button3);
            Bt.setVisibility(View.GONE);
            TextView tx = (TextView) findViewById(R.id.textView33);
            tx.setVisibility(View.VISIBLE);
        }

    }

    public void pay(View v) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=devesh.ephrine.depression.self.diagnosis.pro")); //Google play store
        startActivity(intent);

    }

    public void contact(View v) {

        Intent intent = new Intent(this, WebActivity.class);
        String optionSel = "c";
        intent.putExtra(option, optionSel);
        startActivity(intent);

    }

    public void website(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://ephrine.blogspot.com")); //Google play store
        startActivity(intent);
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

}
