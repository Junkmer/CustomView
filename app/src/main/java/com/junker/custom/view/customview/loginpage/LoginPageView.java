package com.junker.custom.view.customview.loginpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.junker.custom.view.R;
import com.junker.custom.view.customview.numberinput.InputNumberView;

import java.lang.reflect.Field;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

/**
 * 点击获取验证码 --> 条件手机号码正确
 * 点击登录 -->条件：正确的手机号+验证码——同意协议
 */
public class LoginPageView extends FrameLayout {

    private final static int SIZE_VERIFY_CODE_DEFAULT = 4;
    private static final String TAG = LoginPageView.class.getSimpleName();
    private int mColor;
    private int mVerifyCodeLength = SIZE_VERIFY_CODE_DEFAULT;
    private String mProtocolUrl;
    private OnLoginPageActionListener mOnLoginPageActionListener = null;
    private LoginKeyboard loginKeyboard;
    private EditText inputPhoneNum;
    private EditText inputVerifyCode;
    private TextView protocolText;
    private CheckBox checkBoxProtocol;
    private boolean isPhoneNumOk = false;
    private boolean isProtocolOk = false;
    private boolean isVerifyCodeOk = false;
    private TextView getVerifyCode;
    private TextView loginBtn;

    public LoginPageView(@NonNull Context context) {
        this(context, null);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //TODO:初始化自定义属性
        initAttrs(context, attrs);
        //TODO:初始化控件
        initView();
        disableEdtFocus2Keypad();
        //TODO:初始化点击事件
        initEvent();
    }

    private void disableEdtFocus2Keypad() {
        inputPhoneNum.setShowSoftInputOnFocus(false);
        inputVerifyCode.setShowSoftInputOnFocus(false);
    }

