package com.example.dragdroprecyclerexample;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemTouchListenner mListenner;

    // Để cho phép xoá item trong list của recyclerView hay không
    // Default là không được xoá
    private boolean enableItemRemove = false;

    public SimpleItemTouchHelperCallback(ItemTouchListenner mListenner, boolean enableItemRemove) {
        this.mListenner = mListenner;
        this.enableItemRemove = enableItemRemove;
    }

    // Cho phép kéo thả sau khi long press 1 item hay không
    // Ở đây đang ko cho phép
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    // Cho phép swipe trái/phải 1 item hay không
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    // Ở đây, dragFlag cho phép kéo lên / kéo xuống 1 item.
    //      Nếu chỉ để ItemTouchHelper.DOWN => Chỉ cho phép kéo 1 item đi xuống, ko được phép kéo kên
    // swipeFlag, để cho phép swipe 1 item sang trái / sang phải.
    //      Nếu chỉ để ItemTouchHelper.LEFT => Chỉ cho phép kép item sang trái, không kéo sang phải được
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
        if(enableItemRemove){
            int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return makeMovementFlags(dragFlag, 0);
    }

    // Call back từ thư viện ItemTouchHelper trả về khi Move 1 item
    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        // Thông qua listener mình trả callback về cho Adapter để xử lý UI và logic
        mListenner.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    // Call back từ thư viện ItemTouchHelper trả về khi Swipe 1 item
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Thông qua listener mình trả callback về cho Adapter để xử lý UI và logic
        mListenner.onSwipe(viewHolder.getAdapterPosition(), direction);
    }

    // Call back từ thư viện ItemTouchHelper trả về sau khi user hoàn thành tương tác với 1 item
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder instanceof MainAdapter.MyViewHolder){
            ((MainAdapter.MyViewHolder) viewHolder).onItemCleared();
            mListenner.onEndDrag(viewHolder.getAdapterPosition());
        }

    }

    // // Call back từ thư viện ItemTouchHelper trả về khi bắt đầu swipe hoặc drag 1 item
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if(viewHolder instanceof ItemTouchHelperViewHolder){
            ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
            mListenner.onStartDrag(viewHolder.getAdapterPosition());
        }
    }
}
