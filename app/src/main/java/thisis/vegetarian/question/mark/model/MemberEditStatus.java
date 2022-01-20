package thisis.vegetarian.question.mark.model;

import androidx.annotation.NonNull;

public class MemberEditStatus {
    @NonNull
    private Integer nameError;

    private boolean dataValid;

    public MemberEditStatus(@NonNull Integer name_error){
        this.nameError = name_error;
        this.dataValid = false;
    }

    public MemberEditStatus(boolean data_valid){
        this.nameError = null;
        this.dataValid = data_valid;
    }

    @NonNull
    public Integer getNameError() {
        return nameError;
    }

    public boolean isDataValid() {
        return dataValid;
    }
}
