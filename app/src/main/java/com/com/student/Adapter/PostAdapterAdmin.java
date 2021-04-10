package com.com.student.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student.Model.PostModel;
import com.com.student.R;
import com.com.student.fragment.HomeFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapterAdmin extends FirestoreRecyclerAdapter<PostModel ,PostAdapterAdmin.itemAdmin>
{
    itemClickAdmin itemClickAdmin ;
    String idPostAdmin = HomeFragment.AdminPost_id;
    public PostAdapterAdmin(@NonNull FirestoreRecyclerOptions<PostModel> options ,itemClickAdmin itemClickAdmin) {
        super(options);
        this.itemClickAdmin=itemClickAdmin;
    }

    @Override
    protected void onBindViewHolder(@NonNull itemAdmin holder, int position, @NonNull PostModel model) {
        holder.mImage.setImageURI(model.getImage());
        holder.mName.setText(model.getName());
        holder.mTime.setText(model.getTime());
        holder.mPost.setText(model.getPost());

        holder.btn_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickAdmin.OnClickAdminComment(model.getPost_id());
            }
        });

        
    }

    @NonNull
    @Override
    public itemAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_admin, parent, false);
        PostAdapterAdmin.itemAdmin item_post = new PostAdapterAdmin.itemAdmin(view);
        return item_post;
    }

    class itemAdmin extends RecyclerView.ViewHolder
    {
        CircleImageView mImage;
        TextView mName, mTime, mPost ;
        ImageView btn_Comment ,btn_Like ,btn_Like_red  ;
        public itemAdmin(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.image_profile_a);
            btn_Like=itemView.findViewById(R.id.Like_a);
            btn_Like_red=itemView.findViewById(R.id.Like_red_a);
            mName = itemView.findViewById(R.id.name_user_a);
            mTime = itemView.findViewById(R.id.time_a);
            mPost = itemView.findViewById(R.id.post_a);
            btn_Comment=itemView.findViewById(R.id.comment_a);
        }
    }


    public interface itemClickAdmin
    {
        void OnClickAdminComment(long onClick);
    }
}
