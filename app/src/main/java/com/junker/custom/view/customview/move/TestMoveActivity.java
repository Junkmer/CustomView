package com.junker.custom.view.customview.move;

import static com.junker.custom.view.customview.move.TestMoveView.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.junker.custom.view.R;

public class TestMoveActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TestMoveActivity.class.getSimpleName();
    private TestMoveView moveViewBtn;
    private TextView moveStyleText;

    private TestButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_move);

//        initTestVew();
        initMoveView();
    }

    private void initMoveView() {
        setContentView(R.layout.activity_test_move);

        moveStyleText = findViewById(R.id.select_move_type_tip);

        Button layoutBtn = findViewById(R.id.layout_btn);
        Button translationBtn = findViewById(R.id.translation_btn);
        Button offsetBtn = findViewById(R.id.offset_btn);
        Button scrollByBtn = findViewById(R.id.scrollBy_btn);
        Button scrollBtn = findViewById(R.id.scroll_btn);
        Button marginBtn = findViewById(R.id.margin_btn);
        Button animationBtn = findViewById(R.id.animation_btn);
        moveViewBtn = findViewById(R.id.move_view_btn);

        layoutBtn.setOnClickListener(this);
        translationBtn.setOnClickListener(this);
        offsetBtn.setOnClickListener(this);
        scrollByBtn.setOnClickListener(this);
        scrollBtn.setOnClickListener(this);
        marginBtn.setOnClickListener(this);
        animationBtn.setOnClickListener(this);
        moveViewBtn.setOnClickListener(this);
    }

    private void initTestVew() {
        setContentView(R.layout.test_layout);

        mButton = (TestButton) findViewById(R.id.id_btn);
//        mButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.e(TAG, "onTouch ACTION_DOWN");
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.e(TAG, "onTouch ACTION_MOVE");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.e(TAG, "onTouch ACTION_UP");
//                        break;
//                    default:
//                        break;
//                }
//
//                return false;
//            }
//        });

//        mButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Toast.makeText(getApplicationContext(), "onclick",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mButton.setOnLongClickListener(new View.OnLongClickListener()
//        {
//            @Override
//            public boolean onLongClick(View v)
//            {
//                Toast.makeText(getApplicationContext(), "setOnLongClickListener",Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.move_view_btn){
            Toast.makeText(TestMoveActivity.this, "clicke move view btn", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView textView = (TextView) v;
        moveStyleText.setText("当前选择的滑动方式："+textView.getText());
        switch (v.getId()) {
            case R.id.layout_btn:
                moveViewBtn.setMoveViewStyle(MOVE_VIEW_STYLE_LAYOUT);
                break;
            case R.id.translation_btn:
                moveViewBtn.setMoveViewStyle(MOVE_VIEW_STYLE_TRANSLATION);
                break;
            case R.id.offset_btn:
                moveViewBtn.setMoveViewStyle(MOVE_VIEW_STYLE_OFFSET);
                break;
            case R.id.scrollBy_btn:
                moveViewBtn.setMoveViewStyle(MOVE_VIEW_STYLE_SCROLL_BY);
                break;
            case R.id.scroll_btn:
                moveViewBtn.setMoveViewStyle(MOVE_VIEW_STYLE_SCROLL);
                break;
            case R.id.margin_btn:
                moveViewBtn.setMoveViewStyle(MOVE_VIEW_STYLE_MARGIN);
                break;
            case R.id.animation_btn:
                moveViewBtn.setMoveViewStyle(MOVE_VIEW_STYLE_ANIMATION);
                break;
        }
    }
}