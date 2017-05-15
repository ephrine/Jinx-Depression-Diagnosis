package devesh.ephrine.depression.self.diagnosis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Button exit = (Button) findViewById(R.id.button5);
        exit.setVisibility(View.GONE);
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

}

