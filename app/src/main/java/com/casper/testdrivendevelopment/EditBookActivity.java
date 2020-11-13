package com.casper.testdrivendevelopment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {

    private int position;
    private String bookName;
    private EditText editTextBookName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        //获取传递过来的数据
        position = getIntent().getIntExtra("position", 0);
        bookName = getIntent().getStringExtra("book_name");

        editTextBookName = EditBookActivity.this.findViewById(R.id.book_edit_name);
        if (null != bookName)
            editTextBookName.setText(bookName);     //更新书名

        Button buttonOk = (Button)findViewById(R.id.button_ok);
        Button buttonCancel = (Button)findViewById(R.id.button_cancel);
        buttonOk.setOnClickListener(new View.OnClickListener() {    //确认按键
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();   //初始化新的意图

                //传递数据
                intent.putExtra("book_name", editTextBookName.getText().toString());
                intent.putExtra("book_position", position);
                setResult(RESULT_OK, intent);

                finish();   //结束activity
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {    //取消按键
            @Override
            public void onClick(View view) {
                finish();   //什么也不做，结束activity
            }
        });
    }
}