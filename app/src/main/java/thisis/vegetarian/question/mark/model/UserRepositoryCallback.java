package thisis.vegetarian.question.mark.model;

import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;

public interface UserRepositoryCallback {

    interface LoginCallback{
        void onResult(Boolean result);
    }

    interface LogoutCallback{
        void onResult(Boolean result);
    }

    interface GetUserCallback{
        void onResult(UserInfoEntity userInfoEntity);
    }
}
