package thisis.vegetarian.question.mark.data;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource loginDataSource;

    private LoginRepository(LoginDataSource dataSource){
        this.loginDataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance != null){
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }
}
