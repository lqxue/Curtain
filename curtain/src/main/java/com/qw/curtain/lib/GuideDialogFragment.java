package com.qw.curtain.lib;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @author cd5160866
 */
public class GuideDialogFragment extends DialogFragment implements IGuide {

    private static final int MAX_CHILD_COUNT = 2;

    private static final int GUIDE_ID = 0x3;

    private FrameLayout contentView;

    private int animationStyle = R.style.dialogWindowAnim;

    private Dialog dialog;

    private Curtain.CallBack callBack;

    private View topView = null;

    private GuideView guideView;

    public void show() {
        FragmentActivity activity = (FragmentActivity) guideView.getContext();
        guideView.setId(GUIDE_ID);
        this.contentView = new FrameLayout(activity);
        this.contentView.addView(guideView);
        if (topView != null) {
            updateTopView();
        }
        dialog = new AlertDialog.Builder(activity, R.style.TransparentDialog)
                .setView(contentView)
                .create();
        setAnimation(dialog);
        show(activity.getSupportFragmentManager(), GuideDialogFragment.class.getSimpleName());
    }

    public void setAnimationStyle(int animationStyle) {
        this.animationStyle = animationStyle;
    }

    public void setCallBack(Curtain.CallBack callBack) {
        this.callBack = callBack;
    }

    public void setTopView(View topView) {
        this.topView = topView;
    }

    public void setGuideView(GuideView guideView) {
        this.guideView = guideView;
    }

    void updateContent() {
        contentView.removeAllViews();
        contentView.addView(guideView);
        updateTopView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            manager.beginTransaction()
                    .add(this, tag)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void updateHollows(HollowInfo... hollows) {
        GuideView guideView = contentView.findViewById(GUIDE_ID);
        if (null != guideView) {
            guideView.setHollowInfo(hollows);
        }
    }

    @Override
    public void updateTopView(View topView) {
        if (null == contentView || getActivity() == null) {
            return;
        }
        setTopView(topView);
        updateTopView();
    }

    @Override
    public <T extends View> T findViewByIdInTopView(int id) {
        if (null == contentView) {
            return null;
        }
        return contentView.findViewById(id);
    }

    @Override
    public void dismissGuide() {
        dismissAllowingStateLoss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
        } catch (Exception e) {
            return;
        }
        if (null != callBack) {
            callBack.onShow(this);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != callBack) {
            callBack.onDismiss(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null) {
            dialog = null;
        }
    }

    private void setAnimation(Dialog dialog) {
        if (animationStyle != 0 && dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setWindowAnimations(animationStyle);
        }
    }

    private void updateTopView() {
        if (contentView.getChildCount() == MAX_CHILD_COUNT) {
            contentView.removeViewAt(1);
        }
        contentView.addView(topView);
    }
}
