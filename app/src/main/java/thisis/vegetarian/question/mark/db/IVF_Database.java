package thisis.vegetarian.question.mark.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thisis.vegetarian.question.mark.db.dao.ProductDataDao;
import thisis.vegetarian.question.mark.db.dao.MemberProfileDao;
import thisis.vegetarian.question.mark.db.dao.UserLoginInfoDao;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.db.entity.MemberProfileEntity;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;

@Database(entities = {IVF_ProductDataEntity.class, UserInfoEntity.class, MemberProfileEntity.class}, version = 1)
public abstract class IVF_Database extends RoomDatabase {

    public static IVF_Database instance;

    public abstract ProductDataDao productDataDao();

    public abstract UserLoginInfoDao userLoginInfoDao();

    public abstract MemberProfileDao userInfoDao();

    public static synchronized IVF_Database getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), IVF_Database.class, "IVFDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new Callback() {
        ExecutorService executorService;
        ProductDataDao productDataDao;
        @Override
        public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            try {
                executorService = Executors.newFixedThreadPool(1);
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        productDataDao = instance.productDataDao();
                        productDataDao.insertQ("000000001", "Product0", 0, "US", 4, "Test test", 1);
                        productDataDao.insertQ("123456789", "Product1", 1, "TW", 0, "Test Remark", 0);
                        productDataDao.insertQ("987654321", "Product2", 1, "TW", 1, "Test Remark", 0);
                        productDataDao.insertQ("456789132", "Product3", 2, "TW", 2, "Test Remark", 0);
                        productDataDao.insertQ("321654987", "Product4", 3, "TW", 3, "Test Remark", 0);
                        productDataDao.insertQ("987123456", "Product5", 4, "TW", 4, "Test Remark", 0);
                        productDataDao.insertQ("451239845", "Product6", 5, "TW", 5, "Test Remark", 0);
                        productDataDao.insertQ("412485897", "Product7", 6, "TW", 5, "Test Remark", 0);
                        productDataDao.insertQ("124545788", "Product8", 7, "TW", 5, "Test Remark", 0);
                        productDataDao.insertQ("124561321", "Product9", 8, "TW", 5, "Test Remark", 0);
                    }
                });

            } catch (Exception e){
                e.getStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
    };
}
