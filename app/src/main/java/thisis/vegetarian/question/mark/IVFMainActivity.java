package thisis.vegetarian.question.mark;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.math.MathUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;

import thisis.vegetarian.question.mark.databinding.ActivityIvfMainBinding;
import thisis.vegetarian.question.mark.databinding.NavigationIvfHeaderBinding;
import thisis.vegetarian.question.mark.databinding.NavigationIvfMemberBinding;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.model.InfoProduct;
import thisis.vegetarian.question.mark.model.MemberEditStatus;
import thisis.vegetarian.question.mark.model.MemberInfo;
import thisis.vegetarian.question.mark.model.Product;
import thisis.vegetarian.question.mark.viewmodel.IVFMainViewModel;

public class IVFMainActivity extends AppCompatActivity {
    public static final String EXTRA_BARCODE = "thisis.vegetarian.question.mark.EXTRA_BARCODE";
    private static final String SP_NAME = "ivf_value";
    private static final String SP_USER_NAME = "ivf_name";
    private static final String SP_USER_EMAIL = "ivf_email";
    private static final String SP_USER_TOKEN = "ivf_token";
    private static final String SP_USER_COUNTY = "ivf_county";
    private static final String SP_USER_TOWN = "ivf_town";
    private TabLayoutMediator tabLayoutMediator;
    private ActivityIvfMainBinding activityIvfMainBinding;
    private IVFMainViewModel mainViewModel;
    private ViewPager2 viewPager2;
    private BottomSheetBehavior<NavigationView> bottomSheetBehavior;
    private BottomSheetBehavior<NavigationView> infoBottomSheetBehavior;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIvfMainBinding = DataBindingUtil.setContentView(IVFMainActivity.this, R.layout.activity_ivf_main);
        mainViewModel = new ViewModelProvider(this).get(IVFMainViewModel.class);
        activityIvfMainBinding.setViewModel(mainViewModel);
        activityIvfMainBinding.setLifecycleOwner(this);
        sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        //建立ViewPage2
        viewPager2 = activityIvfMainBinding.mainViewPage2;

        //建立ViewPage2Adapter
        IVFViewPage2Adapter viewPage2Adapter = new IVFViewPage2Adapter(getSupportFragmentManager(), getLifecycle());

        //建立各類別Fragment
        mainViewModel.setFragmentList(viewPage2Adapter);
        mainViewModel.getAdapterMutableLiveData().observe(this, new Observer<IVFViewPage2Adapter>() {
            @Override
            public void onChanged(IVFViewPage2Adapter adapter) {
                //set viewpage2 adapter to viewpage2
                viewPager2.setAdapter(adapter);
                setTabLayoutMediator(viewPager2);
            }
        });

