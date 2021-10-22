package thisis.vegetarian.question.mark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class IVFInfoProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setConfig();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivf_infoproduct);
    }

    private void setConfig() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            findViewById(android.R.id.content).setTransitionName("select_item");
            setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());


            MaterialContainerTransform enterTransform = new MaterialContainerTransform();
            enterTransform.addTarget(android.R.id.content);
            enterTransform.setAllContainerColors(MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface));
            enterTransform.setDuration(800);

            MaterialContainerTransform returnTransform = new MaterialContainerTransform();
            returnTransform.addTarget(android.R.id.content);
            returnTransform.setAllContainerColors(MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface));
            returnTransform.setDuration(1000);

            getWindow().setSharedElementEnterTransition(enterTransform);
            getWindow().setSharedElementReturnTransition(returnTransform);
        }
    }
}