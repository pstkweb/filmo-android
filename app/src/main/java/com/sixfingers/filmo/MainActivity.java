package com.sixfingers.filmo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.sixfingers.filmo.dvdfrapi.DVDFrService;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private boolean FABisOpen = false;
    private FloatingActionButton fab_menu, fab_item1, fab_item2;
    private Animation fabMenuOpen, fabMenuClose, fabMenuItemOpen, fabMenuItemClose;

    private BarcodeCallback barcodeCB = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barcodeView.setStatusText(result.getText());
            }

            ImageView img = (ImageView) findViewById(R.id.barcodePreview);
            img.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(barcodeCB);

        fab_menu = (FloatingActionButton) findViewById(R.id.fab_menu);
        fab_item1 = (FloatingActionButton) findViewById(R.id.fab_menu_item1);
        fab_item2 = (FloatingActionButton) findViewById(R.id.fab_menu_item2);

        fabMenuOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        fabMenuClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fabMenuItemOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_item_open);
        fabMenuItemClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_item_close);

        new SearchBatmanTask().execute("Batman");
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

    // Minimal scan call by button
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(
                        MainActivity.this,
                        "Scanned: " + result.getContents(),
                        Toast.LENGTH_LONG
                ).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanBarcode(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);

        integrator.initiateScan();
    }*/

    public void pause(View v) {
        barcodeView.pause();
    }

    public void resume(View v) {
        barcodeView.resume();
    }
    
    public void scan(View v) {
        Toast.makeText(MainActivity.this, "Request a scan", Toast.LENGTH_SHORT).show();
    }
    
    public void search(View v) {
        Toast.makeText(MainActivity.this, "Search by title", Toast.LENGTH_SHORT).show();
    }

    public void afficherRes(SearchResult res) {
        Toast.makeText(
                MainActivity.this,
                (res == null ? "Aucun résultat" : res.toString()),
                Toast.LENGTH_LONG
        ).show();
    }

    public void FABmenu(View v) {
        FABisOpen = !FABisOpen;

        fab_menu.startAnimation(FABisOpen ? fabMenuOpen : fabMenuClose);

        fab_item1.startAnimation(FABisOpen ? fabMenuItemOpen : fabMenuItemClose);
        fab_item1.setClickable(FABisOpen);
        fab_item2.startAnimation(FABisOpen ? fabMenuItemOpen : fabMenuItemClose);
        fab_item2.setClickable(FABisOpen);
    }

    class SearchBatmanTask extends AsyncTask<String, Void, SearchResult>{
        @Override
        protected SearchResult doInBackground(String... params) {
            DVDFrService service = new Retrofit.Builder()
                    .baseUrl(DVDFrService.ENDPOINT)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build()
                    .create(DVDFrService.class);

            try {
                return service.searchByTitle(params[0], SupportType.ALL).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(SearchResult searchResult) {
            super.onPostExecute(searchResult);

            afficherRes(searchResult);
        }
    }
}
