package com.gmail.gerbencdg.dragndrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.gmail.gerbencdg.dragndrop.blockviews.ForBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.IfBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.InstructionContainer;
import com.gmail.gerbencdg.dragndrop.blockviews.RecyclerBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.TextBlockView;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnTouchListener {

    private ScrollView mScrollView;
    private boolean smoothScrolling;
    private boolean isScrollingUp;
    private Handler mHandler;
    private RecyclerView mRv;
    private LinearLayout mLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableFullScreen();

        mRv = findViewById(R.id.main_rv);
        mScrollView = findViewById(R.id.main_scrollview);

        RecyclerAdapter adapter = new RecyclerAdapter(this);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRv.setAdapter(adapter);

        findViewById(R.id.ll).setOnDragListener(this);
        mRv.setOnDragListener(this);

        mLl = findViewById(R.id.ll);
        for (int i = 0; i < 2; i++) {
            BlockView textBv = new TextBlockView(this, "BlockView : " + i);
            mLl.addView(textBv);

            textBv.setOnTouchListener(this);
            textBv.setOnDragListener(this);
        }

        BlockView forBv = new ForBlockView(this);
        BlockView ifBv = new IfBlockView(this);
        IfBlockView ifBv2 = new IfBlockView(this);
        forBv.setOnTouchListener(this);
        ifBv.setOnTouchListener(this);
        ifBv2.setOnTouchListener(this);

        mLl.addView(forBv, 2);
        mLl.addView(ifBv, 2);
        mLl.addView(ifBv2, 2);

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
    private BlockView lastClonedRBV; // lastClonedRecyclerBlockView
    private boolean hasCloned;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view != lastTouchedView) {
            count = 0;
            lastTouchedView = view;
        }

