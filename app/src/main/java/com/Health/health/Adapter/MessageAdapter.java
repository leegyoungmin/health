package com.Health.health.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.Health.health.Model.Chat;
import com.Health.health.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    private Context context;
    private List<Chat> mChat;
    private String imgURL;

    // Firebase

    FirebaseUser fuser;


    //메세지 타입

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    //생성자


    public MessageAdapter(Context context, List<Chat> mChat,String imgURL) {
        this.context = context;
        this.mChat = mChat;
        this.imgURL = imgURL;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT){
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,
                parent,false);
        return new ViewHolder(view);}
        else{
          View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,
                  parent,false);
          return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        if(imgURL.equals("default")){
            holder.profile_image.setImageResource(R.drawable.personicon);

        }else{
            Glide.with(context).load(imgURL).into(holder.profile_image);
        }

        holder.time.setText(chat.getTime());

    }




    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message,time;
        public ImageView profile_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            time = itemView.findViewById(R.id.time);

        }
    }


    @Override
    public int getItemViewType(int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){  //보내는 사람이  fuser 와 같다면

            return MSG_TYPE_RIGHT;

        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
