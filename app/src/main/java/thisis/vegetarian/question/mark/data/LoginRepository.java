package thisis.vegetarian.question.mark.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.dao.UserInfoDao;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.LoginUser;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource loginDataSource;

    private UserInfoDao userInfoDao;

    private final ExecutorService executorService;

    private LoginRepository(LoginDataSource dataSource, IVF_Database ivf_productDatabase){
        this.loginDataSource = dataSource;
        this.userInfoDao = ivf_productDatabase.userInfoDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static LoginRepository getInstance(LoginDataSource dataSource, IVF_Database ivfProductDatabase) {
        if (instance == null){
            instance = new LoginRepository(dataSource, ivfProductDatabase);
        }
        return instance;
    }

    public ResultType<LoginUser> login(String account, String password){
        ResultType<LoginUser> result = loginDataSource.login(account, password);
        if (result instanceof ResultType.Success){
            //if success then set user info to db
            LoginUser successResult = ((ResultType.Success<LoginUser>) result).getData();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    userInfoDao.deleteAll();
                    userInfoDao.insert(new UserInfoEntity(successResult.getUserId(), successResult.getUserDisplayName(), successResult.getUserToken()));
                }
            });
        }
        return result;
    }

    public LiveData<List<UserInfoEntity>> getUser(){
        return userInfoDao.getAll();
    }
}
