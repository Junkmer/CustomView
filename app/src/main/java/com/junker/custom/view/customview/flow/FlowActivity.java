package com.junker.custom.view.customview.flow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.junker.custom.view.R;

import java.util.ArrayList;
import java.util.List;

public class FlowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        FlowLayout flowLayout = this.findViewById(R.id.flow_layout);
        List<String> data = new ArrayList<>();
        data.add("键盘");
        data.add("显示器");
        data.add("鼠标");
        data.add("iPad");
        data.add("Air pad");
        data.add("发生的阿凡达开发商贷款的阿斯顿发顺丰到付啊发士大夫士大夫撒旦啊士大夫士大夫打发士大夫士大夫暗室逢灯");
        data.add("Android手机");
        data.add("mac pro");
        data.add("耳机");
        data.add("春夏秋冬超帅装");
        data.add("明星");
        data.add("女装");
        flowLayout.setTextList(data);

        flowLayout.setOnItemClickListener(new FlowLayout.OnItemClickListener() {
            @Override
            public void onClick(View v, String text) {
                Toast.makeText(getApplication(),text,Toast.LENGTH_SHORT).show();
            }
        });
    }
}