//        if (view instanceof RecyclerBlockView) {
//
//            if (count == 0) {
//                lastClonedRBV = ((RecyclerBlockView) view).clone();
//                lastClonedRBV.setOnTouchListener(this);
//                lastClonedRBV.setOnDragListener(this);
//            }
//            // We work with the cloned realBv from the RecyclerBlockView instead
//            view = lastClonedRBV;
//        }

        if (count < 2) {
            if (count == 0) {
                hasCloned = false;
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

            if (!hasCloned && view instanceof RecyclerBlockView) { // pb : this is called multiple times (it seems not to be the problem)

                lastClonedRBV = ((RecyclerBlockView) view).clone();
                lastClonedRBV.setOnTouchListener(this);
                lastClonedRBV.setOnDragListener(this);
                hasCloned = true;
            }
        }
        if (view instanceof RecyclerBlockView) {
            // We work with the cloned realBv from the RecyclerBlockView instead
            view = lastClonedRBV;
        }

        // else, execute code below
        saveViewPosition(view);

        ClipData clipData = new ClipData("dragged",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                new ClipData.Item("dragged"));

        // returns true if drag started
        if (view.startDrag(clipData, new View.DragShadowBuilder(view), view, 0)) {
            view.setVisibility(View.INVISIBLE);
        } else {
            if (lastTouchedView instanceof RecyclerBlockView) {
                Log("RemoveCloneInstance called from onTouch");
                ((RecyclerBlockView) lastTouchedView).removeCloneInstance();
            }
        }

        return true;
    }

    private ViewGroup lastCont;
    private int lastPos;

    @Override
    public boolean onDrag(View hoveredView, DragEvent e) {

        ViewGroup hoveredContainer = null;
        final View dragged = (View) e.getLocalState();
        boolean hoveredIsRv = hoveredView instanceof RecyclerView;

        if (hoveredView instanceof LinearLayout) {
            if (hoveredView.getParent() instanceof ScrollView) {
                hoveredContainer = (LinearLayout) hoveredView;
            }
        }
        //Log("HoveredView : " + hoveredView);

        if (hoveredView instanceof InstructionContainer || hoveredIsRv) {
            // It can be an InstructionContainer or a ConditionContainer
            hoveredContainer = ((ViewGroup) hoveredView);
        }

        switch (e.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                Log("OnDrag Started");
                // Determines if this View can accept the dragged
                boolean res = hoveredContainer != null;
                Log("Res : " + res);
                Log("Dragged : " + dragged);

                dragged.setOnDragListener(null);
                return res;

            case DragEvent.ACTION_DRAG_ENTERED:

                // Log("OnDrag Entered");
                // Called when dragged enters in this view

                hoveredView.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                hoveredView.invalidate();

                return true;

            case DragEvent.ACTION_DRAG_EXITED:

                // Log("OnDrag Exited");
                hoveredView.getBackground().clearColorFilter();
                hoveredView.invalidate();

                if (!hoveredIsRv) {
                    ((ViewGroup) dragged.getParent()).removeView(dragged);
                }

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:

                Log("Drag Location : " + hoveredView + "\tHovered is Rv : " + hoveredIsRv);

                if (hoveredIsRv) {
                    smoothScrolling = false;
                    return true;
                }

                // check out for coordinates : view.onInterceptTouchEvent
                if (dragged.getParent() != null && dragged.getParent().getParent() instanceof ScrollView) {
                    if (e.getY() < mScrollView.getScrollY() + 100) {
                        smoothScrolling = true;
                        isScrollingUp = false;
                    } else if (e.getY() + 150 > mScrollView.getScrollY() + mScrollView.getHeight()) {
                        smoothScrolling = true;
                        isScrollingUp = true;
                    } else {
                        smoothScrolling = false;
                    }
                }

                AddDraggedView(dragged, hoveredContainer, e.getY());

                return true;

            case DragEvent.ACTION_DROP:

                Log("OnDrag Drop");
                //smoothScrolling = false;

                hoveredView.getBackground().clearColorFilter();
                hoveredView.invalidate();

                if (hoveredIsRv) {
                    return true;
                }

                if (hoveredContainer != null) {
                    AddDraggedView(dragged, hoveredContainer, e.getY());
                }

                return true;

            case DragEvent.ACTION_DRAG_ENDED:

                Log("OnDrag Ended. Result : " + (e.getResult() ? "SUCCESS" : "FAILURE"));

                smoothScrolling = false;

                hoveredView.getBackground().clearColorFilter();
                hoveredView.invalidate();

                if (lastTouchedView instanceof RecyclerBlockView) {
                    ((RecyclerBlockView) lastTouchedView).removeCloneInstance();
                    count = 0; // ensures we will make a new copy if this view is dragged again from the RecyclerView
                }
                // if dragged.getParent() == null, the hoveredview is the recyclerview
                if (hoveredIsRv || dragged.getParent() == null) return true;

                Log("new Parent : " + dragged.getParent() + "\n\n");
                if (!e.getResult()) {
                    resetDraggedView(dragged);
                }

                // sov 10988671
                dragged.post(new Runnable() {
                    @Override
                    public void run() {
                        dragged.setVisibility(View.VISIBLE);
                    }
                });

                return true;
        }

        return false;
    }

    private void saveViewPosition(View dragged) {

        // TODO check if this handles the situation correctly
        if (lastTouchedView instanceof RecyclerBlockView) {
            return; // In this case, this view is being dragged from the RecyclerView
        }
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
        // Log("Dragged : " + dragged + "\nContainer : " + container);
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
        // Log("setDraggedViewInContainer");
        if (dragged.getParent() != null)
            ((ViewGroup) dragged.getParent()).removeView(dragged);

        container.addView(dragged, position);
    }

    private void resetDraggedView(View dragged) {

        Log("LastTouchedView : " + lastTouchedView);
        if (lastTouchedView instanceof RecyclerBlockView) {
            return;
        }

        setDraggedViewInContainer(dragged, lastCont, lastPos);
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

































