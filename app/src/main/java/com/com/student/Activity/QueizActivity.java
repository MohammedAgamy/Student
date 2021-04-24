package com.com.student.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.com.student.Model.QuestionModel;
import com.com.student.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QueizActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private int count = 0;
    private int score = 0;
    private int postion = 0;
    public TextView question, noIndicator;
    private LinearLayout optionsContainer;
    private Button nextBtn;
    private Dialog loadingDialog;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private List<QuestionModel> list;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quaiz_lesson);
        Intent intent = getIntent();

        String titleQuaiz = intent.getStringExtra("title");


        sharedPreferences = getSharedPreferences("SHARD_PRE",MODE_PRIVATE);
        name = sharedPreferences.getString("Name", null);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titleQuaiz);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        question = findViewById(R.id.quastion);
        noIndicator = findViewById(R.id.no_indicator);
        optionsContainer = findViewById(R.id.options_container);
        nextBtn = findViewById(R.id.next_btn);


        list = new ArrayList<>();
        myRef.child("SETS").child(titleQuaiz).child("questions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            list.add(snapshot1.getValue(QuestionModel.class));
                        }

                        if (list.size() > 0) {
                            for (int i = 0; i < 4; i++) {
                                optionsContainer.getChildAt(i).setOnClickListener(v ->
                                        checkAnswer(((Button) v)));
                            }
                            playAnim(question, 0, list.get(postion).getQuestion());
                            nextBtn.setOnClickListener(v -> {

                                nextBtn.setEnabled(false);
                                nextBtn.setAlpha(0.7f);
                                enableOption(true);
                                postion++;
                                if (postion == list.size()) {
                                    // soreActivity
                                    Intent scoreIntent = new Intent(getApplicationContext(), ScoreActivity.class);
                                    scoreIntent.putExtra("score", score);
                                    scoreIntent.putExtra("total", list.size());
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    HashMap<Object, Object> map = new HashMap<>();
                                    map.put("name", name);
                                    map.put("total", list.size());
                                    map.put("score", score);

                                    database.getReference().child("score")
                                            .child("nameLesson")
                                            .child(titleQuaiz)
                                            .setValue(map);

                                    startActivity(scoreIntent);
                                    finish();
                                    return;
                                }


                                count = 0;
                                playAnim(question, 0, list.get(postion).getQuestion());
                            });
                        } else {
                            finish();
                            Toast.makeText(getApplicationContext(), "no question", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        finish();
                    }
                });
    }


    // anmation question
    private void playAnim(View view, final int value, final String data) {
        view.animate().alpha(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {

                        option = list.get(postion).getOptionA();
                    } else if (count == 1) {

                        option = list.get(postion).getOptionB();
                    } else if (count == 2) {
                        option = list.get(postion).getOptionC();

                    } else if (count == 3) {
                        option = list.get(postion).getOptionD();

                    }
                    playAnim(optionsContainer.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        noIndicator.setText(postion + 1 + "/" + list.size());
                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectOption) {

        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);

        if (selectOption.getText().toString().equals(list.get(postion).getCorrectANS())) {
            // correct
            score++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }

        } else {

            // in correct
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            }
            Button correctoption = optionsContainer.findViewWithTag(list.get(postion).getCorrectANS());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }

        }
    }


    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);


            if (enable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
                }
            }

        }
    }
}

/*

 */

/*

 playAnim(question, 0, list.get(postion).getQuestion());
        nextBtn.setOnClickListener(v -> {

            nextBtn.setEnabled(false);
            nextBtn.setAlpha(0.7f);
            enableOption(true);
            postion++;
            if (postion == list.size()) {

                // score activity
                return;
            }

            count = 0;
            playAnim(question, 0, list.get(postion).getQuestion());
        });
list.add(new QuestionModel("Question1", "a", "b", "c", "d", "a"));
        list.add(new QuestionModel("Question1", "a", "b", "c", "d", "b"));
        list.add(new QuestionModel("Question3", "a", "b", "c", "d", "c"));
        list.add(new QuestionModel("Question4", "a", "b", "c", "d", "a"));
        list.add(new QuestionModel("Question5", "a", "b", "c", "d", "d"));
        list.add(new QuestionModel("Question6", "a", "b", "c", "d", "c"));
        list.add(new QuestionModel("Question7", "a", "b", "c", "d", "b"));

 */