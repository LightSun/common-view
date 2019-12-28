package org.heaven7.core.view.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextContainer extends BaseContainer {

    private int mLayoutId;
    private int mTextViewId;
    private CharSequence mText;

    public int getLayoutId() {
        return mLayoutId;
    }
    public void setLayoutId(int mLayoutId) {
        this.mLayoutId = mLayoutId;
    }

    public int getTextViewId() {
        return mTextViewId;
    }
    public void setTextViewId(int mTextViewId) {
        this.mTextViewId = mTextViewId;
    }

    public CharSequence getText() {
        return mText;
    }
    public void setText(CharSequence mText) {
        this.mText = mText;
    }

    @Override
    public View onCreateView(ViewGroup parent, LayoutInflater layoutInflater) {
        return layoutInflater.inflate(getLayoutId(), parent, false);
    }

    @Override
    public void onAttach() {
        final View view = getView();
        int textViewId = getTextViewId();
        TextView tv;
        if (textViewId == 0) {
            tv = (TextView) view;
        } else {
            tv = view.findViewById(textViewId);
        }
        tv.setText(getText());
    }
    @Override
    public void onDetach() {
    }
}
//animate test ok on onAttach.
/*view.post(new Runnable() {
            @Override
            public void run() {
                view.animate()
                        .scaleX(2)
                        .scaleY(2)
                        .setDuration(2000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                view.setPivotX(0);
                                view.setPivotY(0);
                            }
                        })
                        .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            int width = 0;
                            int height = 0;
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                Float val = (Float) animation.getAnimatedValue();
                                ViewGroup.LayoutParams lp = view.getLayoutParams();
                                if(width == 0){
                                    width = view.getMeasuredWidth();
                                    height = view.getMeasuredHeight();
                                }
                                lp.width = (int) (width * (1 + val));
                                lp.height = (int) (height * (1 + val));
                                view.setLayoutParams(lp);
                                System.out.println(lp.width);
                            }
                        }).start();
            }
        });*/