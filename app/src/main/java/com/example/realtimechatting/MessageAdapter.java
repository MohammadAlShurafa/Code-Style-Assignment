package com.example.realtimechatting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    ArrayList<Message> messages;
    String imgURL;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, ArrayList<Message> messages, String imgURL){
        this.context = context;
        this.messages = messages;
        this.imgURL = imgURL;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.right_msg_item, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.left_msg_item, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.showMsg.setText(message.getMessage());
        if (imgURL.equals("Default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Picasso.get().load(imgURL).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(holder.profileImage);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView showMsg;
        ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMsg = itemView.findViewById(R.id.show_msg);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}

