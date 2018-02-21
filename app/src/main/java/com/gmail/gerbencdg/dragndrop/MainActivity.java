package com.gmail.gerbencdg.dragndrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
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
import android.widget.Toast;

import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.RecyclerBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.container.ContainerBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.container.InstructionContainer;
import com.gmail.gerbencdg.dragndrop.blockviews.simple.TextBlockView;

import static com.gmail.gerbencdg.dragndrop.blockviews.BlockView.Categories.ALL;
import static com.gmail.gerbencdg.dragndrop.blockviews.BlockView.Categories.INSTRUCTIONS;
import static com.gmail.gerbencdg.dragndrop.blockviews.BlockView.Categories.LOGIC;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnTouchListener {

    private static final int MAX_BLOCK_DEPTH = 3;
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

        final RecyclerAdapter adapter = new RecyclerAdapter(this, ALL);
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

        TabLayout tabLayout = findViewById(R.id.main_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("Instructions").setTag(INSTRUCTIONS));
        tabLayout.addTab(tabLayout.newTab().setText("Logic").setTag(LOGIC));

        TabLayout.Tab allTab = tabLayout.newTab().setText("All").setTag(ALL);
        tabLayout.addTab(allTab);
        allTab.select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.setFilter(((BlockView.Categories)tab.getTag()));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

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

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            count = 0;
            return true;
        }

//        if (view instanceof RecyclerBlockView) {
//
//            if (!hasCloned) {
//                lastClonedRBV = ((RecyclerBlockView) view).clone();
//                lastClonedRBV.setVisibility(View.INVISIBLE);
//                lastClonedRBV.setOnTouchListener(this);
//                lastClonedRBV.setOnDragListener(this);
//                hasCloned = true;
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
        }

        float dX = Math.abs(motionEvent.getX() - mFirstX);
        float dY = Math.abs(motionEvent.getY() - mFirstY);

        if (lastTouchedView.getParent() instanceof RecyclerView && (dX > dY)
                || lastTouchedView.getParent() instanceof LinearLayout && dY > dX)
            // let me scroll, don't call onTouch again for the current touch.
            return false;

        if (view instanceof RecyclerBlockView) {

            if (!hasCloned) {
                lastClonedRBV = ((RecyclerBlockView) view).clone();
                lastClonedRBV.setOnTouchListener(this);
                lastClonedRBV.setOnDragListener(this);
                hasCloned = true;
            }
            // We work with the cloned realBv from the RecyclerBlockView instead
            view = lastClonedRBV;
        }

        saveViewPosition(view);

        ClipData clipData = new ClipData("dragged",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                new ClipData.Item("dragged"));

        if (view instanceof RecyclerBlockView) {
            view.setVisibility(View.VISIBLE);
        }
        // returns true if drag started
        if (view.startDrag(clipData, new View.DragShadowBuilder(lastTouchedView), view, 0)) {
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

            if (hoveredView instanceof InstructionContainer) {
                if ((((BlockView) hoveredView.getParent().getParent())).getDepth() >= MAX_BLOCK_DEPTH
                        && dragged instanceof ContainerBlockView) {
                    Toast.makeText(this, "Forbidden to nest more than " + MAX_BLOCK_DEPTH + " Blocks in depth.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
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

                hoveredView.getBackground().setColorFilter(Color.parseColor("#FFE082"), PorterDuff.Mode.SRC_IN);
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

                //Log("Drag Location : " + hoveredView + "\tHovered is Rv : " + hoveredIsRv);

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
                if (hoveredIsRv) return true;

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

































