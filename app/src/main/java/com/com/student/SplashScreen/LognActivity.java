package com.com.student.SplashScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.com.student.Home.MainActivity;
import com.com.student.Model.ModelSaveData;
import com.com.student.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LognActivity extends AppCompatActivity {

    //find filed view..
    LottieAnimationView lottieAnimationView;
    private EditText edt_email, edt_password, mEdit_Name;
    private ProgressBar progressBar;
    private Button btn_login;
    private  String name ,email,password;
    //save_data
    ModelSaveData data_student;
    //firebase
    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logn);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_Password);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        mEdit_Name = findViewById(R.id.edt_name);

        //  Add Anmation
        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.animate();

        //Save Data
        ModelSaveData saveData = new ModelSaveData(this);


        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = new Intent(LognActivity.this, MainActivity.class);
        if (firebaseAuth.getCurrentUser() != null) {
            sendData();
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
               name = mEdit_Name.getText().toString();
                 email = edt_email.getText().toString();
                 password = edt_password.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String UserID = task.getResult().getUser().getUid();
                            saveData.SaveData(name, email, UserID);
                            sendData();
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


    public void sendData() {
        sharedPreferences = getSharedPreferences("SHARD_PRE", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Name",name);
        editor.apply();
        editor.commit();
    }
}