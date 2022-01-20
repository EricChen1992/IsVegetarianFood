package thisis.vegetarian.question.mark.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

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
    MemberProfileEntity getUser(String email, String password);

    @Query("SELECT * FROM MemberProfile WHERE email = :email AND tokenId = :token")
    MemberProfileEntity getUserInfo(String email, String token);

    @Query("UPDATE memberprofile SET name =  :name,county = :county, town = :town WHERE email = :email AND tokenId = :token")
    int updateMember(String name, int county, int town, String email, String token);

//    @Update
//    long updateUser(MemberProfileEntity memberProfileEntity);
}
