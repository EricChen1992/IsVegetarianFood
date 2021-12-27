package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import thisis.vegetarian.question.mark.IVFCategoryFragment;
import thisis.vegetarian.question.mark.IVFViewPage2Adapter;
import thisis.vegetarian.question.mark.data.DataProductRepository;
import thisis.vegetarian.question.mark.data.DataUserRepository;
import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;

public class IVFMainViewModel extends AndroidViewModel {
    private DataProductRepository dataProductRepository;
    private DataUserRepository dataUserRepository;
    private MutableLiveData<IVFViewPage2Adapter> adapterMutableLiveData = new MutableLiveData<>();
    public IVFMainViewModel(@NonNull Application application) {
        super(application);
        this.dataProductRepository = new DataProductRepository(application);
        this.dataUserRepository = new DataUserRepository(IVF_Database.getInstance(application));
    }

    public void setFragmentList(IVFViewPage2Adapter adapter){
        if (null != adapter){
            //建立各類別Fragment
            String[] fragmentList = {"topSearch", "cookies", "candy", "drinks", "instantNoodles", "Ingredients", "cannedFood", "jam", "other"};
            for (String fragmentName : fragmentList){
                adapter.addFragment(new IVFCategoryFragment(fragmentName));
            }
            adapterMutableLiveData.postValue(adapter);
        }

    }

    public MutableLiveData<IVFViewPage2Adapter> getAdapterMutableLiveData() {
        return adapterMutableLiveData;
    }

    public LiveData<List<UserInfoEntity>> getCheckUser() {
        return dataUserRepository.getLoginUser();
    }

    public void insert(IVF_ProductDataEntity ivf_productDataEntity){
        this.dataProductRepository.insert(ivf_productDataEntity, new InsertCallback() {
            @Override
            public void insertFinish(Boolean result) {

            }
        });
    }
}
