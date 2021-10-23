package thisis.vegetarian.question.mark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.viewmodel.IVFCreateProductViewModel;

public class IVFCreateProduct extends AppCompatActivity {
    int tempCategory = -1;
    String tempOrigin;
    int tempVegetarian = -1;
    String tempBarcode;
    private ActivityIvfCreateproductBinding activityIvfCreateproductBinding;
    private IVFCreateProductViewModel createProductViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createProductViewModel = new ViewModelProvider(this).get(IVFCreateProductViewModel.class);
        activityIvfCreateproductBinding = DataBindingUtil.setContentView(IVFCreateProduct.this, R.layout.activity_ivf_createproduct);
        activityIvfCreateproductBinding.setLifecycleOwner(this);
        activityIvfCreateproductBinding.setViewModel(createProductViewModel);

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

        createProductViewModel.getUpdateType().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean){
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }
        });
    }

    private void saveData(){
        String createBarcode = tempBarcode;
        String createName = Objects.requireNonNull(activityIvfCreateproductBinding.ivfCreateName.getText(), "Create name is Null").toString();
        String createRemark = Objects.requireNonNull(activityIvfCreateproductBinding.ivfCreateRemark.getText(), "Create remark is Null").toString();
        int createCategory = tempCategory;
        String createOrigin = tempOrigin;
        int createVegetarian = tempVegetarian;
        if ("".equals(createBarcode) ) {
            Toast.makeText(this, "Barcode 發生錯誤", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(createName) && -1 == createCategory && ("".equals(createOrigin) || null == createOrigin)&& -1 == createVegetarian){
            Toast.makeText(this, "名稱、種類、產地、素食類型 未輸入", Toast.LENGTH_LONG).show();
            return;
        }
        createProductViewModel.insert(new IVF_ProductDataEntity(createBarcode, createName, createCategory, createOrigin, createVegetarian, createRemark, 0));
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