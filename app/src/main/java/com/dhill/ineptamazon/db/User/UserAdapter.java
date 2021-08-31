package com.dhill.ineptamazon.db.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhill.ineptamazon.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> users=new ArrayList<>();
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_layout, parent, false);
        return new UserHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = users.get(position);
        holder.textViewTitle.setText(currentUser.getUserName());
        holder.textViewDescription.setText(currentUser.getPassword());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users){
        this.users=users;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        public UserHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.user_name);
            textViewDescription = itemView.findViewById(R.id.user_password);
        }
    }

}
