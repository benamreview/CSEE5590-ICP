package com.csee5590.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

//AppCompatActivity:
public class SignUpActivity extends AppCompatActivity {
    TextView errorText;
    private FirebaseAuth mAuth;
    private EditText mEmail, mPassword, mName, mUniversity, mAge, mPhone, mMajor;
    private Button mSignUp;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String user_id;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RunAnimation();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //get the information of the current user
                if (user != null){
                    //if not null, move to another activity, to be created later.
                    //Remember the current context!
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra("user_id", user.getUid());
                    intent.putExtra("user_email", user.getEmail());
                    startActivity(intent);
                    finish();
                }
            }
        };
        mSignUp = (Button)findViewById(R.id.btn_signup);
        mEmail = (EditText)findViewById(R.id.txt_uname);
        mPassword = (EditText) findViewById(R.id.txt_Pwd);
        mName = (EditText) findViewById(R.id.txt_name);
        mPhone = (EditText) findViewById(R.id.txt_phone);
        mUniversity = (EditText) findViewById(R.id.txt_university);
        mAge = (EditText) findViewById(R.id.txt_age);
        mMajor = (EditText) findViewById(R.id.txt_major);
        errorText = (TextView)findViewById(R.id.lbl_Error);

        /**
         * This function will make sure that the error message disappears
         * before user retypes the username/password
         */
        mEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mEmail.requestFocus();
                        errorText.setVisibility(View.GONE);
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        mPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPassword.requestFocus();
                        errorText.setVisibility(View.GONE);
                        // PRESSED
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //This is where the identity check is. The main variable is the validation flag. If it is false, then the user
    //will not go anywhere beyond the login page. Else if the user enters the right username and password, he will be directed
    //to the next page. Username and password are determined by the 2 variables "userName" and "password"
    public void checkCredentials(View v)
    {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        final String age = mAge.getText().toString();
        final String phone = mPhone.getText().toString();
        final String university = mUniversity.getText().toString();
        final String name = mName.getText().toString();
        final String major = mMajor.getText().toString();
        if(email.isEmpty()){
            mEmail.setError("Email Address cannot be blank");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Enter a Valid Email");
            mEmail.requestFocus();
        }
        else if(password.isEmpty()){
            mPassword.setError("Password cannot be blank");
            mPassword.requestFocus();
        }
        else if(password.length()<6 || password.length()>12){
            mPassword.setError("Password should be of minimum 6 characters and maximum 12 characters");
            mPassword.requestFocus();
        }
        else if(name.isEmpty()){
            mName.setError("Name cannot be blank");
            mName.requestFocus();
        }
        else if(age.isEmpty()){
            mAge.setError("Age cannot be blank");
            mAge.requestFocus();
        }
        else if(university.isEmpty()){
            mUniversity.setError("University cannot be blank");
            mUniversity.requestFocus();
        }
        else if(phone.isEmpty()){
            mPhone.setError("Phone cannot be blank");
            mPhone.requestFocus();
        }
        else if(major.isEmpty()){
            mMajor.setError("Major cannot be blank");
            mMajor.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //If user has already signed up, therefore the createUserWithEmailandPassword would fail.
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                    //If the user email cannot be found in the database, reference the database and add variables to it.
                    else {
                        user_id = mAuth.getCurrentUser().getUid(); //id assigned to Technician at moment of sign-up

                        ///Using FireStore
                        DocumentReference docRef = db.collection("users").document(user_id);
                        // Add document data  with string user_id using a hashmap
                        Map<String, Object> data = new HashMap<>();
                        data.put("email", email);
                        data.put("name", name);
                        data.put("phone", phone);
                        data.put("age", age);
                        data.put("university", university);
                        data.put("major", major);
                        //asynchronously write data
                        docRef.set(data);
                    }
                }
            });
        }
    }

    /**
     * this function animates the Login title by continuously scaling it back and forth
     */
    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.lbl_Header);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
