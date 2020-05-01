package com.read.restaurantautomationsystem.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.read.restaurantautomationsystem.R;

public class OrderItemsActivity extends AppCompatActivity {

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        // Receive the key of the selected Order object.
        Intent intent = getIntent();

        key = intent.getStringExtra("key");
    }

}
