package thisis.vegetarian.question.mark.model;

import androidx.annotation.Nullable;

public class LoginEditStatus {
    @Nullable
    private Integer accountError;
    @Nullable
    private Integer passwordError;

    private boolean dataValid;

    public LoginEditStatus(@Nullable Integer account_error, @Nullable Integer password_error){
        this.accountError = account_error;
        this.passwordError = password_error;
        this.dataValid = false;
    }

    public LoginEditStatus(boolean data_valid){
        this.accountError = null;
        this.passwordError = null;
        this.dataValid = data_valid;
    }

    @Nullable
    public Integer getAccountError() {
        return accountError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    public boolean getDataValid() {
        return dataValid;
    }

}
