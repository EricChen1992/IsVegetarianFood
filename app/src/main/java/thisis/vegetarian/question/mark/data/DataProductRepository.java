package thisis.vegetarian.question.mark.data;

import android.accounts.AuthenticatorException;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.dao.ProductDataDao;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.db.entity.MemberProfileEntity;
import thisis.vegetarian.question.mark.model.DataProductRepositoryCallback;
import thisis.vegetarian.question.mark.model.InfoProduct;

public class DataProductRepository {
    private ProductDataDao productDataDao;
    private ExecutorService executorService;

    public DataProductRepository(Application application){
        IVF_Database ivfDatabase = IVF_Database.getInstance(application);
        productDataDao = ivfDatabase.productDataDao();

        executorService = Executors.newFixedThreadPool(1);
    }

    public void insert(IVF_ProductDataEntity ivfProductDataEntity, DataProductRepositoryCallback.InsertProductCallback callback){
        if (executorService == null) executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long r = productDataDao.insert(ivfProductDataEntity);
                    Thread.sleep(2000);//模擬上傳等待
                    callback.onResult(r > -1 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    callback.onResult(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onResult(false);
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

    public void getSearchProduct(String barCode, String email,String token, DataProductRepositoryCallback.GetProductCallback callback){
        if (executorService == null) executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long auth = productDataDao.authenticateUser(email, token);
                    Thread.sleep(2000);
                    if (auth > 0) {
                        IVF_ProductDataEntity productDataEntity = productDataDao.getSearchProduct(barCode);
                        if (productDataEntity != null){
                            callback.onResult(new ResultType.Success(new InfoProduct(productDataEntity)));
                        } else {
                            callback.onResult(new ResultType.Error(new Exception("Not find product")));
                        }
                    } else {
                        callback.onResult(new ResultType.Error(new AuthenticatorException("User auth fail")));
                    }
                } catch (Exception e){
                    callback.onResult(new ResultType.Error(new Exception("")));
                }

            }
        });
    }
}
