package com.sixfingers.filmo.ormlite;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class OrmLiteAppCompatActivity<H extends OrmLiteSqliteOpenHelper> extends AppCompatActivity {
    private volatile H helper;
    private volatile boolean created = false;
    private volatile boolean destroyed = false;

    public H getHelper() {
        if (helper == null) {
            if (!created) {
                throw new IllegalStateException("A call has not been made to onCreate() yet so the helper is null");
            } else if (destroyed) {
                throw new IllegalStateException(
                        "A call to onDestroy has already been made and the helper cannot be used after that point");
            } else {
                throw new IllegalStateException("Helper is null for some unknown reason");
            }
        } else {
            return helper;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (helper == null) {
            helper = getHelperInternal(this);
            created = true;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseHelper(helper);
        destroyed = true;
    }

    protected H getHelperInternal(Context context) {
        @SuppressWarnings({ "unchecked", "deprecation" })
        H newHelper = (H) OpenHelperManager.getHelper(context);
        return newHelper;
    }

    protected void releaseHelper(H helper) {
        OpenHelperManager.releaseHelper();
        this.helper = null;
    }
}
