package thisis.vegetarian.question.mark.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import thisis.vegetarian.question.mark.db.entity.MemberProfileEntity;

@Dao
public interface MemberProfileDao {

    default long insert(MemberProfileEntity memberProfileEntity){
        return insertQ(memberProfileEntity.getName(),
                memberProfileEntity.getGender(),
                memberProfileEntity.getEmail(),
                memberProfileEntity.getPassword(),
                memberProfileEntity.getCounty(),
                memberProfileEntity.getTown(),
                memberProfileEntity.getPhone(),
                memberProfileEntity.getTokenId());
    }

    @Query("Insert into MemberProfile('name', 'gender', 'email', 'password', 'county', 'town', 'phone', 'tokenId') values(:name, :gender, :email, :password, :county, :town, :phone, :tokenId)")
    long insertQ(String name, int gender,String email, String password, int county, int town, String phone, String tokenId);

    @Query("DELETE FROM MemberProfile WHERE email = :email")
    void delUser(String email);

    @Query("SELECT * FROM MemberProfile WHERE email = :email AND password = :password")
    LiveData<MemberProfileEntity> getUser(String email, String password);

}
