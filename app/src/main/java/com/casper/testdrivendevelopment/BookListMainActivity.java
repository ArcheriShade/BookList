package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.casper.testdrivendevelopment.dataprocessor.Book;
import com.casper.testdrivendevelopment.dataprocessor.DataBank;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_ITEM_NEW = 1;
    public static final int CONTEXT_MENU_ITEM_APPEND = 2;
    public static final int CONTEXT_MENU_ITEM_UPDATE = 3;
    public static final int CONTEXT_MENU_ITEM_DELETE = 4;
    public static final int CONTEXT_MENU_ITEM_MOREINFO = 5;

    public static final int REQUEST_CODE_ADD_BOOK = 100;
    public static final int REQUEST_CODE_UPDATE_BOOK = 101;

    //声明列表
    private DataBank dataBank;
    private BookAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //当activity启动时执行
        switch (requestCode) {  //获取返回码
            case REQUEST_CODE_ADD_BOOK:     //添加书籍
                if (resultCode == RESULT_OK) {  //如果通过
                    String bookName = data.getStringExtra("book_name"); //获取书名
                    int position = data.getIntExtra("book_position", 0);    //设置位置
                    dataBank.getBooks().add(position, new Book(bookName, R.drawable.book_no_name)); //往数组里添加元素
                    dataBank.Save();
                    adapter.notifyDataSetChanged();                                 //提示适配器数据变更，刷新
                    Toast.makeText(BookListMainActivity.this, "已创建新书", Toast.LENGTH_LONG).show();   //提示
                }
                break;
            case REQUEST_CODE_UPDATE_BOOK:  //更新书籍
                if (resultCode == RESULT_OK) {  //如果通过
                    String book_name = data.getStringExtra("book_name");    //获取书名
                    int position = data.getIntExtra("book_position", 0);    //设置位置
                    dataBank.getBooks().get(position).setName(book_name);               //修改书名
                    dataBank.Save();
                    adapter.notifyDataSetChanged();                                //提示适配器数据变更，刷新
                }
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        initData();

        initView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v == findViewById(R.id.list_view_books)) {
            menu.setHeaderTitle("操作");      //设置菜单标题
            menu.add(1, CONTEXT_MENU_ITEM_NEW, 1, "新增");        //给菜单新增选项
            menu.add(1, CONTEXT_MENU_ITEM_APPEND, 1, "追加");
            menu.add(1, CONTEXT_MENU_ITEM_UPDATE, 1, "修改");
            menu.add(1, CONTEXT_MENU_ITEM_DELETE, 1, "删除");
            menu.add(1, CONTEXT_MENU_ITEM_MOREINFO, 1, "关于");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = menuInfo.position;
        AlertDialog.Builder builder;
        Intent intent;
        switch (item.getItemId()) {
            case CONTEXT_MENU_ITEM_NEW:             //选项：新增
                //初始化新的意图
                intent = new Intent(BookListMainActivity.this, EditBookActivity.class);
                intent.putExtra("position",position);   //向新的activity传递数据
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK);  //启动新的activity
                break;
            case CONTEXT_MENU_ITEM_APPEND:
                intent = new Intent(BookListMainActivity.this, EditBookActivity.class);
                intent.putExtra("position",position + 1);
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK);
                break;
            case CONTEXT_MENU_ITEM_UPDATE:
                intent = new Intent(BookListMainActivity.this, EditBookActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("book_name", dataBank.getBooks().get(position).getTitle());
                startActivityForResult(intent, REQUEST_CODE_UPDATE_BOOK);
                break;
            case CONTEXT_MENU_ITEM_DELETE:          //选项：删除
                builder =  new AlertDialog.Builder(BookListMainActivity.this);      //新建提示窗口
                builder.setTitle("确认删除");       //设置提示窗标题
                builder.setMessage("你确定要删除《" + dataBank.getBooks().get(position).getTitle() + "》吗？");    //设置提示内容
                builder.setCancelable(true);        //允许取消

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {     //确认删除
                    @Override
                    public void onClick(DialogInterface dialog, int which) {        //设置按钮点击监视函数
                        dataBank.getBooks().remove(position);                            //移除元素
                        dataBank.Save();
                        adapter.notifyDataSetChanged();                             //提示适配器数据变更，刷新
                        Toast.makeText(BookListMainActivity.this, "删除成功", Toast.LENGTH_LONG).show();    //提示
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {     //取消删除
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;   //什么都不做
                    }
                });
                builder.create().show();    //显示提示窗
                break;
            case CONTEXT_MENU_ITEM_MOREINFO:        //选项：关于
                builder = new AlertDialog.Builder(BookListMainActivity.this);
                builder.setTitle("关于");
                builder.setMessage("书名：《" + dataBank.getBooks().get(position).getTitle() + "》");
                builder.setCancelable(false);       //不允许取消

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void initView() {
        //初始化适配器，指定当前context、模板layout以及数组
        adapter = new BookAdapter(BookListMainActivity.this, R.layout.books_item, dataBank.getBooks());
        //将适配器绑定到指定ListView
        ListView listView = (ListView) findViewById(R.id.list_view_books);
        listView.setAdapter(adapter);
        this.registerForContextMenu(listView);
    }

    private void initData() {
        dataBank = new DataBank(this);
        dataBank.Load();
        if (0 == dataBank.getBooks().size()) {
            dataBank.getBooks().add(new Book("请添加书籍", R.drawable.book_no_name));
        }
    }

    public List<Book> getArrayListBooks() {
        return dataBank.getBooks();
    }

    private class BookAdapter extends ArrayAdapter<Book> {
        private int resourceId;

        //构造函数
        public BookAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        //重载getView方法
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Book book = getItem(position);  //获取当前项的实例
            View view;
            if (null == convertView)
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            else
                view = convertView;
            //设置图片
            ((ImageView) view.findViewById(R.id.image_view_book_cover)).setImageResource(book.getCoverResourceId());
            //设置书名
            ((TextView) view.findViewById(R.id.text_view_book_title)).setText(book.getTitle());
            return view;
        }
    }
}
