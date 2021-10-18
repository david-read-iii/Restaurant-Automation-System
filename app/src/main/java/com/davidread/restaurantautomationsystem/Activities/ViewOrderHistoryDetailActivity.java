package com.davidread.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.davidread.restaurantautomationsystem.Models.Order;
import com.davidread.restaurantautomationsystem.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class ViewOrderHistoryDetailActivity extends AppCompatActivity {

    private Order selected;
    private TextView textViewStatus, textViewTotalPrice, textViewDate, textViewTime, textViewTable;

    /**
     * When this activity is created, inflate a layout containing several EditTexts. Each EditText
     * will represent an attribute of the selected Order object. A ChildEventListener will be setup
     * to close this activity when the selected Order object is changed by another user in the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Receive the attributes of the selected Order object.
        Intent intent = getIntent();

        selected = new Order(
                intent.getStringExtra("key"),
                intent.getIntExtra("number", 0),
                intent.getStringExtra("status"),
                intent.getDoubleExtra("totalPrice", 0),
                new Date(intent.getLongExtra("dateTimeOrdered", 0)),
                intent.getStringExtra("tableNameOrdered"),
                null
        );

        // Bring XML elements to Java.
        textViewStatus = findViewById(R.id.text_view_order_detail_status);
        textViewTotalPrice = findViewById(R.id.text_view_order_detail_total_price);
        textViewDate = findViewById(R.id.text_view_order_detail_date);
        textViewTime = findViewById(R.id.text_view_order_detail_time);
        textViewTable = findViewById(R.id.text_view_order_detail_table);

        // Create NumberFormat object to format currency attributes.
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        // Set the TextView values as the attributes of the selected Order object.
        textViewStatus.setText(getString(R.string.label_status, selected.getStatus()));
        textViewTotalPrice.setText(getString(R.string.label_total_price, currencyFormat.format(selected.getTotalPrice())));
        textViewDate.setText(getString(R.string.label_date, dateFormat.format(selected.getDateTimeOrdered())));
        textViewTime.setText(getString(R.string.label_time, timeFormat.format(selected.getDateTimeOrdered())));
        textViewTable.setText(getString(R.string.label_origin_table, selected.getTableNameOrdered()));
    }
}
