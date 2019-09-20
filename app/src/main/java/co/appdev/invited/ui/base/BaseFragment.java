package co.appdev.invited.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import co.appdev.invited.injection.component.ActivityComponent;


public abstract class BaseFragment extends Fragment{
    public static final String ARGS_INSTANCE = "co.appdev.boilerplate.argsInstance";

    public View parent;
    public FragmentNavigation mFragmentNavigation;
    public BaseActivity baseActivity;
    int mInt = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle args = getArguments();
//        if (args != null) {
//            mInt = args.getInt(ARGS_INSTANCE);
//        }

        ActivityComponent component = ((BaseActivity) getActivity()).activityComponent();
        if (component == null) {

            return;
        }
        component.inject(this);
        baseActivity = (BaseActivity) getActivity();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
            mFragmentNavigation.navigationTitle(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            parent = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, parent);
//            parent.setOnClickListener(this);
            initViews(parent);
        return parent;
    }

    public interface FragmentNavigation {
        public void pushFragment(Fragment fragment, boolean detach);
        public void replaceFragment(Fragment fragment);
        public void navigationTitle(Fragment fragment);
        public void popCurrentFragment();
    }

    public abstract void initViews(View parentView);

    public abstract int getLayoutId();


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
