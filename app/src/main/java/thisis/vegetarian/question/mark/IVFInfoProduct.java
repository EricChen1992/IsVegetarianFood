package thisis.vegetarian.question.mark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import java.util.Locale;

import thisis.vegetarian.question.mark.databinding.ActivityIvfInfoproductBinding;

public class IVFInfoProduct extends AppCompatActivity {
    public static final String EXTRA_BARCODE = "thisis.vegetarian.question.mark.info.EXTRA_BARCODE";
    public static final String EXTRA_NAME = "thisis.vegetarian.question.mark.info.EXTRA_NAME";
    public static final String EXTRA_CATEGORY = "thisis.vegetarian.question.mark.info.EXTRA_CATEGORY";
    public static final String EXTRA_ORIGIN = "thisis.vegetarian.question.mark.info.EXTRA_ORIGIN";
    public static final String EXTRA_VEGETARIAN = "thisis.vegetarian.question.mark.info.EXTRA_VEGETARIAN";
    public static final String EXTRA_REMARK = "thisis.vegetarian.question.mark.info.EXTRA_REMARK";

    private ActivityIvfInfoproductBinding activityIvfInfoproductBinding;
    private int[] vegetarian_image = new int[]{
            R.drawable.icon_vegan,
            R.drawable.icon_lacto_vegetarian,
            R.drawable.icon_ovo_vegetarian,
            R.drawable.icon_lacto_vov_vegetarian,
            R.drawable.icon_five_pungent_spices_vegetarian,
            R.drawable.icon_meat,
            R.drawable.icon_unknow_vegetarian};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setConfig();
        super.onCreate(savedInstanceState);

        activityIvfInfoproductBinding = DataBindingUtil.setContentView(this, R.layout.activity_ivf_infoproduct);
        Intent intent = getIntent();
        String infoBarcode = intent.getStringExtra(EXTRA_BARCODE);
        String infoName = intent.getStringExtra(EXTRA_NAME);
        int infoCategory = intent.getIntExtra(EXTRA_CATEGORY, 7);
        String infoOrigin = intent.getStringExtra(EXTRA_ORIGIN);
        int infoVegetarian = intent.getIntExtra(EXTRA_VEGETARIAN, 6);
        String infoRemark = intent.getStringExtra(EXTRA_REMARK);
        if (null == infoRemark) infoRemark = "";
        if (checkValue(infoBarcode, infoName, infoCategory, infoOrigin, infoVegetarian)) {
            activityIvfInfoproductBinding.ivfInfoProductName.setText(infoName);
            activityIvfInfoproductBinding.ivfInfoProductCategory.setText(getResources().getStringArray(R.array.create_category)[infoCategory]);
            activityIvfInfoproductBinding.ivfInfoProductOrigin.setText(new Locale("", infoOrigin).getDisplayCountry());
            activityIvfInfoproductBinding.ivfInfoProductVegetarian.setText(getResources().getStringArray(R.array.vegetarian)[infoVegetarian]);
            activityIvfInfoproductBinding.ivfInfoProductVegetarianIcon.setImageResource(vegetarian_image[infoVegetarian]);
            activityIvfInfoproductBinding.ivfInfoProductRemark.setText(infoRemark);
        } else {
            //Do Black Activity
            Toast.makeText(this, R.string.product_info_error_msg, Toast.LENGTH_SHORT).show();
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean checkValue(String info_barcode, String info_name, int info_category, String info_origin, int info_vegetarian){
        if (null == info_barcode || info_barcode.isEmpty()) return false;
        if (null == info_name || info_name.isEmpty()) return false;
        if (info_category < -1 || info_category > 7) return false;
        if (null == info_origin || info_origin.isEmpty()) return false;
        if (info_vegetarian < -1 || info_vegetarian > 6) return false;
        return true;
    }

    private void setConfig() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            findViewById(android.R.id.content).setTransitionName("select_item");
            setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());


            MaterialContainerTransform enterTransform = new MaterialContainerTransform();
            enterTransform.addTarget(android.R.id.content);
            enterTransform.setAllContainerColors(MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface));
            enterTransform.setDuration(800);

            MaterialContainerTransform returnTransform = new MaterialContainerTransform();
            returnTransform.addTarget(android.R.id.content);
            returnTransform.setAllContainerColors(MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface));
            returnTransform.setDuration(1000);

            getWindow().setSharedElementEnterTransition(enterTransform);
            getWindow().setSharedElementReturnTransition(returnTransform);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}