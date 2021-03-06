package thisis.vegetarian.question.mark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import thisis.vegetarian.question.mark.databinding.ActivityIvfLoginBinding;

public class IVFLoginActivity extends AppCompatActivity {
    ActivityIvfLoginBinding activityIvfLoginBinding;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TabLayoutMediator tabLayoutMediator;
    TextView tittle;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIvfLoginBinding = DataBindingUtil.setContentView(IVFLoginActivity.this, R.layout.activity_ivf_login);

        tabLayout = activityIvfLoginBinding.ivfLoginTablelayout;
        viewPager2 = activityIvfLoginBinding.ivfLoginViewpager;
        tittle = activityIvfLoginBinding.ivfLoginTittle;
        icon = activityIvfLoginBinding.ivfLoginIcon;

        //set Login and Signup Fragment
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new IVFLoginFragment());
        fragmentList.add(new IVFSignupFragment());

        //Create ViewPageAdapter
        final IVFLoginViewPage2Adapter ivfLoginViewPage2Adapter = new IVFLoginViewPage2Adapter(getSupportFragmentManager(), getLifecycle());
        ivfLoginViewPage2Adapter.setFragmentList(fragmentList);
        viewPager2.setAdapter(ivfLoginViewPage2Adapter);

        //Set TableLayout Name
        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText(R.string.ivf_login_tab_login_zh);
                        break;
                    case 1:
                        tab.setText(R.string.ivf_login_tab_signup_zh);
                        break;
                }
            }
        });

        tabLayoutMediator.attach();

        //set TabLayout Animation
        icon.setTranslationY(200);
        tabLayout.setTranslationY(300);
        tabLayout.setAlpha(0);
        tittle.setAlpha(0);

        icon.animate().translationY(0).setDuration(800).start();
        tittle.animate().alpha(1).setDuration(900).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
    }

    public void selectPage(int page){
        viewPager2.setCurrentItem(page);
    }

    private void setBackPressedDialog(){
        AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(this);
        alertDialogBuild.setTitle(getString(R.string.ivf_dialog_close_tittle_zh));
        alertDialogBuild.setMessage(getString(R.string.ivf_dialog_close_message_zh));
        alertDialogBuild.setPositiveButton(getString(R.string.ivf_dialog_right_zh), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        alertDialogBuild.setNegativeButton(getString(R.string.ivf_dialog_left_zh), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertDialogBuild.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tabLayoutMediator != null) tabLayoutMediator.detach();
    }

    @Override
    public void onBackPressed() {
        setBackPressedDialog();
    }
}