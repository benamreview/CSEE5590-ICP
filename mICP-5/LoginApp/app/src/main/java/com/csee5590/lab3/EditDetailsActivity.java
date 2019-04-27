package com.csee5590.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//AppCompatActivity:
public class EditDetailsActivity extends AppCompatActivity {
    private static final String TAG = "EditDetailsActivity";
    TextView errorText;
    private FirebaseAuth mAuth;
    private EditText mEmail, mName, mUniversity, mAge, mPhone, mMajor;
    private Button mApply;
    String user_id;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RunAnimation();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mApply = (Button)findViewById(R.id.btn_apply);
        mEmail = (EditText)findViewById(R.id.txt_uname);
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
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges(v);
            }
        });
        user_id = mAuth.getCurrentUser().getUid();
        getUserInfo(user_id);
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
    public void applyChanges(View v)
    {
        final String email = mEmail.getText().toString();
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
                docRef.update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Personal Details Updated Successfully!");
                                Intent intent = new Intent(EditDetailsActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error Updating Details! Please Try Again Later!", e);
                            }
                        });

        }
    }
    public void getUserInfo(final String ID) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(ID)) {

                                    Map<String, Object> user;
                                    user = document.getData();
                                    displayUserInfo(user);

                                }
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void displayUserInfo (Map<String, Object> usermap){
//        ArrayList<String> items = new ArrayList<String>();
//        items.add("Name: " + usermap.get("name"));
//        items.add("Email: " + usermap.get("email"));
//        items.add("Age: " + usermap.get("age"));
//        items.add("Phone Number: " + usermap.get("phone"));
//        items.add("Institution/ Organization: " + usermap.get("university"));
//        items.add("Major: " + usermap.get("major"));
//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//        listView.setAdapter(itemsAdapter);
        String name = ((usermap == null) || (usermap.get("name") == null) ? "N/A" : usermap.get("name").toString());
        String age = ((usermap == null) || (usermap.get("age") == null) ? "N/A" : usermap.get("age").toString());
        String email = ((usermap == null) || (usermap.get("email") == null) ? "N/A" : usermap.get("email").toString());
        String phone = ((usermap == null) || (usermap.get("phone") == null) ? "N/A" : usermap.get("phone").toString());
        String university = ((usermap == null) || (usermap.get("university") == null) ? "N/A" : usermap.get("university").toString());
        String major = ((usermap == null) || (usermap.get("major") == null) ? "N/A" : usermap.get("major").toString());
        mEmail.setText(email);
        mName.setText(name);
        mAge.setText(age);
        mUniversity.setText(university);
        mPhone.setText(phone);
        mMajor.setText(major);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