        activityIvfMainBinding.mainScannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(IVFMainActivity.this)
                        .setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
                        .setOrientationLocked(false)
                        .setCaptureActivity(IVFScannerActivity.class)
                        .initiateScan();
            }
        });

        //Set NavigationView
        bottomSheetBehavior = BottomSheetBehavior.from(activityIvfMainBinding.mainBottomNavigationView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //set Info NavigationView
        infoBottomSheetBehavior = BottomSheetBehavior.from(activityIvfMainBinding.userInfoBottomNavigationView);
        infoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        activityIvfMainBinding.mainScrim.setVisibility(View.GONE);
        activityIvfMainBinding.mainScrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                infoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
                int black = Color.BLACK;
                float baseAlpha = ResourcesCompat.getFloat(getResources(), R.dimen.material_emphasis_medium);
                float offset = (slideOffset - (-1f)) / (1f - (-1f)) * (1f - 0f) + 0f;
                int alpha = (int) MathUtils.lerp(0f, 255f, offset * baseAlpha);
                int color = Color.argb(alpha, Color.red(black), Color.green(black), Color.blue(black));
                activityIvfMainBinding.mainScrim.setBackgroundColor(color);
            }
        });

        infoBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
                int black = Color.BLACK;
                float baseAlpha = ResourcesCompat.getFloat(getResources(), R.dimen.material_emphasis_medium);
                float offset = (slideOffset - (-1f)) / (1f - (-1f)) * (1f - 0f) + 0f;
                int alpha = (int) MathUtils.lerp(0f, 255f, offset * baseAlpha);
                int color = Color.argb(alpha, Color.red(black), Color.green(black), Color.blue(black));
                activityIvfMainBinding.mainScrim.setBackgroundColor(color);
            }
        });

        activityIvfMainBinding.mainBottomNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_topSearch:
                        viewPager2.setCurrentItem(0, true);
                        break;
                    case R.id.menu_cookies:
                        viewPager2.setCurrentItem(1, true);
                        break;
                    case R.id.menu_candy:
                        viewPager2.setCurrentItem(2, true);
                        break;
                    case R.id.menu_drinks:
                        viewPager2.setCurrentItem(3, true);
                        break;
                    case R.id.menu_instantNoodles:
                        viewPager2.setCurrentItem(4, true);
                        break;
                    case R.id.menu_Ingredients:
                        viewPager2.setCurrentItem(5, true);
                        break;
                    case R.id.menu_cannedFood:
                        viewPager2.setCurrentItem(6, true);
                        break;
                    case R.id.menu_jam:
                        viewPager2.setCurrentItem(7, true);
                        break;
                    case R.id.menu_other:
                        viewPager2.setCurrentItem(8, true);
                        break;
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                activityIvfMainBinding.mainScrim.setVisibility(View.GONE);
                return true;
            }
        });

        //Set Navigation Header Close Button
        NavigationIvfHeaderBinding navigationIvfHeaderBinding = NavigationIvfHeaderBinding.bind(activityIvfMainBinding.mainBottomNavigationView.getHeaderView(0));
        navigationIvfHeaderBinding.setViewmodel(mainViewModel);
        navigationIvfHeaderBinding.setLifecycleOwner(this);
        navigationIvfHeaderBinding.ivfNavigationHeaderClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Main", "Header close icon click.");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                activityIvfMainBinding.mainScrim.setVisibility(View.GONE);
            }
        });

        //Set Info Navigation Header
        NavigationIvfMemberBinding navigationIvfMemberBinding = NavigationIvfMemberBinding.bind(activityIvfMainBinding.userInfoBottomNavigationView.getHeaderView(0));
        navigationIvfMemberBinding.setViewmodel(mainViewModel);
        navigationIvfMemberBinding.setLifecycleOwner(this);

        //set name edittext text change
        navigationIvfMemberBinding.ivfMainMemberName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String oldName = sharedPreferences.getString(SP_USER_NAME,"");
                if (!"".equals(oldName)) {
                    mainViewModel.memberDataChange(editable.toString(), oldName);
                }
            }
        });

        //set county and town spinner listener
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                navigationIvfMemberBinding.ivfMainMemberName.setEnabled(false);//跳脫編輯
                int oldCounty = sharedPreferences.getInt(SP_USER_COUNTY, 0);
                int oldTown = sharedPreferences.getInt(SP_USER_TOWN, 0);
                if (oldCounty != 0 || oldTown != 0) {
                    mainViewModel.memberDataChange(navigationIvfMemberBinding.ivfMainMemberLocalCounty.getSelectedItemPosition(),
                            oldCounty,
                            navigationIvfMemberBinding.ivfMainMemberLocalTown.getSelectedItemPosition(),
                            oldTown);
                }
                navigationIvfMemberBinding.ivfMainMemberName.setEnabled(true);//跳脫編輯
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        navigationIvfMemberBinding.ivfMainMemberLocalCounty.setOnItemSelectedListener(spinnerListener);
        navigationIvfMemberBinding.ivfMainMemberLocalTown.setOnItemSelectedListener(spinnerListener);

        //set confirm click
        navigationIvfMemberBinding.ivfMainMemberConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard(view);//close keyboard
                String email = sharedPreferences.getString(SP_USER_EMAIL, "");
                String token = sharedPreferences.getString(SP_USER_TOKEN, "");
                int intCounty = navigationIvfMemberBinding.ivfMainMemberLocalCounty.getSelectedItemPosition();
                int intTown = navigationIvfMemberBinding.ivfMainMemberLocalTown.getSelectedItemPosition();
                if (intCounty == 0) {
                    showMessage(getString(R.string.ivf_navigation_member_county_error));
                    return;
                } else if (intTown == 0){
                    showMessage(getString(R.string.ivf_navigation_member_town_error));
                    return;
                }
                if (email.isEmpty() || token.isEmpty()) showMessage(getString(R.string.ivf_navigation_member_data_error));
                mainViewModel.saveMemberData(navigationIvfMemberBinding.ivfMainMemberName.getText().toString(), intCounty, intTown, email, token);
            }
        });

        //set cancel click
        navigationIvfMemberBinding.ivfMainMemberCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard(view);
                activityIvfMainBinding.mainScrim.setVisibility(View.GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                infoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        //Member edit status
        mainViewModel.getMemberEditStatus().observe(this, new Observer<MemberEditStatus>() {
            @Override
            public void onChanged(MemberEditStatus memberEditStatus) {
                if (memberEditStatus == null) return;

                setMemberSaveView(navigationIvfMemberBinding.ivfMainMemberConfirm, memberEditStatus.isDataValid());

                if (memberEditStatus.getNameError() != null){
                    navigationIvfMemberBinding.ivfMainMemberName.setError(getString(memberEditStatus.getNameError()));
                }
            }
        });

        //Get update result
        mainViewModel.getUpdateStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                if(aBoolean) {
                    activityIvfMainBinding.mainScrim.setVisibility(View.GONE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    infoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    showMessage(getString(R.string.ivf_navigation_member_update_success));
                } else {
                    showMessage(getString(R.string.ivf_navigation_member_update_fail));
                }
            }
        });

        //Set User info on Navigation Header
        mainViewModel.getCheckUser();

        //Set BottomBar Menu Click
        activityIvfMainBinding.mainBottomBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Main", "Navigation icon click.");
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
                    activityIvfMainBinding.mainScrim.setVisibility(View.VISIBLE);
                    activityIvfMainBinding.mainBottomNavigationView.setVisibility(View.VISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //info
                    activityIvfMainBinding.userInfoBottomNavigationView.setVisibility(View.GONE);
                    infoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    activityIvfMainBinding.mainScrim.setVisibility(View.GONE);
                    activityIvfMainBinding.mainBottomNavigationView.setVisibility(View.GONE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        activityIvfMainBinding.mainBottomBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_bottom_bar_member:
                        getMemberInfo();
                        return true;
                    case R.id.main_bottom_bar_feedback:
                        Log.d("Main","bottom bar feedback");
                        setFeedbackLauncher(mainViewModel.userInfo.get());
                        return true;
                    case R.id.main_bottom_bar_logout:
                        Log.d("Main","Log out.");
                        setBackPressedDialog(1, R.string.ivf_dialog_logout_tittle_zh, R.string.ivf_dialog_logout_message_zh);
                        return true;
                    default:
                        return false;
                }
            }
        });

        /**
         * 觀察登出
         * */
        mainViewModel.getLogoutStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                if (aBoolean){
                    startActivity(new Intent(IVFMainActivity.this, IVFLoginActivity.class));
                    showMessage(getString(R.string.ivf_main_logout_success_zh));
                } else {
                    showMessage(getString(R.string.ivf_main_logout_fail_zh));
                }
            }
        });

        //get remote info
        mainViewModel.getUserRemoteInfoResult().observe(this, new Observer<MemberInfo>() {
            @Override
            public void onChanged(MemberInfo memberInfo) {
                if (memberInfo.getError() == null){
                    if (infoBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
                        //menu
                        activityIvfMainBinding.mainBottomNavigationView.setVisibility(View.GONE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                        activityIvfMainBinding.userInfoBottomNavigationView.setVisibility(View.VISIBLE);
                        infoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        activityIvfMainBinding.mainScrim.setVisibility(View.VISIBLE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ivf_name", memberInfo.getUserDisplayName());
                        editor.putString("ivf_email", memberInfo.getUserEmail());
                        editor.putInt("ivf_county", memberInfo.getUserCounty());
                        editor.putInt("ivf_town", memberInfo.getUserTown());
                        editor.putString("ivf_token", memberInfo.getUserToken());
                        editor.apply();
                        editor.commit();
                    }
                } else {
                    showMessage(getString(memberInfo.getError()));
                }
            }
        });


        //get search product
        mainViewModel.getSearchProductResult().observe(this, new Observer<InfoProduct>() {
            @Override
            public void onChanged(InfoProduct infoProduct) {
                if (infoProduct == null) return;
                if (infoProduct.getError() == null){
                    if (infoProduct.getType()){
                        //Get product
                        productLauncher.launch(infoProduct, ActivityOptionsCompat.makeSceneTransitionAnimation(IVFMainActivity.this, activityIvfMainBinding.mainScannerButton, "scanner_product"));
                    } else {
                        //Create product
                        createProductLauncher.launch(infoProduct.getBarcode());//跳轉至建立頁面
                    }

                } else {
                    //Error
                    showMessage(getString(infoProduct.getError()));
                }
            }
        });

    }

    private void setTabLayoutMediator(ViewPager2 vP2){
        //Set TabLayout and ViewPage2 sync
        tabLayoutMediator = new TabLayoutMediator(activityIvfMainBinding.mainTablayout, vP2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(getResources().getStringArray(R.array.table_item)[position]);
            }
        });

        tabLayoutMediator.attach();
    }

    /**
     * setBackPressedDialog
     * @param type 0.離開程式. 1.登出.
     * @param tittle
     * @param message
     * */
    private void setBackPressedDialog(int type, Integer tittle, Integer message){
        AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(this);
        alertDialogBuild.setTitle(getString(tittle));
        alertDialogBuild.setMessage(getString(message));
        alertDialogBuild.setPositiveButton(getString(R.string.ivf_dialog_right_zh), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (type == 0){
                    finishAffinity();
                } else if (type == 1){
                    mainViewModel.logout();
                }
            }
        });
        alertDialogBuild.setNegativeButton(getString(R.string.ivf_dialog_left_zh), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertDialogBuild.create();
        alertDialog.show();
    }

    private void setFeedbackLauncher(UserInfoEntity userInfoEntity){
        if (userInfoEntity == null || "".equals(userInfoEntity.getDisplayName())){
            showMessage(getString(R.string.ivf_main_feedback_fail_zh));
        } else {
            feedbackLauncher.launch(userInfoEntity, ActivityOptionsCompat.makeSceneTransitionAnimation(IVFMainActivity.this, activityIvfMainBinding.mainBottomBar, "bottom_feedback"));
        }
    }

    private void getMemberInfo(){
        mainViewModel.getMemberInfo();
    }

    @SuppressLint("NewApi")
    private void setMemberSaveView(TextView save_view, boolean status){
        save_view.setTypeface(status ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        save_view.setTextColor(status ? Color.BLACK : getColor(R.color.grey));
        save_view.setClickable(status);
    }

    private void showMessage(String msg){
        if (!"".equals(msg)){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void closeKeyBoard(View v){
        InputMethodManager imm =  (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        try {
            Thread.sleep(10);
        } catch (Exception e){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tabLayoutMediator != null) tabLayoutMediator.detach();
    }

    @Override
    public void onBackPressed() {
        setBackPressedDialog(0, R.string.ivf_dialog_close_tittle_zh, R.string.ivf_dialog_close_message_zh);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //判斷格式是否正確
                if (IntentIntegrator.PRODUCT_CODE_TYPES.contains(result.getFormatName())){
                    String barcode = result.getContents();
                    //判斷商品Barcode格式是否正確
                    if (!"".equals(barcode) && IVFVerifyBarcode.verifyBarcode(barcode)){
                        //判斷商品是否存在
                        String emil = sharedPreferences.getString(SP_USER_EMAIL,"");
                        String token = sharedPreferences.getString(SP_USER_TOKEN, "");
                        if ("".equals(emil) && "".equals(token)) return;
                        mainViewModel.searchProduct(barcode, emil, token);
                    } else {
                        Toast.makeText(this, R.string.ivf_activity_scanner_decode_error_msg, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.ivf_activity_scanner_format_error_msg, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //導頁至建立商品
    ActivityResultLauncher<String> createProductLauncher = registerForActivityResult(new ActivityResultContract<String, String>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String barcode) {
            Intent intent = new Intent(IVFMainActivity.this, IVFCreateProduct.class);
            intent.putExtra(EXTRA_BARCODE, barcode);
            return intent; //start activity
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {//return result
            if (resultCode == RESULT_OK && intent != null){
                return "Create product success.";
            }
            return "";
        }
    }, new ActivityResultCallback<String>() {
        @Override
        public void onActivityResult(String result) {//CallBack result
            if (!"".equals(result)) Log.e("main", result);
        }
    });


    //導頁至回報頁面
    ActivityResultLauncher<UserInfoEntity> feedbackLauncher = registerForActivityResult(new ActivityResultContract<UserInfoEntity, String>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, UserInfoEntity userInfoEntity) {
            Intent intent = new Intent(IVFMainActivity.this, IVFFeedBackActivity.class);
            intent.putExtra(IVFFeedBackActivity.EXTRA_NAME, userInfoEntity.getDisplayName());
            intent.putExtra(IVFFeedBackActivity.EXTRA_ID, String.valueOf(userInfoEntity.getId()));
            intent.putExtra(IVFFeedBackActivity.EXTRA_TRANSITIONS, 1);
            return intent; //start activity
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {//return result
            if (resultCode == RESULT_OK && intent != null){
                return "Send mail success.";
            } else if (resultCode == RESULT_CANCELED){
                return "Send mail cancel.";
            }
            return null;
        }
    }, new ActivityResultCallback<String>() {
        @Override
        public void onActivityResult(String result) {//CallBack result
            if ("Send mail cancel.".equals(result)) return;
            if (!"".equals(result)) {
                Log.e("main", result);
                showMessage(MessageFormat.format(getString(R.string.ivf_main_feedback_result_zh), mainViewModel.userName.get()));
            }
        }
    });

    //導頁至商品頁面
    ActivityResultLauncher<InfoProduct> productLauncher = registerForActivityResult(
            new ActivityResultContract<InfoProduct, String>() {

                @NonNull
                @Override
                public Intent createIntent(@NonNull  Context context, InfoProduct product) {
                    Intent intent = new Intent(IVFMainActivity.this, IVFInfoProduct.class);
                    intent.putExtra(IVFInfoProduct.EXTRA_BARCODE, product.getBarcode());
                    intent.putExtra(IVFInfoProduct.EXTRA_NAME, product.getName());
                    intent.putExtra(IVFInfoProduct.EXTRA_CATEGORY, product.getCategory());
                    intent.putExtra(IVFInfoProduct.EXTRA_ORIGIN, product.getOrigin());
                    intent.putExtra(IVFInfoProduct.EXTRA_VEGETARIAN, product.getVegetarian());
                    intent.putExtra(IVFInfoProduct.EXTRA_REMARK, product.getRemark());
                    intent.putExtra(IVFInfoProduct.EXTRA_TRANSITIONS, 1);
                    return intent;
                }

                @Override
                public String parseResult(int resultCode, @Nullable Intent intent) {
                    if (resultCode == RESULT_OK && intent != null){
                        return "Call back Success.";
                    }
                    return "Back Activity";
                }
            },
            new ActivityResultCallback<String>() {
                @Override
                public void onActivityResult(String result) {
                    Log.e("Result", "Parse Result: " + result);
                }
            }
    );
}