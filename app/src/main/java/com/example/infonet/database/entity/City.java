package com.example.infonet.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private long country_id;
    @NonNull
    private String name;

    @Ignore
    public City(long country_id, @NonNull String name) {
        this.country_id = country_id;
        this.name = name;
    }

    public City(long id, long country_id, @NonNull String name) {
        this.id = id;
        this.country_id = country_id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public long getCountry_id() {
        return country_id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
