package co.appdev.invited.ui.userProfile;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import co.appdev.invited.R;
import co.appdev.invited.data.model.User;
import co.appdev.invited.ui.base.BaseFragment;
import co.appdev.invited.ui.home.HomeActivity;

public class ProfileFullImageFragment extends BaseFragment {

    @BindView(R.id.full_image)
    ImageView fullImage;

    private User user;

    public static ProfileFullImageFragment newInstance(User user) {
        ProfileFullImageFragment eventDetailFragment = new ProfileFullImageFragment();
        eventDetailFragment.setUserEvent(user);
        return eventDetailFragment;
    }

    private void setUserEvent(User userEvent) {
        this.user = userEvent;
    }

    @Override
    public void initViews(View parentView) {
        baseActivity.activityComponent().inject(this);
        ((HomeActivity) getActivity()).hideNavMenu();

        if(!(user.getProfileImage() == null) && !user.getProfileImage().equals("")) {
            Uri profilePhotoUri = Uri.parse(user.getProfileImage());
            Glide.with(getContext()).load(profilePhotoUri).fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(fullImage);
        }else{
            Glide.with(getContext())
                    .load(R.drawable.icon_red_person)
                    .into(fullImage);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.full_image;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity) getActivity()).showNavMenu();

    }
}
