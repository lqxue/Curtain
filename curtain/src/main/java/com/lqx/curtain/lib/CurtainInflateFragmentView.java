package com.lqx.curtain.lib;

import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.lqx.curtain.lib.debug.CurtainDebug;
import com.lqx.curtain.lib.shape.Shape;

/**
 * https://github.com/lqxue/Curtain
 *
 * 针对fragment 使用根view
 * @author lqx
 */
public class CurtainInflateFragmentView {

    SparseArray<HollowInfo> hollows;

    boolean cancelBackPressed = true;

    int curtainColor = 0xAA000000;

    View topView;

    int animationStyle = 0;

    FragmentActivity activity;

    private CallBack callBack;

    public CurtainInflateFragmentView(Fragment fragment) {
        this(fragment.getActivity());
    }

    public CurtainInflateFragmentView(FragmentActivity activity) {
        this.activity = activity;
        this.hollows = new SparseArray<>();
    }

    /**
     * @param which 页面上任一要高亮的view
     */
    public CurtainInflateFragmentView with(@NonNull View which) {
        return with(which, true);
    }

    /**
     * @param which                     页面上任一要高亮的view
     * @param isAutoAdaptViewBackGround 是否自动适配View背景形状 (不一定完全生效，如果无法满足的话，可自定义形状)
     * @see #withShape(View, Shape)
     */
    public CurtainInflateFragmentView with(@NonNull View which, boolean isAutoAdaptViewBackGround) {
        getHollowInfo(which)
                .setAutoAdaptViewBackGround(isAutoAdaptViewBackGround);
        return this;
    }

    /**
     * @param paddingSize the size of padding in all directions
     * @param which       the view will be set the padding
     */
    public CurtainInflateFragmentView withPadding(@NonNull View which, int paddingSize) {
        getHollowInfo(which).padding = Padding.all(paddingSize);
        return this;
    }

    /**
     * @param padding the describe of padding
     * @param which   the view will be set the padding
     * @see Padding  use Padding.all() or Padding.only() to build an padding
     */
    public CurtainInflateFragmentView withPadding(@NonNull View which, Padding padding) {
        getHollowInfo(which).padding = padding;
        return this;
    }

    /**
     * 指定蒙层大小
     *
     * @param which  以该View的左上角作为初始坐标
     * @param width  宽
     * @param height 高
     */
    public CurtainInflateFragmentView withSize(@NonNull View which, int width, int height) {
        getHollowInfo(which).targetBound = new Rect(0, 0, width, height);
        return this;
    }

    /**
     * 设置蒙层偏移量
     *
     * @param which     view对应产生的蒙层
     * @param offset    偏移量 px
     * @param direction 偏移方向
     */
    public CurtainInflateFragmentView withOffset(@NonNull View which, int offset, @HollowInfo.direction int direction) {
        getHollowInfo(which).setOffset(offset, direction);
        return this;
    }

    /**
     * 设置自定义形状
     *
     * @param which 目标view
     * @param shape 形状
     */
    public CurtainInflateFragmentView withShape(@NonNull View which, Shape shape) {
        getHollowInfo(which).setShape(shape);
        return this;
    }

    /**
     * 自定义的引导页蒙层和镂空部分View
     */
    public CurtainInflateFragmentView setTopView(View view) {
        this.topView = view;
        return this;
    }

    /**
     * 设置蒙层背景颜色
     *
     * @param color 颜色
     */
    public CurtainInflateFragmentView setCurtainColor(int color) {
        this.curtainColor = color;
        return this;
    }

    public CurtainInflateFragmentView setCurtainColorRes(@ColorRes int color) {
        this.curtainColor = color;
        return this;
    }

    /**
     * 是否允许回退关闭蒙层
     *
     * @param cancelBackPress 是否允许回退关闭蒙层
     */
    public CurtainInflateFragmentView setCancelBackPressed(boolean cancelBackPress) {
        this.cancelBackPressed = cancelBackPress;
        return this;
    }

    /**
     * 设置蒙层展示回调回调
     *
     * @param callBack 如果你需要监听的话
     */
    public CurtainInflateFragmentView setCallBack(CallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    /**
     * 设置蒙层出现的动画 默认渐隐
     *
     * @param animation 动画style
     */
    public CurtainInflateFragmentView setAnimationStyle(@StyleRes int animation) {
        this.animationStyle = animation;
        return this;
    }

    @MainThread
    public void show(@NonNull final FrameLayout frameLayout) {
        if (hollows.size() == 0) {
            CurtainDebug.w(Constance.TAG, "with out any views");
            return;
        }
        View checkStatusView = hollows.valueAt(0).targetView;
        if (checkStatusView.getWidth() == 0) {
            checkStatusView.post(new Runnable() {
                @Override
                public void run() {
                    show(frameLayout);
                }
            });
            return;
        }
        GuideFragmentViewWrapper guider = new GuideFragmentViewWrapper();
        guider.setCallBack(callBack);
        guider.setTopView(topView);
        GuideView guideView = new GuideView(activity);
        guideView.setCurtainColor(curtainColor);
        addHollows(guideView);
        guider.setGuideView(guideView);
        guider.show(frameLayout);
    }

    void addHollows(GuideView guideView) {
        HollowInfo[] tobeDraw = new HollowInfo[hollows.size()];
        for (int i = 0; i < hollows.size(); i++) {
            tobeDraw[i] = hollows.valueAt(i);
        }
        guideView.setHollowInfo(tobeDraw);
    }

    private HollowInfo getHollowInfo(View which) {
        HollowInfo info = hollows.get(which.hashCode());
        if (null == info) {
            info = new HollowInfo(which);
            info.targetView = which;
            hollows.append(which.hashCode(), info);
        }
        return info;
    }

    public interface CallBack {

        /**
         * 展示成功
         */
        void onShow(IGuide iGuide);

        /**
         * 消失
         */
        void onDismiss(IGuide iGuide);

    }

}