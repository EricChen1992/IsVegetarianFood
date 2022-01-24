package thisis.vegetarian.question.mark;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import thisis.vegetarian.question.mark.databinding.FragmentIvfCategoryBinding;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.model.OnItemListener;
import thisis.vegetarian.question.mark.model.Product;
import thisis.vegetarian.question.mark.viewmodel.IVFCategoryViewModel;

import static android.app.Activity.RESULT_OK;

public class IVFCategoryFragment extends Fragment implements OnItemListener {

    private String TAG;

    private FragmentIvfCategoryBinding fragmentIvfCategoryBinding;

    private IVFCategoryAdapter ivfCategoryAdapter;

    private IVFCategoryViewModel mViewModel;

    public IVFCategoryFragment(String tag){
        this.TAG = tag;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentIvfCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ivf_category, container, false);
        ivfCategoryAdapter = new IVFCategoryAdapter();
        ivfCategoryAdapter.setOnItemListener(this);
        fragmentIvfCategoryBinding.ivfCategoryRecycler.setAdapter(ivfCategoryAdapter);

        return fragmentIvfCategoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IVFCategoryViewModel.Factory factory = new IVFCategoryViewModel.Factory(this.TAG, requireActivity().getApplication());
        mViewModel = new ViewModelProvider(this, factory).get(IVFCategoryViewModel.class);

        //監聽商品List 變動
        mViewModel.getProduct().observe(getViewLifecycleOwner(), new Observer<List<IVF_ProductDataEntity>>() {
            @Override
            public void onChanged(List<IVF_ProductDataEntity> ivf_productDataEntities) {
                Log.e(TAG, ivf_productDataEntities.size() + "");
                ivfCategoryAdapter.setProductDataEntityList(ivf_productDataEntities);
            }
        });

    }

    @Override
    public void onItemClick(View view, Product product) {
        Log.e("Click", "---> " + product.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemProductLauncher.launch(product, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, "select_item"));
        } else {
            itemProductLauncher.launch(product);
        }
    }

    private ActivityResultLauncher<Product> itemProductLauncher = registerForActivityResult(
        new ActivityResultContract<Product, String>() {

            @NonNull
            @Override
            public Intent createIntent(@NonNull  Context context, Product product) {
                Intent intent = new Intent(getActivity(), IVFInfoProduct.class);
                intent.putExtra(IVFInfoProduct.EXTRA_BARCODE, product.getBarcode());
                intent.putExtra(IVFInfoProduct.EXTRA_NAME, product.getName());
                intent.putExtra(IVFInfoProduct.EXTRA_CATEGORY, product.getCategory());
                intent.putExtra(IVFInfoProduct.EXTRA_ORIGIN, product.getOrigin());
                intent.putExtra(IVFInfoProduct.EXTRA_VEGETARIAN, product.getVegetarian());
                intent.putExtra(IVFInfoProduct.EXTRA_REMARK, product.getRemark());
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