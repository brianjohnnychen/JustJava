/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int coffeeQuantity = 1;
    //String orderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        //name
        EditText editText = (EditText) findViewById(R.id.name);
        String orderName = editText.getText().toString();

        //TOPPING: whipped cream
        CheckBox checkBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = checkBox.isChecked();
        Log.v("MainActivity", "Has whipped cream: " + hasWhippedCream);

        //TOPPING: chocolate
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = checkBox2.isChecked();
        Log.v("MainActivity", "Has chocolate: " + hasChocolate);

        //calculate price
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        Log.v("MainActivity", "Price is: " + price);
        displayPrice(price);

        //print summary
        String priceMessage = createOrderSummary(orderName, price, hasWhippedCream, hasChocolate);
        TextView summary = (TextView) findViewById(R.id.orderSummary);
        summary.setText(priceMessage);

        //send order summary to email using Intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        String[] emailArray = {
                "brianjohnnychen@gmail.com",
                "meijiaous@gmail.com"};
        intent.putExtra(intent.EXTRA_EMAIL, emailArray); //must use array, inputting only a String won't work.
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java coffee order for " + orderName);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText("$" + number);
    }

    public void plusButton(View view) {
        //set maximum cups per order at 100
        if (coffeeQuantity >= 100) {
            Toast.makeText(this, "Maximum coffees per order is 100.", Toast.LENGTH_LONG).show();
            return;
        } else
            coffeeQuantity++;
        display(coffeeQuantity);
    }

    public void minusButton(View view) {
        //set minimum cups per order at 1
        if (coffeeQuantity <= 1) {
            Toast.makeText(this, "Minimum coffees per order is 1.", Toast.LENGTH_SHORT).show();
            return;
        } else
            coffeeQuantity--;
        display(coffeeQuantity);
    }

    //Lesson 7

    /**
     * @param hasWhippedCream If customer wants whipped cream
     * @param hasChocolate    If customer wants chocolate
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int price = 5;

        //increase price if whipped cream or chocolate is wanted
        if (hasWhippedCream == true)
            price += 1;
        if (hasChocolate == true)
            price += 1;

        return price * coffeeQuantity;
    }

    /**
     * @param name            The name for the order
     * @param price           The price of a cup of coffee.
     * @param addWhippedCream Determines if whipped cream checkbox is checked
     * @param addChocolate    Determines if chocolate checkbox is checked
     * @return This method creates the format for the order summary.
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\nAdd whipped cream? " + addWhippedCream;
        priceMessage += "\nAdd chocolate? " + addChocolate;
        priceMessage = priceMessage + "\nQuantity: " + coffeeQuantity;
        priceMessage = priceMessage + "\nTotal: $" + price;
        priceMessage = priceMessage + "\n" + getString((R.string.thank_you)); //use a string from the strings.xml for language localization
        return priceMessage;
    }

}