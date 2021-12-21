package thisis.vegetarian.question.mark.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MemberProfile")
public class MemberProfileEntity {
    @PrimaryKey
    private long id;

    private String name;

    private int gender;

    private String email;

    private String password;

    private int county;

    private int town;

    private String phone;

    private String tokenId;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String create_at;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String update_at;

    public MemberProfileEntity(){}

    public MemberProfileEntity(String user_name, int user_gender, String user_email, String user_password, int user_county, int user_town, String user_phone){
        this.name = user_name;
        this.gender = user_gender;
        this.email = user_email;
        this.password = user_password;
        this.county = user_county;
        this.town = user_town;
        this.phone = user_phone;
    }

    public MemberProfileEntity(String user_name, int user_gender, String user_email, String user_password, int user_county, int user_town, String user_phone, String user_tokenId){
        this.name = user_name;
        this.gender = user_gender;
        this.email = user_email;
        this.password = user_password;
        this.county = user_county;
        this.town = user_town;
        this.phone = user_phone;
        this.tokenId = user_tokenId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCounty(int county) {
        this.county = county;
    }

    public void setTown(int town) {
        this.town = town;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getCounty() {
        return county;
    }

    public int getTown() {
        return town;
    }

    public String getPhone() {
        return phone;
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
