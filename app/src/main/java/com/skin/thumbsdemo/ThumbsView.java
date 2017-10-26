package com.skin.thumbsdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * 模仿即可点赞的View
 * Created by wangcheng on 2017/10/24.
 */

public class ThumbsView extends View {

    /**
     * 点赞数文字大小（SP值）默认值
     */
    public static final float CONTENT_DEFAULT_DP = 17;
    /**
     * 跳动文字的最大距离
     */
    public static final float MAX_JUMP = 40f;
    /**
     * 跳动文字的最小距离
     */
    public static final float MIN_JUMP = 0f;
    /**
     * 点击时圆心的半径
     */
    public static final int CLICK_CIRCLE_RADIUS = 35;
    /**
     * 点赞数
     */
    private String mContent;
    /**
     * 跳动的文字
     */
    private String mJumpOutText;
    /**
     * 跳动的文字
     */
    private String mJumpInText;
    /**
     * 跳动的文字的长度
     */
    private int mJumpTextLength;
    /**
     * 点赞数文字颜色
     */
    private int mContentColor;
    /**
     * 点赞数文字大小（像素值）
     */
    private float mContentPx;
    /**
     * 绘制点赞数文字的画笔
     */
    private Paint mTextPaint;
    /**
     * 绘制点赞数文字的画笔
     */
    private Paint mCirclePaint;
    /**
     * 绘制点赞图片外圈的画笔
     */
    private Paint mBitmapCirclePaint;
    /**
     * 绘制点赞数文字的画笔
     */
    private Paint mJumpOutTextPaint;
    /**
     * 绘制点赞数文字的画笔
     */
    private Paint mJumpInTextPaint;
    /**
     * 绘制点赞图片的画笔
     */
    private Paint mDrawablePaint;
    /**
     * 未点赞的手势默认图片
     */
    private Bitmap mThumbsDefaultBm;
    /**
     * 点赞后手势的图片
     */
    private Bitmap mThumbsSelectedBm;
    /**
     * 点赞后手势叠加的图片
     */
    private Bitmap mThumbsSelectedshiningBm;
    /**
     * 点赞后手势图片绘制区域
     */
    private RectF mBitmapRectF;
    /**
     * 图片的伸缩系数
     */
    private float mScaleOff;
    /**
     * 是否点赞
     */
    private boolean isSelected;
    /**
     * 以绘制的内容作为源图像，以 View 中已有的内容作为目标图像，选取一个 PorterDuff.Mode 作为绘制内容的颜色处理方案
     */
    private Xfermode mXfermode;

    /**
     * 跳动部分之前文字沿Y的偏移量
     */
    private float mContentOutOffY;
    /**
     * 跳动部分当前文字沿Y的偏移量
     */
    private float mContentInOffY;
    /**
     * 点击时圆心的X轴位置
     */
    private float clickX;
    /**
     * 点击时圆心的Y轴位置
     */
    private float clickY;
    /**
     * 点赞图片外圈的圆圈半径
     */
    private float mBitmapCircleRadius;
    /**
     * 点赞图片外圈的圆圈半径默认值
     */
    private float mBitmapCircleRadiusDefault;

    /**
     * 设置跳动部分文字沿Y的偏移量
     *
     * @param offY 跳动部分文字沿Y的偏移量
     */
    public void setTextOutY(float offY) {
        mJumpOutText = StringUtil.getOldLastContent(isSelected, mContent);
        mContentOutOffY = offY;
    }

    /**
     * 设置跳动部分文字沿Y的偏移量
     *
     * @param offY 跳动部分文字沿Y的偏移量
     */
    public void setTextInY(float offY) {
        mJumpInText = mContent.substring(mContent.length() - mJumpTextLength);
        mContentInOffY = offY;
    }

    /**
     * 设置弹出文字的透明度
     *
     * @param alpha 透明度
     */
    public void setOutTextAlpha(int alpha) {
        mJumpOutTextPaint.setAlpha(alpha);
    }

