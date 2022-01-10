package thisis.vegetarian.question.mark.model;

public class LoginUser {
    private String userId;
    private String userDisplayName;
    private String email;
    private String userToken;
    private Integer error;
    public LoginUser(String user_id, String user_display_name, String user_email, String user_token){
        this.userId = user_id;
        this.userDisplayName = user_display_name;
        this.email = user_email;
        this.userToken = user_token;
        this.error = null;
    }

    public LoginUser(Integer error){
        this.userId = null;
        this.userDisplayName = null;
        this.email = null;
        this.userToken = null;
        this.error = error;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserToken() {
        return userToken;
    }

    public Integer getError() {
        return error;
    }
}
