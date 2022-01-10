package thisis.vegetarian.question.mark.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User_Info")
public class UserInfoEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String userId;

    private String displayName;

    private String email;

    private String tokenId;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String create_at;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String update_at;

    public UserInfoEntity(){}

    @Ignore
    public UserInfoEntity(String userId, String displayName, String email, String tokenId){
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.tokenId = tokenId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
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
