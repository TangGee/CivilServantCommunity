package com.mdove.dependent.common.view.tablayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mdove.dependent.common.BuildConfig;
import com.mdove.dependent.common.R;
import com.mdove.dependent.common.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.mdove.dependent.common.view.tablayout.CubicBezierInterpolator.CUBICOUT;

/**
 * 滑动TabLayout,对于ViewPager的依赖性强
 */
public class SlidingTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    public static String TAG = SlidingTabLayout.class.getSimpleName();
    protected Context mContext;
    private ViewPager mViewPager;
    private SmartTabIndicationInterpolator mIndicationInterpolator;

    public ArrayList<String> getmTitles() {
        return mTitles;
    }

    private ArrayList<String> mTitles;
    protected LinearLayout mTabsContainer;
    protected int mCurrentTab;
    private float mCurrentPositionOffset;
    protected int mTabCount;
    private int mDirection = DIRECTION_IDEL; // 0: normal    1: left    2: right
    protected static final int DIRECTION_IDEL = 0;
    protected static final int DIRECTION_LEFT = 1;
    protected static final int DIRECTION_RIGHT = 2;

    /**
     * 用于绘制显示器
     */
    private RectF mIndicatorRect = new RectF();
    private Paint mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 用于实现滚动居中
     */
    private Rect mTabRect = new Rect();

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    private int mIndicatorStyle = STYLE_NORMAL;

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /**
     * indicator
     */
    private int mDefaultIndicatorColor; //default indicator color
    private ArrayList<Integer> mIndicatorColors = new ArrayList<>(); //each color in this arrays is representing the corresponding tab index's indicator color, color index equal to tab index.
    protected float mIndicatorHeight;
    private float mIndicatorWidth;
    protected float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    protected float mIndicatorMarginBottom;
    private int mIndicatorGravity;
    private boolean mIndicatorWidthEqualTitle;

    /**
     * underline
     */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;

    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /**
     * title
     */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    protected float mTextsize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;

    private int mLastScrollX;
    private int mHeight;
    private boolean mSnapOnTabClick;

    public SlidingTabLayout(Context context) {
        this(context, null, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        obtainAttributes(context, attrs);

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }
    }

    protected void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);

        mIndicatorStyle = ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_style, STYLE_NORMAL);
        mDefaultIndicatorColor = ta.getColor(R.styleable.SlidingTabLayout_tl_indicator_color, Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_height,
                dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_width, dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_corner_radius, dp2px(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_left, dp2px(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_top, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_right, dp2px(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_bottom, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorGravity = ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_gravity, Gravity.BOTTOM);
        mIndicatorWidthEqualTitle = ta.getBoolean(R.styleable.SlidingTabLayout_tl_indicator_width_equal_title, false);
        mIndicationInterpolator = SmartTabIndicationInterpolator.of(ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_interpolator, SmartTabIndicationInterpolator.ID_LINEAR));

        mUnderlineColor = ta.getColor(R.styleable.SlidingTabLayout_tl_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta.getDimension(R.styleable.SlidingTabLayout_tl_underline_height, dp2px(0));
        mUnderlineGravity = ta.getInt(R.styleable.SlidingTabLayout_tl_underline_gravity, Gravity.BOTTOM);

        mDividerColor = ta.getColor(R.styleable.SlidingTabLayout_tl_divider_color, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_divider_width, dp2px(0));
        mDividerPadding = ta.getDimension(R.styleable.SlidingTabLayout_tl_divider_padding, dp2px(12));

        mTextsize = ta.getDimension(R.styleable.SlidingTabLayout_tl_textsize, sp2px(14));
        mTextSelectColor = ta.getColor(R.styleable.SlidingTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
        mTextUnselectColor = ta.getColor(R.styleable.SlidingTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"));
        mTextBold = ta.getInt(R.styleable.SlidingTabLayout_tl_textBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.SlidingTabLayout_tl_textAllCaps, false);

        mTabSpaceEqual = ta.getBoolean(R.styleable.SlidingTabLayout_tl_tab_space_equal, false);
        mTabWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_width, dp2px(-1));
        mTabPadding = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(20));

        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            mTabsContainer = new LinearLayout(getContext());
            addView(mTabsContainer);
        } else {
            mTabsContainer = (LinearLayout) getChildAt(0);
        }
    }

    /**
     * 关联ViewPager
     */
    public void setViewPager(@NotNull ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        this.mViewPager = vp;
        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    public void setViewPager(@NotNull ViewPager vp, @NotNull ArrayList<String> titles) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (String item :
                titles) {
            colors.add(mDefaultIndicatorColor);
        }

        setViewPager(vp, titles, colors);
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    public void setViewPager(@NotNull ViewPager vp, @NotNull ArrayList<String> titles, @NotNull ArrayList<Integer> colors) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        if (titles == null || titles.size() == 0) {
            if (BuildConfig.DEBUG) {
//                throw new IllegalStateException("Titles can not be EMPTY !");
            }

            return;
        }

        if (titles.size() != vp.getAdapter().getCount()) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException("Titles length must be the same as the page count !");
            }

            return;
        }

        this.mViewPager = vp;
        mTitles = titles;
        if (colors.size() <= 0) {
            for (String item :
                    titles) {
                colors.add(mDefaultIndicatorColor);
            }
        }
        mIndicatorColors = colors;

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
        updateTabSelection(mCurrentTab);
    }

    /**
     * 关联ViewPager,用于连适配器都不想自己实例化的情况
     */
    public void setViewPager(@NotNull ViewPager vp, @NotNull String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) {
        if (vp == null) {
            throw new IllegalStateException("ViewPager can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (String item :
                titles) {
            colors.add(mDefaultIndicatorColor);
        }
        mIndicatorColors = colors;

        this.mViewPager = vp;
        this.mViewPager.setAdapter(new InnerPagerAdapter(fa.getSupportFragmentManager(), fragments, titles));

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }


    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        int childCount = mTabsContainer.getChildCount();
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();

        for (int i = childCount; i < mTabCount; i++) {
            MainFeedTabView tabView = new MainFeedTabView(mContext);
            mTabsContainer.addView(tabView, i);
        }

        if (childCount > mTabCount) {
            mTabsContainer.removeViews(mTabCount, childCount - mTabCount);
        }

        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(i) : mTitles.get(i);
            addTab(i, pageTitle.toString(), tabView);
        }

        updateTabStyles();
    }

    public void addNewTab(String title, Integer indicatorColor) {
        View tabView = View.inflate(mContext, R.layout.layout_tab, null);
        if (mTitles != null) {
            mTitles.add(title);
            if (indicatorColor != null) {
                mIndicatorColors.add(indicatorColor);
            } else {
                mIndicatorColors.add(mDefaultIndicatorColor);
            }
        }
        CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(mTabCount) : mTitles.get(mTabCount);
        addTab(mTabCount, pageTitle.toString(), tabView);
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();

        updateTabStyles();
    }

    public void addNewTab(String title) {
        addNewTab(title, null);
    }


    /**
     * 创建并添加tab
     */
    protected void addTab(final int position, String title, View tabView) {
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        if (tv_tab_title != null) {
            if (title != null) tv_tab_title.setText(title);
            tv_tab_title.setSingleLine(true);
        }

        Paint paint = new Paint();
        paint.setTextSize(dp2px(16));
        float tabWidth = dp2px(84);
        float titleWidth = paint.measureText(title);
        if (tabWidth < titleWidth) {
            mTextsize = dp2px(14);
        }
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTab(v);
            }
        });

        /** 每一个Tab的布局参数 */
        if (tv_tab_title != null) {
            mTextPaint.setTextSize(mTextsize);
            setTabLayout((int) (mTextPaint.measureText(title) + mTabPadding * 2), tabView);
        } else {
            setTabLayout(ViewGroup.LayoutParams.WRAP_CONTENT, tabView);
        }
    }

    protected void setTabLayout(int width, View tabView) {
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(width, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }

        tabView.setLayoutParams(lp_tab);
    }

    protected void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
