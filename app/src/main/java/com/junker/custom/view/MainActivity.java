package com.junker.custom.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.junker.custom.view.customview.loginpage.LoginActivity;
import com.junker.custom.view.customview.numberinput.NumberInputActivity;
import com.junker.custom.view.customview.rebound.ReboundActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private List<String> titles;

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
                switch (titles.get(position)) {
                    case "计数器布局":
                        intent = new Intent(MainActivity.this, NumberInputActivity.class);
                        break;
                    case "登录布局":
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        break;
                    case "回弹布局":
                        intent = new Intent(MainActivity.this, ReboundActivity.class);
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
        titles.add("计数器布局");
        titles.add("登录布局");
        titles.add("回弹布局");
    }

    private void initAdapter() {
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public String getItem(int position) {
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
//                textView.setText(String.format("第（%s）数据",(position + 1)));
                textView.setText(getItem(position));
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

}