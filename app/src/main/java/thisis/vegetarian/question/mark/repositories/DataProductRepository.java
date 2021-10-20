package thisis.vegetarian.question.mark.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thisis.vegetarian.question.mark.db.IVF_ProductDatabase;
import thisis.vegetarian.question.mark.db.dao.ProductDataDao;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;

public class DataProductRepository {
    private ProductDataDao productDataDao;
    private LiveData<List<IVF_ProductDataEntity>> topSearchProduct;
    private LiveData<List<IVF_ProductDataEntity>> categoryProduct;
    private ExecutorService executorService;

    public DataProductRepository(Application application){
        IVF_ProductDatabase ivfProductDatabase = IVF_ProductDatabase.getInstance(application);
        productDataDao = ivfProductDatabase.productDataDao();

        executorService = Executors.newFixedThreadPool(1);
    }

    public void insert(IVF_ProductDataEntity ivfProductDataEntity){
        if (executorService == null) executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                productDataDao.insert(ivfProductDataEntity);
            }
        });
    }

    public LiveData<List<IVF_ProductDataEntity>> getTopSearchProduct(){
        topSearchProduct = productDataDao.getTopSearch();
        return topSearchProduct;
    }

    public LiveData<List<IVF_ProductDataEntity>> getCategoryProduct(int categoryId){
        categoryProduct = productDataDao.getCategory(categoryId);
        return categoryProduct;
    }

}
