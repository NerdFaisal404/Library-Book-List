package bd.edu.mediaplayer.phonecallbook.helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ravi on 30/10/15.
 * updated by Ravi on 14/11/17
 */
public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {

   /* private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;
    private int mOrientation;
    private Context context;
    private int margin;
    private Paint paint;
    private int mColor;

    public MyDividerItemDecoration(Context context,
                                   int orientation,
                                   int margin, int color) {
        this.context = context;
        this.margin = margin;
        this.paint = new Paint();
        paint.setAntiAlias(true);
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            paint.setColor(mColor);
            mDivider.setBounds(left + dpToPx(margin), top, right - dpToPx(margin), bottom);
            mDivider.draw(c);
        }

    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin));
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
*/

    private int mDividerHeight;
    private int mColor;

    private int mOffsets;
    private int mAdjustedOffsets;
    private boolean mToAdjustOffsets = false;



    /**
     * Create new DividerItemDecoration with the specified height and color
     * without additional offsets
     */
    public MyDividerItemDecoration(int dividerHeight, int color) {
        mDividerHeight = dividerHeight;
        mColor = color;
        mOffsets = mDividerHeight;
    }



    /**
     * Create new DividerItemDecoration with the specified height, color and offsets between
     * elements. Offsets can not be less than divider height.
     */
    public MyDividerItemDecoration(int dividerHeight, int color, int offsets, boolean adjustOffsets) {
        super();

        if(offsets < dividerHeight){
            throw new IllegalArgumentException("Offsets can not be less than divider height");
        }else if(adjustOffsets){
            mToAdjustOffsets = true;
            mAdjustedOffsets = (offsets - dividerHeight)/2;
        }

        mOffsets = offsets;
        mDividerHeight = dividerHeight;
        mColor = color;
    }



    /**
     * Create new DividerItemDecoration without any decor
     */
    public MyDividerItemDecoration() {
        super();
        mDividerHeight = 1;
        mColor = 0x8a000000;
    }



    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager == null){
            throw new RuntimeException("LayoutManager not found");
        }
        if(layoutManager.getPosition(view) != 0)
            outRect.top = mOffsets;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        Paint paint = new Paint();
        paint.setColor(mColor);

        int left = parent.getPaddingLeft();
        int right = left + parent.getWidth()-parent.getPaddingRight();

        for(int i = 0;i<parent.getChildCount();i++){

            View child = parent.getChildAt(i);

            int top = 0;
            //try to place divider in the middle of the space between elements
            if(!mToAdjustOffsets)
                top = child.getBottom();
            else top = child.getBottom() + mAdjustedOffsets;
            int bottom = top + mDividerHeight;

            c.drawRect(left, top, right, bottom, paint);
        }
    }

    public Paint getPaint(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        return paint;
    }
}