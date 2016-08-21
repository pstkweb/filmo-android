package com.sixfingers.filmo;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ListView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.sixfingers.filmo.adapter.BarcodeListItemAdapter;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;
import com.sixfingers.filmo.model.Movie;
import com.sixfingers.filmo.runnable.SearchByGencode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchByScanActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private BarcodeListItemAdapter simpleAdapter;
    
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            Set<BarcodeFormat> acceptedFormats = new HashSet<BarcodeFormat>(){{
                add(BarcodeFormat.EAN_13);
                add(BarcodeFormat.UPC_A);
            }};

            if (result.getText() != null) {
                String barcode = ("0000000000000" + result.getText()).substring(
                        result.getText().length()
                );
                if (acceptedFormats.contains(result.getBarcodeFormat())) {
                    if (simpleAdapter.add(barcode)) {
                        barcodeView.setStatusText(barcode);

                        new SearchByGencode(
                                (Activity) barcodeView.getContext(),
                                simpleAdapter
                        ).execute(
                                barcode,
                                String.valueOf(SupportType.ALL)
                        );
                    } else {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                String.format(
                                        getResources().getString(R.string.already_scanned),
                                        barcode
                                ),
                                Snackbar.LENGTH_LONG
                        ).show();
                    }
                } else {
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            R.string.wrong_format,
                            Snackbar.LENGTH_LONG
                    ).show();
                }
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

        simpleAdapter = new BarcodeListItemAdapter(new HashMap<String, List<Movie>>(), this);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
