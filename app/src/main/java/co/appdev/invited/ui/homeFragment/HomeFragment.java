package co.appdev.invited.ui.homeFragment;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.appdev.invited.R;
import co.appdev.invited.events.HomeScrollEvent;
import co.appdev.invited.events.StatusScrollEvent;
import co.appdev.invited.ui.base.BaseFragment;
import co.appdev.invited.util.MainBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeFragment extends BaseFragment {


    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Inject
    HomeFragmentPresenter homeFragmentPresenter;

    private ProgressDialog myDialog;

    @Override
    public void initViews(View parentView) {
        baseActivity.activityComponent().inject(this);
        ButterKnife.bind(getActivity());
        myDialog = new ProgressDialog(getActivity());

        viewPager.setAdapter(new HomePagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity()));

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        MainBus.getInstance().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moveSubscriber);

    }

    private Subscriber<? super Object> moveSubscriber = new Subscriber<Object>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Object o) {
            if (o instanceof HomeScrollEvent) {
                HomeScrollEvent object = (HomeScrollEvent) o;
                viewPager.setCurrentItem(2);
                MainBus.getInstance().post(new StatusScrollEvent(object.getPage()));

            }
        }
    };
}
