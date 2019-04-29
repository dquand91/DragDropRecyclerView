package com.example.dragdroprecyclerexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.MyEventHandler {
    // implements MainAdapter.MyEventHandler để tương tác giữa Activity với Adapter

    private MainAdapter mAdapter;

    // biến chứa thư viện
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReyclerView();
    }

    private void initReyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_sample);
        mAdapter = new MainAdapter();
        mAdapter.setMyEventHandler(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mAdapter.addData(getData());
        addItemTouchCallback(recyclerView);
    }

    private void addItemTouchCallback(RecyclerView recyclerView) {
        // khởi tạo callback từ thư viện, truyền vào adapter và cho phép swipe
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter, true);
        itemTouchHelper = new ItemTouchHelper(callback);
        // gắn cái itemTouchHelper và recyclerView của mình
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private List getData() {
        // Tạo data giả
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("Android " + i);
        }
        return data;
    }

    // Callback trả về từ bên Adapter
    @Override
    public void onStartDrag(int position) {
        Toast.makeText(this, "onStartDrag", Toast.LENGTH_SHORT).show();
    }

    // Callback trả về từ bên Adapter
    @Override
    public void onEndDrag(int position) {
        Toast.makeText(this, "onEndDrag", Toast.LENGTH_SHORT).show();
    }

    // Callback trả về từ bên Adapter
    @Override
    public void onIconPress(RecyclerView.ViewHolder vh) {
        // Sau khi user touch cái dấu 3 gạch bên Adapter => mình gọi thư viện bắt đầu Drag.
        // bắt đầu di chuyển item trong recyclerView khi touch vào cái nút 3 dấu gạch
        itemTouchHelper.startDrag(vh);
    }
}
