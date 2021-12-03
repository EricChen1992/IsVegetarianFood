package thisis.vegetarian.question.mark.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.dao.ProductDataDao;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;

public class DataProductRepository {
    private ProductDataDao productDataDao;
    private ExecutorService executorService;

    public DataProductRepository(Application application){
        IVF_Database ivfDatabase = IVF_Database.getInstance(application);
        productDataDao = ivfDatabase.productDataDao();

        executorService = Executors.newFixedThreadPool(1);
    }

    public void insert(IVF_ProductDataEntity ivfProductDataEntity, InsertCallback callback){
        if (executorService == null) executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long r = productDataDao.insert(ivfProductDataEntity);
                    Thread.sleep(2000);//模擬上傳等待
                    callback.insertFinish(r > -1 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    callback.insertFinish(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.insertFinish(false);
                }
            }
        });
    }

    public LiveData<List<IVF_ProductDataEntity>> getTopSearchProduct(){
        return productDataDao.getTopSearch();
    }

    public LiveData<List<IVF_ProductDataEntity>> getCategoryProduct(int categoryId){
        return productDataDao.getCategory(categoryId);
    }

}
