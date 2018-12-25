package devesh.ephrine.depression.self.diagnosis;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import androidx.annotation.NonNull;

public class LoginActivity extends Activity {

    public String uid;
    public String UserFname;
    public String UserLname;
    public String UserEmailID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//printKeyHash();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

             //       Intent i = new Intent(getBaseContext(), MainActivity.class);
               //     startActivity(i);
                    // Remove activity
                 //   finish();
                    // User is signed in
                    Log.d("Jinx", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Jinx", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        // Initialize Facebook Login button
  /*      mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Jinx FB", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

                ImageView img = (ImageView) findViewById(R.id.imageViewProgress);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar3);
                // img.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                Log.d("Jinx FB", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Jinx FB", "facebook:onError", error);
                // ...
            }
        });
*/

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                //startActivity(SignedInActivity.createIntent(this, response));
                //finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                  //  showSnackbar(R.string.sign_in_cancelled);

                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //showSnackbar(R.string.no_internet_connection);
                    return;
                }

                //showSnackbar(R.string.unknown_error);
                Log.e("Ep", "Sign-in error: ", response.getError());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void GetUserInfo() {

    }

    public void accountinfo() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            uid = user.getUid();
        }

// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + uid + "/new");
// Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value == null || value.equals(null)) {
                    Log.d("", "Value is: null  " + value);
                    //  String UserUID="users/" + uid + "";
                    Createprofile();

                } else if (value != null) {
                    Log.d("", "Value is: !null " + value);
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                    // Remove activity
                    finish();
                }
                Log.d("", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public void Createprofile() {
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
        user.updateEmail(UserEmailID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });

        // Blank Data
        DatabaseReference data = database1.getReference("users/" + uid + "/data/total");
        data.setValue("1");

        // Blank Data
        DatabaseReference newuser = database1.getReference("users/" + uid + "/new");
        newuser.setValue("1");

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        // Remove activity
        finish();

    }

    public void privacy(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://ephrine.blogspot.com/p/privacy-policy.html")); //Google play store
        startActivity(intent);
    }

    public void GoogleLoginButton(View v){

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()
                           //     new AuthUI.IdpConfig.FacebookBuilder().build(),
                           //     new AuthUI.IdpConfig.TwitterBuilder().build(),
                            //    new AuthUI.IdpConfig.GitHubBuilder().build(),
                             //   new AuthUI.IdpConfig.EmailBuilder().build(),
                             //   new AuthUI.IdpConfig.PhoneBuilder().build(),
                               // new AuthUI.IdpConfig.AnonymousBuilder().build()
                                        ))
                        .build(),
                RC_SIGN_IN);
    }

    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "devesh.ephrine.depression.self.diagnosis",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("--------------------", "----------");
                Log.d("KeyHash FB 1:---", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("--------------------", "----------");

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash FB 2:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash FB 3:", e.toString());
        }
    }

}
