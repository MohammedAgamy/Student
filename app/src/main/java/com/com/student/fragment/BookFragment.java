package com.com.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student.Adapter.PDFAdapter;
import com.com.student.Listener.ItemClickListener;
import com.com.student.Model.PDFModel;
import com.com.student.R;

import java.util.ArrayList;
import java.util.List;


public class BookFragment extends Fragment {

    RecyclerView recyclerView;
    public static List<PDFModel> list;
    Fragment fragment;
    PDFAdapter adapter;

    public BookFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.Rv);

        list = new ArrayList<>();
        list.add(new PDFModel("PDF One", "https://firebasestorage.googleapis.com/v0/b/intro-slider-82f6f.appspot.com/o/uploads%2F1609973936479.pdf?alt=media&token=baab24f4-0009-47f8-92f2-4e951403b46a"));
        list.add(new PDFModel("PDF Two", "https://firebasestorage.googleapis.com/v0/b/intro-slider-82f6f.appspot.com/o/uploads%2F1609974053307.pdf?alt=media&token=6aaefaf6-716d-44df-a34c-87ff0a376979"));
        list.add(new PDFModel("PDF Three", "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"));
        list.add(new PDFModel("PDF Four", "https://drive.google.com/file/d/1lmfLT-oecAsu3L-Uy0_3BttCCyCwYdUX/view?usp=sharing"));
        list.add(new PDFModel("PDF Five", "http://www.pdf995.com/samples/pdf.pdf"));
        list.add(new PDFModel("PDF Six", "https://www.cs.cmu.edu/afs/cs.cmu.edu/user/gchen/www/download/java/LearnJava.pdf"));
        list.add(new PDFModel("PDF Seven", "https://www.cs.cmu.edu/afs/cs.cmu.edu/user/gchen/www/download/java/LearnJava.pdf"));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                fragment = new Pdf_Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container1, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                Bundle bundle = new Bundle();
                bundle.putString("url", list.get(position).getPdfUrl());
                bundle.putInt("position",position);

                fragment.setArguments(bundle);
            }
        };
        PDFAdapter adapter = new PDFAdapter(list,getContext(),itemClickListener);
        recyclerView.setAdapter(adapter);
    }
}