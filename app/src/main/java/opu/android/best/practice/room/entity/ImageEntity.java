package opu.android.best.practice.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "img_list")
public class ImageEntity {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "img_url")
    public String imgUrl;
    @ColumnInfo(name="img_name")
    public String name;
    public ImageEntity(int id, String imgUrl,  String name){
        this.id=id;
        this.imgUrl=imgUrl;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }
}
