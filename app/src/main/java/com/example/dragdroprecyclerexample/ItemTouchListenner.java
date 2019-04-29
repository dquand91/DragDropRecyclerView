package com.example.dragdroprecyclerexample;

public interface ItemTouchListenner {
    void onMove(int oldPosition, int newPosition);

    void onSwipe(int position, int direction);

    void onStartDrag(int position);

    void onEndDrag(int position);

}
