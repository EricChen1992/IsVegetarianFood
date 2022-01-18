package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thisis.vegetarian.question.mark.IVFCategoryFragment;
import thisis.vegetarian.question.mark.IVFViewPage2Adapter;
import thisis.vegetarian.question.mark.R;
import thisis.vegetarian.question.mark.data.DataProductRepository;
import thisis.vegetarian.question.mark.data.DataUserRepository;
import thisis.vegetarian.question.mark.data.ResultType;
import thisis.vegetarian.question.mark.db.IVF_Database;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.db.entity.MemberProfileEntity;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.InsertCallback;
import thisis.vegetarian.question.mark.model.MemberInfo;
import thisis.vegetarian.question.mark.model.UserRepositoryCallback;

public class IVFMainViewModel extends AndroidViewModel {
    private DataProductRepository dataProductRepository;
    private DataUserRepository dataUserRepository;
    private MutableLiveData<IVFViewPage2Adapter> adapterMutableLiveData = new MutableLiveData<>();
    public ObservableField<String> userName = new ObservableField<>("unKnow");
    public ObservableField<String> userId = new ObservableField<>("unKnow");
    public ObservableField<UserInfoEntity> userInfo = new ObservableField<>();
    public ObservableBoolean progressStatus = new ObservableBoolean(false);
    private MutableLiveData<Boolean> logoutStatus = new MutableLiveData<>(null);
    public MutableLiveData<MemberInfo> userRemoteInfoResult = new MutableLiveData<>();

    public ObservableField<List<String>> countyString = new ObservableField<>();
    public ObservableField<List<String>> townString = new ObservableField<>();
    public ObservableInt selectedPosition = new ObservableInt(0);
    public ObservableInt selectedPosition2 = new ObservableInt(0);
    public List<List<String>> townListContainer = new ArrayList<>();
    final Integer[] townListName = {  R.array.taipei_city_town_zh,
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

    public IVFMainViewModel(@NonNull Application application) {
        super(application);
        this.dataProductRepository = new DataProductRepository(application);
        this.dataUserRepository = new DataUserRepository(IVF_Database.getInstance(application));
        initSpinnerList(application.getResources());//set County and Town list on spinner
    }

    public void setFragmentList(IVFViewPage2Adapter adapter){
        progressStatus.set(true);
        if (null != adapter){
            //建立各類別Fragment
            String[] fragmentList = {"topSearch", "cookies", "candy", "drinks", "instantNoodles", "Ingredients", "cannedFood", "jam", "other"};
            for (String fragmentName : fragmentList){
                adapter.addFragment(new IVFCategoryFragment(fragmentName));
            }
            adapterMutableLiveData.postValue(adapter);
        }
        progressStatus.set(false);

    }

    public MutableLiveData<IVFViewPage2Adapter> getAdapterMutableLiveData() {
        return adapterMutableLiveData;
    }

    public void getCheckUser(){
        dataUserRepository.getUser(new UserRepositoryCallback.GetUserCallback() {
            @Override
            public void onResult(UserInfoEntity userInfoEntity) {
                userName.set(userInfoEntity.getDisplayName());
                userId.set(String.valueOf(userInfoEntity.getId()));
                userInfo.set(userInfoEntity);
            }
        });
    }

    //
    public void getMemberInfo(){
        progressStatus.set(true);

        dataUserRepository.getMemberInfo(userInfo.get().getEmail(), userInfo.get().getTokenId(), new UserRepositoryCallback.GetUserInfoCallback() {
            @Override
            public void onResult(ResultType resultType) {
                if (resultType instanceof ResultType.Success){
                    MemberInfo memberInfo = ((ResultType.Success<MemberInfo>) resultType).getData();
                    userRemoteInfoResult.postValue(memberInfo);
                    selectedPosition.set(memberInfo.getUserCounty());//set user county on spinner
                    selectedPosition2.set(memberInfo.getUserTown());//set user town on spinner
                } else {
                    String errorMsg = ((ResultType.Error) resultType).getException().getMessage();
                    if (errorMsg.equals("Get Fail")){
                        userRemoteInfoResult.postValue(new MemberInfo(R.string.ivf_main_member_remote_get_fail_zh));
                    } else {
                        userRemoteInfoResult.postValue(new MemberInfo(R.string.ivf_main_member_remote_fail_zh));
                    }
                }
                progressStatus.set(false);
            }
        });
    }

    //Return Remote User info
    public MutableLiveData<MemberInfo> getUserRemoteInfoResult() {
        return userRemoteInfoResult;
    }

    public void insert(IVF_ProductDataEntity ivf_productDataEntity){
        this.dataProductRepository.insert(ivf_productDataEntity, new InsertCallback() {
            @Override
            public void insertFinish(Boolean result) {

            }
        });
    }

    public void logout(){
        progressStatus.set(true);
        this.dataUserRepository.logout(new UserRepositoryCallback.LogoutCallback() {
            @Override
            public void onResult(Boolean result) {
                logoutStatus.postValue(result);
                progressStatus.set(false);
            }
        });
    }

    public MutableLiveData<Boolean> getLogoutStatus() {
        return logoutStatus;
    }

    //Count
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
                } else {
                    townString.set(townListContainer.get(0));
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
}
