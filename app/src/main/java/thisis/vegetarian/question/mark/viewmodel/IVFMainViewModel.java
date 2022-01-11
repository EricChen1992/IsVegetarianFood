package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import thisis.vegetarian.question.mark.IVFCategoryFragment;
import thisis.vegetarian.question.mark.IVFViewPage2Adapter;
import thisis.vegetarian.question.mark.data.DataProductRepository;
import thisis.vegetarian.question.mark.data.DataUserRepository;
import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;
import thisis.vegetarian.question.mark.model.UserRepositoryCallback;

public class IVFMainViewModel extends AndroidViewModel {
    private DataProductRepository dataProductRepository;
    private DataUserRepository dataUserRepository;
    private MutableLiveData<IVFViewPage2Adapter> adapterMutableLiveData = new MutableLiveData<>();
    public ObservableField<String> userName = new ObservableField<>("");
    public ObservableField<String> userId = new ObservableField<>("");
    public ObservableField<UserInfoEntity> userInfo = new ObservableField<>();
    public ObservableBoolean progressStatus = new ObservableBoolean(false);
    private MutableLiveData<Boolean> logoutStatus = new MutableLiveData<>(null);
    public IVFMainViewModel(@NonNull Application application) {
        super(application);
        this.dataProductRepository = new DataProductRepository(application);
        this.dataUserRepository = new DataUserRepository(IVF_Database.getInstance(application));
    }

    public void setFragmentList(IVFViewPage2Adapter adapter){
        progressStatus.set(true);
        if (null != adapter){
            //建立各類別Fragment
            String[] fragmentList = {"topSearch", "cookies", "candy", "drinks", "instantNoodles", "Ingredients", "cannedFood", "jam", "other"};
            for (String fragmentName : fragmentList){
                adapter.addFragment(new IVFCategoryFragment(fragmentName));
            }
            adapterMutableLiveData.postValue(adapter);
        }
        progressStatus.set(false);

    }

    public MutableLiveData<IVFViewPage2Adapter> getAdapterMutableLiveData() {
        return adapterMutableLiveData;
    }

    public void getCheckUser(){
        dataUserRepository.getUser(new UserRepositoryCallback.GetUserCallback() {
            @Override
            public void onResult(UserInfoEntity userInfoEntity) {
                userName.set(userInfoEntity.getDisplayName());
                userId.set(String.valueOf(userInfoEntity.getId()));
                userInfo.set(userInfoEntity);
            }
        });
    }

    public void insert(IVF_ProductDataEntity ivf_productDataEntity){
        this.dataProductRepository.insert(ivf_productDataEntity, new InsertCallback() {
            @Override
            public void insertFinish(Boolean result) {

            }
        });
    }

    public void logout(){
        progressStatus.set(true);
        this.dataUserRepository.logout(new UserRepositoryCallback.LogoutCallback() {
            @Override
            public void onResult(Boolean result) {
                logoutStatus.postValue(result);
                progressStatus.set(false);
            }
        });
    }

    public MutableLiveData<Boolean> getLogoutStatus() {
        return logoutStatus;
    }
}
