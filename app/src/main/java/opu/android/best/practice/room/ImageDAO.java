package opu.android.best.practice.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import opu.android.best.practice.room.entity.ImageEntity;

@Dao
public interface ImageDAO {
    @Insert
    public void insertImage(ImageEntity entity);
    @Query("SELECT * from img_list")
    public List<ImageEntity>getImages();
}
