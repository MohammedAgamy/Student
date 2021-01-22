package com.com.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.com.student.R;


public class Pdf_Fragment extends Fragment {
    WebView webView;
    private ProgressBar progressBar;

    public Pdf_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.WV);
        progressBar = view.findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setWebChromeClient(new WebChromeClient());
        String url =getArguments().getString("url");
        final   int position =getArguments().getInt("position",0);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                progressBar.setVisibility(View.GONE);
            }
        });
      //   webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+ BookFragment.list.get(position).getPdfUrl());
        webView.loadUrl("https://docs.google.com/viewerng/viewer?embedded=true&url=" + url);
      //  webView.loadUrl("https://docs.google.com/viewerng/viewer?embedded=true&url=");
      //  webView.loadUrl(url);
    }
}