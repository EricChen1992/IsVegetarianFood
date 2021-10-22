package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;
import thisis.vegetarian.question.mark.repositories.DataProductRepository;


public class IVFCreateProductViewModel extends AndroidViewModel {
    public String TAG = "IVFCreateProductViewModel";
    private DataProductRepository repository;
    public MutableLiveData<Boolean> showProgress = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> updateType = new MutableLiveData<>(null);
    public IVFCreateProductViewModel(@NonNull Application application) {
        super(application);
        this.repository = new DataProductRepository(application);
    }

    public void insert(IVF_ProductDataEntity ivf_productDataEntity){
        showProgress.postValue(true);
        repository.insert(ivf_productDataEntity, new InsertCallback() {
            @Override
            public void insertFinish(Boolean result) {
                updateType.postValue(result);
                showProgress.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getUpdateType(){
        return updateType;
    }
}
