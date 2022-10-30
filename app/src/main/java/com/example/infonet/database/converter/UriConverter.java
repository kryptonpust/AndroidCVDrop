package com.example.infonet.database.converter;

import android.net.Uri;

import androidx.room.TypeConverter;

import java.util.Date;

public class UriConverter {
    @TypeConverter
    public static Uri toUri(String uriString){
        return uriString == null ? null: Uri.parse(uriString);
    }

    @TypeConverter
    public static String fromUri(Uri uri){
        return uri == null ? null : uri.toString();
    }
}
