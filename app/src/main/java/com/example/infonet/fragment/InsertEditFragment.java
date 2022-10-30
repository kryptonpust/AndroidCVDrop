package com.example.infonet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infonet.R;
import com.example.infonet.Utils;
import com.example.infonet.adapter.LanguageAdapter;
import com.example.infonet.database.entity.City;
import com.example.infonet.database.entity.Country;
import com.example.infonet.database.entity.User;
import com.example.infonet.viewmodel.UserViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InsertEditFragment extends Fragment {


    private static final String ARG_PARAM1 = "user_id";

    private Long user_id;
    private User old_user;
    private UserViewModel userViewModel;
    private AutoCompleteTextView countrySelect,citySelect;
    private EditText nameEditText,dateSelect;
    private Button uploadButton;
    private ActivityResultLauncher<Intent> uploadIntent;
    private RecyclerView gridView;
    private LanguageAdapter languageAdapter;
    private Uri fileUri;
    private Button saveButton;

    public InsertEditFragment() {
        // Required empty public constructor
    }

    public static InsertEditFragment newInstance(Long param1) {
        InsertEditFragment fragment = new InsertEditFragment();
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
            user_id = getArguments().getLong(ARG_PARAM1);
        }
        uploadIntent=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode()== Activity.RESULT_OK)
            {
                assert result.getData() != null;
                fileUri=result.getData().getData();
                uploadButton.setText(Utils.getFileNameFromUri(requireActivity().getContentResolver(),fileUri));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        nameEditText=view.findViewById(R.id.text_input_name);
        countrySelect=view.findViewById(R.id.country_select);
        citySelect=view.findViewById(R.id.city_select);
        dateSelect=view.findViewById(R.id.date_select);
        uploadButton=view.findViewById(R.id.file_upload);
        gridView=view.findViewById(R.id.grid_view);
        saveButton=view.findViewById(R.id.save);



        saveButton.setOnClickListener(view1 -> {
            String name=nameEditText.getText().toString();
            if(TextUtils.isEmpty(name))
            {
                nameEditText.setError("This Field is Required");
                Snackbar.make(requireContext(),view,"Name is Required",Snackbar.LENGTH_SHORT).show();
                return;
            }
            String country=countrySelect.getText().toString();
            String city=citySelect.getText().toString();
            Date date;
            try {
                date=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).parse(dateSelect.getText().toString());
            } catch (ParseException e) {
                dateSelect.setError("Date format Error");
                Snackbar.make(requireContext(),view,"Date format Error",Snackbar.LENGTH_SHORT).show();
                return;
            }
            List<String> languages=languageAdapter.getChecked_items();
            if(languages.size()==0)
            {
                Snackbar.make(requireContext(),view,"Language is Required",Snackbar.LENGTH_SHORT).show();
                return;
            }

            if(fileUri==null)
            {
                Snackbar.make(requireContext(),view,"Upload a Resume",Snackbar.LENGTH_SHORT).show();
                return;
            }

            assert date != null;
            if(user_id==null)
            {
                userViewModel.insert(new User(name,country,city,languages,date.getTime(),fileUri, Utils.getFileNameFromUri(requireActivity().getContentResolver(),fileUri)));
                Toast.makeText(getContext(),"Inserted",Toast.LENGTH_SHORT).show();
            }else {
                userViewModel.updateUser(new User(user_id,name,country,city,languages,date.getTime(),fileUri,fileUri==old_user.getResumeUri()?old_user.getFileName():Utils.getFileNameFromUri(requireActivity().getContentResolver(),fileUri)));
                Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
            }
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        userViewModel.getAllCountry().observe(getViewLifecycleOwner(), countries -> {
            ArrayAdapter<Country> temp=new ArrayAdapter<>(getContext(), R.layout.item_autocomplete, countries);
            countrySelect.setAdapter(temp);

        });
        userViewModel.getAllCity().observe(getViewLifecycleOwner(), cities -> {
            ArrayAdapter<City> temp=new ArrayAdapter<>(getContext(), R.layout.item_autocomplete, cities);
            citySelect.setAdapter(temp);
        });


        uploadButton.setOnClickListener(view12 -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            String[] extraMimeTypes = {"application/pdf", "application/msword"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //starts new activity to select file and return data
//                startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
            uploadIntent.launch(intent);
        });


        dateSelect.setOnClickListener(view15 -> {

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                    .datePicker()
                    .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build())
                    .build();
            datePicker.show(getParentFragmentManager(),"SELECT A DATE");
            datePicker.addOnPositiveButtonClickListener(selection -> dateSelect.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selection))));
        });

        languageAdapter=new LanguageAdapter(requireContext(),List.of("C","C++","Java","Kotlin","Python","Ruby","Matlab","PHP"));
        gridView.setLayoutManager(new GridLayoutManager(getContext(),3));
        gridView.setAdapter(languageAdapter);
//        languageAdapter.setChecked_items(List.of("JAVA"));


        countrySelect.setOnItemClickListener((adapterView, view14, i, l) -> {

            Country country= (Country) adapterView.getAdapter().getItem(i);
            userViewModel.getAllCityByCountryId(country.getId()).observe(getViewLifecycleOwner(), cities -> {
                ArrayAdapter<City> temp = new ArrayAdapter<>(getContext(), R.layout.item_autocomplete, cities);
                citySelect.setAdapter(temp);
            });
        });
        citySelect.setOnItemClickListener((adapterView, view13, i, l) -> {
            City city= (City) adapterView.getAdapter().getItem(i);
            userViewModel.getCountryById(city.getCountry_id()).observe(getViewLifecycleOwner(), country -> countrySelect.setText(country.getName()));
        });

        // EDIT MODE
        if(user_id!=null)
        {
            userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
                for (User u:users)
                {
                    if(u.getId().equals(user_id))
                    {
                        old_user=u;
                        nameEditText.setText(u.getName());
                        countrySelect.setText(u.getCountry());
                        citySelect.setText(u.getCity());
                        dateSelect.setText(new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(u.getDate_of_birth()));
                        languageAdapter.setChecked_items(u.getLanguage());
                        fileUri=u.getResumeUri();
                        saveButton.setText(R.string.btn_update_text);
                        saveButton.setBackgroundTintList( AppCompatResources.getColorStateList(requireContext(), android.R.color.holo_orange_light));
                        break;
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_edit, container, false);
    }


}