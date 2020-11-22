package ui.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;  
    private boolean paddingLeftRight;
    private boolean justPaddingRight;

    public SpacesItemDecoration(int space) {  
        this.space = space;  
    }

    public void setLeftRight(boolean paddingLeftRight) {
        this.paddingLeftRight = paddingLeftRight;
    }

    public void setJustPaddingRight(boolean justPaddingRight) {
        this.justPaddingRight = justPaddingRight;
    }
  
    @Override  
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (paddingLeftRight) {
            outRect.left = space;
            outRect.right = space;
        } else if (justPaddingRight) {
            outRect.left = 0;
            outRect.right = space;
        } else {
            outRect.right = space;
        }
        // Add top margin only for the first item to avoid double space between items
//        if(parent.getChildPosition(view) == 0)
//            outRect.top = space;
    }  
}  