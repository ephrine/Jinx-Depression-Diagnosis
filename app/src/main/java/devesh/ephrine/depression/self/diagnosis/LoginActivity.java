package devesh.ephrine.depression.self.diagnosis;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    public CallbackManager mCallbackManager;
    public String uid;
    public String UserFname;
    public String UserLname;
    public String UserEmailID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {


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
        mCallbackManager = CallbackManager.Factory.create();
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


    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Kinx FB", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Kinx FB", "signInWithCredential:success");


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
                                                accountinfo();

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


                            //     updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Kinx FB", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

}
