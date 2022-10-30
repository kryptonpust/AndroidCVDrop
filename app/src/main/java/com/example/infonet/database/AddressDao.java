package com.example.infonet.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.infonet.database.entity.City;
import com.example.infonet.database.entity.Country;

import java.util.List;

@Dao
public interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertCountry(Country country);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCity(City city);

    @Query("DELETE FROM countries")
    void deleteAllCountry();

    @Query("DELETE FROM cities")
    void deleteAllCity();

    @Query("SELECT * FROM countries")
    LiveData<List<Country>> getAllCountry();

    @Query("SELECT * FROM countries WHERE id=:id")
    LiveData<Country> getCountryById(long id);

    @Query("SELECT * FROM cities")
    LiveData<List<City>> getAllCity();

    @Query("SELECT  * FROM countries WHERE name LIKE :item ORDER BY name ASC LIMIT 5")
    LiveData<List<Country>> getCountryByName(String item);

    @Query("SELECT  * FROM cities WHERE country_id=:id")
    LiveData<List<City>> getAllCityByCountryId(Long id);
}
