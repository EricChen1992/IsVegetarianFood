package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thisis.vegetarian.question.mark.R;
import thisis.vegetarian.question.mark.data.DataUserRepository;
import thisis.vegetarian.question.mark.data.DataUserSource;
import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.entity.MemberProfileEntity;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;
import thisis.vegetarian.question.mark.model.SignupEditStatus;
import thisis.vegetarian.question.mark.model.UserRepositoryCallback;

public class IVFSignupViewModel extends ViewModel {

    public ObservableField<List<String>> countyString = new ObservableField<>();
    public ObservableField<List<String>> townString = new ObservableField<>();
    public ObservableInt selectedPosition = new ObservableInt(0);
    public List<List<String>> townListContainer = new ArrayList<>();
    public ObservableField<Boolean> townClickable = new ObservableField<>(false);
    Integer[] townListName = {  R.array.taipei_city_town_zh,
                                R.array.new_taipei_city_town_zh,
                                R.array.keelung_city_town_zh,
                                R.array.taoyuan_city_town_zh,
                                R.array.hsinchu_town_zh,
                                R.array.hsinchu_city_town_zh,
                                R.array.miaoli_town_zh,
                                R.array.taichung_city_town_zh,
                                R.array.nantou_town_zh,
                                R.array.changhua_town_zh,
                                R.array.yunlin_town_zh,
                                R.array.chiayi_town_zh,
                                R.array.chiayi_city_town_zh,
                                R.array.tainan_city_town_zh,
                                R.array.kaohsiung_city_town_zh,
                                R.array.pingtung_town_zh,
                                R.array.yilan_town_zh,
                                R.array.hualien_town_zh,
                                R.array.taitung_zh,
                                R.array.penghu_town_zh,
                                R.array.kinmen_town_zh,
                                R.array.lienchiang_town_zh};

    private DataUserRepository repository;

    private MutableLiveData<SignupEditStatus> signupEditStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> showProgressView = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> signupStatus = new MutableLiveData<>(false);

    public IVFSignupViewModel(){}

    public IVFSignupViewModel(Application application, DataUserRepository repository){
        initSpinnerList(application.getResources());//set County and Town list on spinner
        this.repository = repository;
    }

