package thisis.vegetarian.question.mark.viewmodel;

import android.app.Application;
import android.content.res.Resources;
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
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thisis.vegetarian.question.mark.R;

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

    public IVFSignupViewModel(){}

    public IVFSignupViewModel(Application application){
        initSpinnerList(application.getResources());//set County and Town list on spinner

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
}
