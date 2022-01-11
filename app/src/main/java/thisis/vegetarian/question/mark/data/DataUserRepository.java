package thisis.vegetarian.question.mark.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import thisis.vegetarian.question.mark.IVFHashEncode;
import thisis.vegetarian.question.mark.R;
import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.dao.MemberProfileDao;
import thisis.vegetarian.question.mark.db.dao.UserLoginInfoDao;
import thisis.vegetarian.question.mark.db.entity.MemberProfileEntity;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;
import thisis.vegetarian.question.mark.model.LoginCallback;
import thisis.vegetarian.question.mark.model.LoginUser;
import thisis.vegetarian.question.mark.model.UserRepositoryCallback;

public class DataUserRepository {

    private static volatile DataUserRepository instance;

    private DataUserSource dataUserSource;

    private UserLoginInfoDao userLoginInfoDao;

    private MemberProfileDao memberProfileDao;

    private final ExecutorService executorService;

    public DataUserRepository(IVF_Database ivf_productDatabase){
        this.userLoginInfoDao = ivf_productDatabase.userLoginInfoDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    private DataUserRepository(DataUserSource dataSource, IVF_Database ivf_productDatabase){
        this.dataUserSource = dataSource;
        this.userLoginInfoDao = ivf_productDatabase.userLoginInfoDao();
        this.memberProfileDao = ivf_productDatabase.userInfoDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static DataUserRepository getInstance(DataUserSource dataSource, IVF_Database ivfProductDatabase) {
        if (instance == null){
            instance = new DataUserRepository(dataSource, ivfProductDatabase);
        }
        return instance;
    }

    public void login(String account, String password, LoginCallback callback){
        //On-line
//        ResultType<LoginUser> result = dataUserSource.login(account, password);

        //Test get Faker User
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    MemberProfileEntity m = memberProfileDao.getUser(account, IVFHashEncode.generatePassword(password));
                    Thread.sleep(2000);//Fake loading
                    if (null != m) {
                        callback.onLoginResult(new ResultType.Success(new LoginUser(String.valueOf(m.getId()), m.getName(), m.getEmail(), m.getTokenId())));
                        userLoginInfoDao.deleteAll();
                        userLoginInfoDao.insert(new UserInfoEntity(String.valueOf(m.getId()), m.getName(), m.getEmail(), m.getTokenId()));
                    } else {
                        callback.onLoginResult(new ResultType.Error(new LoginException("Login Fail")));
                    }
                } catch (Exception e){
                    e.getStackTrace();
                }
            }
        });
    }

    public void getUser(UserRepositoryCallback.GetUserCallback callback){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                List<UserInfoEntity> list = userLoginInfoDao.getUser();
                if (list.size() > 0){
                    callback.onResult(list.get(0));
                }
            }
        });
    }
    public void signup(MemberProfileEntity memberProfileEntity, UserRepositoryCallback.LoginCallback callback){
        //on-line DB
//        ResultType<Boolean> result = dataUserSource.signup(memberProfileEntity);
        //Save to DB for test
        MemberProfileEntity insertMpe = memberProfileEntity;
        insertMpe.setPassword(IVFHashEncode.generatePassword(memberProfileEntity.getPassword()));
        insertMpe.setTokenId(IVFHashEncode.generateToken(memberProfileEntity.getName()));

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long r = memberProfileDao.insert(insertMpe);
                    Thread.sleep(2000);
                    callback.onResult(r > -1);
                } catch (Exception e){
                    callback.onResult(false);
                }
            }
        });

    }

    public void logout(UserRepositoryCallback.LogoutCallback callback){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    userLoginInfoDao.deleteAll();
                    callback.onResult(true);
                } catch (Exception e){
                    callback.onResult(false);
                }
            }
        });
    }

}
