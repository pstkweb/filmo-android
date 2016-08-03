package com.sixfingers.ui.floatingactionmenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.sixfingers.filmo.R;

import java.util.ArrayList;
import java.util.Collections;

public class FloatingActionMenu extends LinearLayout {
    private boolean isVertical;
    private boolean isOpen = false;
    private Direction direction;
    private FloatingActionButton mainButton;
    private ArrayList<FloatingActionButton> items = new ArrayList<>();
    private Animation menuOpen, menuClose, itemOpen, itemClose;

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, 0, 0);
        direction = Direction.values()[a.getInt(R.styleable.FloatingActionMenu_direction, 0)];
        int mainIcon = a.getResourceId(
                R.styleable.FloatingActionMenu_main_icon,
                R.drawable.ic_add_white_48dp
        );

        int mainColor = a.getColor(
                R.styleable.FloatingActionMenu_main_color,
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );
        a.recycle();

        isVertical = direction == Direction.TOP || direction == Direction.BOTTOM;

        menuOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        menuClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        itemOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_item_open);
        itemClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_item_close);

        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        setOrientation(isVertical ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);

        mainButton = new FloatingActionButton(getContext());
        mainButton.setClickable(true);
        mainButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mainButton.setSize(FloatingActionButton.SIZE_NORMAL);
        mainButton.setUseCompatPadding(true);
        mainButton.setImageResource(mainIcon);
        mainButton.setBackgroundTintList(ColorStateList.valueOf(mainColor));
        mainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isOpen = !isOpen;

                mainButton.startAnimation(isOpen ? menuClose : menuOpen);
                if (items != null) {
                    for (FloatingActionButton item : items) {
                        item.startAnimation(isOpen ? itemClose : itemOpen);
                        item.setClickable(isOpen);
                    }
                }
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT
        );
        switch (direction) {
            case TOP:
            case BOTTOM:
                params.gravity = Gravity.CENTER_HORIZONTAL;
                break;
            case END:
            case START:
                params.gravity = Gravity.CENTER_VERTICAL;
                break;
        }
        mainButton.setLayoutParams(params);

        addView(mainButton);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                switch (direction) {
                    case TOP:
                    case START:
                        ArrayList<View> children = new ArrayList<>();
                        for (int i=0; i<getChildCount(); i++) {
                            children.add(getChildAt(i));
                            removeViewAt(i);
                        }
                        Collections.reverse(children);

                        for (View v : children) {
                            addView(v);
                        }
                        break;
                }

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    public FloatingActionMenu(Context context) {
        this(context, null);
    }

    @Override
    public void onViewAdded(View child) {
        FloatingActionButton item = (FloatingActionButton) child;

        if (item != mainButton) {
            item.setSize(FloatingActionButton.SIZE_MINI);
            item.setUseCompatPadding(true);
            item.setVisibility(INVISIBLE);
            item.setClickable(false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            switch (direction) {
                case TOP:
                case BOTTOM:
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case END:
                case START:
                    params.gravity = Gravity.CENTER_VERTICAL;
                    break;
            }
            item.setLayoutParams(params);

            items.add(item);
        }
    }
}

enum Direction {
    TOP,
    END,
    BOTTOM,
    START
}
