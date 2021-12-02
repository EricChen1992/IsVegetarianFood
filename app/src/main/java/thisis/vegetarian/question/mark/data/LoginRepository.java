package thisis.vegetarian.question.mark.data;

import thisis.vegetarian.question.mark.model.LoginUser;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource loginDataSource;

    private LoginRepository(LoginDataSource dataSource){
        this.loginDataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null){
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public ResultType<LoginUser> login(String account, String password){
        ResultType<LoginUser> result = loginDataSource.login(account, password);
        if (result instanceof ResultType.Success){
            //if success then set user info to db
        }
        return result;
    }
}
