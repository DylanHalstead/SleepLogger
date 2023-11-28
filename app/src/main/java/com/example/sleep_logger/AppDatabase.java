package com.example.sleep_logger;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.sleep_logger.models.Log;
import com.example.sleep_logger.models.Converters;
import com.example.sleep_logger.models.LogDao;

@Database(entities = {Log.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract LogDao logDao();
}