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
import thisis.vegetarian.question.mark.model.LoginUser;

public class DataUserRepository {

    private static volatile DataUserRepository instance;

    private DataUserSource dataUserSource;

    private UserLoginInfoDao userLoginInfoDao;

    private MemberProfileDao memberProfileDao;

    private final ExecutorService executorService;

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

    public ResultType<LoginUser> login(String account, String password){
        ResultType<LoginUser> result = dataUserSource.login(account, password);
        if (result instanceof ResultType.Success){
            //if success then set user info to db
            LoginUser successResult = ((ResultType.Success<LoginUser>) result).getData();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    userLoginInfoDao.deleteAll();
                    userLoginInfoDao.insert(new UserInfoEntity(successResult.getUserId(), successResult.getUserDisplayName(), successResult.getUserToken()));
                }
            });
        }
        return result;
    }

    public LiveData<List<UserInfoEntity>> getLoginUser(){
        return userLoginInfoDao.getAll();
    }

    public void signup(MemberProfileEntity memberProfileEntity, InsertCallback callback){
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
                    callback.insertFinish(r > -1);
                } catch (Exception e){
                    callback.insertFinish(false);
                }
            }
        });

    }

}
