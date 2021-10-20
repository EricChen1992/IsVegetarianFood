package thisis.vegetarian.question.mark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Objects;

import thisis.vegetarian.question.mark.databinding.ActivityIvfCreateproductBinding;

public class IVFCreateProduct extends AppCompatActivity {
    public static final String EXTRA_BARCODE = "thisis.vegetarian.question.mark.EXTRA_BARCODE";
    public static final String EXTRA_NAME = "thisis.vegetarian.question.mark.EXTRA_NAME";
    public static final String EXTRA_CATEGORY = "thisis.vegetarian.question.mark.EXTRA_CATEGORY";
    public static final String EXTRA_ORIGIN = "thisis.vegetarian.question.mark.EXTRA_ORIGIN";
    public static final String EXTRA_VEGETARIAN = "thisis.vegetarian.question.mark.EXTRA_VEGETARIAN";
    public static final String EXTRA_REMARK = "thisis.vegetarian.question.mark.EXTRA_REMARK";

    int tempCategory = -1;
    String tempOrigin;
    int tempVegetarian = -1;
    String tempBarcode;
    private ActivityIvfCreateproductBinding activityIvfCreateproductBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIvfCreateproductBinding = DataBindingUtil.setContentView(IVFCreateProduct.this, R.layout.activity_ivf_createproduct);

        Intent intent = getIntent();
        String barcode = intent.getStringExtra(IVFMainActivity.EXTRA_BARCODE);
        tempBarcode = String.format("%013d",Long.parseLong(barcode));
        if (!"".equals(barcode) && !"".equals(tempBarcode)) {
            activityIvfCreateproductBinding.ivfCreateCard.cardViewBarcode.setText(tempBarcode);
        }

        //建立
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Arrays.asList(getResources().getStringArray(R.array.create_category)));
        activityIvfCreateproductBinding.ivfCreateCategory.setAdapter(categoryAdapter);
        activityIvfCreateproductBinding.ivfCreateCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("Catrgory", adapterView.getItemAtPosition(i) + "< Postion");
                tempCategory = i;
            }
        });

        ArrayAdapter<String> originAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Arrays.asList(getResources().getStringArray(R.array.origin_zh)));
        String[] origin_us = getResources().getStringArray(R.array.origin_us);//建立轉換英文對照
        activityIvfCreateproductBinding.ivfCreateOrigin.setAdapter(originAdapter);
        activityIvfCreateproductBinding.ivfCreateOrigin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("Vegetarian", "AdapterView: " + adapterView.getItemAtPosition(i) + "\narray_us: " + origin_us[i] +"\nView: " + view + "\n i: " + i + "\nl: " + l);
                tempOrigin = origin_us[i];
            }
        });

        ArrayAdapter<String> vegetarianAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Arrays.asList(getResources().getStringArray(R.array.vegetarian)));
        activityIvfCreateproductBinding.ivfCreateVegetarian.setAdapter(vegetarianAdapter);
        activityIvfCreateproductBinding.ivfCreateVegetarian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("Vegetarian", "AdapterView: " + adapterView + "\nView: " + view + "\n i: " + i + "\nl: " + l);
                tempVegetarian = i;
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ivf_close);
    }

    private void saveData(){
        String createBarcode = tempBarcode;
        String createName = Objects.requireNonNull(activityIvfCreateproductBinding.ivfCreateName.getText(), "Create name is Null").toString();
        String createRemark = Objects.requireNonNull(activityIvfCreateproductBinding.ivfCreateRemark.getText(), "Create remark is Null").toString();
        int createCategory = tempCategory;
        String createOrigin = tempOrigin;
        int createVegetarian = tempVegetarian;
//        Log.e("TEST", "createName: " + createName + " createCategory: " + createCategory + " createOrigin: " + createOrigin + " createVegetarian: " + createVegetarian);
        if ("".equals(createBarcode) ) {
            Toast.makeText(this, "Barcode 發現錯誤", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(createName) && -1 == createCategory && ("".equals(createOrigin) || null == createOrigin)&& -1 == createVegetarian){
            Toast.makeText(this, "名稱、種類、產地、素食類型 未輸入", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_BARCODE,createBarcode);
        intent.putExtra(EXTRA_NAME, createName);
        intent.putExtra(EXTRA_CATEGORY, createCategory);
        intent.putExtra(EXTRA_ORIGIN, createOrigin);
        intent.putExtra(EXTRA_VEGETARIAN, createVegetarian);
        intent.putExtra(EXTRA_REMARK, createRemark);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_save_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_product:
                saveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}