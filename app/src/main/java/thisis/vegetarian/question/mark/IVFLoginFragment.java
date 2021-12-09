package thisis.vegetarian.question.mark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import thisis.vegetarian.question.mark.databinding.FragmentIvfLoginBinding;
import thisis.vegetarian.question.mark.model.LoginEditStatus;
import thisis.vegetarian.question.mark.model.LoginUser;
import thisis.vegetarian.question.mark.viewmodel.IVFLoginViewModel;


public class IVFLoginFragment extends Fragment {
    FragmentIvfLoginBinding fragmentIvfLoginBinding;
    IVFLoginViewModel loginViewModel;
    EditText etAccount, etPassword;
    TextView tvForget;
    Button btLogin;
    ProgressBar progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fragmentIvfLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ivf_login, container, false);
        loginViewModel = new ViewModelProvider(this, new IVFLoginViewModel.Factory(getActivity().getApplication())).get(IVFLoginViewModel.class);

        etAccount = fragmentIvfLoginBinding.ivfLoginAccount;
        etPassword = fragmentIvfLoginBinding.ivfLoginPassword;
        tvForget = fragmentIvfLoginBinding.ivfLoginForget;
        btLogin = fragmentIvfLoginBinding.ivfLoginLogin;
        progressBar = fragmentIvfLoginBinding.ivfLoginPrgBar;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //check have error for account and password then enable button
        loginViewModel.getLoginEditStatus().observe(getViewLifecycleOwner(), new Observer<LoginEditStatus>() {
            @Override
            public void onChanged(LoginEditStatus loginEditStatus) {

                if (null == loginEditStatus) return;

                btLogin.setEnabled(loginEditStatus.getDataValid());

                if (loginEditStatus.getAccountError() != null){
                    etAccount.setError(getString(loginEditStatus.getAccountError()));
                }
                if (loginEditStatus.getPasswordError() != null){
                    etPassword.setError(getString(loginEditStatus.getPasswordError()));
                }
            }
        });

        //listener
        TextWatcher textChangedWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.loginDataChange(etAccount.getText().toString(), etPassword.getText().toString());
            }
        };

        //set listen function to edit account and password
        etAccount.addTextChangedListener(textChangedWatcher);
        etPassword.addTextChangedListener(textChangedWatcher);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    progressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(etAccount.getText().toString(), etPassword.getText().toString());
                }
                return false;
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(etAccount.getText().toString(), etPassword.getText().toString());
                closeKeyBoard(view);
            }
        });


        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginUser>() {
            @Override
            public void onChanged(LoginUser loginUser) {
                progressBar.setVisibility(View.GONE);
                if (loginUser.getError() == null){
                    Toast.makeText(getContext(), "Welcome " + loginUser.getUserDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), IVFMainActivity.class));//Into MainActivity
                } else {
                    Toast.makeText(getContext(), getString(loginUser.getError()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void closeKeyBoard(View v){
        InputMethodManager imm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
