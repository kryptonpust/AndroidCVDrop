package com.example.infonet.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infonet.R;
import com.example.infonet.adapter.UserAdapter;
import com.example.infonet.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Objects;

public class ListFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private UserViewModel userViewModel;
    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;


    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }


    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userAdapter=new UserAdapter(requireContext(),Collections.emptyList(),userViewModel);

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);
        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            userAdapter.setUsers(users);
        });
        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, InsertEditFragment.newInstance(null)).addToBackStack("InsertDelete").commit());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
}