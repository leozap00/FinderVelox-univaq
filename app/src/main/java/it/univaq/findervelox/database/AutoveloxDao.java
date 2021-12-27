package it.univaq.findervelox.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.univaq.findervelox.model.Autovelox;

@Dao
public interface AutoveloxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<Autovelox> autoveloxList);

    @Query("SELECT * FROM autovelox ORDER BY id")
    List<Autovelox> findAll();
}
