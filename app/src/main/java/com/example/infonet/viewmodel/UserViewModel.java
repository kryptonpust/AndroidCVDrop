package com.example.infonet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.infonet.database.entity.City;
import com.example.infonet.database.entity.Country;
import com.example.infonet.database.entity.User;
import com.example.infonet.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private final LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository=new UserRepository(application);
        allUsers=userRepository.getAllUsers();
    }

    public LiveData<List<Country>> getAllCountry()
    {
        return userRepository.getAllCountry();
    }
    public LiveData<List<City>> getAllCity()
    {
        return userRepository.getAllCity();
    }


    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    public void insert(User user)
    {
        userRepository.insert(user);
    }


    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }


    public LiveData<List<City>> getAllCityByCountryId(Long id) {
        return userRepository.getAllCityByCountryId(id);
    }

    public LiveData<Country> getCountryById(long country_id) {
        return userRepository.getCountryById(country_id);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
}
