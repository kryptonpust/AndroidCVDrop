package com.example.infonet.database.converter;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LangConverter {
    @TypeConverter
    public List<String> gettingListFromString(String genreIds) {
        List<String> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        Collections.addAll(list, array);
        return list;
    }

    @TypeConverter
    public String writingStringFromList(List<String> list) {
        return String.join(",", list);
    }
}
