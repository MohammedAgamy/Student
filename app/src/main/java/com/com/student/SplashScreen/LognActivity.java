package com.com.student.SplashScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.com.student.MainActivity;
import com.com.student.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LognActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    private EditText edt_email, edt_password;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logn);


        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_Password);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);

        //  Add Anmation
        lottieAnimationView =findViewById(R.id.lottie);
        lottieAnimationView.animate();



        //  Login


        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = new Intent(LognActivity.this, MainActivity.class);
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(intent);
            finish();

            return;
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_email.getText().toString().isEmpty()) {
                    edt_email.setError("please write email");
                    return;
                } else {
                    edt_email.setError(null);
                }
                if (edt_password.getText().toString().isEmpty()) {
                    edt_password.setError("please write password");
                    return;
                } else {
                    edt_password.setError(null);
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(intent);
                            finish();
                            Toast.makeText(LognActivity.this, "success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LognActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

}