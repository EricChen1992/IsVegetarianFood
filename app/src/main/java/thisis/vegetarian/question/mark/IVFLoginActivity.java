package thisis.vegetarian.question.mark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import thisis.vegetarian.question.mark.databinding.ActivityIvfLoginBinding;

public class IVFLoginActivity extends AppCompatActivity {
    ActivityIvfLoginBinding activityIvfLoginBinding;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TabLayoutMediator tabLayoutMediator;
    TextView tittle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIvfLoginBinding = DataBindingUtil.setContentView(IVFLoginActivity.this, R.layout.activity_ivf_login);

        tabLayout = activityIvfLoginBinding.ivfLoginTablelayout;
        viewPager2 = activityIvfLoginBinding.ivfLoginViewpager;
        tittle = activityIvfLoginBinding.ivfLoginTittle;

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new IVFLoginFragment());
        fragmentList.add(new IVFSignupFragment());

        final IVFLoginViewPage2Adapter ivfLoginViewPage2Adapter = new IVFLoginViewPage2Adapter(getSupportFragmentManager(), getLifecycle());
        ivfLoginViewPage2Adapter.setFragmentList(fragmentList);

        viewPager2.setAdapter(ivfLoginViewPage2Adapter);

        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Login");
                        break;
                    case 1:
                        tab.setText("Signup");
                        break;
                }
            }
        });

        tabLayoutMediator.attach();

        //set TabLayout Animation
        tabLayout.setTranslationY(300);
        tabLayout.setAlpha(0);
        tittle.setAlpha(0);

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        tittle.animate().alpha(1).setDuration(900).setStartDelay(50).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tabLayoutMediator != null) tabLayoutMediator.detach();
    }
}