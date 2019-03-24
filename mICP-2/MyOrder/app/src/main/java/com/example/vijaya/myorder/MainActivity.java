package com.example.vijaya.myorder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_TAG = "MainActivity";
    final int PINEAPPLE_PRICE = 1;
    final int BACON_PRICE = 1;
    final int PEPPERONI = 5;
    final int PHILLY = 8;
    final int CHICAGO = 7;
    final int HAWAIIAN = 7;
    int quantity = 3;
    ArrayList<String> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */

    public void submitOrder(View view) {
//        Log.d("View", view.getId());
        // get user input
        EditText userInputNameView = (EditText) findViewById(R.id.user_input);
        String userInputName = userInputNameView.getText().toString();
        // Check main base
        RadioButton pepperoni = (RadioButton) findViewById(R.id.pepperoni_checked);
        RadioButton philly = (RadioButton) findViewById(R.id.philly_checked);
        RadioButton chicago = (RadioButton) findViewById(R.id.chicago_checked);
        RadioButton hawaiian = (RadioButton) findViewById(R.id.hawaiian_checked);
        String chosen = "";
        items = new ArrayList<String>();
        int chosenPrice = -1;
        if (pepperoni.isChecked()){
            chosenPrice = PEPPERONI;
            chosen = pepperoni.getText().toString();
            items.add(chosen);
        }
        else if (philly.isChecked()){
            chosenPrice = PHILLY;
            chosen = philly.getText().toString();
            items.add(chosen);
        }
        else if (chicago.isChecked()){
            chosenPrice = CHICAGO;
            chosen = chicago.getText().toString();
            items.add(chosen);
        }
        else if (hawaiian.isChecked()){
            chosenPrice = HAWAIIAN;
            chosen = hawaiian.getText().toString();
            items.add(chosen);
        }
        else {
            Log.i("MainActivity", "Please select a base!");
            return;
        }
        // Check additional condiments/ingredients
        // check if whipped cream is selected
        CheckBox pineapple = (CheckBox) findViewById(R.id.pineapple_checked);
        boolean hasPineapple = pineapple.isChecked();
        if (hasPineapple){
            items.add("Added: Pineapple");
        }
        // check if chocolate is selected
        CheckBox bacon = (CheckBox) findViewById(R.id.bacon_checked);
        boolean hasBacon = bacon.isChecked();
        if (hasBacon){
            items.add("Added: Bacon");
        }
        // calculate and store the total price
        float totalPrice = calculatePrice(chosenPrice, hasPineapple, hasBacon);
        String formattedPrice = String.format("%.02f", totalPrice);
        items.add("Total: $" + formattedPrice);
        // create and store the order summary
        String orderSummaryMessage = createOrderSummary(userInputName, chosen, hasPineapple, hasBacon, totalPrice);
        Log.d("SUMMARY", orderSummaryMessage);

        // Write the relevant code for making the buttons work(i.e implement the implicit and explicit intents
        switch (view.getId()) {
            case R.id.btn_summary:
                // do something
                Intent redirect = new Intent(MainActivity.this, Summary.class);
                redirect.putExtra("summary", orderSummaryMessage);
                redirect.putExtra("userName", userInputName);
                redirect.putStringArrayListExtra("items", items);
                startActivity(redirect);
                break;
            case R.id.btn_email:
                // do something else
                sendEmail(userInputName, orderSummaryMessage);
                break;
        }
    }

    public void sendEmail(String name, String output) {
        // Write the relevant code for triggering email

        // Hint to accomplish the task

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"dhh3hb@mail.umkc.edu"});
        i.putExtra(Intent.EXTRA_SUBJECT, name + "'s Order");
        i.putExtra(Intent.EXTRA_TEXT   , output);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private String boolToString(boolean bool) {
        return bool ? (getString(R.string.yes)) : (getString(R.string.no));
    }

    private String createOrderSummary(String userInputName, String Base, boolean hasPineapple, boolean hasBacon, float price) {
        String orderSummaryMessage = getString(R.string.order_summary_name, userInputName) + "\n" +
                "Pizza: " + Base + "\n" +
                getString(R.string.order_summary_pineapple, boolToString(hasPineapple)) + "\n" +
                getString(R.string.order_summary_bacon, boolToString(hasBacon)) + "\n" +
                getString(R.string.order_summary_quantity, quantity) + "\n" +
                getString(R.string.order_summary_total_price, price) + "\n" +
                getString(R.string.thank_you);
        return orderSummaryMessage;
    }

    /**
     * Method to calculate the total price
     *
     * @return total Price
     */
    private float calculatePrice(int chosenPrice, boolean hasPineapple, boolean hasBacon) {
        int basePrice = chosenPrice;
        if (hasPineapple) {
            basePrice += PINEAPPLE_PRICE;
        }
        if (hasBacon) {
            basePrice += BACON_PRICE;
        }
        return quantity * basePrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method increments the quantity of coffee cups by one
     *
     * @param view on passes the view that we are working with to the method
     */

    public void increment(View view) {
        if (quantity < 100) {
            quantity = quantity + 1;
            display(quantity);
        } else {
            Log.i("MainActivity", "Please select less than one hundred cups of coffee");
            Context context = getApplicationContext();
            String lowerLimitToast = getString(R.string.too_much_coffee);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, lowerLimitToast, duration);
            toast.show();
            return;
        }
    }

    /**
     * This method decrements the quantity of coffee cups by one
     *
     * @param view passes on the view that we are working with to the method
     */
    public void decrement(View view) {
        if (quantity > 1) {
            quantity = quantity - 1;
            display(quantity);
        } else {
            Log.i("MainActivity", "Please select atleast one cup of coffee");
            Context context = getApplicationContext();
            String upperLimitToast = getString(R.string.too_little_coffee);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, upperLimitToast, duration);
            toast.show();
            return;
        }
    }
}