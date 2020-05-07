/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.heaven7.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress.
 * <p>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or Fragment call
 * {@link #setViewPager(ViewPager)} providing it the ViewPager this layout is being used for.
 * <p>
 * The colors can be customized in two ways. The first and simplest is to provide an array of colors
 * via {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)}. The
 * alternative is via the {@link TabColorizer} interface which provides you complete control over
 * which color is used for any individual position.
 * <p>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int)},
 * providing the layout ID of your custom layout.
 */
public class SlidingTabLayout extends HorizontalScrollView {


    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @param position the position
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**@param position the position
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    public static abstract class AbsTabColorizer implements TabColorizer{

        private final Context context;

        public AbsTabColorizer( Context context) {
            this.context = context;
        }

        @Override
        public int getIndicatorColor(int position) {
            return context.getResources().getColor(getIndicatorColorRes(position));
        }

        protected abstract int getIndicatorColorRes(int position);
        protected abstract int getDividerColorRes(int position);

        @Override
        public int getDividerColor(int position) {
            return context.getResources().getColor(getDividerColorRes(position));
        }
    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStrip mTabStrip;

    private List<TextView> mTitleTextViews;
    private OnTabListener mInternalTabListener;
    private OnInitTitleListener mInitTitleListener;
    private OnPopulateListener mPopulateListener;

    /** the select position */
    private int mSelectPosition;

    private TabSelectDecoration mTabTitleDecoration;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);

        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);
            try {
                boolean value = a.getBoolean(R.styleable.SlidingTabLayout_sliding_tl_fill_viewport, false);
                setFillViewport(value);
            }finally {
                a.recycle();
            }
        }
        // Make sure that the Tab Strips fills this View
        //if set true. the content gravity will changed .
        //setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStrip(context);
        setOnTabListener(mTabStrip);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * set tab decoration
     * @param mTabDecoration the tab decoration
     * @since 1.2.4
     */
    public void setTabDecoration(SlidingTabLayout.TabDecoration mTabDecoration) {
        mTabStrip.setTabDecoration(mTabDecoration);
    }
    /**
     * you should use the weight of {@link android.widget.LinearLayout}
     * @param splitEquality true to equality
     */
    @Deprecated
    public void setSplitWidthEquality(boolean splitEquality){
        /*if(splitEquality){
            mTabStrip.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //let child split width
                    final int width = getMeasuredWidth();
                    for (int i = 0, size = mTabStrip.getChildCount(); i < size; i++) {
                        mTabStrip.getChildAt(i).getLayoutParams().width = width / size;
                    }
                    mTabStrip.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }*/
    }

    public OnPopulateListener getPopulateListener() {
        return mPopulateListener;
    }
    public void setOnPopulateListener(OnPopulateListener mPopulateListener) {
        this.mPopulateListener = mPopulateListener;
    }

    /**
     * set limit positions
     * @param minPos the min position
     * @param maxPos the max position.
     * @since 1.2.5
     */
    public void setLimitPositions(int minPos, int maxPos){
        mTabStrip.setLimitPositions(minPos, maxPos);
    }
    /**
     * sey the tab title decoration
     * @param tpd the tab title decoration
     * @since 1.2.5
     */
    public void setTabSelectDecoration(TabSelectDecoration tpd){
        if(tpd == null){
            throw new NullPointerException();
        }
        this.mTabTitleDecoration = tpd;
    }

    public void toggleSelect(int position) {
        toogleSelect(position);
    }
    @Deprecated
    public void toogleSelect(int position) {
        if(mSelectPosition == position) return;
        mSelectPosition = position;

        if(mTitleTextViews == null){
             for(int i=0,size = mTabStrip.getChildCount() ; i<size ;i++){
                 TextView child = (TextView) mTabStrip.getChildAt(i);
                 mTabTitleDecoration.onDecorate(child, i, position == i);
             }
        }else{
            for(int i=0,size = mTitleTextViews.size() ; i<size ;i++){
                TextView child = mTitleTextViews.get(i);
                mTabTitleDecoration.onDecorate(child, i, position == i);
            }
        }
    }
    public void setSelectIndicatorHeight(int height){
        mTabStrip.mSelectedIndicatorThickness = height;
    }
    public void setBottomIndicatorHeight(int height){
        mTabStrip.mBottomBorderThickness = height;
    }
    public void setDrawBottomUnderLine(boolean draw){
        mTabStrip.mDrawBottomUnderline = draw;
    }
    /** set the select text color and unselect text color.
     * @param selectColor the color of select
     * @param unSelectColor the color of unselect*/
    public void setSelectRelativeTextColors(int selectColor, int unSelectColor) {
        mTabTitleDecoration = new TabTextColorSelectDecoration(selectColor, unSelectColor);
    }
    public void setSelectRelativeTextColorsRes(int selectColorId, int unSelectColorId) {
        mTabTitleDecoration = new TabTextColorSelectDecoration(
                getContext().getResources().getColor(selectColorId),
                getContext().getResources().getColor(unSelectColorId));
    }

    /**
     * get the tab view for target index
     * @param index the tab index
     * @param <T> the view type
     * @return the tab view.
     * @since 1.2.4
     */
    public <T extends View> T getTabView(int index){
        return (T) mTabStrip.getChildAt(index);
    }
    /**
     * Set the custom {@link TabColorizer} to be used.
     *
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     * @param tabColorizer the Tab Colorizer
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }
    public void setDrawHorizontalIndicator(boolean drawHorizontalIndicator){
        mTabStrip.setDrawHorizontalIndicator(drawHorizontalIndicator);
    }
    public void setDrawVerticalIndicator(boolean drawVerticalIndicator){
        mTabStrip.setDrawVerticalIndicator(drawVerticalIndicator);
    }

    public List<TextView> getTitleTextViews(){
        return mTitleTextViews;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     * @param colors the colors
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     * @param colors the colors
     */
    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SlidingTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     * @param listener the listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
        setViewPagerInternal();
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     * @param viewPager the view pager
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        setViewPagerInternal();
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }
    public void setOnInitTitleListener(OnInitTitleListener mInitTitleListener) {
        this.mInitTitleListener = mInitTitleListener;
    }

    private void setOnTabListener(OnTabListener l){
        mInternalTabListener = l ;
    }
    private void setViewPagerInternal() {
        if(mViewPagerPageChangeListener instanceof SlidingPageChangeListener){
            ((SlidingPageChangeListener) mViewPagerPageChangeListener).setViewPager(mViewPager);
        }
    }
    /*
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     *
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();
        if(mTitleTextViews!=null)
            mTitleTextViews.clear();

        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View tabView = null;
            TextView tabTitleView = null;

            boolean initSuccess = false;
            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                if(mTitleTextViews == null)
                    mTitleTextViews = new ArrayList<>();
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
                if(mInitTitleListener!=null){
                    initSuccess = mInitTitleListener.onInitTitle(i,tabTitleView);
                }
                mTitleTextViews.add(tabTitleView);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
                if(mInitTitleListener!=null){
                    initSuccess = mInitTitleListener.onInitTitle(i, (TextView) tabView);
                }
            }

            if(!initSuccess) {
                if(tabTitleView == null){
                    if( TextView.class.isInstance(tabView)){
                        tabTitleView = (TextView) tabView;
                    }else{
                        throw new IllegalStateException("tabView not intanceof TextView");
                    }
                }
                tabTitleView.setText(adapter.getPageTitle(i));
                /*if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                    if( TextView.class.isInstance(tabView)) {
                        tabTitleView = (TextView) tabView;
                        tabTitleView.setText(adapter.getPageTitle(i));
                    }
                }*/
            }

            tabView.setOnClickListener(tabClickListener);

            mTabStrip.addView(tabView);
            //default select 0
            mTabTitleDecoration.onDecorate(tabTitleView, i, i == 0);

            if(mPopulateListener != null){
                mPopulateListener.onPopulateTab(this, i, count ,tabView, tabTitleView);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }
            scrollTo(targetScrollX, 0);
            if(mInternalTabListener != null){
                mInternalTabListener.onScroll(tabIndex,positionOffset,targetScrollX);
            }
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
            if(mSelectPosition != position){
                mSelectPosition = position;
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    mSelectPosition = i;
                    return;
                }
            }
        }
    }

    public interface OnTabListener{
        void onScroll(int tabIndex, int positionOffset, int targetScrollX);
    }

    /**
     * this must be called
     */
    public interface OnInitTitleListener{

        /**
         * init title
         * @param position  the position of this tab
         * @param title the tab text/title
         * @return  true  while init title  success!
         */
       boolean onInitTitle(int position, TextView title);
    }

    public static abstract class SlidingPageChangeListener implements ViewPager.OnPageChangeListener{
        WeakReference<ViewPager> mWeakVp;
        boolean mIgnore;

        public void setViewPager(ViewPager vp){
            if(vp == null){
                mWeakVp = null;
            }else {
                mWeakVp = new WeakReference<>(vp);
            }
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(!mIgnore){
                //adjust position to avoid a bug.
                if(mWeakVp != null){
                    ViewPager vp = mWeakVp.get();
                    if(vp != null){
                        position = vp.getCurrentItem();
                    }
                }
                //callback
                onPageSelected(position);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            mIgnore = state == ViewPager.SCROLL_STATE_SETTLING;
        }
    }
    public interface OnPopulateListener {
        void onPopulateTab(SlidingTabLayout stl, int position, int count, View itemView, TextView tabview);
    }

    /**
     * the tab decoration
     * @author heaven7
     * @since 1.2.4
     */
    public interface TabDecoration{
        /**
         * called on draw horizontal indicator
         * @param canvas the canvas
         * @param paint the paint
         * @param rect the expect rect to draw
         * @return true if handled this draw.
         */
        boolean drawHorizontalIndicator(Canvas canvas, Paint paint, Rect rect);
        /**
         * called on draw bottom underline. this depend on horizontal indicator
         * @param canvas the canvas
         * @param paint the paint
         * @param rect the expect rect to draw
         * @return true if handled this draw.
         */
        boolean drawBottomUnderline(Canvas canvas, Paint paint, Rect rect);

        /**
         * called on draw vertical indicator
         * @param canvas the canvas
         * @param tabStrip the tab strip
         * @param dividerPaint the divider paint
         * @param separatorTop the expect top of vertical indicator
         * @param dividerHeightPx the divider height
         * @return true if handled this draw.
         */
        boolean drawVerticalIndicator(Canvas canvas, ViewGroup tabStrip,  Paint dividerPaint, int separatorTop, int dividerHeightPx);
    }

    /**
     * the tab position decoration. used to decorate the target position on position changed.
     * @since 1.2.5
     */
    public interface TabSelectDecoration{
        /**
         * called on decorate the position.
         * @param title the title text view
         * @param position the current position
         * @param selected true if the position of this tab is selected.
         */
        void onDecorate(TextView title, int position, boolean selected);
    }

    /**
     * the text color impl for {@linkplain TabSelectDecoration}
     * @since 1.2.5
     */
    public static class TabTextColorSelectDecoration implements TabSelectDecoration{

        private final int selectTextColor;
        private final int unselectTextColor;

        public TabTextColorSelectDecoration(int selectTextColor, int unselectTextColor) {
            this.selectTextColor = selectTextColor;
            this.unselectTextColor = unselectTextColor;
        }
        @Override
        public void onDecorate(TextView title, int position, boolean selected) {
            title.setTextColor(selected ? selectTextColor : unselectTextColor);
        }
    }
    /*
     *
     private void initSlidingTabLayout() {
     final Resources res = getResources();
     mSlidingTabLayout.setOnInitTitleListener(new SlidingTabLayout.OnInitTitleListener() {
    @Override
    public boolean onInitTitle(int position, TextView title) {

    switch (position) {
    case 0:
    title.setTextColor(title.getContext().getResources().getColor(R.color.c_333333));
    title.setText(getString(R.string.commodity));
    break;
    case 1:
    title.setTextColor(title.getContext().getResources().getColor(R.color.c_666666));
    title.setText(getString(R.string.detail));
    break;
    case 2:
    title.setTextColor(title.getContext().getResources().getColor(R.color.c_666666));
    title.setText(getString(R.string.comment));
    break;
    }
    return true;
    }
    });
     mSlidingTabLayout.setSelectIndicatorHeight(res.getDimensionPixelSize(R.dimen.common_indicator_height));
     mSlidingTabLayout.setSplitWidthEquality(false);
     mSlidingTabLayout.setSelectRelativeTextColorsRes(R.color.c_333333, R.color.c_666666);
     mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.AbsTabColorizer(this) {
    @Override
    protected int getIndicatorColorRes(int position) {
    return R.color.c_333333;
    }

    @Override
    protected int getDividerColorRes(int position) {
    return R.color.c_ff9e1f;
    }
    });
     mSlidingTabLayout.setOnPageChangeListener(new BasePageChangeListener() {
    @Override
    public void onPageSelected(int position) {
    onSelect(position);
    }
    });
     }
     */
}
