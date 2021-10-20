package thisis.vegetarian.question.mark;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import thisis.vegetarian.question.mark.viewmodel.IVFMainViewModel;

public class IVFMainActivity extends AppCompatActivity {
    public static final String EXTRA_BARCODE = "thisis.vegetarian.question.mark.EXTRA_BARCODE";
    private TabLayoutMediator tabLayoutMediator;
    private ActivityIvfMainBinding activityIvfMainBinding;
    private IVFMainViewModel mainViewModel;
    private ViewPager2 viewPager2;
    private BottomSheetBehavior<NavigationView> bottomSheetBehavior;
    private BottomSheetBehavior<NavigationView> infoBottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIvfMainBinding = DataBindingUtil.setContentView(IVFMainActivity.this, R.layout.activity_ivf_main);
        mainViewModel = new ViewModelProvider(this).get(IVFMainViewModel.class);
        activityIvfMainBinding.setViewModel(mainViewModel);
        activityIvfMainBinding.setLifecycleOwner(this);

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
                        if (infoBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
                            //menu
                            activityIvfMainBinding.mainBottomNavigationView.setVisibility(View.GONE);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                            activityIvfMainBinding.userInfoBottomNavigationView.setVisibility(View.VISIBLE);
                            infoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            activityIvfMainBinding.mainScrim.setVisibility(View.VISIBLE);
                        }
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
            feedbackLauncher.launch(userInfoEntity);
        }
    }

    private void showMessage(String msg){
        if (!"".equals(msg)){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
                    //判斷商品是否存在
                    if (IVFVerifyBarcode.verifyBarcode(result.getContents())){
                        Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                        createProductLauncher.launch(result.getContents());//跳轉至建立頁面
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


    ActivityResultLauncher<UserInfoEntity> feedbackLauncher = registerForActivityResult(new ActivityResultContract<UserInfoEntity, String>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, UserInfoEntity userInfoEntity) {
            Intent intent = new Intent(IVFMainActivity.this, IVFFeedBackActivity.class);
            intent.putExtra(IVFFeedBackActivity.EXTRA_NAME, userInfoEntity.getDisplayName());
            intent.putExtra(IVFFeedBackActivity.EXTRA_ID, String.valueOf(userInfoEntity.getId()));
            return intent; //start activity
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {//return result
            if (resultCode == RESULT_OK && intent != null){
                return "Send mail success.";
            }
            return null;
        }
    }, new ActivityResultCallback<String>() {
        @Override
        public void onActivityResult(String result) {//CallBack result
            if (!"".equals(result)) {
                Log.e("main", result);
                showMessage(MessageFormat.format(getString(R.string.ivf_main_feedback_result_zh), mainViewModel.userName.get()));
            }
        }
    });
}