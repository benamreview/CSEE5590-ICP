package com.csee5590.lab3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private String user_id;
    private ArrayList<String> items;
    private ListView listView;
    private ImageView mProfileImage;
    private String mProfileURL;
    private Uri resultURI;
    //Firebase Authentication Instances
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button logOutBtn = (Button) findViewById(R.id.button);
        Button editBtn = (Button) findViewById(R.id.btn_edit);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
//      Animation upon clicking/releasing click
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        view.setScaleY((float) 1.2);
                        view.setScaleX((float) 1.2);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        view.setScaleY((float) 1);
                        view.setScaleX((float) 1);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        logOutBtn.setOnTouchListener(new View.OnTouchListener() {
            private long start = 0;
            private long end = 0;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        //Start counting the pressing duration using start and end
                        start = System.currentTimeMillis();
                        view.setScaleY((float) 1.2);
                        view.setScaleX((float) 1.2);
                        view.setBackgroundColor(Color.BLUE);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        end = System.currentTimeMillis();
                        view.setScaleY((float) 1);
                        view.setScaleX((float) 1);
                        view.setBackgroundColor(Color.RED);
                        //If the button is held/pressed for more than 3 seconds, the screen will not log out to prevent loss of information.
                        //Holding a button for more than three seconds can mean that the user is hesitating leaving the site.
                        if (end - start <=3000){
                            LogOut(view);
                        }
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
//        Click listener for edit button
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditDetailsActivity.class);
                startActivity(intent);
            }
        });

        ///Retrieve User Info
        user_id = mAuth.getCurrentUser().getUid();
        getUserInfo(user_id);
        listView = (ListView) findViewById(R.id.listView);
        items = new ArrayList<String>();
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);

        //Profile Image
        mProfileImage = (ImageView) findViewById(R.id.profileImg);
        //Click a picture
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); //restricts selection
                startActivityForResult(intent, 1);
            }
        });
    }
//    Log out function
    public void LogOut(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT);
    }
//    Retrieve user information
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
//    This function takes in a map and display it as a listview
    public void displayUserInfo (Map<String, Object> usermap){
        ArrayList<String> items = new ArrayList<String>();
        String name = ((usermap == null) || (usermap.get("name") == null) ? "N/A" : usermap.get("name").toString());
        String age = ((usermap == null) || (usermap.get("age") == null) ? "N/A" : usermap.get("age").toString());
        String email = ((usermap == null) || (usermap.get("email") == null) ? "N/A" : usermap.get("email").toString());
        String phone = ((usermap == null) || (usermap.get("phone") == null) ? "N/A" : usermap.get("phone").toString());
        String university = ((usermap == null) || (usermap.get("university") == null) ? "N/A" : usermap.get("university").toString());
        String major = ((usermap == null) || (usermap.get("major") == null) ? "N/A" : usermap.get("major").toString());

        items.add("Name: " + name);
        items.add("Email: " + email);
        items.add("Age: " + age);
        items.add("Phone Number: " + phone);
        items.add("Institution/ Organization: " + university);
        items.add("Major: " + major);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
    }
//    The section below is for a profile picture, not tested thoroughly yet.
//    Profile Picture (Not tested)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //See if it returns the same code from the startactivityforresult above
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageURI = data.getData();
            resultURI = imageURI;
            mProfileImage.setImageURI(resultURI);
            applyChanges();
        }
    }
    //Apply changes for profile images on Firebase Storage
    //Note: need to enable storage first for permission issues.
    private void applyChanges(){

        if (resultURI != null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(user_id);
            Bitmap bitmap = null;
            //Add uri to bitmap
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); //compress images
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);
            //Upload task will save the image to Firebase Database
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileImageURL", uri.toString());
                            DocumentReference docRef = db.collection("users").document(user_id);
                            docRef.update("profileImageURL", uri.toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            });
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            finish();
                            return;
                        }
                    });
                }
            });
        }
        else {
            finish();
        }
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