    private void initEvent() {
        loginKeyboard.setOnKeyPressListener(new LoginKeyboard.OnKeyPressListener() {
            @Override
            public void onNumberPress(int number) {
                //数字键
                EditText focsEdt = getFocsEdt();
                Log.e(TAG, "number = > " + number);
                if (focsEdt != null) {
                    Editable text = focsEdt.getText();
                    int index = focsEdt.getSelectionEnd();
                    Log.e(TAG, "index = > " + index);
                    text.insert(index, String.valueOf(number));
                }
            }

            @Override
            public void onBackPress() {
                //退格键
                EditText focsEdt = getFocsEdt();
                if (focsEdt != null) {
                    Editable text = focsEdt.getText();
                    int index = focsEdt.getSelectionEnd();
                    Log.e(TAG, "index = > " + index);
                    if (index > 0) {
                        text.delete(index - 1, index);
                    }
                }

            }
        });
        getVerifyCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLoginPageActionListener != null){
                    //拿到手机号码
                    String phoneNum = inputPhoneNum.getText().toString().trim();
                    mOnLoginPageActionListener.onGetVerifyCodeClick(phoneNum);
                }
            }
        });
        inputPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化时检查手机号码是否符合格式
                Log.e(TAG, "content = >" + s);
                String phoneNum = s.toString();
                boolean isMatch = isPhone(phoneNum);
                isPhoneNumOk = phoneNum.length() == 11 && isMatch;
                updateAllBtnState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化时检查验证码是否符合格式
                Log.e(TAG, "content = >" + s);
                String verifyCode = s.toString();
                isVerifyCodeOk = verifyCode.length() == mVerifyCodeLength;
                updateAllBtnState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkBoxProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isProtocolOk = isChecked;
                updateAllBtnState();
            }
        });
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.login_page_view, this);
        RelativeLayout layout = this.findViewById(R.id.login_page_view_layout);
        if (mColor != -1) {
            layout.setBackgroundColor(mColor);
        }
        checkBoxProtocol = this.findViewById(R.id.check_protocol);
        protocolText = this.findViewById(R.id.check_protocol_text);
        inputPhoneNum = this.findViewById(R.id.input_phone_num);
        inputVerifyCode = this.findViewById(R.id.input_verify_code);
        inputVerifyCode.setMaxHeight(mVerifyCodeLength);
        inputVerifyCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mVerifyCodeLength)});
        getVerifyCode = this.findViewById(R.id.get_verify_code);
        loginBtn = this.findViewById(R.id.login_btn);
        loginKeyboard = this.findViewById(R.id.login_key_board);
        disableCopyAndPaste(inputPhoneNum);
        disableCopyAndPaste(inputVerifyCode);

    }

    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LoginPageView);
        mColor = attributes.getColor(R.styleable.LoginPageView_mainColor, -1);
        mVerifyCodeLength = attributes.getInt(R.styleable.LoginPageView_verifyCodeLength, 4);
        mProtocolUrl = attributes.getString(R.styleable.LoginPageView_protocolUrl);
        attributes.recycle();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    private void updateAllBtnState() {
        getVerifyCode.setEnabled(isPhoneNumOk);
        loginBtn.setEnabled(isPhoneNumOk && isVerifyCodeOk && isProtocolOk);
    }

    /**
     * 获取当前有焦点的输入框
     * <p>
     * 使用时要注意判空
     *
     * @return null or editText instance.
     */
    private EditText getFocsEdt() {
        View view = this.findFocus();
        if (view instanceof EditText) {
            return (EditText) view;
        }
        return null;
    }

    public int getVerifyCodeLength() {
        return mVerifyCodeLength;
    }

    public void setVerifyCodeLength(int mVerifyCodeLength) {
        this.mVerifyCodeLength = mVerifyCodeLength;
    }

    public String getProtocolUrl() {
        return mProtocolUrl;
    }

    public void setProtocolUrl(String mProtocolUrl) {
        this.mProtocolUrl = mProtocolUrl;
    }

    public void setOnLoginPageActionListener(OnLoginPageActionListener listener) {
        this.mOnLoginPageActionListener = listener;
    }

    public interface OnLoginPageActionListener {
        void onGetVerifyCodeClick(String phoneNum);

        void onOpenProtocolClick();

        void onConfirmClick(String verifyCode, String phoneNum);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void disableCopyAndPaste(final EditText editText) {
        try {
            if (editText == null) {
                return;
            }

            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            editText.setLongClickable(false);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // setInsertionDisabled when user touches the view
                        setInsertionDisabled(editText);
                    }

                    return false;
                }
            });
            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机号码校验(三大运营商最新号段 合作版 2021-03)
     * 移动号段：
     * 134 135 136 137 138 139 147 148 150 151 152 157 158 159 172 178 182 183 184 187 188 195 198
     * <p>
     * 联通号段：
     * 130 131 132 145 146 155 156 166 167 171 175 176 185 186 196
     * <p>
     * 电信号段：
     * 133 149 153 173 174 177 180 181 189 191 193 199
     * <p>
     * 虚拟运营商:
     * 162 165 167 170 171
     * <p>
     * 13开头排序：(0-9)（134 135 136 137 138 139 130 131 132 133）
     * 14开头排序：(5-9)（147 148 145 146 149）
     * 15开头排序：(0-3|5-9)（150 151 152 157 158 159 155 156 153）
     * 16开头排序：(6-7)（166 167）
     * 17开头排序：(1-8)（172 178 171 175 176 173 174 177）
     * 18开头排序：(0-9)（182 183 184 187 188 185 186 180 181 189）
     * 19开头排序：(1|3|5|6|8|9)（195 198 196 191 193 199）
     *
     * @param phone 手机号码
     * @return 是否属于三大运营商号段范围
     * @see {https://www.qqzeng.com/article/phone.html}
     */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[6-7])|(17[1-8])|(18[0-9])|(19[1|3])|(19[5|6])|(19[8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        return p.matcher(phone).matches();
    }
}
