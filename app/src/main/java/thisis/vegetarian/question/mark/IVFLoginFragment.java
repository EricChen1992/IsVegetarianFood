package thisis.vegetarian.question.mark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import thisis.vegetarian.question.mark.databinding.FragmentIvfLoginBinding;


public class IVFLoginFragment extends Fragment {
    FragmentIvfLoginBinding fragmentIvfLoginBinding;
    EditText etAccount, etPassword;
    TextView tvForget;
    Button btLogin;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fragmentIvfLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ivf_login, container, false);

        etAccount = fragmentIvfLoginBinding.ivfLoginAccount;
        etPassword = fragmentIvfLoginBinding.ivfLoginPassword;
        tvForget = fragmentIvfLoginBinding.ivfLoginForget;
        btLogin = fragmentIvfLoginBinding.ivfLoginLogin;

        //set animation
        etAccount.setTranslationX(800);
        etPassword.setTranslationX(800);
        tvForget.setTranslationX(800);
        btLogin.setTranslationX(800);

        etAccount.setAlpha(0);
        etPassword.setAlpha(0);
        tvForget.setAlpha(0);
        btLogin.setAlpha(0);

        etAccount.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        tvForget.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btLogin.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return fragmentIvfLoginBinding.getRoot();
    }
}
