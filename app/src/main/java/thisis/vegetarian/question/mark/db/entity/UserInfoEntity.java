package thisis.vegetarian.question.mark.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "User_Info")
public class UserInfoEntity {

    private String advertisingId;

    private String tokenId;

    private String create_at;

    private String update_at;

    public UserInfoEntity(){}

    @Ignore
    public UserInfoEntity(String advertisingId, String tokenId){
        this.advertisingId = advertisingId;
        this.tokenId = tokenId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getCreate_at() {
        return create_at;
    }

    public String getUpdate_at() {
        return update_at;
    }
}