    public void signupDataChange(String name, String email, String password, String confirm_password, String phone){

        if (!isNameValid(name)){
            signupEditStatus.setValue(new SignupEditStatus(R.string.ivf_signup_edit_name_error, null, null, null, null, null));
        } else if (!isEmailValid(email) && !email.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(null, R.string.ivf_signup_edit_email_error, null, null, null, null));
        } else if (!isPasswordValid(password) && !password.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(null, null, R.string.ivf_signup_edit_password_error, null, null, null));
        } else if (!isPasswordMatch(password, confirm_password) && !confirm_password.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(null, null, null, R.string.ivf_signup_edit_password_match_error, null, null));
        } else if (!isPhoneValid(phone) && !phone.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(null, null, null, null, R.string.ivf_signup_edit_phone_error, null));
        } else if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || phone.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(false));
        } else {
            signupEditStatus.setValue(new SignupEditStatus(true));
        }
    }

    public MutableLiveData<SignupEditStatus> getSignupEditStatus() {
        return signupEditStatus;
    }

    private boolean isNameValid(String user_name){
        return null != user_name && user_name.length() < 20;
    }

    private boolean isEmailValid(String user_email){
        if (null == user_email) return false;

        if (user_email.contains("@")){
            return Patterns.EMAIL_ADDRESS.matcher(user_email).matches();
        }

        return false;
    }

    private boolean isPasswordMatch(String user_password, String user_confirm_password){
        return user_password.equals(user_confirm_password);
    }

    private boolean isPasswordValid(String user_password){
        return null != user_password && user_password.length() > 5;
    }

    private boolean isPhoneValid(String user_phone){
        return null != user_phone && Patterns.PHONE.matcher(user_phone).matches();
    }

    public void signup(String name, int gender, String email, String password, int county, int town, String phone){
        //check all spinner have select.
        if (!doubleCheckValue(name, gender, email, password, county, town, phone)) return;
        //check all edittext have empty.
        if (name.isEmpty() && email.isEmpty() && password.isEmpty() && phone.isEmpty()) return;

        showProgressView.postValue(true);
        repository.signup(new MemberProfileEntity(name, gender, email, password, county, town, phone), new UserRepositoryCallback.LoginCallback() {
            @Override
            public void onResult(Boolean result) {
                signupStatus.postValue(result);
                //set mutableLiveData show ProgressView
                showProgressView.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getShowProgressView() {
        return showProgressView;
    }

    public MutableLiveData<Boolean> getSignupStatus() {
        return signupStatus;
    }

    private boolean doubleCheckValue(String check_name, int check_gender, String check_email, String check_password, int check_county, int check_town, String check_phone){
        if (check_name.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(R.string.ivf_signup_edit_name_empty_error, null, null, null, null, null));
            return false;
        }else if (check_email.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(null, R.string.ivf_signup_edit_email_empty_error, null, null, null, null));
            return false;
        }else if (check_password.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(null, null, R.string.ivf_signup_edit_password_empty_error, null, null, null));
            return false;
        }else if (check_phone.isEmpty()){
            signupEditStatus.setValue(new SignupEditStatus(null, null, null, null, R.string.ivf_signup_edit_password_empty_error, null));
            return false;
        }else if (check_gender <= 0 ){
            signupEditStatus.setValue(new SignupEditStatus(null, null, null, null, null, R.string.ivf_signup_edit_gender_error));
            return false;
        } else if (check_county <= 0){
            signupEditStatus.setValue(new SignupEditStatus(null, null, null, null, null, R.string.ivf_signup_edit_county_error));
            return false;
        } else if (check_town <= 0){
            signupEditStatus.setValue(new SignupEditStatus(null, null, null, null, null, R.string.ivf_signup_edit_town_error));
            return false;
        }
        return true;
    }

    private void initSpinnerList(Resources resources){
        if (resources != null){
            //set County list
            List<String> county = Arrays.asList(resources.getStringArray(R.array.taiwan_county_name_zh));

            //set all town list in container
            for(int i=0; i < townListName.length; i++){
                townListContainer.add(Arrays.asList(resources.getStringArray(townListName[i])));
            }

            this.countyString.set(county);//set county list
            this.townString.set(townListContainer.get(0));//set town list

            setSelectedPositionOnChange();//set change callback
        }
    }

    private void setSelectedPositionOnChange(){
        //Use onPropertyChangedCallback then change town list.
        selectedPosition.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                //set selectedPosition not 0 then change town list and enable town list.
                if (selectedPosition.get() != 0){
                    townString.set(townListContainer.get(selectedPosition.get() - 1));
                    townClickable.set(true);
                } else {
                    townString.set(townListContainer.get(0));
                    townClickable.set(false);
                }
            }
        });
    }

    //set custom spinner content.
    @BindingAdapter("spinnerContent")
    public static void setSpinnerList(Spinner mSpinner, ObservableField<List<String>> contentList){
        ArrayAdapter arrayAdapter = new ArrayAdapter(mSpinner.getContext(), android.R.layout.simple_spinner_dropdown_item, contentList.get());
        mSpinner.setAdapter(arrayAdapter);

    }

    //set and get is custom selection position on spinner
    @BindingAdapter("selectionPosition")
    public static void setSpinnerSelectionPosition(Spinner mSpinner, int selectPosition){
        mSpinner.setSelection(selectPosition);
    }

    @InverseBindingAdapter(attribute = "selectionPosition")
    public static int getSpinnerSelectionPosition(Spinner mSpinner){
        return mSpinner.getSelectedItemPosition();
    }

    @BindingAdapter("selectionPositionAttrChanged")
    public static void setSpinnerOnChangeListener(Spinner mSpinner, InverseBindingListener listener){
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        Application application;
        public Factory(Application application){
            this.application = application;
        }
        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(IVFSignupViewModel.class)){
                return (T) new IVFSignupViewModel(application, DataUserRepository.getInstance(new DataUserSource(), IVF_Database.getInstance(application)));
            } else {
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }
    }

}
