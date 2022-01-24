package thisis.vegetarian.question.mark.model;

import thisis.vegetarian.question.mark.data.ResultType;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;

public interface DataProductRepositoryCallback {
    interface InsertProductCallback{
        void onResult(Boolean result);
    }

//    interface GetProductCallback{
//        void onResult(IVF_ProductDataEntity product);
//    }

    interface GetProductCallback{
        void onResult(ResultType result);
    }
}
