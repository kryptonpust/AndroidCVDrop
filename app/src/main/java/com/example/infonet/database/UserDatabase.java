package com.example.infonet.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.infonet.database.converter.DateConverter;
import com.example.infonet.database.converter.LangConverter;
import com.example.infonet.database.converter.UriConverter;
import com.example.infonet.database.entity.City;
import com.example.infonet.database.entity.Country;
import com.example.infonet.database.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {User.class, Country.class, City.class},version = 1)
@TypeConverters({LangConverter.class, DateConverter.class, UriConverter.class})
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AddressDao addressDao();

    private static volatile UserDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS=2;
    public static final ExecutorService databaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static UserDatabase getINSTANCE(final Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null)
                {
                    if(INSTANCE==null)
                    {
                        INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                                        UserDatabase.class,
                                        "Infonet_db")
//                                .addCallback(seedUser)
                                .addCallback(seedAddress(context))
                                .build();
                    }
                }
            }
        }
        return INSTANCE;
    }

    private static Callback seedAddress(Context context) {
        return new RoomDatabase.Callback(){
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                databaseWriteExecutor.execute(()->{
                    AddressDao addressDao= INSTANCE.addressDao();
                    addressDao.deleteAllCountry();
                    addressDao.deleteAllCity();

                    String json = null;
                    try {
                        InputStream is = context.getApplicationContext().getAssets().open("countries.json");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        json = new String(buffer, StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    JSONObject jsonobject;
                    try {
                        assert json != null;
                        jsonobject = new JSONObject(json);
                        for (Iterator<String> it = jsonobject.keys(); it.hasNext(); ) {
                            String s = it.next();
                            long id=addressDao.insertCountry(new Country(s));
                            JSONArray jsonArray=jsonobject.optJSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                addressDao.insertCity(new City(id,jsonArray.getString(i)));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
    }

//    private static RoomDatabase.Callback seedUser=new RoomDatabase.Callback(){
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//
//            databaseWriteExecutor.execute(()->{
//                UserDao userDao= INSTANCE.userDao();
//                userDao.deleteAllUser();
//
//                User user=new User("Nafiul Fatta");
//                userDao.insert(user);
//            });
//        }
//    };

}
