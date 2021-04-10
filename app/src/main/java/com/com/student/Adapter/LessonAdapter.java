package com.com.student.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student.Listener.ItemLessonListener;
import com.com.student.Model.LessonModel;
import com.com.student.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.MyViewHolder> {


    private List<LessonModel> lessonModels;
    private ItemLessonListener itemLessonListener;

    public LessonAdapter(ItemLessonListener itemLessonListener, List<LessonModel> lessonModels) {
        this.itemLessonListener = itemLessonListener;
        this.lessonModels = lessonModels;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson
                        , parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(lessonModels.get(position).getImageLesson())
                .into(holder.imageView);
        holder.txt_nameCourse.setText(lessonModels.get(position).getNameLesson());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemLessonListener.onItemCourseClicked(lessonModels.get(position), position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return lessonModels == null ? 0 : lessonModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txt_nameCourse;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            txt_nameCourse = itemView.findViewById(R.id.title);
        }
    }
}
