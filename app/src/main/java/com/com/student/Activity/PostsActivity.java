package com.com.student.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.com.student.Adapter.PostAdapter;
import com.com.student.Home.MainActivity;
import com.com.student.Model.ModelSaveData;
import com.com.student.Model.PostModel;
import com.com.student.R;
import com.com.student.fragment.Comment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class PostsActivity extends AppCompatActivity implements View.OnClickListener, PostAdapter.OnClick
        , PostAdapter.OnClick_Like {

    //find Filed view
    EditText mEdit_Post;
    Button mBtn_Post, mBtn_Cunt;
    ImageView btn_like_red, btn_Like;
    TextView mCunt;
    RecyclerView mRecycler;
    LottieAnimationView mAnimationView;
    ViewPager mPager;
    //Model  SharedPreferences
    ModelSaveData saveData;
    //firebase
    FirebaseFirestore firestore;
    //Adapter
    PostAdapter mAdapter;
    PostModel postModel;

    String counter;
    String UserId;

    public long Post_id;
    private static final String TAG = "AppDepug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initToolbar();
        iniView();

        RetriveData();
        //get_post_id();


    }


    //start toolbar
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        String title = "posts";
        getSupportActionBar().setTitle(title);


    } //end toolbar


    //Start find View by id
    private void iniView() {
        mEdit_Post = findViewById(R.id.edt_post);
        mBtn_Post = findViewById(R.id.btn_post);
        btn_like_red = findViewById(R.id.Like_red);
        btn_Like = findViewById(R.id.Like);
        mBtn_Post.setOnClickListener(this); //for on click
        mRecycler = findViewById(R.id.recycler_post);
        mAnimationView = findViewById(R.id.lottie_anim);
        mPager = findViewById(R.id.pager);

        //Model  SharedPreferences
        saveData = new ModelSaveData(this);

        //firebase
        firestore = FirebaseFirestore.getInstance();
    } //end find View by id


    //Start On Click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                UploadPost();
                break;

        }
    }    //end On Click

    ///start dialog if user dont enter data
    private void ShowDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_dialog);
        Button dialogButton = dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();

    }///end dialog if user dont enter data

    //start Upload Data to firebase ................
    private void UploadPost() {
        String Post = mEdit_Post.getText().toString();
        UserId = saveData.loadUserID();
        String Name = saveData.laod_Name();
        if (Post.isEmpty()) {
            ShowDialog();
        } else {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            Post_id = System.currentTimeMillis();
            postModel = new PostModel();
            postModel.setPost_id(Post_id);
            postModel.setName(Name);
            postModel.setPost(Post);
            postModel.setImage(null);
            postModel.setTime(date);
            firestore.collection("Posts").document(String.valueOf(Post_id)).set(postModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Snackbar.make(findViewById(android.R.id.content), "Post Created...", Snackbar.LENGTH_LONG)
                                    .show();
                            mEdit_Post.setText(null);

                        }
                    });
        }    // end Upload Data to firebase ................


    }


    ///Start retrive data from fire base in recycler view
    public void RetriveData() {
        CollectionReference collection = firestore.collection("Posts");
        Query query = collection.orderBy("time", Query.Direction.ASCENDING);
        collection.orderBy("time");
        FirestoreRecyclerOptions<PostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel.class)
                .build();

        // Adapter ............................
        mAdapter = new PostAdapter(recyclerOptions, this::OnIttemClick, this::OnIttemClick_Like);
        mRecycler.setLayoutManager(new LinearLayoutManager(PostsActivity.this));
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

    } ///end retrive data from fire base in recycler view

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void OnIttemClick(long click) {

        Log.d(TAG, String.valueOf(click));
        Comment fragment = new Comment();
        Bundle bundel = new Bundle();
        bundel.putLong("post_id", click);
        fragment.setArguments(bundel);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.cont, fragment);
        ft.commit();

        
    }


    @Override
    public void OnIttemClick_Like(long click_Like, int count) {
       // Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show();
        counter = String.valueOf(count);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PostsActivity.this,MainActivity.class));
        finish();
    }
}