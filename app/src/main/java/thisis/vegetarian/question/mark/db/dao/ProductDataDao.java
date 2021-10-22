package thisis.vegetarian.question.mark.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;

@Dao
public interface ProductDataDao {

    default long insert(IVF_ProductDataEntity ivf_productDataEntity){
        return insertQ(ivf_productDataEntity.getBarcode(),
                ivf_productDataEntity.getName(),
                ivf_productDataEntity.getCategory(),
                ivf_productDataEntity.getOrigin(),
                ivf_productDataEntity.getVegetarian(),
                ivf_productDataEntity.getRemark(),
                ivf_productDataEntity.getStatus());
    };

    @Query("Insert into IVF_Product('barcode', 'name', 'category', 'origin', 'vegetarian', 'remark', 'status') values(:barcode, :name, :category, :origin, :vegetarian, :remark, :status)")
    long insertQ(String barcode, String name, int category, String origin, int vegetarian, String remark, int status);

    @Update
    void update(IVF_ProductDataEntity ivf_productDataEntity);

    @Delete
    void delete(IVF_ProductDataEntity ivf_productDataEntity);

    @Query("DELETE FROM IVF_Product")
    void deleteAll();

    @Query("SELECT * FROM IVF_Product")
    LiveData<List<IVF_ProductDataEntity>> getAll();

    //前十大熱門
    @Query("SELECT * FROM IVF_Product")
    LiveData<List<IVF_ProductDataEntity>> getTopSearch();

    //類別篩選
    @Query("SELECT * FROM IVF_Product WHERE category LIKE :category_id ")
    LiveData<List<IVF_ProductDataEntity>> getCategory(int category_id);
}
