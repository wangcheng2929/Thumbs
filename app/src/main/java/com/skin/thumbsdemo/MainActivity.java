package com.skin.thumbsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ThumbsView mThumbsView;
    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThumbsView = (ThumbsView) findViewById(R.id.tbv_like);
        mEditText = (EditText) findViewById(R.id.et_content);
        mButton = (Button) findViewById(R.id.bt_sure);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mEditText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), "输入不能为空且只能为数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                mThumbsView.setContent(content);
            }
        });
    }
}
