package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.repositories.DataProductRepository;

public class IVFCategoryViewModel extends ViewModel {
    private String TAG;
    private DataProductRepository repository;
    // TODO: Implement the ViewModel


    public IVFCategoryViewModel(String TAG, DataProductRepository rpo) {
        this.TAG = TAG;
        this.repository = rpo;
    }

    public LiveData<List<IVF_ProductDataEntity>> getProduct(){
        if (this.TAG.equals("hotSearch")){
            return repository.getTopSearchProduct();
        } else if (this.TAG.equals("cookies")){
            return repository.getCategoryProduct(0);
        } else if (this.TAG.equals("candy")){
            return repository.getCategoryProduct(1);
        } else if (this.TAG.equals("drinks")){
            return repository.getCategoryProduct(2);
        } else if (this.TAG.equals("instantNoodles")){
            return repository.getCategoryProduct(3);
        } else if (this.TAG.equals("Ingredients")){
            return repository.getCategoryProduct(4);
        } else if (this.TAG.equals("cannedFood")){
            return repository.getCategoryProduct(5);
        } else if (this.TAG.equals("jam")){
            return repository.getCategoryProduct(6);
        } else if (this.TAG.equals("other")){
            return repository.getCategoryProduct(7);
        }
        return repository.getTopSearchProduct();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final String TAG;
        private final DataProductRepository dataProductRepository;

        public Factory(@NotNull String tag, Application application) {
            this.TAG = tag;
            this.dataProductRepository = new DataProductRepository(application);
        }

        @SuppressWarnings("unchecked")
        @NotNull
        @Override
        public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
            return (T) new IVFCategoryViewModel(TAG, dataProductRepository);
        }
    }
}