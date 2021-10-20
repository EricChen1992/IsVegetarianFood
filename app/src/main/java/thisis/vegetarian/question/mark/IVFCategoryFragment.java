package thisis.vegetarian.question.mark;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import thisis.vegetarian.question.mark.databinding.FragmentIvfCategoryBinding;
import thisis.vegetarian.question.mark.db.entity.IVF_ProductDataEntity;
import thisis.vegetarian.question.mark.viewmodel.IVFCategoryViewModel;

public class IVFCategoryFragment extends Fragment {

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
}