    /**
     * 设置弹入文字的透明度
     *
     * @param alpha 透明度
     */
    public void setInTextAlpha(int alpha) {
        mJumpInTextPaint.setAlpha(alpha);
    }

    /**
     * 设置图片的伸缩系数
     *
     * @param scaleOff 图片的伸缩系数
     */
    public void setScaleOff(float scaleOff) {
        mScaleOff = scaleOff;
    }

    /**
     * 设置点击处圆圈的透明度
     *
     * @param alpha 透明度
     */
    public void setClickCircleAlpha(int alpha) {
        mCirclePaint.setAlpha(alpha);
    }

    /**
     * 设置点赞图片圆圈的缩放系数
     *
     * @param scale 透明度
     */
    public void setBitmapCircleScale(float scale) {
        mBitmapCircleRadius = mBitmapCircleRadiusDefault * scale;
        postInvalidate();
    }

    /**
     * 设置点赞图片圆圈的透明度
     *
     * @param alpha 透明度
     */
    public void setBitmapCircleAlpha(int alpha) {
        mBitmapCirclePaint.setAlpha(alpha);
        postInvalidate();
    }

    public ThumbsView(Context context) {
        this(context, null);
    }

    public ThumbsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ThumbsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 点赞
     *
     * @param flag 点赞/取消点赞
     */
    public void like(boolean flag) {
        isSelected = flag;
        int likeCount = Integer.parseInt(mContent);
        if (flag) {
            likeCount++;
        } else {
            likeCount--;
        }
        refreshContent(String.valueOf(likeCount));
        startAnimator();
    }

    /**
     * 设置点赞数
     *
     * @param content 点赞数
     */
    public void setContent(String content) {
        refreshContent(content);
        postInvalidate();
    }

    /**
     * 更新点赞数相关数据
     *
     * @param content 点赞数
     */
    public void refreshContent(String content) {
        mContent = content;
        mJumpTextLength = StringUtil.getLastContentLength(isSelected, mContent);
        //改变文字时如果是点赞引起的，则跳动的文字为改变之前的文字
        mJumpOutText = mContent.substring(mContent.length() - mJumpTextLength);
        //改变文字时如果不是点赞引起的，则跳动的文字为最新文字
        mJumpInText = mContent.substring(mContent.length() - mJumpTextLength);
    }

