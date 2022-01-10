package thisis.vegetarian.question.mark.model;

import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;

public interface UserRepositoryCallback {
    interface DatabaseCallback {
        void onInsertResult(Boolean result);
    }
    interface UserCallback {
        void onGetUserResult(UserInfoEntity userInfoEntity);
    }
}
