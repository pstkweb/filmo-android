package com.sixfingers.filmo;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ListView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.sixfingers.filmo.adapter.BarcodeListItemAdapter;
import com.sixfingers.filmo.model.Movie;

import java.util.HashMap;
import java.util.List;

public class SearchByScanActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private BarcodeListItemAdapter simpleAdapter;
    
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                if (!simpleAdapter.add(result.getText())) {
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            String.format(
                                    getResources().getString(R.string.already_scanned),
                                    result.getText()
                            ),
                            Snackbar.LENGTH_LONG
                    ).show();
                }

                barcodeView.setStatusText(result.getText());
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_scan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        barcodeView.decodeContinuous(callback);

        ListView list = (ListView) findViewById(R.id.scanned_list);
        list.setEmptyView(findViewById(android.R.id.empty));

        simpleAdapter = new BarcodeListItemAdapter(new HashMap<String, Movie >());
        list.setAdapter(simpleAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
