package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;
import thisis.vegetarian.question.mark.repositories.DataProductRepository;

public class IVFMainViewModel extends AndroidViewModel {
    private DataProductRepository repository;
    public IVFMainViewModel(@NonNull Application application) {
        super(application);
        this.repository = new DataProductRepository(application);
    }

    public void insert(IVF_ProductDataEntity ivf_productDataEntity){
        this.repository.insert(ivf_productDataEntity, new InsertCallback() {
            @Override
            public void insertFinish(Boolean result) {

            }
        });
    }
}
