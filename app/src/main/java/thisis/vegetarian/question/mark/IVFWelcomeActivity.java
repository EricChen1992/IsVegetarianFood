package thisis.vegetarian.question.mark;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import thisis.vegetarian.question.mark.databinding.ActivityIvfWelcomeBinding;
import thisis.vegetarian.question.mark.db.entity.UserInfoEntity;
import thisis.vegetarian.question.mark.viewmodel.IVFWelcomeViewModel;

public class IVFWelcomeActivity extends AppCompatActivity implements Animator.AnimatorListener {
    public static final String EXTRA_LOGIN = "IVFLoginActivity";
    public static final String EXTRA_MAIN = "IVFMainActivity";
    private ActivityIvfWelcomeBinding activityIvfWelcomeBinding;
    private IVFWelcomeViewModel ivfWelcomeViewModel;
    private ImageView welcome_icon;
    private TextView welcome_name;
    private TextView welcome_slogan;
    private ProgressBar welcome_progressbar;
    float alphaValue=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivf_welcome);
        activityIvfWelcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_ivf_welcome);
        ivfWelcomeViewModel = new ViewModelProvider(this, new IVFWelcomeViewModel.Factory(getApplication())).get(IVFWelcomeViewModel.class);
        welcome_icon = activityIvfWelcomeBinding.ivfWelcomeIcon;
        welcome_name = activityIvfWelcomeBinding.ivfWelcomeName;
        welcome_slogan = activityIvfWelcomeBinding.ivfWelcomeSlogan;
        welcome_progressbar = activityIvfWelcomeBinding.ivfWelcomeProgressBar;

        welcome_icon.setAlpha(alphaValue);
        welcome_slogan.setAlpha(alphaValue);
        welcome_name.setAlpha(alphaValue);
        welcome_progressbar.setAlpha(alphaValue);

        welcome_icon.animate().alpha(1).setDuration(800).start();
        welcome_name.animate().alpha(1).setDuration(800).start();
        welcome_slogan.animate().alpha(1).setDuration(800).start();
        welcome_progressbar.animate().alpha(1).setStartDelay(1000).setDuration(1000).setListener(this).start();

    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        //check have login
        ivfWelcomeViewModel.getCheckUser().observe(this, new Observer<List<UserInfoEntity>>() {
            @Override
            public void onChanged(List<UserInfoEntity> userInfoEntities) {
                if (userInfoEntities != null && userInfoEntities.size() > 0){
                    UserInfoEntity entity = userInfoEntities.get(0);
                    if (entity.getDisplayName() != null
                            && !"".equals(entity.getDisplayName())
                            && entity.getTokenId() != null
                            && !"".equals(entity.getTokenId())
                    ){
                        goActivity(EXTRA_MAIN);//主畫面
                    } else {
                        goActivity(EXTRA_LOGIN);//登入畫面
                    }
                } else {
                    goActivity(EXTRA_LOGIN);//登入畫面
                }
            }
        });
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    private void goActivity(String activity_name){
        Intent intent = null;
        if (activity_name.equals(EXTRA_LOGIN)){
            intent =  new Intent(IVFWelcomeActivity.this, IVFLoginActivity.class);
        }
        if (activity_name.equals(EXTRA_MAIN)){
            intent = new Intent(IVFWelcomeActivity.this, IVFMainActivity.class);
        }

        if (intent != null){
            startActivity(intent);
        }
    }

//    private ActivityResultLauncher<String> loginLauncher = registerForActivityResult(new ActivityResultContract<String, String>() {
//        @NonNull
//        @Override
//        public Intent createIntent(@NonNull Context context, String activity) {
//            if (activity.equals(EXTRA_LOGIN)){
//                return new Intent(IVFWelcomeActivity.this, IVFLoginActivity.class);
//            }
//            if (activity.equals(EXTRA_MAIN)){
//                return new Intent(IVFWelcomeActivity.this, IVFMainActivity.class);
//            }
//            return null;
//        }
//
//        @Override
//        public String parseResult(int resultCode, @Nullable Intent intent) {
//            return null;
//        }
//    }, new ActivityResultCallback<String>() {
//        @Override
//        public void onActivityResult(String result) {
//
//        }
//    });
}