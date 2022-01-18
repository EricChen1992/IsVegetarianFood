package thisis.vegetarian.question.mark.model;

public class MemberInfo {
    private String userId;
    private String userDisplayName;
    private String userEmail;
    private int userCounty;
    private int userTown;
    private String userPhone;
    private String userToken;
    private String registrationDate;
    private Integer error;

    public MemberInfo(String user_id, String user_name, String user_email, int user_county, int user_town, String user_phone, String user_token, String userRegistrationDate){
        this.userId = user_id;
        this.userDisplayName = user_name;
        this.userEmail = user_email;
        this.userCounty = user_county;
        this.userTown = user_town;
        this.userPhone = user_phone;
        this.userToken = user_token;
        this.registrationDate = userRegistrationDate;
        this.error = null;
    }

    public MemberInfo(Integer error){
        this.userId = null;
        this.userDisplayName = null;
        this.userEmail = null;
        this.userCounty = 0;
        this.userTown = 0;
        this.userPhone = null;
        this.userToken = null;
        this.error = error;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserCounty() {
        return userCounty;
    }

    public int getUserTown() {
        return userTown;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public Integer getError() {
        return error;
    }
}
