package pub.ron.tablayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pub.ron.tablayout.R;


/**
 * @author ron
 */
public class TabLayout extends LinearLayout {

    private static final int DEFAULT_LINE_HEIGHT = 5;
    private static final int DEFAULT_SELECTED = 0;



    private final List<TextView> textViews = new ArrayList<>();

    private final Paint paint = new Paint();

    /**
     * 空闲状态的选中
     */
    private int selected = DEFAULT_SELECTED;
    /**
     * 页面滑动过程中的选中
     */
    private int curr = DEFAULT_SELECTED;


    private float tabTextSize;
    private int tabTextColor;
    private int tabSelectedTextColor;
    private int lineColor;

    private int lineHeight = DEFAULT_LINE_HEIGHT;



    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
        tabTextSize = ta.getDimension(R.styleable.TabLayout_tabTextSize, 20);
        tabTextColor = ta.getColor(R.styleable.TabLayout_tabTextColor, Color.BLACK);
        tabSelectedTextColor = ta.getColor(R.styleable.TabLayout_tabSelectedTextColor, Color.BLUE);
        lineHeight = ta.getDimensionPixelSize(R.styleable.TabLayout_lineHeight, 4);
        lineColor = ta.getColor(R.styleable.TabLayout_lineColor, Color.BLUE);
        ta.recycle();
    }


    /**
     * 将改控件绑定到viewPager上
     * @param viewPager
     */
    public void setup(ViewPager viewPager) {
        final FragmentPagerAdapter adapter = (FragmentPagerAdapter) viewPager.getAdapter();
        if (adapter == null) {
            throw new NullPointerException("viewpager adapter is null!");
        }
        dynamicCreateByAdapter(adapter);
        setSelectedStyle(textViews.get(selected));
        for (int i = 0; i < textViews.size(); i++) {
            TextView textView = textViews.get(i);
            addClickEvent(textView, i, viewPager);
            addView(textView);
        }
        enableDraw();
        viewPager.addOnPageChangeListener(scrollPageChange);
    }

    /**
     * 检查viewpager的滑动，然后重绘线
     */
    private ViewPager.OnPageChangeListener scrollPageChange = new ViewPager.OnPageChangeListener() {

        private float lastPositionOffset = -1F;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 1 || positionOffset == 0) {
                lastPositionOffset = positionOffset;
            }
            if (lastPositionOffset > positionOffset) {
                positionOffset = lastPositionOffset - positionOffset;
            }
            offset = getTabWidth() * positionOffset;
            selected = position;

            invalidate();
        }

        @Override
        public void onPageSelected(int i) {
            setSelectedStyle(textViews.get(i));
            setInitialStyle(textViews.get(curr));
            curr = i;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 根据adapter的title创建tab
     * @param adapter
     */
    private void dynamicCreateByAdapter(FragmentPagerAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            final CharSequence pageTitle = adapter.getPageTitle(i);
            TextView textViewByText = createTextViewByText(pageTitle);
            textViews.add(textViewByText);
        }
    }

    /**
     * 默认viewGroup不会调用onDraw
     */
    private void enableDraw() {
        setWillNotDraw(false);
        invalidate();
    }


    private TextView createTextViewByText(CharSequence text) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab, this, false);
        TextView textView = inflate.findViewById(R.id.text);
        textView.setText(text);
        setInitialStyle(textView);
        return textView;
    }

    /**
     * 设置初始样式
     * @param textView
     */
    private void setInitialStyle(TextView textView) {
        textView.setTextColor(tabTextColor);
        textView.setTextSize(tabTextSize);
    }

    /**
     * 设置选中的样式
     * @param textView
     */
    private void setSelectedStyle(TextView textView) {
        textView.setTextColor(tabSelectedTextColor);
    }

    /**
     * 添加点击事件
     * @param textView
     * @param index
     * @param viewPager
     */
    private static void addClickEvent(TextView textView, int index, final ViewPager viewPager) {
        textView.setTag(index);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem((Integer) v.getTag());
            }
        });
    }

    /**
     * 滚动线的偏移量
     */
    private float offset;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBottomLine(canvas);
    }

    /**
     * 获取tab的宽度
     * @return
     */
    private float getTabWidth() {
        return getWidth() / (float) textViews.size();
    }

    private void drawBottomLine(Canvas canvas) {
        final int y = getHeight() - lineHeight;
        updatePaint();

        final float textWidth = getTextWidth();
        float tabWidth = getTabWidth();
        final float relativeX = selected * tabWidth + offset;
        final float tabX = (tabWidth / 2 - textWidth / 2);
        canvas.drawLine(relativeX + tabX, y, relativeX + tabX + textWidth, y, paint);
    }

    private void updatePaint() {
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineHeight);
    }

    private float getTextWidth() {
        return textViews.get(curr).getLayout().getLineWidth(0);
    }

    public void setTabTextSize(float tabTextSize) {
        this.tabTextSize = tabTextSize;
    }

    public void setTabTextColor(int tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public void setTabSelectedTextColor(int tabSelectedTextColor) {
        this.tabSelectedTextColor = tabSelectedTextColor;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }
}
