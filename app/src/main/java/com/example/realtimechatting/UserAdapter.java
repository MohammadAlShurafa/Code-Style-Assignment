package com.example.realtimechatting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    ArrayList<User> users;


    public UserAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user = users.get(position);
       // holder.profileImage.setImageResource(item.getProfileImage());
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("Default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Picasso.get().load(user.getImageURL()).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(holder.profileImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatMessagesActivity.class);
                intent.putExtra("userId" ,user.getUserId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        TextView username;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username_tv);
        }


    }
}
