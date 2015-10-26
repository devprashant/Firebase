package com.example.sony.firebase;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.SyncTree;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://luminous-inferno-9046.firebaseio.com/");
        myFirebaseRef.child("message").setValue("Do you have data? You'll love firebase");
        final TextView txtView = (TextView) findViewById(R.id.txtView);
        setContentView(R.layout.activity_main);
        myFirebaseRef.child("message")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.getValue());
                        //txtView.setText((dataSnapshot.getValue()).toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        Firebase alanRef = myFirebaseRef.child("users").child("alanisawesome");

        user alan = new user("Alan Turing", 1912);
        //alanRef.setValue(alan);

        Map<String, Object> nickname = new HashMap<>();
        nickname.put("nickname", "Alan The Machine");
        alanRef.updateChildren(nickname, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                    //txtView.setText("Data saved");
                }
            }
        });

        //Adding posts
        Firebase postRef = myFirebaseRef.child("posts");
        Firebase newPostRef = postRef.push();

        Map<String, String> post1 = new HashMap<>();
        post1.put("author", "gracehop");
        post1.put("title", "annoucing COBOL, a New Programming Language");
        newPostRef.setValue(post1);
        String postId = newPostRef.getKey();
            System.out.println("Post ID" + postId);
        Map<String, String> post2 = new HashMap<>();
        post2.put("author", "alanisawesome");
        post2.put("title", "The Turing Machine");
        postRef.push().setValue(post2);

        //Reading posts
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BlogPost post = postSnapshot.getValue(BlogPost.class);
                    System.out.println(post.getAuthor() + " - " + post.getTitle());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                BlogPost newPost = dataSnapshot.getValue(BlogPost.class);
                System.out.println("Author: " + newPost.getAuthor());
                System.out.println("Title: " + newPost.getTitle());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                String title = (String) dataSnapshot.child("title").getValue();
                System.out.println("The updates post title is: " + title);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();
                System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        myFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {

                } else {

                }
            }
        });

        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler(){

            @Override
            public void onAuthenticated(AuthData authData) {

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                        System.out.println("Error occured in auth" + firebaseError);
            }
        };

        myFirebaseRef.authWithOAuthToken("google","ekcauwwVcpB57ERCrYo0UFnq", authResultHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
