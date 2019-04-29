package com.example.dragdroprecyclerexample;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>
        implements ItemTouchListenner {
    // implements ItemTouchListenner, để nhận tương tác với thư viện ItemTouchHelper.

    private LayoutInflater mInflater;

    // Data để hiển thị ở đây chỉ là 1 list String
    private List<String> mData;

    // interface do mình tạo để tương tác drag/drop/swipe giữa Activity/Fragment với Adapter
    private MyEventHandler myEventHandler;

    public MainAdapter() {
        mData = new ArrayList();
    }

    public void addData(List<String> data) {
        if (data == null) {
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(mData.get(position), myEventHandler);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwipe(int position, int direction) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onStartDrag(int position) {
        if(myEventHandler != null){
            myEventHandler.onStartDrag(position);
        }
    }

    @Override
    public void onEndDrag(int position) {
        if(myEventHandler != null){
            myEventHandler.onEndDrag(position);
        }

    }

    public MyEventHandler getMyEventHandler() {
        return myEventHandler;
    }

    public void setMyEventHandler(MyEventHandler myEventHandler) {
        this.myEventHandler = myEventHandler;
    }


    // CustomViewHolder
    static class MyViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {
        // implements ItemTouchHelperViewHolder để xử lý UI khi nhận callback từ ItemTouchHelper

        private TextView mTextTitle;
        private ImageView icDragDrop;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_title);
            icDragDrop = itemView.findViewById(R.id.icDragDrop);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void bindData(String data, final MyEventHandler eventHandler) {
            if (data == null) {
                return;
            }
            mTextTitle.setText(data);

            // Trả callback về cho Activity/Fragment để xử lý sau khi user chạm vào cái nút 3 dấu gạch
            // Ở đây, ý đồ là muốn bắt đầu drag item khi chạm vào 3 dấu gạch
            icDragDrop.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
                        if(eventHandler != null){
                            eventHandler.onIconPress(MyViewHolder.this);
                        }
                    }
                    return false;
                }
            });
        }

        @Override
        public void onItemCleared() {
            // Callback nhận được từ bên thư viện ItemTouchHelper thông qua interface ItemTouchHelperViewHolder do mình tạo
            // Thay đổi background của item khi item bị xoá
            itemView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), R.color.white, null));
        }

        @Override
        public void onItemSelected() {
            // Thay đổi background của item khi chọn item
            // R.drawable.bg_order_drag là cái background custom do mình tạo khi 1 item được chọn.
            // Ở đây khi chạy code, mình sẽ thấy khi item được chọn, nó sẽ thu nhỏ lại item lại.
            // Rồi khi hết chọn item => nó sẽ bình thường trở lại thông qua cái callback onItemCleared ở trên
            itemView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.bg_order_drag, null));
        }
    }

    // interface để tương tác giữa Adapter và Activity/Fragment
    public interface MyEventHandler{
        void onStartDrag(int position);
        void onEndDrag(int position);

        // Để trả callback khi cái icon trên adapter được nhấn.
        // Trong adapter này, mình trả callback này khi icon 3 gạch được chạm
        // Và bên Activity/Fragment khi nhận callback này => sẽ bắt đầu Drag item
        void onIconPress(RecyclerView.ViewHolder vh);
    }
}
