package com.bytedesk.demo.common;


import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bytedesk.demo.agent.AgentController;
import com.bytedesk.demo.social.SocialController;
import com.bytedesk.demo.visitor.VisitorController;
import com.bytedesk.demo.R;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TabFragment extends BaseFragment {

    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.tabs) QMUITabSegment mTabSegment;
    private HashMap<Pager, BaseController> mPages;

    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        private int mChildCount = 0;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            BaseController page = mPages.get(Pager.getPagerFromPosition(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(page, params);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount == 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };


    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab, null);
        ButterKnife.bind(this, layout);
        initTabs();
        initPagers();
        return layout;
    }

    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_gray_6);
        int selectColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_blue);
        mTabSegment.setDefaultNormalColor(normalColor);
        mTabSegment.setDefaultSelectedColor(selectColor);

        QMUITabSegment.Tab home = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_favorites),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_favorites),
                "访客端接口", false
        );

        QMUITabSegment.Tab contact = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_friends),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_friends),
                "客服端接口(暂未开放)", false
        );

        QMUITabSegment.Tab social = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_friends),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_friends),
                "云通讯接口(暂未开放)", false
        );

        mTabSegment.addTab(home)
                .addTab(contact)
                .addTab(social);
    }

    private void initPagers() {

        BaseController.HomeControlListener listener = new BaseController.HomeControlListener() {
            @Override
            public void startFragment(BaseFragment fragment) {
                TabFragment.this.startFragment(fragment);
            }
        };

        mPages = new HashMap<>();

        BaseController homeController = new VisitorController(getActivity());
        homeController.setHomeControlListener(listener);
        mPages.put(Pager.Visitor, homeController);

        BaseController contactController = new AgentController(getActivity());
        contactController.setHomeControlListener(listener);
        mPages.put(Pager.Agent, contactController);

        BaseController socialController = new SocialController(getActivity());
        socialController.setHomeControlListener(listener);
        mPages.put(Pager.Social, socialController);

        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager, false);
    }

    enum Pager {
        Visitor, Agent, Social;

        public static Pager getPagerFromPosition(int position) {
            switch (position) {
                case 0:
                    return Visitor;
                case 1:
                    return Agent;
                case 2:
                    return Social;
                default:
                    return Visitor;
            }
        }
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }


}
