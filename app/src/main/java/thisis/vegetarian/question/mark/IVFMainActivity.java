package thisis.vegetarian.question.mark;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import thisis.vegetarian.question.mark.databinding.ActivityIvfMainBinding;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.viewmodel.IVFMainViewModel;

public class IVFMainActivity extends AppCompatActivity {
    public static final String EXTRA_BARCODE = "thisis.vegetarian.question.mark.EXTRA_BARCODE";
    private TabLayoutMediator tabLayoutMediator;
    private ActivityIvfMainBinding activityIvfMainBinding;
    private IVFMainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIvfMainBinding = DataBindingUtil.setContentView(IVFMainActivity.this, R.layout.activity_ivf_main);
        mainViewModel = new ViewModelProvider(this).get(IVFMainViewModel.class);
        activityIvfMainBinding.setLifecycleOwner(this);

        //建立ViewPage2
        ViewPager2 viewPager2 = activityIvfMainBinding.mainViewPage2;

        //建立ViewPage1Adapter
        IVFViewPage2Adapter viewPage2Adapter = new IVFViewPage2Adapter(getSupportFragmentManager(), getLifecycle());
        //建立各類別Fragment
        String[] fragmentList = {"topSearch", "cookies", "candy", "drinks", "instantNoodles", "Ingredients", "cannedFood", "jam", "other"};
        for (String fragmentName : fragmentList){
            viewPage2Adapter.addFragment(new IVFCategoryFragment(fragmentName));
        }
        //set viewpage2 adapter to viewpage2
        viewPager2.setAdapter(viewPage2Adapter);


        //Set TabLayout and ViewPage2 sync
        tabLayoutMediator = new TabLayoutMediator(activityIvfMainBinding.mainTablayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(getResources().getStringArray(R.array.table_item)[position]);
            }
        });

        tabLayoutMediator.attach();

        activityIvfMainBinding.mainFaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(IVFMainActivity.this)
                        .setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
                        .setOrientationLocked(false)
                        .setCaptureActivity(IVFScannerActivity.class)
                        .initiateScan();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tabLayoutMediator != null) tabLayoutMediator.detach();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //判斷格式是否正確
                //判斷商品是否存在
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                createProductLauncher.launch(result.getContents());//跳轉至建立頁面
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
                return "Create Success";
            }
            return null;
        }
    }, new ActivityResultCallback<String>() {
        @Override
        public void onActivityResult(String result) {//CallBack result
            if (!"".equals(result)) Log.e("main", result);
        }
    });
}