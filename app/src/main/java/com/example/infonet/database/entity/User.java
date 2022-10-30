package com.example.infonet.database.entity;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.infonet.Utils;

import java.util.List;

@Entity(tableName = "user_info")
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    @NonNull
    private String name;
    private String country;
    private String city;
    @NonNull
    private List<String> language;
    @NonNull
    private Long date_of_birth;
    @NonNull
    private Uri resumeUri;
    @NonNull
    private String fileName;

    @Ignore
    public User(@NonNull String name, String country, String city, @NonNull List<String> language, @NonNull Long date_of_birth, @NonNull Uri resume ,@NonNull String fileName) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.language = language;
        this.date_of_birth = date_of_birth;
        this.resumeUri = resume;
        this.fileName=fileName;
    }


    public User(@NonNull Long id, @NonNull String name, String country, String city, @NonNull List<String> language, @NonNull Long date_of_birth, @NonNull Uri resumeUri, @NonNull String fileName) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.language = language;
        this.date_of_birth = date_of_birth;
        this.resumeUri = resumeUri;
        this.fileName = fileName;
    }


    @NonNull
    public Long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public List<String> getLanguage() {
        return language;
    }

    public Uri getResumeUri() {
        return resumeUri;
    }

    public Long getDate_of_birth() {
        return date_of_birth;
    }

    @NonNull
    public String getFileName() {
        return fileName;
    }
}
