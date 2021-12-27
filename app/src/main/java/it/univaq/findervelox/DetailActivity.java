package it.univaq.findervelox;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.univaq.findervelox.model.Autovelox;

public class DetailActivity extends AppCompatActivity {

    private Autovelox autovelox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {
            autovelox = (Autovelox) getIntent().getSerializableExtra("autovelox");
            setupTextView();
        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }
    }

    private void setupTextView() {

        ((TextView) findViewById(R.id.textTitle)).setText(autovelox.getInfoPosition());
        ((TextView) findViewById(R.id.textCity)).setText(autovelox.getCity());
        ((TextView) findViewById(R.id.textProvince)).setText(autovelox.getProvince());
        ((TextView) findViewById(R.id.textRegion)).setText(autovelox.getRegion());
        ((TextView) findViewById(R.id.textDateTimeInsertion)).setText(convertTimestampToDate(autovelox.getDateTimeInsertion()));
        ((TextView) findViewById(R.id.textLatitude)).setText(autovelox.getLatitude() + "");
        ((TextView) findViewById(R.id.textLongitude)).setText(autovelox.getLongitude() + "");

    }

    private String convertTimestampToDate(long timestamp) {
        if(timestamp == -1)
            return "No date";

        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dataFormat.format(timestamp);

    }
}
