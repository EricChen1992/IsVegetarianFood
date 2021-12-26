package thisis.vegetarian.question.mark;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import thisis.vegetarian.question.mark.databinding.FragmentIvfSignupBinding;
import thisis.vegetarian.question.mark.model.SignupEditStatus;
import thisis.vegetarian.question.mark.viewmodel.IVFSignupViewModel;
import thisis.vegetarian.question.mark.viewmodel.IVFWelcomeViewModel;

public class IVFSignupFragment extends Fragment {
    FragmentIvfSignupBinding fragmentIvfSignupBinding;
    IVFSignupViewModel ivfSignupViewModel;
    EditText editName, editEmail, editPassword, editConfirmPassword, editPhone;
    Spinner spinnerGender, spinnerCounty, spinnerTown;
    Button btnConfirm;
    ProgressBar progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentIvfSignupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ivf_signup, container, false);
        ivfSignupViewModel = new ViewModelProvider(this, new IVFSignupViewModel.Factory(getActivity().getApplication())).get(IVFSignupViewModel.class);
        fragmentIvfSignupBinding.setViewmodel(ivfSignupViewModel);
        initView();
        return fragmentIvfSignupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setEditListener();
        setSpinnerListener();

        ivfSignupViewModel.getSignupEditStatus().observe(getViewLifecycleOwner(), new Observer<SignupEditStatus>() {
            @Override
            public void onChanged(SignupEditStatus signupEditStatus) {

                if (null == signupEditStatus) return;

                btnConfirm.setEnabled(signupEditStatus.isDataValid());

                if (signupEditStatus.getNameError() != null){
                    editName.setError(getString(signupEditStatus.getNameError()));
                } else if (signupEditStatus.getEmailError() != null){
                    editEmail.setError(getString(signupEditStatus.getEmailError()));
                } else if (signupEditStatus.getPasswordError() != null){
                    editPassword.setError(getString(signupEditStatus.getPasswordError()));
                } else if (signupEditStatus.getConfirmPasswordError() != null){
                    editConfirmPassword.setError(getString(signupEditStatus.getConfirmPasswordError()));
                } else if (signupEditStatus.getPhoneError() != null){
                    editPhone.setError(getString(signupEditStatus.getPhoneError()));
                } else if (signupEditStatus.getSpinnerError() != null){
                    showToastMessage(getString(signupEditStatus.getSpinnerError()));
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard(view);
                ivfSignupViewModel.signup(editName.getText().toString(),
                                            spinnerGender.getSelectedItemPosition(),
                                            editEmail.getText().toString(),
                                            editPassword.getText().toString(),
                                            spinnerCounty.getSelectedItemPosition(),
                                            spinnerTown.getSelectedItemPosition(),
                                            PhoneNumberUtils.stripSeparators(editPhone.getText().toString()));
            }
        });

        ivfSignupViewModel.getSignupStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showToastMessage("註冊成功.");
                    clearAllInfo();
                    ((IVFLoginActivity) requireActivity()).selectPage(0);
                } else if (!aBoolean && !editName.getText().toString().isEmpty()){
                    showToastMessage("註冊失敗.");
                }
            }
        });

        ivfSignupViewModel.getShowProgressView().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void clearAllInfo(){
        editName.setText("");
        editName.setError(null);
        editEmail.setText("");
        editEmail.setError(null);
        editPassword.setText("");
        editPassword.setError(null);
        editConfirmPassword.setText("");
        editConfirmPassword.setError(null);
        editPhone.setText("");
        editPhone.setError(null);
        spinnerGender.setSelection(0);
        spinnerCounty.setSelection(0);
        spinnerTown.setSelection(0);

    }

    private void setSpinnerListener() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ivfSignupViewModel.signupDataChange(editName.getText().toString(),
                                            editEmail.getText().toString(),
                                            editPassword.getText().toString(),
                                            editConfirmPassword.getText().toString(),
                                            editPhone.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spinnerGender.setOnItemSelectedListener(spinnerListener);
        spinnerCounty.setOnItemSelectedListener(spinnerListener);
        spinnerTown.setOnItemSelectedListener(spinnerListener);

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        closeKeyBoard(view);
                }
                return false;
            }
        };
        spinnerGender.setOnTouchListener(touchListener);
        spinnerCounty.setOnTouchListener(touchListener);
        spinnerTown.setOnTouchListener(touchListener);
    }

    private void setEditListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ivfSignupViewModel.signupDataChange(editName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString(),  editConfirmPassword.getText().toString(), editPhone.getText().toString());
            }
        };
        editName.addTextChangedListener(textWatcher);
        editEmail.addTextChangedListener(textWatcher);
        editPassword.addTextChangedListener(textWatcher);
        editConfirmPassword.addTextChangedListener(textWatcher);
        editPhone.addTextChangedListener(textWatcher);
    }

    private void initView(){
        editName = fragmentIvfSignupBinding.ivfSignupName;
        spinnerGender = fragmentIvfSignupBinding.ivfSignupGender;
        editEmail = fragmentIvfSignupBinding.ivfSignupEmail;
        editPassword = fragmentIvfSignupBinding.ivfSignupPassword;
        editConfirmPassword = fragmentIvfSignupBinding.ivfSignupConfirmPassword;
        editPhone = fragmentIvfSignupBinding.ivfSignupPhone;
        spinnerCounty = fragmentIvfSignupBinding.ivfSignupLocalCounty;
        spinnerTown = fragmentIvfSignupBinding.ivfSignupLocalTown;
        btnConfirm = fragmentIvfSignupBinding.ivfSignupConfirm;
        progressBar = fragmentIvfSignupBinding.progressBar;
    }

    private void showToastMessage(String msg){
        if (null != msg && !"".equals(msg)) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void closeKeyBoard(View v){
        InputMethodManager imm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
