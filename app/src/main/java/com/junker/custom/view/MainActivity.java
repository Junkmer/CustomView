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

import com.junker.custom.view.customview.loginpage.LoginActivity;
import com.junker.custom.view.customview.move.TestMoveActivity;
import com.junker.custom.view.customview.numberinput.NumberInputActivity;
import com.junker.custom.view.customview.rebound.JunkerReboundActivity;
import com.junker.custom.view.customview.rebound.ReboundActivity;
import com.junker.custom.view.customview.rebound.ScrollReboundActivity;

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
//                        int selfPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
//                        if (selfPermission != PackageManager.PERMISSION_GRANTED) {
//                            test();
//                        } else {
//                            Toast.makeText(MainActivity.this, "已申请该权限", Toast.LENGTH_SHORT).show();
//                        }

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

    private void test() {
        ActivityCompat.requestPermissions(MainActivity.this, GROUP_STORAGE, 123);
//        requestPermissions(GROUP_STORAGE,123);
    }

    private static final String[] GROUP_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            int position = 0;
            for (String item : permissions) {
                Log.e(TAG,"permissions -> "+item);
            }

            for (int item : grantResults) {
                Log.e(TAG,"grantResults -> "+item);
            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //同意权限申请
                Toast.makeText(this, "已同意该权限", Toast.LENGTH_SHORT).show();
            } else { //拒绝权限申请
                Toast.makeText(this, "权限被拒绝了", Toast.LENGTH_SHORT).show();
            }
        }
    }
}