package thisis.vegetarian.question.mark;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IVFLoginViewPage2Adapter extends FragmentStateAdapter {
    List<Fragment> fList;
    public IVFLoginViewPage2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }

    public void setFragmentList(List<Fragment> fList) {
        this.fList = fList;
    }

    @Override
    public int getItemCount() {
        return fList.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.fList.get(position);
    }
}
