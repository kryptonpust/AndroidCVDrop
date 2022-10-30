package com.example.infonet.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infonet.R;
import com.example.infonet.Utils;
import com.example.infonet.database.entity.User;
import com.example.infonet.viewmodel.UserViewModel;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Locale;


public class ViewFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";

    private TextView name,country,city,language,date_of_birth;
    private Chip resumeChip;

    private Long userId;

    public ViewFragment() {
        // Required empty public constructor
    }



    public static ViewFragment newInstance(Long param1) {
        ViewFragment fragment = new ViewFragment();
        if(param1!=null)
        {
            Bundle args = new Bundle();
            args.putLong(ARG_PARAM1, param1);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_view, container, false);
        UserViewModel userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        name=v.findViewById(R.id.view_name);
        country=v.findViewById(R.id.view_country);
        city=v.findViewById(R.id.view_city);
        language=v.findViewById(R.id.view_language);
        date_of_birth=v.findViewById(R.id.view_date_of_birth);
        resumeChip=v.findViewById(R.id.resumechip);
        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for(User u: users)
                {
                    if(u.getId().equals(userId))
                    {
                        name.setText(u.getName());
                        country.setText(TextUtils.isEmpty(u.getCountry())?"None":u.getCountry());
                        city.setText(TextUtils.isEmpty(u.getCity())?"None":u.getCity());
                        language.setText(String.join(",",u.getLanguage()));
                        date_of_birth.setText(Utils.geDateStringFromLong(u.getDate_of_birth(), Locale.getDefault()));
                        resumeChip.setText(u.getFileName());
                        resumeChip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, u.getResumeUri());
                                String mimeTypeInfo=requireActivity().getContentResolver().getType(u.getResumeUri());
                                intent.setDataAndType(u.getResumeUri(),mimeTypeInfo);
                                try{
                                    requireActivity().startActivity(intent);
                                }catch (ActivityNotFoundException e)
                                {
                                    Toast.makeText(requireActivity(),"No supported app!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
        return v;
    }

}