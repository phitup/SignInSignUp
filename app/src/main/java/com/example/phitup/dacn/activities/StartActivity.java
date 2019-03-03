package com.example.phitup.dacn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phitup.dacn.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class StartActivity extends Activity {

    Button btnSignup, btnPhone;
    TextView txtRegister;
    FirebaseUser user;
    FirebaseAuth mAuth;

    LoginButton loginButton;
    CallbackManager callbackManager;
    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);

        Configuration config = getResources().getConfiguration();
        if(config.smallestScreenWidthDp >= 400){
            setContentView(R.layout.activity_start);
        }else{
            setContentView(R.layout.start_small_size);
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AnhXa();

        if(user != null){
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        }

        findViewById(R.id.textviewStartTerms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookLogin();
            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, SignInWithPhoneActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void FacebookLogin() {
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        if(loginButton != null){
            buttonClickLoginFB();
        }

    }

    private void buttonClickLoginFB() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(StartActivity.this, "User cancelled it", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(StartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookToken(final AccessToken accessToken) {
        mProgress.setMessage("Please Wait");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, this);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            mProgress.dismiss();
                            try{
                                mAuthListener = new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user != null){
                                            updateUI(user);
                                        }
                                    }
                                };
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            mAuth.addAuthStateListener(mAuthListener);

                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields","email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }else{
                    mProgress.hide();
                    Toast.makeText(StartActivity.this, "Could not register to firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(StartActivity.this, MainActivity.class) ;
        intent.putExtra("user", user.getUid());



        if (user != null){
            user.updateEmail("SADSadsad");
            Log.d("BBB", "update email");
        }else {
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }
        Log.d("BBB","Uid " + user.getUid());
        Log.d("BBB", "Display Name " + user.getDisplayName());
        Log.d("BBB", "email " + user.getEmail());
        Log.d("BBB", "Phone number " + user.getPhoneNumber());
        Log.d("BBB", "Photo " + user.getPhotoUrl().toString());
        Log.d("BBB", "ProviderId " + user.getProviderId());

    }

    private void AnhXa() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnSignup = findViewById(R.id.buttonStartSignUp);
        txtRegister = findViewById(R.id.textviewStartRegister);
        loginButton = findViewById(R.id.login_button);
        btnPhone = findViewById(R.id.buttonStartPhoneNumber);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
    }

    private void ShowDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_start_terms);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_terms);
        dialog.show();

    }

}
