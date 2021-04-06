package com.com.student.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.com.student.Activity.PostsActivity;
import com.com.student.Adapter.CommentAdapter;
import com.com.student.Adapter.PostAdapter;
import com.com.student.Model.CommentModel;
import com.com.student.Model.ModelSaveData;
import com.com.student.Model.PostModel;
import com.com.student.R;
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


public class Comment extends Fragment  implements View.OnClickListener  {

    //find Filed view
    EditText mEdit_Comment;
    Button mBtn_Comment;
    RecyclerView mRecycler;

    //FireBase
    FirebaseFirestore firestore;

    //adapter
    CommentAdapter mAdapter;
    //model
    CommentModel mCommentModel;
    ModelSaveData saveData;
    //get id post
    String id;
    String User_Name;

    public Comment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        id = String.valueOf(bundle.getLong("post_id"));



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get name from save data
        saveData = new ModelSaveData(getActivity());
        User_Name = saveData.laod_Name();


        Bundle bundle = getArguments();
        id = String.valueOf(bundle.getLong("post_id"));
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();

        //Start find View by id
        mEdit_Comment = view.findViewById(R.id.edt_comment);
        mBtn_Comment = view.findViewById(R.id.btn_comment);
        mBtn_Comment.setOnClickListener(this);
        mRecycler = view.findViewById(R.id.list_comment);

        //firebase
        firestore = FirebaseFirestore.getInstance();

        //Adapter
        RetriveData();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_comment:
                UploudComent();
                break;
        }
    }


    private void UploudComent() {
        String comment = mEdit_Comment.getText().toString();

        if (comment.isEmpty()) {
            Toast.makeText(getActivity(), "Add Comment", Toast.LENGTH_SHORT).show();
        } else {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            long Comment_id = System.currentTimeMillis();
            mCommentModel = new CommentModel();
            mCommentModel.setName(User_Name);
            mCommentModel.setImage(null);
            mCommentModel.setTime(date);
            mCommentModel.setComment(comment);
            mCommentModel.setComment_id(Comment_id);

            firestore.collection("Posts").document(id).collection("Comments")
                    .document(String.valueOf(Comment_id))
                    .set(mCommentModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Comment Created", Toast.LENGTH_SHORT).show();
                            mEdit_Comment.setText(null);
                        }
                    });


        }


    }

    ///Start retrive data from fire base in recycler view
    public void RetriveData() {
        CollectionReference collection = firestore.collection("Posts")
                .document(id)
                .collection("Comments")
                ;
        Query query = collection.orderBy("time", Query.Direction.ASCENDING);
        collection.orderBy("time");
        FirestoreRecyclerOptions<CommentModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<CommentModel>()
                .setQuery(query, CommentModel.class)
                .build();

        // Adapter ............................
        mAdapter = new CommentAdapter(recyclerOptions);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

    } ///end retrive data from fire base in recycler view

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }




}