    /**
     * View初始化
     *
     * @param context 上下文
     */
    public void initView(Context context, AttributeSet attrs) {
        // 硬件加速下 ComposeShader 不能使用两个同类型的 Shader
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mScaleOff = 1f;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ThumbsView);
        mContent = ta.getString(R.styleable.ThumbsView_content);
        refreshContent(TextUtils.isEmpty(mContent) ? "0" : mContent);
        mContentPx = ta.getDimensionPixelOffset(R.styleable.ThumbsView_contentSize, ViewUtil.dp2px(context, CONTENT_DEFAULT_DP));
        int colorRes = ta.getResourceId(R.styleable.ThumbsView_contentColor, R.color.thumbs_text_color);
        mContentColor = ContextCompat.getColor(context, colorRes);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mContentPx);
        mTextPaint.setColor(mContentColor);
        mJumpOutTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mJumpOutTextPaint.setTextSize(mContentPx);
        mJumpOutTextPaint.setColor(mContentColor);
        mJumpInTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mJumpInTextPaint.setTextSize(mContentPx);
        mJumpInTextPaint.setColor(mContentColor);
        mJumpInTextPaint.setAlpha(0x00);
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setColor(mContentColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAlpha(0x00);
        mBitmapCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapCirclePaint.setStrokeWidth(5);
        mBitmapCirclePaint.setColor(mContentColor);
        mBitmapCirclePaint.setStyle(Paint.Style.STROKE);
        mBitmapCirclePaint.setAlpha(0x00);
        mThumbsDefaultBm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        mThumbsSelectedBm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        mThumbsSelectedshiningBm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);
        mBitmapCircleRadiusDefault = Math.min(mThumbsDefaultBm.getWidth(), mThumbsDefaultBm.getHeight()) - 25;
        //源图像和目标图像都显示，重合的部位显示源图像
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
        mBitmapRectF = new RectF();
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textWith = mTextPaint.measureText(mContent);
        Bitmap bitmap = isSelected ? mThumbsSelectedBm : mThumbsDefaultBm;
        int bitmapLeft = (int) ((getWidth() - bitmap.getWidth() - textWith) / 2);
        int bitmapTop = (getHeight() - bitmap.getHeight()) / 2;
        if (isSelected) {
            //绘制点赞图形
            mBitmapRectF.set(bitmapLeft, bitmapTop - 20, bitmapLeft + bitmap.getWidth(), bitmapTop + bitmap.getHeight() + 20);
            drawLikedDrawable(mScaleOff, bitmapLeft, bitmapTop, canvas, bitmap);
        } else {
            drawUnLikDrawable(mScaleOff, bitmapLeft, bitmapTop, canvas, bitmap);
        }
        drawText(canvas, bitmap, bitmapLeft);
        canvas.drawCircle(bitmapLeft + bitmap.getWidth() / 2, getHeight() / 2, mBitmapCircleRadius, mBitmapCirclePaint);
        canvas.drawCircle(clickX, clickY, CLICK_CIRCLE_RADIUS, mCirclePaint);
    }

    /**
     * 绘制未点赞时的点赞位图
     *
     * @param scaleCv    伸缩系数
     * @param bitmapLeft 位图绘制左边距
     * @param bitmapTop  位图绘制上边距
     * @param canvas     绘制幕
     * @param bitmap     绘制位图
     */
    public void drawUnLikDrawable(float scaleCv, int bitmapLeft, int bitmapTop, Canvas canvas, Bitmap bitmap) {
        canvas.save();
        canvas.scale(scaleCv, scaleCv, bitmapLeft + bitmap.getWidth() / 2, bitmapTop + bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, bitmapLeft, bitmapTop, mDrawablePaint);
        canvas.restore();
    }

    /**
     * 绘制点赞时的点赞位图
     *
     * @param scaleCv    伸缩系数
     * @param bitmapLeft 位图绘制左边距
     * @param bitmapTop  位图绘制上边距
     * @param canvas     绘制幕
     * @param bitmap     绘制位图
     */
    public void drawLikedDrawable(float scaleCv, int bitmapLeft, int bitmapTop, Canvas canvas, Bitmap bitmap) {
        //离屏缓存
        canvas.saveLayer(mBitmapRectF, mDrawablePaint);
        //位图做伸缩的几何变换
        canvas.scale(scaleCv, scaleCv, bitmapLeft + bitmap.getWidth() / 2, bitmapTop + bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, bitmapLeft, bitmapTop, mDrawablePaint);
        // 设置 Xfermode
        mDrawablePaint.setXfermode(mXfermode);
        canvas.drawBitmap(mThumbsSelectedshiningBm, bitmapLeft + 5, bitmapTop - 20, mDrawablePaint);
        // 用完及时清除 Xfermode
        mDrawablePaint.setXfermode(null);
        canvas.restore();
    }

    /**
     * 绘制点赞数
     *
     * @param canvas     绘制幕
     * @param bitmap     绘制位图
     * @param bitmapLeft 位图绘制左边距
     */
    public void drawText(Canvas canvas, Bitmap bitmap, int bitmapLeft) {
        float textLeft;
        float textTop = (getHeight() + bitmap.getHeight() - 20) / 2;
        textLeft = bitmapLeft + bitmap.getWidth() + 10;
        String defaultText = mContent.substring(0, mContent.length() - mJumpTextLength);
        //绘制不动部分的文字
        canvas.drawText(defaultText, textLeft, textTop, mTextPaint);
        float jumpTextLeft = textLeft + mTextPaint.measureText(defaultText);
        //绘制跳动部分的文字
        canvas.drawText(mJumpOutText, jumpTextLeft, textTop + mContentOutOffY, mJumpOutTextPaint);
        canvas.drawText(mJumpInText, jumpTextLeft, textTop + mContentInOffY, mJumpInTextPaint);
    }

    public void startAnimator() {
        //设置点赞的图片的伸缩动画，该动画在点击后就执行
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(this, "scaleOff", 1f, 0.8f, 1f);
        scaleAnimator.setDuration(200);
        scaleAnimator.setInterpolator(new OvershootInterpolator());

        //设置点赞的文字的离屏动画，该动画在点击后就执行
        ObjectAnimator textOutAnimator;
        if (isSelected) {
            //点赞时,文字向上离开
            textOutAnimator = ObjectAnimator.ofFloat(this, "textOutY", MIN_JUMP, -MAX_JUMP);
        } else {
            //取消点赞时,文字向下离开
            textOutAnimator = ObjectAnimator.ofFloat(this, "textOutY", MIN_JUMP, MAX_JUMP);
        }
        textOutAnimator.setDuration(200);

        //设置点赞的文字的入屏动画，该动画在离屏动画执行结束后执行
        ObjectAnimator textInAnimator;
        if (isSelected) {
            //点赞时,文字从下往上进入
            textInAnimator = ObjectAnimator.ofFloat(this, "textInY", MAX_JUMP, MIN_JUMP);
        } else {
            //取消点赞时,文字从上往下进入
            textInAnimator = ObjectAnimator.ofFloat(this, "textInY", -MAX_JUMP, MIN_JUMP);
        }
        textInAnimator.setDuration(200);
        //设置点赞的文字的离屏时透明度变换动画，从不透明到全透明，该动画伴随离屏动画执行
        ObjectAnimator textOutAlphaAnimator = ObjectAnimator.ofInt(this, "outTextAlpha", 0xff, 0x00);
        textOutAlphaAnimator.setDuration(200);
        //设置点赞的文字的入屏时透明度变换动画，从全透明到不透明，该动画伴随入屏动画执行
        ObjectAnimator textInAlphaAnimator = ObjectAnimator.ofInt(this, "inTextAlpha", 0x00, 0xff);
        textInAlphaAnimator.setDuration(200);
        //设置点赞时点击点的圆圈画笔透明度动画
        ObjectAnimator clickCircleAlphaAnimator = ObjectAnimator.ofInt(this, "clickCircleAlpha", 0xff, 0x00);
        clickCircleAlphaAnimator.setDuration(200);

        //设置点赞时点赞图片圆圈伸缩动画
        ObjectAnimator bitmapCircleScaleimator = ObjectAnimator.ofFloat(this, "bitmapCircleScale", 0.5f, 1f);
        bitmapCircleScaleimator.setDuration(200);
        bitmapCircleScaleimator.setInterpolator(new DecelerateInterpolator());
        //设置点赞时点赞图片圆圈画笔透明度动画
        ObjectAnimator bitmapCircleAlphaAnimator = ObjectAnimator.ofInt(this, "bitmapCircleAlpha", 0xff, 0x00);
        bitmapCircleAlphaAnimator.setDuration(50);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(clickCircleAlphaAnimator).with(scaleAnimator);
        animatorSet.play(clickCircleAlphaAnimator).with(bitmapCircleScaleimator);
        animatorSet.play(clickCircleAlphaAnimator).with(textOutAnimator);
        animatorSet.play(clickCircleAlphaAnimator).with(textInAnimator);
        animatorSet.play(textOutAnimator).with(textOutAlphaAnimator);
        animatorSet.play(textInAnimator).with(textInAlphaAnimator);
        animatorSet.play(bitmapCircleScaleimator).before(bitmapCircleAlphaAnimator);
        animatorSet.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBitmapCirclePaint.setAlpha(0xff);
                clickX = event.getX();
                clickY = event.getY();
                isSelected = !isSelected;
                like(isSelected);
                return true;
            case MotionEvent.ACTION_UP:
                return super.dispatchTouchEvent(event);
            default:
                return super.dispatchTouchEvent(event);
        }
    }
}