//            v.setPadding((int) mTabPadding, v.getPaddingTop(), (int) mTabPadding, v.getPaddingBottom());
            TextView tv_tab_title = (TextView) v.findViewById(R.id.tv_tab_title);
            if (tv_tab_title != null) {
                tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize);
                tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
                if (mTextAllCaps) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }

                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tv_tab_title.getPaint().setFakeBoldText(false);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position:当前View的位置
         * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
         */
        this.mCurrentTab = position;
        this.mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            mDirection = DIRECTION_IDEL;
        }
    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    protected void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }
        View view = mTabsContainer.getChildAt(mCurrentTab);
        if (view == null) {
            return;
        }
        int offset = (int) (mCurrentPositionOffset * view.getWidth());
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = mTabsContainer.getChildAt(mCurrentTab).getLeft() + offset;
        float oldLeft = mIndicatorRect.left;

        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        //calculate direction according to diff between old left and new left.
        if (mIndicatorRect.left - oldLeft > 0) {
            mDirection = DIRECTION_RIGHT;
        } else if (mIndicatorRect.left - oldLeft < 0) {
            mDirection = DIRECTION_LEFT;
        } else {
            mDirection = DIRECTION_IDEL;
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             *  x:表示离起始位置的x水平方向的偏移量
             *  y:表示离起始位置的y垂直方向的偏移量
             */
            scrollToDes(newScrollX, 0);
        }
    }

    protected void scrollToDes(int x, int y) {
        scrollTo(x, 0);
    }

    protected void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            if (tab_title != null) {
                tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tab_title.getPaint().setFakeBoldText(isSelect);
                }
            }
            mTabsContainer.getChildAt(mCurrentTab);
        }
    }

    private float margin;

    private void calcIndicatorRect() {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            TextView tab_title = (TextView) currentTabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextsize);
            float textWidth = mTextPaint.measureText(tab_title.getText().toString());
            margin = (right - left - textWidth) / 2;
        }

        float startOffset = mIndicationInterpolator.getLeftEdge(mCurrentPositionOffset);
        float endOffset = mIndicationInterpolator.getRightEdge(mCurrentPositionOffset);
        if (this.mCurrentTab < mTabCount - 1) {
            View nextTabView = mTabsContainer.getChildAt(this.mCurrentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + startOffset * (nextTabLeft - left);
            right = right + endOffset * (nextTabRight - right);

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                TextView next_tab_title = (TextView) nextTabView.findViewById(R.id.tv_tab_title);
                mTextPaint.setTextSize(mTextsize);
                float nextTextWidth = mTextPaint.measureText(next_tab_title.getText().toString());
                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                margin = margin + mCurrentPositionOffset * (nextMargin - margin);
            }
        }

        mTabRect.left = (int) left;
        mTabRect.right = (int) right;

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
            mIndicatorRect.left = left;
            mIndicatorRect.right = right;
            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                mIndicatorRect.left = (left + margin - 1);
                mIndicatorRect.right = (right - margin - 1);
            }
        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;
            float indicatorRight = indicatorLeft + mIndicatorWidth;

            if (this.mCurrentTab < mTabCount - 1) {
                View nextTab = mTabsContainer.getChildAt(this.mCurrentTab + 1);
                indicatorLeft = indicatorLeft + startOffset * (currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
                indicatorRight = indicatorRight + endOffset * (currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
            }

            mIndicatorRect.left = indicatorLeft;
            mIndicatorRect.right = indicatorRight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderlineHeight, mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft, mUnderlineHeight, mRectPaint);
            }
        }

        //draw indicator line
        calcIndicatorRect();
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.setColor(mDefaultIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
            } else {

            }

            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2;
                }
            }

            drawIndicator(canvas,
                    (int) (paddingLeft + mIndicatorMarginLeft + mIndicatorRect.left),
                    (int) mIndicatorMarginTop,
                    (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                    (int) (mIndicatorMarginTop + mIndicatorHeight));
        } else {
            drawIndicator(canvas,
                    (int) (paddingLeft + mIndicatorMarginLeft + mIndicatorRect.left),
                    (int) (height - mIndicatorHeight - mIndicatorMarginBottom),
                    (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                    (int) (height - mIndicatorMarginBottom));
        }
    }

    private void drawIndicator(Canvas canvas, int left, int top, int right, int bottom) {
        if (mIndicatorHeight <= 0) {
            return;
        }

        int[] colors = getLinearGradientColors();
        float targetOffset = mIndicationInterpolator.getRightEdge(mCurrentPositionOffset);
        float[] positions = new float[]{1 - targetOffset, targetOffset};
        LinearGradient linearGradient = new LinearGradient(left, top, right, bottom, colors, positions, Shader.TileMode.CLAMP);

        mIndicatorRect.set(left, top, right, bottom);
        mIndicatorPaint.setShader(linearGradient);

        if (mIndicatorCornerRadius > 0f) {
            canvas.drawRoundRect(
                    mIndicatorRect, mIndicatorCornerRadius,
                    mIndicatorCornerRadius, mIndicatorPaint);
        } else {
            canvas.drawRect(mIndicatorRect, mIndicatorPaint);
        }
    }

    private int[] getLinearGradientColors() {
        int startColor;
        int endColor;
        int[] linearGradientColors = new int[2];
        final int size = mIndicatorColors.size();
        if (size > 0) {
            int endColorIndex = mCurrentTab;
            if (mDirection == DIRECTION_IDEL) {
                endColorIndex = mCurrentTab;
            } else if (mDirection == DIRECTION_LEFT) {
                endColorIndex = (mCurrentTab - 1 < 0) ? size - 1 : mCurrentTab - 1;
            } else if (mDirection == DIRECTION_RIGHT) {
                //direction right
                endColorIndex = (mCurrentTab + 1 < size) ? mCurrentTab + 1 : 0;
            }

            startColor = mIndicatorColors.get(mCurrentTab % size);
            endColor = mIndicatorColors.get(endColorIndex % size);
        } else {
            startColor = mDefaultIndicatorColor;
            endColor = mDefaultIndicatorColor;
        }
        linearGradientColors[0] = startColor;
        linearGradientColors[1] = endColor;

        return linearGradientColors;
    }

    public void firstInitTextSeletion() {
        updateTabSelection(0);
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab);
    }

    public void setCurrentTab(int currentTab, boolean smoothScroll) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab, smoothScroll);
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public void setIndicatorInterpolator(SmartTabIndicationInterpolator interpolator) {
        this.mIndicationInterpolator = interpolator;
        invalidate();
    }

    public void setTabPadding(float tabPadding) {
        this.mTabPadding = dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.mTabWidth = dp2px(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mDefaultIndicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = dp2px(indicatorHeight);
        invalidate();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.mIndicatorWidth = dp2px(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                   float indicatorMarginRight, float indicatorMarginBottom) {
        this.mIndicatorMarginLeft = dp2px(indicatorMarginLeft);
        this.mIndicatorMarginTop = dp2px(indicatorMarginTop);
        this.mIndicatorMarginRight = dp2px(indicatorMarginRight);
        this.mIndicatorMarginBottom = dp2px(indicatorMarginBottom);
        invalidate();
    }

    public void setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
        this.mIndicatorWidthEqualTitle = indicatorWidthEqualTitle;
        invalidate();
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(float underlineHeight) {
        this.mUnderlineHeight = dp2px(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public void setDividerWidth(float dividerWidth) {
        this.mDividerWidth = dp2px(dividerWidth);
        invalidate();
    }

    public void setDividerPadding(float dividerPadding) {
        this.mDividerPadding = dp2px(dividerPadding);
        invalidate();
    }

    public void setTextsize(float textsize) {
        this.mTextsize = sp2px(textsize);
        updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    public void setTextBold(int textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
    }

    public void setSnapOnTabClick(boolean snapOnTabClick) {
        mSnapOnTabClick = snapOnTabClick;
    }


    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth(View tabView) {
        if (mTabWidth > 0) {
            return mTabWidth;
        } else if (mTabSpaceEqual) {
            return mTabCount > 0 ? UIUtils.getScreenWidth(getContext()) / mTabCount : getTitleLength();
        } else {
            return tabView.getWidth();
        }
    }

    public int getIndicatorColor() {
        return mDefaultIndicatorColor;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

    public float getTextsize() {
        return mTextsize;
    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return mTextUnselectColor;
    }

    public int getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    public View getTabView(int tab) {
        return mTabsContainer.getChildAt(tab);
    }

    public void setTabViewScaleAnim(int tab) {
        float fromScale = 1.0f;
        float toScale = 0.8f;
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);

        Animator scaleX1 = ObjectAnimator.ofFloat(tv_tab_title, "scaleX", fromScale, toScale);
        Animator scaleY1 = ObjectAnimator.ofFloat(tv_tab_title, "scaleY", fromScale, toScale);
        scaleX1.setInterpolator(new CubicBezierInterpolator(CUBICOUT));
        scaleY1.setInterpolator(new CubicBezierInterpolator(CUBICOUT));
        scaleX1.setDuration(100);
        scaleY1.setDuration(100);

        Animator scaleX2 = ObjectAnimator.ofFloat(tv_tab_title, "scaleX", toScale, fromScale);
        Animator scaleY2 = ObjectAnimator.ofFloat(tv_tab_title, "scaleY", toScale, fromScale);
        scaleX2.setInterpolator(new OvershootInterpolator(2.0f));
        scaleY2.setInterpolator(new OvershootInterpolator(2.0f));
        scaleX2.setDuration(300);
        scaleY2.setDuration(300);
        scaleX2.setStartDelay(100);
        scaleY2.setStartDelay(100);
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        animatorSet.setDuration(400);
        animatorSet.playTogether(scaleX1, scaleY1, scaleX2, scaleY2);
        animatorSet.start();
    }

    //setter and getter

    // show MsgTipView
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int position, int num) {
        showBadge(position, String.valueOf(num));
    }

    public void showBadge(int position) {
        showBadge(position, null);
    }

    public void showBadge(int position, String badgeText) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = (MsgView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            if (!TextUtils.isEmpty(badgeText) && TextUtils.isDigitsOnly(badgeText)) {
                UnreadMsgUtils.show(tipView, Integer.valueOf(badgeText), false);
            } else {
                UnreadMsgUtils.show(tipView, badgeText);
            }
            if (!TextUtils.isEmpty(badgeText)) {
                setMsgMargin(position, -4, 6);
            } else {
                setMsgMargin(position, 4, 3);
            }
            if (mInitSetMap.get(position) != null && mInitSetMap.get(position)) {
                return;
            }

            mInitSetMap.put(position, true);
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        showBadge(position);
    }

    public int getTitleLength() {
        int position = mTabCount - 1;
        View tabView = mTabsContainer.getChildAt(position);
        TextView tipView = (TextView) tabView.findViewById(R.id.tv_tab_title);
        if (tipView != null) {
            Paint paint = new Paint();
            paint.setTextSize(tipView.getTextSize());
            return (int) (tabView.getWidth() - paint.measureText(tipView.getText().toString()));
        }
        return 0;
    }

    /**
     * 隐藏未读消息
     */
    public void hideMsg(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = (MsgView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置未读消息偏移,原点为文字的右上角.当控件高度固定,消息提示位置易控制,显示效果佳
     */
    public void setMsgMargin(int position, float leftMargin, float topMargin) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = (MsgView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            mTextPaint.setTextSize(tipView.getTextSize());
            float tipWidth = tipView.getPaddingLeft() + tipView.getPaddingRight() + mTextPaint.measureText(tipView.getText().toString());

            TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextsize);
            float textWidth = mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();

            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();

            final float tabWidth = getTabWidth(tabView);
            if (getTabWidth(tabView) > 0) {
                lp.leftMargin = (int) (tabWidth / 2 + textWidth / 2 + dp2px(leftMargin));
                if (lp.leftMargin + tipWidth > tabWidth) {
                    lp.leftMargin = (int) (tabWidth - tipWidth) - dp2px(2);
                }
            } else {
                lp.leftMargin = (int) (mTabPadding + textWidth + dp2px(leftMargin));
            }
            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight) / 2 - dp2px(topMargin) : 0;
            tipView.setLayoutParams(lp);
        }
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    public MsgView getMsgView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = (MsgView) tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }

    public void removeAllTabs() {
        mTabsContainer.removeAllViews();
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    protected void onClickTab(View view) {
        int position = mTabsContainer.indexOfChild(view);
        if (position != -1) {
            if (mViewPager.getCurrentItem() != position) {
                if (mSnapOnTabClick) {
                    mViewPager.setCurrentItem(position, false);
                } else {
                    mViewPager.setCurrentItem(position);
                }

                if (mListener != null) {
                    mListener.onTabSelect(position);
                }
            } else {
                if (mListener != null) {
                    mListener.onTabReselect(position);
                }
            }
        }
    }
}
