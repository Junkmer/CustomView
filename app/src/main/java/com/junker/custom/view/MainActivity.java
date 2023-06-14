package com.junker.custom.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junker.custom.view.customview.flow.FlowActivity;
import com.junker.custom.view.customview.keypad.KeypadActivity;
import com.junker.custom.view.customview.loginpage.LoginActivity;
import com.junker.custom.view.customview.move.TestMoveActivity;
import com.junker.custom.view.customview.numberinput.NumberInputActivity;
import com.junker.custom.view.customview.rebound.JunkerReboundActivity;
import com.junker.custom.view.customview.rebound.ReboundActivity;
import com.junker.custom.view.customview.rebound.ScrollReboundActivity;
import com.junker.custom.view.customview.slidemenu.SlideMenuActivity;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private List<ActivityBean> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        listView = findViewById(R.id.main_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "click item position = " + position);
                Intent intent;
                switch (titles.get(position).getPosition()) {
                    case 1:
                        intent = new Intent(MainActivity.this, NumberInputActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, ReboundActivity.class);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, TestMoveActivity.class);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, JunkerReboundActivity.class);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, ScrollReboundActivity.class);
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this, FlowActivity.class);
                        break;
                    case 8:
                        intent = new Intent(MainActivity.this, KeypadActivity.class);
                        break;
                    case 9:
                        intent = new Intent(MainActivity.this, SlideMenuActivity.class);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + titles.get(position));
                }
                startActivity(intent);
            }
        });
    }

    private void initData() {
        titles = new ArrayList<>();
        titles.add(new ActivityBean(1, "计数器布局", NumberInputActivity.class));
        titles.add(new ActivityBean(2, "登录布局", LoginActivity.class));
        titles.add(new ActivityBean(3, "回弹布局", ReboundActivity.class));
        titles.add(new ActivityBean(4, "测试多种方式View移动", TestMoveActivity.class));
        titles.add(new ActivityBean(5, "实现类似QQ微信阻尼回调效果", JunkerReboundActivity.class));
        titles.add(new ActivityBean(6, "ScrollView+阻尼回调效果", ScrollReboundActivity.class));
        titles.add(new ActivityBean(7, "流式动态布局", FlowActivity.class));
        titles.add(new ActivityBean(8, "继承ViewGroup实现的Keypad", KeypadActivity.class));
        titles.add(new ActivityBean(9, "继承ViewGroup实现的SlideMenu", KeypadActivity.class));
    }

    private void initAdapter() {
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public ActivityBean getItem(int position) {
                return titles.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                @SuppressLint("ViewHolder") View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(getItem(position).getTitle());
                return view;
            }
        };
        listView.setAdapter(adapter);
    }
}