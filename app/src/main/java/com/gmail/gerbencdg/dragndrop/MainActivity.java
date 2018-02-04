package com.gmail.gerbencdg.dragndrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.ContainerBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.TextBlockView;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnTouchListener {

    private ScrollView mScrollView;
    private boolean smoothScrolling;
    private boolean isScrollingUp;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableFullScreen();

        RecyclerView mRv = findViewById(R.id.main_rv);
        CardView mCardView1 = findViewById(R.id.bv1);
        CardView mCardView2 = findViewById(R.id.bv2);
        mScrollView = findViewById(R.id.main_scrollview);

        RecyclerAdapter adapter = new RecyclerAdapter(this);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRv.setAdapter(adapter);

        mCardView1.setOnTouchListener(this);
        mCardView1.setOnDragListener(this);

        mCardView2.setOnTouchListener(this);
        mCardView2.setOnDragListener(this);

        findViewById(R.id.ll).setOnDragListener(this);

        LinearLayout ll = findViewById(R.id.ll);
        for (int i = 0; i < 10; i++) {
            BlockView textBv = new TextBlockView(this, "BlockView : " + i);
            ll.addView(textBv);

            textBv.setOnTouchListener(this);
            textBv.setOnDragListener(this);
        }

        BlockView bv = new ContainerBlockView(this);
        ll.addView(bv, 2);

        mHandler = new Handler();
        mStatusChecker.run();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (smoothScrolling) {
                    mScrollView.scrollBy(0, isScrollingUp ? 15 : -15);
                }
            } finally {
                mHandler.postDelayed(this, 1000 / 60);
            }
        }
    };


    private int count;
    private float mFirstX;
    private float mFirstY;
    private View lastTouchedView;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log("Count : " + count);
        if (view != lastTouchedView) {
            count = 0;
            lastTouchedView = view;
        }
        if (count < 2) {
            if (count == 0) {
                mFirstX = motionEvent.getX();
                mFirstY = motionEvent.getY();
            }
            count++;

            return true;

        } else if (count == 2) {

            float dX = Math.abs(motionEvent.getX() - mFirstX);
            float dY = Math.abs(motionEvent.getY() - mFirstY);

            if (view.getParent() instanceof RecyclerView && dX > dY
                    || view.getParent() instanceof LinearLayout && dY > dX)
                // let me scroll !
                return true;
        }
        // else, execute code below


        saveViewPosition(view);

        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item("dragged");

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data;
        data = new ClipData("dragged", mimeTypes, item);

        // Instanciates the drag shadow builder.
        View.DragShadowBuilder mShadowBuilder = new View.DragShadowBuilder(view);

        // Starts the drag
        view.startDrag(data  //data to be dragged
                , mShadowBuilder //drag shadow
                , view  //local data about the drag and drop operation
                , 0
        );
        //Set view visibility to INVISIBLE as we are going to drag the view
        view.setVisibility(View.INVISIBLE);

        return true;
    }

    private ViewGroup lastCont;
    private int lastPos;

    @Override
    public boolean onDrag(View hoveredView, DragEvent e) {
        ViewGroup hoveredContainer = null;
        View dragged = (View) e.getLocalState();

        if (hoveredView instanceof LinearLayout) {
            hoveredContainer = (LinearLayout) hoveredView;
        }

        if (hoveredView instanceof BlockView) {
            if (((BlockView) hoveredView).isContainer()){
                hoveredContainer = ((ViewGroup) hoveredView);
            }
        }

        switch (e.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                Log("OnDrag called");
                // Determines if this View can accept the dragged
                return hoveredContainer != null && e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            case DragEvent.ACTION_DRAG_ENTERED:

                Log("OnDrag Entered");
                // Called when dragged enters in this view

                hoveredView.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                hoveredView.invalidate();

                return true;

            case DragEvent.ACTION_DRAG_EXITED:

                Log("OnDrag Exited");
                hoveredView.getBackground().clearColorFilter();
                hoveredView.invalidate();

                ((ViewGroup) dragged.getParent()).removeView(dragged);

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:

//                Log("Drag Y: " + e.getY());
                //  Called each time the dragged changes position

                //  if (hoveredContainer.getChildCount() == 0) return false;

                //  View childTop = hoveredContainer.getChildAt(0);
                //  View childBottom = hoveredContainer.getChildAt(hoveredContainer.getChildCount() - 1);


                /*   if ((e.GetY() >= childTop.GetY() - childTop.Height * 2) &&
                        (e.GetY() <= childBottom.GetY() + childBottom.Height * 2))  */

//                Log("mScrollView.getScrollY : " + mScrollView.getScrollY());
                if (e.getY() < mScrollView.getScrollY() + 100) {
                    smoothScrolling = true;
                    isScrollingUp = false;
                } else if (e.getY() + 150 > mScrollView.getScrollY() + mScrollView.getHeight()) {
                    smoothScrolling = true;
                    isScrollingUp = true;
                } else {
                    smoothScrolling = false;
                }

                AddDraggedView(dragged, hoveredContainer, e.getY());

                return true;

            case DragEvent.ACTION_DROP:

                Log("OnDrag Drop");
                smoothScrolling = false;

                ClipData.Item item = e.getClipData().getItemAt(0);

                hoveredView.getBackground().clearColorFilter();
                hoveredView.invalidate();

                if (hoveredContainer != null) {
                    AddDraggedView(dragged, hoveredContainer, e.getY());
                }

                return true;

            case DragEvent.ACTION_DRAG_ENDED:

                Log("OnDrag Ended. Result : " + (e.getResult() ? "SUCCESS" : "FAILURE"));
                smoothScrolling = false;

                hoveredView.getBackground().clearColorFilter();
                hoveredView.invalidate();

                if (!e.getResult()) {
                    resetDraggedView(dragged);
                }
                dragged.setVisibility(View.VISIBLE);

                return true;
        }

        return false;
    }

    private void saveViewPosition(View dragged) {

        lastCont = (ViewGroup) dragged.getParent();

        for (int i = 0; i < lastCont.getChildCount(); i++) {
            if (lastCont.getChildAt(i).equals(dragged)) {
                lastPos = i;
                return;
            }
        }
        throw new IllegalStateException("Couldn't find the position of the dragged view");
    }

    private void AddDraggedView(View dragged, ViewGroup container, float draggedY) {
        dragged.setVisibility(View.INVISIBLE);

        View child;
        for (int i = 0; i < container.getChildCount(); i++) {
            child = container.getChildAt(i);

            if (draggedY <= (child.getY() + child.getHeight() / 2)) {
                setDraggedViewInContainer(dragged, container, i);
                return;
            }
        }
        // insert at the end
        setDraggedViewInContainer(dragged, container, container.getChildCount() - 1);
    }

    private void setDraggedViewInContainer(View dragged, ViewGroup container, int position) {
        if (dragged.getParent() != null)
            ((ViewGroup) dragged.getParent()).removeView(dragged);

        container.addView(dragged, position);
    }

    private void resetDraggedView(View dragged) {

        if (lastCont instanceof RecyclerView)
            return;

        setDraggedViewInContainer(dragged, lastCont, lastPos);
        dragged.setVisibility(View.VISIBLE);
    }

    private void Log(String s) {
        Log.w("MainActivity", s);
    }

    private void enableFullScreen() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}

































