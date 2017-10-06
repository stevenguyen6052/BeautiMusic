package com.example.windows10gamer.beautimusic.utilities.dragandswipe;

/**
 * Created by Windows 10 Gamer on 12/09/2017.
 */

public interface ItemTouchHelperAdapter {
    void onItemDismiss(int position);
    boolean onItemMove(int fromPosition, int toPosition);
}
