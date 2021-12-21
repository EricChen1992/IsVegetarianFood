package thisis.vegetarian.question.mark.model;

import androidx.annotation.NonNull;

public class SignupEditStatus {
    @NonNull
    private Integer nameError;

    @NonNull
    private Integer emailError;

    @NonNull
    private Integer passwordError;

    @NonNull
    private Integer confirmPasswordError;

    @NonNull
    private Integer phoneError;

    @NonNull
    private Integer spinnerError;

    private boolean dataValid;

    public SignupEditStatus(@NonNull Integer name_error, @NonNull Integer email_error, @NonNull Integer password_error, @NonNull Integer confirm_password_error, @NonNull Integer phone_error, @NonNull Integer spinner_error){
        this.nameError = name_error;
        this.emailError = email_error;
        this.passwordError = password_error;
        this.confirmPasswordError = confirm_password_error;
        this.phoneError = phone_error;
        this.spinnerError = spinner_error;
        this.dataValid = false;
    }

    public SignupEditStatus(boolean data_valid){
        this.nameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.confirmPasswordError = null;
        this.phoneError = null;
        this.spinnerError = null;
        this.dataValid = data_valid;
    }

    @NonNull
    public Integer getNameError() {
        return nameError;
    }

    @NonNull
    public Integer getEmailError() {
        return emailError;
    }

    @NonNull
    public Integer getPasswordError() {
        return passwordError;
    }

    @NonNull
    public Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }

    @NonNull
    public Integer getPhoneError() {
        return phoneError;
    }

    @NonNull
    public Integer getSpinnerError() {
        return spinnerError;
    }

    public boolean isDataValid() {
        return dataValid;
    }
}
