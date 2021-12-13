package thisis.vegetarian.question.mark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import org.jetbrains.annotations.NotNull;

import thisis.vegetarian.question.mark.databinding.FragmentIvfSignupBinding;
import thisis.vegetarian.question.mark.viewmodel.IVFSignupViewModel;

public class IVFSignupFragment extends Fragment {
    FragmentIvfSignupBinding fragmentIvfSignupBinding;
    IVFSignupViewModel ivfSignupViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentIvfSignupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ivf_signup, container, false);
        ivfSignupViewModel = new IVFSignupViewModel(getActivity().getApplication());
        fragmentIvfSignupBinding.setViewmodel(ivfSignupViewModel);
        return fragmentIvfSignupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
