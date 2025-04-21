package nix.cake.android.ui.main.profile.address.detail.fragment.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import nix.cake.android.ui.main.profile.address.detail.fragment.CommuneFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.DistrictFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.ProvinceFragment;

public class AddressViewPagerAdapter extends FragmentStateAdapter {
    public AddressViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new DistrictFragment();
            case 2:
                return new CommuneFragment();
            default:
                return new ProvinceFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
