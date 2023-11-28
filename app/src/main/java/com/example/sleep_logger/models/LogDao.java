package com.example.sleep_logger.models;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogDao {
    @Query("SELECT * FROM logs")
    List<Log> getAll();

    @Insert
    void insertAll(Log... logs);

    @Delete
    void delete(Log log);

    @Query("DELETE FROM logs")
    void nukeTable();
}
