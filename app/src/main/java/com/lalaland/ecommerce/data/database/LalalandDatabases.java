package com.lalaland.ecommerce.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lalaland.ecommerce.data.dao.SearchCategoryDao;
import com.lalaland.ecommerce.data.models.globalSearch.SearchCategory;
import com.lalaland.ecommerce.helpers.AppPreference;

import static com.lalaland.ecommerce.helpers.AppConstants.APP_DATABASE;

@Database(entities = SearchCategory.class, version = 1)
public abstract class LalalandDatabases extends RoomDatabase {

    private static LalalandDatabases INSTANCE;
    private static AppPreference appPreference;

    private static Callback sRoomDatabaseCallback = new Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };

    public static LalalandDatabases getInstance(Context context) {
        if (INSTANCE == null) {
            appPreference = AppPreference.getInstance(context);
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LalalandDatabases.class, APP_DATABASE)
                    .addCallback(sRoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract SearchCategoryDao searchCategoryDao();
}
