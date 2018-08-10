package opu.android.best.practice.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import opu.android.best.practice.room.entity.ImageEntity;

@Database(entities = {ImageEntity.class}, version = 1,exportSchema = false)
public abstract class ArchComponentDatabase extends RoomDatabase {
    private static ArchComponentDatabase database;

    public abstract ImageDAO getImgDAO();

    public static ArchComponentDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (ArchComponentDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                            ArchComponentDatabase.class, "arch_component_database")
                            .build();

                }
            }
        }
        return database;
    }

}
