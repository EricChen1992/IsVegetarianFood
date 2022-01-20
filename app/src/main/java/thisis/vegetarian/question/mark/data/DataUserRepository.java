package thisis.vegetarian.question.mark.data;

import android.util.Log;

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
import thisis.vegetarian.question.mark.model.MemberInfo;
import thisis.vegetarian.question.mark.model.UserRepositoryCallback;

public class DataUserRepository {

    private static volatile DataUserRepository instance;

    private DataUserSource dataUserSource;

    private UserLoginInfoDao userLoginInfoDao;

    private MemberProfileDao memberProfileDao;

    private final ExecutorService executorService;

    public DataUserRepository(IVF_Database ivf_productDatabase){
        this.userLoginInfoDao = ivf_productDatabase.userLoginInfoDao();
        this.memberProfileDao = ivf_productDatabase.userInfoDao();
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
                    callback.onLoginResult(new ResultType.Error(new Exception("")));
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

    public void getMemberInfo(final String user_email, final String user_token, UserRepositoryCallback.GetUserInfoCallback callback){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    MemberProfileEntity member = memberProfileDao.getUserInfo(user_email, user_token);
                    Thread.sleep(1000);
                    if (member != null) {
                        callback.onResult(new ResultType.Success(new MemberInfo(String.valueOf(member.getId()),
                                member.getName(),
                                member.getEmail(),
                                member.getCounty(),
                                member.getTown(),
                                member.getPhone(),
                                member.getTokenId(),
                                member.getCreate_at().split(" ")[0])));
                    } else {
                        new ResultType.Error(new Exception("Get Fail"));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    callback.onResult(new ResultType.Error(new Exception("")));
                }

            }
        });
    }

    public void updateMemberInfo(final String user_name,
                                 final int user_county,
                                 final int user_town,
                                 final String user_email,
                                 final String user_token,
                                 UserRepositoryCallback.UpdateUserInfoCallback callback){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    MemberProfileEntity m = memberProfileDao.getUserInfo(user_email, user_token);
                    if (m.getName().equals(user_name) && m.getCounty() == user_county && m.getTown() == user_town) {
                        callback.onResult(true);
                        return;
                    }

                    int re =  memberProfileDao.updateMember(user_name, user_county, user_town, user_email, user_token);
                    callback.onResult(re > 0);

                } catch (Exception e){
                    callback.onResult(false);
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
