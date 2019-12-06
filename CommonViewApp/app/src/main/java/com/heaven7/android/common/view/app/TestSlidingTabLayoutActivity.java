package com.heaven7.android.common.view.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.heaven7.adapter.BaseFragmentPagerAdapter;
import com.heaven7.android.common.view.app.frag.SlidingTabFragment;
import com.heaven7.android.common.view.app.utils.DimenUtil;
import com.heaven7.core.util.BundleHelper;

import org.heaven7.core.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestSlidingTabLayoutActivity extends BaseActivity {

    @BindView(R.id.slidingTab)
    SlidingTabLayout mSlidingTabLayout;

    @BindView(R.id.vp)
    ViewPager mVp;

    private BaseFragmentPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_sliding_tab;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        initSlidingTabLayout();
        mVp.setAdapter(mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), createFragmentListData()));
        mVp.setOffscreenPageLimit(3);
        mSlidingTabLayout.setViewPager(mVp);
        mVp.setCurrentItem(0);
    }

    private List<BaseFragmentPagerAdapter.FragmentData> createFragmentListData() {
        Resources res = getResources();
        String[] tabs = {
                res.getString(R.string.all),
                res.getString(R.string.doing),
                res.getString(R.string.wait_settle),
                res.getString(R.string.exception_ing),
                res.getString(R.string.already_finish),
        };
        List<BaseFragmentPagerAdapter.FragmentData> list = new ArrayList<>();
        for (String tab : tabs){
            BaseFragmentPagerAdapter.FragmentData data = new BaseFragmentPagerAdapter.FragmentData(tab, SlidingTabFragment.class,
                    new BundleHelper()
                            .putString(SlidingTabFragment.KEY_TEXT, tab)
                            .getBundle());
            list.add(data);
        }
        return list;
    }

    private void initSlidingTabLayout() {
        FragmentActivity activity = this;
        mSlidingTabLayout.setDrawBottomUnderLine(true);
        mSlidingTabLayout.setDrawHorizontalIndicator(true);
        mSlidingTabLayout.setSelectIndicatorHeight(DimenUtil.dip2px(activity, 2));
        mSlidingTabLayout.setSelectRelativeTextColorsRes(R.color.colorTheme, R.color.colorSecond);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.AbsTabColorizer(activity) {
            @Override
            protected int getIndicatorColorRes(int position) {
                return R.color.colorTheme;
            }

            @Override
            protected int getDividerColorRes(int position) {
                return android.R.color.transparent;
            }
        });
        mSlidingTabLayout.setCustomTabView(R.layout.tab_transport_bill, R.id.tv_tab);
        mSlidingTabLayout.setOnPageChangeListener(new SlidingTabLayout.SlidingPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //  Logger.d(TAG, "onPageSelected", "position = " + position);
                mSlidingTabLayout.toogleSelect(position);
            }
        });
        mSlidingTabLayout.setTabDecoration(new SlidingTabLayout.TabDecoration() {
            @Override
            public boolean drawHorizontalIndicator(Canvas canvas, Paint paint, Rect rect) {
                int width = rect.width();
                rect.inset(width / 6, 0);
                //hold 2/3
                canvas.drawRect(rect, paint);
                return true;
            }
            @Override
            public boolean drawBottomUnderline(Canvas canvas, Paint paint, Rect rect) {
                return false;
            }
            @Override
            public boolean drawVerticalIndicator(Canvas canvas, ViewGroup tabStrip, Paint dividerPaint, int separatorTop, int dividerHeightPx) {
                return false;
            }
        });
    }
}
