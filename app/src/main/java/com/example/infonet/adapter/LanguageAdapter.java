package com.example.infonet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infonet.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private List<String> item_list;
    private Set<String> checked_items;
    private Context context;
    public LanguageAdapter(@NonNull Context context,  @NonNull List<String> objects) {
        item_list=objects;
        checked_items=new HashSet<>();
        this.context=context;
    }


    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public void setChecked_items(List<String> checked_items) {
        this.checked_items.addAll(checked_items);
    }

    public List<String> getChecked_items() {
        return new ArrayList<>(checked_items);
    }

    @NonNull
    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new LanguageAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder holder, int position) {
        String s= item_list.get(position);
        holder.checkBox.setText(s);
        if(checked_items!=null)
        {
            holder.checkBox.setChecked(checked_items.contains(s));
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    checked_items.add(s);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.item_gridview_checkbox);
        }
    }
}
