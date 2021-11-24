package thisis.vegetarian.question.mark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import thisis.vegetarian.question.mark.databinding.FragmentIvfSignupBinding;

public class IVFSignupFragment extends Fragment {
    FragmentIvfSignupBinding fragmentIvfSignupBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentIvfSignupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ivf_signup, container, false);
        return fragmentIvfSignupBinding.getRoot();
    }
}
