package com.spisoft.spanimswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SpaSwitch extends RelativeLayout {
    private Context mContext;
    private View rootView;
    private ImageView iIcon;
    private TextView iText;
    private String TitlaDefault;
    private Drawable IBackGround;
    private long animate_duration = 600;
    private int SetAnimate;
    private int TextColor;
    private int mVal;
    private boolean ConfirmQS;
    private Context context;
    private String ConfirmTitle, ConfirmContent, ConfirmOK, ConfirmCancel;
    private boolean Disable = false;
//    private String[] ListValue;
//    private Drawable[] ListDrawable;
    OnChangeValueListener mListener;
    private List<SpaSwitchVal> Values = new ArrayList<>();

    public SpaSwitch(Context context) {
        super(context);
        init(context, null);
    }

    public SpaSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SpaSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpaSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(final Context context, AttributeSet attrs) {

        mContext = context;

        rootView = inflate(context, R.layout.sps_switch, this);
        iIcon = rootView.findViewById(R.id.i_icon);
        iText = rootView.findViewById(R.id.i_text);

        Values.add(new SpaSwitchVal(getResources().getString(R.string.title_male), getResources().getDrawable(R.drawable.i_male)));
        Values.add(new SpaSwitchVal(getResources().getString(R.string.title_female), getResources().getDrawable(R.drawable.i_female)));
//        ListValue = new String[2];
//        ListValue[0] = getResources().getString(R.string.title_male);
//        ListValue[1] = getResources().getString(R.string.title_female);
//        ListDrawable = new Drawable[2];
//        ListDrawable[0] = getResources().getDrawable(R.drawable.i_male);
//        ListDrawable[1] = getResources().getDrawable(R.drawable.i_female);

        IBackGround = getResources().getDrawable(R.drawable.i_gender);

        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpaSwitch, 0, 0);

            iIcon.getLayoutParams().height = (int) typedArray.getDimension(R.styleable.SpaSwitch_SwitchSize, R.dimen.sps_lpr_sz_30);
            iIcon.getLayoutParams().width = (int) typedArray.getDimension(R.styleable.SpaSwitch_SwitchSize, R.dimen.sps_lpr_sz_30);

            String _TitlaDefault = typedArray.getString(R.styleable.SpaSwitch_TitleMain);
            if (_TitlaDefault != null) TitlaDefault = _TitlaDefault;

            animate_duration = typedArray.getInteger(R.styleable.SpaSwitch_AnimateDuration, 600);

            if (!typedArray.getBoolean(R.styleable.SpaSwitch_TitleShow, true))
                iText.setVisibility(GONE);

            Drawable _Src_Background = typedArray.getDrawable(R.styleable.SpaSwitch_SrcBackground);
            if (_Src_Background != null) IBackGround = _Src_Background;

            if (typedArray.getBoolean(R.styleable.SpaSwitch_SetDefault, false)) mVal = 1;
            SetAnimate = typedArray.getInt(R.styleable.SpaSwitch_AnimateType, 0);

            TextColor = typedArray.getColor(R.styleable.SpaSwitch_TextColor, Color.BLACK);
            typedArray.recycle();
        }

        SwitchView(mVal);

        iText.setText(TitlaDefault);
        iText.setTextColor(TextColor);

        iIcon.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mVal = 0;
                SwitchView(mVal);
                if (mListener != null)
                    mListener.onEvent();
                return true;
            }
        });
        iIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Disable) {
                    if (!ConfirmQS)
                        Switch(view, SetAnimate);
                    else
                        Confirmation(view);
                }
            }
        });
    }

    private void Switch(View view, int i) {
        Animation animation;
        switch (i) {
            default:
                animation = new RotateAnimation(0, 160, 0, view.getHeight() + 20);
                break;

            case 1:
                float xCurrentPos = view.getLeft();
                float yCurrentPos = view.getTop();
                float yHeight = view.getHeight()*2;
                animation = new TranslateAnimation(xCurrentPos, xCurrentPos, yCurrentPos, yCurrentPos+yHeight);
                break;

        }

        animation.setDuration(animate_duration);
        view.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iText.setText("");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iIcon.setVisibility(GONE);
                mVal++;
                if(mVal > Values.size()) mVal = 1;
                SwitchView(mVal);

                if(mListener!=null) mListener.onEvent();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public interface OnChangeValueListener {
        void onEvent();
    }

    public void setChangeValueListener(OnChangeValueListener eventListener) {
        mListener = eventListener;
    }

    public void SetConfirm(Context mContext,boolean QSConfirm, String mTitle, String mContentTxt, String mOkTxt, String mCancelTxt){
        ConfirmQS = QSConfirm;
        context = mContext;
        ConfirmTitle = mTitle;
        ConfirmContent = mContentTxt;
        ConfirmOK = mOkTxt;
        ConfirmCancel = mCancelTxt;
    }

    private void Confirmation(final View view) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(ConfirmTitle)
                .setContentText(ConfirmContent)
                .setConfirmText(ConfirmOK)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        Switch(view, SetAnimate);
                    }
                })
                .setCancelText(ConfirmCancel)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private void SwitchView(int val) {
        mVal = val;
        iIcon.setVisibility(VISIBLE);
        if(mVal > 0) {
            iIcon.setImageDrawable(Values.get(mVal-1).getmDrawable());
            iText.setText(Values.get(mVal-1).getmTitle());
        }else {
            iIcon.setImageDrawable(IBackGround);
            iText.setText(TitlaDefault);
        }
    }

    public int getValue(){
        return mVal;
    }

    public String getText(){
        return iText.getText().toString();
    }

    public SpaSwitch setValue(int val){
        SwitchView(val);
        return this;
    }

    public SpaSwitch setDisable(boolean disable){
        this.Disable = disable;
        return this;
    }

//    public SpaSwitch setListValu(String[] listValue){
//        this.ListValue = listValue;
//        return this;
//    }
//
//    public SpaSwitch setListDrawable(Drawable[] listDrawable){
//        this.ListDrawable = listDrawable;
//        return this;
//    }

    public SpaSwitch setListValus(List<SpaSwitchVal> values){
        this.Values = values;
        return this;
    }

    public static class SpaSwitchVal{
        private int mValue;
        private String mTitle;
        private Drawable mDrawable;

        public SpaSwitchVal(String title, Drawable drawable){
            this.mTitle = title;
            this.mDrawable = drawable;
        }

        public SpaSwitchVal(int value, String title, Drawable drawable){
            this.mValue = value;
            this.mTitle = title;
            this.mDrawable = drawable;
        }

        public int getmValue() {
            return mValue;
        }

        public void setmValue(int mValue) {
            this.mValue = mValue;
        }

        public String getmTitle() {
            return mTitle;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public Drawable getmDrawable() {
            return mDrawable;
        }

        public void setmDrawable(Drawable mDrawable) {
            this.mDrawable = mDrawable;
        }
    }
}
