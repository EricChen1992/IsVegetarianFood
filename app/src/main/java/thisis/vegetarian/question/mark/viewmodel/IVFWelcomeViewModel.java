package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import thisis.vegetarian.question.mark.data.DataUserSource;
import thisis.vegetarian.question.mark.data.DataUserRepository;
import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.UserRepositoryCallback;

public class IVFWelcomeViewModel extends ViewModel {
    private DataUserRepository dataUserRepository;
    public ObservableBoolean haveUser = new ObservableBoolean(false);
    public IVFWelcomeViewModel(DataUserRepository dataUserRepository){
        this.dataUserRepository = dataUserRepository;
    }

    public void getCheckUser(){
        dataUserRepository.getUser(new UserRepositoryCallback.GetUserCallback() {
            @Override
            public void onResult(UserInfoEntity userInfoEntity) {
                haveUser.set(!userInfoEntity.getDisplayName().equals(""));
            }
        });
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
                return (T) new IVFWelcomeViewModel(DataUserRepository.getInstance(new DataUserSource(), database));
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }

}
