package com.Health.health.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Health.health.Activity.MessageActivity;
import com.Health.health.Model.Chat;
import com.Health.health.Model.Users;
import com.Health.health.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context context;
    private List<Users> mUsers;
    private boolean isChat;
    String theLastMessage;


    //생성자


    public UserAdapter(Context context, List<Users> mUsers,boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_item,
                parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final Users users = mUsers.get(position);
        holder.userName.setText(users.getUsername());
        //사용자에서 프로필 Showing
        if(users.getImageURL()!=null&&users.getImageURL().equals("default")){
            holder.profileImage.setImageResource(R.drawable.personicon);
        }else{
            Glide.with(context)
                    .load(users.getImageURL())
                    .into(holder.profileImage);
        }


        if(isChat){
            lastMessage(users.getId(),holder.message);
        }else {
            holder.message.setVisibility(View.GONE);
        }

        //상태 체크

        if(isChat){

            if(users.getStatus().equals("online")){
                holder.imageViewON.setVisibility(View.VISIBLE);
                holder.imageViewOFF.setVisibility(View.GONE);

            }else{
                holder.imageViewON.setVisibility(View.GONE);
                holder.imageViewOFF.setVisibility(View.VISIBLE);

            }
        }else{
            holder.imageViewON.setVisibility(View.GONE);
            holder.imageViewOFF.setVisibility(View.GONE);
        }


        //아이템 클릭 리스너

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사용자 클릭시 대화창으로
                Intent i =new Intent(context, MessageActivity.class);
                i.putExtra("id",users.getId());
                context.startActivity(i);
            }
        });
    }

    private void lastMessage(final String id, final TextView message) {

        theLastMessage = "default";
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Chat chat = ds.getValue(Chat.class);
                    if(chat.getReceiver().equals(user.getUid()) && chat.getSender().equals(id) ||
                    chat.getReceiver().equals(id) && chat.getSender().equals(user.getUid())){
                        theLastMessage = chat.getMessage();
                    }
                }


                switch (theLastMessage){
                    case "default":
                        message.setText("No Message");
                        break;

                    default:
                        message.setText(theLastMessage);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName,message;
        public ImageView profileImage;
        public ImageView imageViewON;
        public ImageView imageViewOFF;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            profileImage = itemView.findViewById(R.id.profileImage);
            imageViewON = itemView.findViewById(R.id.statusimageOn);
            imageViewOFF = itemView.findViewById(R.id.statusimageOFF);
            message = itemView.findViewById(R.id.messageTv);
        }
    }

}
