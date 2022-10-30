package com.example.infonet.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "countries")
public class Country {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String name;

    @Ignore
    public Country(@NonNull String name) {
        this.name = name;
    }

    public Country(long id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
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
