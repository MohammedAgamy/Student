package com.com.student.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.com.student.Adapter.PostAdapter;
import com.com.student.Adapter.PostAdapterAdmin;
import com.com.student.Model.PostModel;
import com.com.student.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment implements PostAdapterAdmin.itemClickAdmin {

    //Adapter
    PostAdapterAdmin mAdapterPost;
    //fireBase
    public static   String AdminPost_id ;
    Task<QuerySnapshot> documentReference;
    FirebaseFirestore mFireStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RetriveData(view);
        TestId();
    }

    ///Start retrive data from fire base in recycler view
    public void RetriveData(View view) {
        RecyclerView mRecPostAdmin = view.findViewById(R.id.recycler_postFromAdmin);
        mFireStore = FirebaseFirestore.getInstance();
        CollectionReference collection = mFireStore.collection("AdminPosts");
        Query query = collection.orderBy("time", Query.Direction.ASCENDING);
        collection.orderBy("time");
        FirestoreRecyclerOptions<PostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel.class)
                .build();

        // Adapter ............................
        mAdapterPost = new PostAdapterAdmin(recyclerOptions,this::OnClickAdminComment);
        mRecPostAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterPost.notifyDataSetChanged();
        mRecPostAdmin.setAdapter(mAdapterPost);


    } ///end retrive data from fire base in recycler view

    @Override
    public void onStart() {
        super.onStart();
        mAdapterPost.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapterPost.stopListening();
    }

    public void TestId() {
        documentReference = mFireStore.collection("AdminPosts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot documentSnapshot=task.getResult() ;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                               AdminPost_id =document.getId();
                                Log.d("TAGid", AdminPost_id );
                            }
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });


    }


    @Override
    public void OnClickAdminComment(long onClick) {
        Log.d("TagId",String.valueOf(onClick));
        CommentAdminFragment fragment = new CommentAdminFragment();
        Bundle bundel = new Bundle();
        bundel.putLong("post_id_admin", onClick);
        fragment.setArguments(bundel);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.drawer_layout, fragment);
        ft.commit();
    }
}