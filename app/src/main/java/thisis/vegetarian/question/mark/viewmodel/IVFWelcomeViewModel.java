package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import thisis.vegetarian.question.mark.data.LoginDataSource;
import thisis.vegetarian.question.mark.data.LoginRepository;
import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;

public class IVFWelcomeViewModel extends ViewModel {
    private LoginRepository loginRepository;

    public IVFWelcomeViewModel(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }

    public LiveData<List<UserInfoEntity>> getCheckUser() {
        return loginRepository.getUser();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        IVF_Database database;
        public Factory(Application application){
            database = IVF_Database.getInstance(application);
        }

        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(IVFWelcomeViewModel.class)){
                return (T) new IVFWelcomeViewModel(LoginRepository.getInstance(new LoginDataSource(), database));
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }

}
