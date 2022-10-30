package com.example.infonet.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.infonet.database.AddressDao;
import com.example.infonet.database.UserDao;
import com.example.infonet.database.UserDatabase;
import com.example.infonet.database.entity.City;
import com.example.infonet.database.entity.Country;
import com.example.infonet.database.entity.User;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private AddressDao addressDao;
    private LiveData<List<User>> allUsers;
    private LiveData<List<Country>> allCountry;
    private LiveData<List<City>> allCity;

    public UserRepository(Application application) {
        UserDatabase database=UserDatabase.getINSTANCE(application);
        userDao=database.userDao();
        addressDao=database.addressDao();
        allUsers=userDao.getAllUser();
        allCountry=addressDao.getAllCountry();
        allCity=addressDao.getAllCity();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<Country>> getAllCountry() {
        return allCountry;
    }
    public LiveData<List<City>> getAllCity() {
        return allCity;
    }

    public void insert(User user)
    {
        UserDatabase.databaseWriteExecutor.execute(()->{
            userDao.insert(user);
        });
    }


    public LiveData<List<Country>> getCountryByName(String item) {
        return addressDao.getCountryByName(item);
    }

    public void deleteUser(User user) {
        UserDatabase.databaseWriteExecutor.execute(()->{
            userDao.delete(user);
        });
    }

    public LiveData<List<City>> getAllCityByCountryId(Long id) {
        return addressDao.getAllCityByCountryId(id);
    }

    public LiveData<Country> getCountryById(long country_id) {
        return addressDao.getCountryById(country_id);
    }

    public void updateUser(User user) {
        UserDatabase.databaseWriteExecutor.execute(()->{
            userDao.update(user);
        });

    }
}
