package thisis.vegetarian.question.mark.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;

@Dao
public interface UserLoginInfoDao {

    default long insert(UserInfoEntity userInfoEntity){
        return insertQ(userInfoEntity.getUserId(), userInfoEntity.getDisplayName(), userInfoEntity.getEmail(), userInfoEntity.getTokenId());
    }

    @Query("Insert into User_Info('userId', 'displayName', 'email', 'tokenId') values(:userId, :displayname, :email, :tokenId)")
    long insertQ(String userId, String displayname, String email, String tokenId);

    @Query("DELETE FROM User_Info")
    void deleteAll();

    @Query("SELECT * FROM User_Info")
    LiveData<List<UserInfoEntity>> getAll();

    @Query("SELECT * FROM User_Info")
    List<UserInfoEntity> getUser();
}
