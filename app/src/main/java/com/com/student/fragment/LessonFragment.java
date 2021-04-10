package com.com.student.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.com.student.Activity.QueizActivity;
import com.com.student.Activity.ScoreActivity;
import com.com.student.Adapter.LessonAdapter;
import com.com.student.Model.LessonModel;
import com.com.student.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LessonFragment extends Fragment {
    private RecyclerView recyclerView;
    private LessonAdapter adapter;
    private List<LessonModel> modelList = new ArrayList<>();
    ;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private Dialog loadingDialog;

    public LessonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lesson, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.Rv);
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        loadingDialog.show();


        RecyclerView.LayoutManager horizontalLayout = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayout);
        adapter = new LessonAdapter(this::onItemLessonClicked, modelList);
        recyclerView.setAdapter(adapter);


        myRef.child("Lesson").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    modelList.add(snapshot1.getValue(LessonModel.class));

                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                getActivity().finish();
            }
        });
    }


    public void onItemLessonClicked(LessonModel lessonModel, int position) {
        Intent intent = new Intent(getContext(), QueizActivity.class);
        String txt_lesson = modelList.get(position).getNameLesson();
        intent.putExtra("setNo", position);
        intent.putExtra("title", txt_lesson);
        startActivity(intent);
    }
}