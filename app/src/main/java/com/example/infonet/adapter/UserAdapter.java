package com.example.infonet.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infonet.R;
import com.example.infonet.Utils;
import com.example.infonet.database.entity.User;
import com.example.infonet.fragment.InsertEditFragment;
import com.example.infonet.fragment.ViewFragment;
import com.example.infonet.viewmodel.UserViewModel;
import com.google.android.material.chip.Chip;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users;
    private AppCompatActivity context;
    private UserViewModel userViewModel;
    public UserAdapter(@NonNull Context context, List<User> users,UserViewModel userViewModel) {
        this.context= (AppCompatActivity) context;
        this.users = users;
        this.userViewModel=userViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_empty, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_recyclerview, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemViewType(position)!=0){
            User user=users.get(position);
            holder.languages.setText(String.join(",",user.getLanguage()));

            holder.chip.setText(user.getFileName());

            holder.chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, user.getResumeUri());
                    String mimeTypeInfo=context.getContentResolver().getType(user.getResumeUri());
                    intent.setDataAndType(user.getResumeUri(),mimeTypeInfo);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try{
                        context.startActivity(intent);
                    }catch (ActivityNotFoundException e)
                    {
                        Toast.makeText(context,"No supported app!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, InsertEditFragment.newInstance(user.getId()))
                            .addToBackStack("Update")
                            .commit();
                }
            });
            holder.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?");
                    builder.setTitle("Delete an item");
                    builder.setPositiveButton("I am sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userViewModel.deleteUser(user);
                            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();
                }
            });
            holder.viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                context.getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragmentContainerView, ViewFragment.newInstance(user.getId()))
//                        .addToBackStack("Update")
//                        .commit();

                    ViewFragment.newInstance(user.getId()).show(context.getSupportFragmentManager(), "ViewDialog");
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (this.users.size() == 0) {
            return 1;
        }
        return users.size();
    }

    public int getItemViewType(int i) {
        return this.users.size() == 0 ? 0 : 1;
    }

    public void setUsers(List<User> users) {
        this.users=users;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView languages;
        private Chip chip;
        ImageButton editButton,delButton,viewButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            languages=itemView.findViewById(R.id.languageTextview);
            chip=itemView.findViewById(R.id.resumechip);
            editButton=itemView.findViewById(R.id.editbtn);
            delButton=itemView.findViewById(R.id.deletebtn);
            viewButton=itemView.findViewById(R.id.viewbtn);

        }

    }
}
