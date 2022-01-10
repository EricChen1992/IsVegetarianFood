package thisis.vegetarian.question.mark.data;

import android.util.Log;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import thisis.vegetarian.question.mark.db.entity.MemberProfileEntity;
import thisis.vegetarian.question.mark.model.LoginUser;

public class DataUserSource {

    //set message
    public ResultType<LoginUser> login(String account, String password){
        try {
            if (account != null && password != null){
                return new ResultType.Success(new LoginUser(java.util.UUID.randomUUID().toString(), "Eric", "eric@clickforce.com.tw", "udjd-kwhu-kaud-loii"));
            } else {
                return new ResultType.Error(new LoginException("Login Fail"));
            }
        } catch (Exception e){
            return new ResultType.Error(new IOException("Login Error", e));
        }
    }

    public ResultType<Boolean> signup(MemberProfileEntity memberProfileEntity){
        return new ResultType.Success<>(false);
    }
}
