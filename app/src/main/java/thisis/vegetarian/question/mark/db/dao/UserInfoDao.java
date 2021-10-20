package thisis.vegetarian.question.mark.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;

@Dao
public interface UserInfoDao {

    default void insert(UserInfoEntity userInfoEntity){
        insertQ(userInfoEntity.getAdvertisingId(), userInfoEntity.getTokenId());
    }

    @Query("Insert into User_Info('advertisingId', 'tokenId') values(:advertisingId, :tokenId)")
    void insertQ(String advertisingId, String tokenId);

    @Query("DELETE FROM User_Info")
    void deleteAll();

    @Query("SELECT * FROM User_Info")
    void getAllId();

}
