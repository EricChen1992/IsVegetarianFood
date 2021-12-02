package thisis.vegetarian.question.mark.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import thisis.vegetarian.question.mark.R;
import thisis.vegetarian.question.mark.data.LoginDataSource;
import thisis.vegetarian.question.mark.data.LoginRepository;
import thisis.vegetarian.question.mark.data.ResultType;
import thisis.vegetarian.question.mark.model.LoginEditStatus;
import thisis.vegetarian.question.mark.model.LoginUser;

public class IVFLoginViewModel extends ViewModel {
    private LoginRepository loginRepository;

    private MutableLiveData<LoginEditStatus> loginEditStatus = new MutableLiveData<>();
    private MutableLiveData<LoginUser> loginResult = new MutableLiveData<>();

    IVFLoginViewModel(LoginRepository repository){
        this.loginRepository = repository;
    }

    public void loginDataChange(String account_data, String password_data){
        if (!isAccountValid(account_data)){
            loginEditStatus.setValue(new LoginEditStatus(R.string.ivf_login_edit_account_error, null));
        } else if (!isPasswordValid(password_data)){
            loginEditStatus.setValue(new LoginEditStatus(null, R.string.ivf_login_edit_password_error));
        } else {
            loginEditStatus.setValue(new LoginEditStatus(true));
        }
    }

    private boolean isAccountValid(String account){
        if (null == account) return false;

        if (account.contains("@")){
            return Patterns.EMAIL_ADDRESS.matcher(account).matches();
        }

        return false;
    }

    private boolean isPasswordValid(String password){
        return null != password && password.trim().length() > 5;
    }

    public MutableLiveData<LoginEditStatus> getLoginEditStatus() {
        return loginEditStatus;
    }

    public void login(String account, String password){
        ResultType<LoginUser> result = loginRepository.login(account, password);
        if (result instanceof ResultType.Success){
            LoginUser loginUser = ((ResultType.Success<LoginUser>) result).getData();
            loginResult.setValue(loginUser);
        } else if (result instanceof ResultType.Error){
            String errorMsg = ((ResultType.Error) result).getException().getMessage();
            if (errorMsg.equals("Login Fail")){
                loginResult.setValue(new LoginUser(R.string.ivf_login_error));
            } else {
                loginResult.setValue(new LoginUser(R.string.ivf_logging_error));
            }
        }
    }

    public MutableLiveData<LoginUser> getLoginResult() {
        return loginResult;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(IVFLoginViewModel.class)){
                return (T) new IVFLoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }
}
