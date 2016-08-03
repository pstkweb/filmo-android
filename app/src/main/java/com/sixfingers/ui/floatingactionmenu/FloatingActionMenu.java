package com.sixfingers.ui.floatingactionmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sixfingers.filmo.R;

public class FloatingActionMenu extends LinearLayout {
    private boolean isVertical;
    private boolean isOpen = false;
    private LinearLayout layout;
    private FloatingActionButton mainButton;
    private FloatingActionButton[] items;
    private Animation menuOpen, menuClose, itemOpen, itemClose;

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, 0, 0);
        int direction = a.getInt(R.styleable.FloatingActionMenu_direction, 0);
        int mainIcon = a.getInt(
                R.styleable.FloatingActionMenu_main_icon,
                R.drawable.ic_add_white_48dp
        );
        a.recycle();

        isVertical = direction % 2 == 0;

        Toast.makeText(getContext(), "Total: " + getChildCount(), Toast.LENGTH_LONG).show();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.floating_action_menu, this, true);

        menuOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        menuClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        itemOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_item_open);
        itemClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_item_close);

        layout = (LinearLayout) getChildAt(0);
        layout.setOrientation(isVertical ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        layout.setGravity(isVertical ? Gravity.CENTER_HORIZONTAL : Gravity.CENTER_VERTICAL);

        mainButton = new FloatingActionButton(getContext());
        mainButton.setClickable(true);
        mainButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mainButton.setSize(FloatingActionButton.SIZE_NORMAL);
        mainButton.setUseCompatPadding(true);
        mainButton.setImageResource(mainIcon);
        mainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isOpen = !isOpen;

                mainButton.startAnimation(isOpen ? menuOpen : menuClose);

                for (FloatingActionButton item : items) {
                    item.startAnimation(isOpen ? itemOpen : itemClose);
                    item.setClickable(isOpen);
                }
            }
        });
        layout.addView(mainButton, direction == 0 || direction == 3 ? 0 : getChildCount());

        items = new FloatingActionButton[layout.getChildCount() - 1];
        Toast.makeText(getContext(), "Total: " + layout.getChildCount(), Toast.LENGTH_LONG).show();
        for (int i = 1; i<layout.getChildCount(); i++) {
            items[i - 1] = (FloatingActionButton) layout.getChildAt(i);
        }
    }

    public FloatingActionMenu(Context context) {
        this(context, null);
    }
}
