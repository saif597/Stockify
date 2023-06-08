package com.example.stockify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

public class Reports extends AppCompatActivity {

    String ids;
    DrawerLayout drawerLayout;

    TextView dname,demail;
    String uname,uemail;
    ImageView menu;
    LinearLayout home,addProduct,products,addSale,sales,about,logout,reports;
    Spinner spinP,spinP2;

    SpinKitView spinnerReports;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        spinP=findViewById(R.id.spinnerP);
        spinP2=findViewById(R.id.spinnerP2);
        home=findViewById(R.id.home);
        addProduct=findViewById(R.id.addProduct);
        products=findViewById(R.id.products);
        addSale=findViewById(R.id.addSales);
        sales=findViewById(R.id.salesT);
        about=findViewById(R.id.about);
        logout=findViewById(R.id.logout);
        reports=findViewById(R.id.reports);
        spinnerReports = findViewById(R.id.spinnerReports);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinnerReports.setIndeterminateDrawable(threeBounce);
        Intent i=getIntent();
        ids=i.getStringExtra("userId");
        uname=i.getStringExtra("name");
        uemail=i.getStringExtra("email");



        dname=findViewById(R.id.drawerName);
        demail=findViewById(R.id.drawerEmail);
        dname.setText(uname);
        demail.setText(uemail);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Reports.this,MainActivity.class,ids,uname,uemail);
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Reports.this,AddProduct.class,ids,uname,uemail);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Reports.this,Products.class,ids,uname,uemail);
            }
        });
        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Reports.this,AddSales.class,ids,uname,uemail);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Reports.this, SalesPage.class,ids,uname,uemail);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Reports.this,About.class,ids,uname,uemail);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Reports.this, "Logged Out", Toast.LENGTH_SHORT).show() ;
                redirectActivity(Reports.this,Login.class,ids,uname,uemail);
            }
        });
        setSpinnerVisible(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        DatabaseReference productsRef = usersRef.child(ids).child("products");
        DatabaseReference salesRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ids).child("Sales");
        Reports.FetchDataAsyncTaskForReports asyncTask = new Reports.FetchDataAsyncTaskForReports(productsRef, salesRef);
        asyncTask.execute();

    }
    private void setSpinnerVisible(boolean visible) {
        if (visible) {
            spinnerReports.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinnerReports.setVisibility(View.GONE);
            // Remove the content blur
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity acitivity,Class secondActivity,String s,String n ,String e){
        Intent intent=new Intent(acitivity,secondActivity);
        intent.putExtra("userId",s);
        intent.putExtra("name",n);
        intent.putExtra("email",e);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        acitivity.startActivity(intent);
        acitivity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout) ;
    }


    private class FetchDataAsyncTaskForReports extends AsyncTask<Void, Void, Pair<List<HashMap<String, Object>>, List<HashMap<String, Object>>>> {
        private DatabaseReference productsRef;
        private DatabaseReference salesRef;

        public FetchDataAsyncTaskForReports(DatabaseReference productsRef, DatabaseReference salesRef) {
            this.productsRef = productsRef;
            this.salesRef = salesRef;
        }

        // Inside the FetchDataAsyncTask class

        @Override
        protected Pair<List<HashMap<String, Object>>, List<HashMap<String, Object>>> doInBackground(Void... voids) {
            List<HashMap<String, Object>> productList = new ArrayList<>();
            List<HashMap<String, Object>> salesList = new ArrayList<>();

            try {
                // Fetch product data
                DataSnapshot productDataSnapshot = Tasks.await(productsRef.get());
                for (DataSnapshot productSnapshot : productDataSnapshot.getChildren()) {
                    String productKey = productSnapshot.getKey();
                    String name = productSnapshot.child("name").getValue(String.class);
                    String description = productSnapshot.child("description").getValue(String.class);
                    int quantity = productSnapshot.child("quantity").getValue(Integer.class);
                    double price = productSnapshot.child("price").getValue(Double.class);
                    String imageUrl = productSnapshot.child("imageUrl").getValue(String.class);
                    int discount = productSnapshot.child("discount").getValue(Integer.class);
                    String category = productSnapshot.child("category").getValue(String.class);

                    HashMap<String, Object> product = new HashMap<>();
                    product.put("productKey", productKey);
                    product.put("name", name);
                    product.put("description", description);
                    product.put("quantity", quantity);
                    product.put("price", price);
                    product.put("imageUrl", imageUrl);
                    product.put("discount", discount);
                    product.put("category", category);

                    productList.add(product);
                }

                // Fetch sales data
                DataSnapshot salesDataSnapshot = Tasks.await(salesRef.get());
                for (DataSnapshot saleSnapshot : salesDataSnapshot.getChildren()) {
                    String saleId = saleSnapshot.getKey();
                    String date = saleSnapshot.child("date").getValue(String.class);
                    String productId = saleSnapshot.child("productId").getValue(String.class);
                    String productName = saleSnapshot.child("productName").getValue(String.class);
                    double profit = saleSnapshot.child("profit").getValue(Double.class);
                    int quantity = saleSnapshot.child("quantity").getValue(Integer.class);
                    double revenue = saleSnapshot.child("revenue").getValue(Double.class);
                    double totalPrice = saleSnapshot.child("totalPrice").getValue(Double.class);

                    HashMap<String, Object> saleInfo = new HashMap<>();
                    saleInfo.put("saleId", saleId);
                    saleInfo.put("date", date);
                    saleInfo.put("productId", productId);
                    saleInfo.put("productName", productName);
                    saleInfo.put("profit", profit);
                    saleInfo.put("quantity", quantity);
                    saleInfo.put("revenue", revenue);
                    saleInfo.put("totalPrice", totalPrice);

                    salesList.add(saleInfo);
                }
            } catch (Exception e) {
                // Handle exceptions
            }

            return new Pair<>(productList, salesList);
        }


        @Override
        protected void onPostExecute(Pair<List<HashMap<String, Object>>, List<HashMap<String, Object>>> result) {
            setSpinnerVisible(false);
            List<HashMap<String, Object>> productList = result.first;
            List<HashMap<String, Object>> salesList = result.second;

            // Create an array to store the product names
            List<String> productNames = new ArrayList<>();

// Iterate over the sales list to extract the product names
            for (HashMap<String, Object> sale : salesList) {
                String productName = (String) sale.get("productName");
                if (!productNames.contains(productName)) {
                    productNames.add(productName);
                }
            }

// Create an ArrayAdapter using the productNames array and a default spinner layout
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Reports.this, android.R.layout.simple_spinner_item, productNames);

// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Get a reference to your Spinner view

// Set the adapter to the Spinner
            spinP.setAdapter(adapter);
            spinP2.setAdapter(adapter);
            generateBarChart(salesList);
            generateLineChart(salesList);
            spinP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    createProductSalesLineChart(salesList,spinP.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spinP2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    createProductSalesBarChart(salesList,spinP2.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




        }
        private void generateBarChart(List<HashMap<String, Object>> salesList) {
            BarChart barChart = findViewById(R.id.barChart);

            List<BarEntry> barEntries = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            List<Integer> colors = new ArrayList<>();

            // Calculate total profit for each product
            Map<String, Double> productProfitMap = new HashMap<>();

            for (HashMap<String, Object> sale : salesList) {
                String productName = (String) sale.get("productName");
                double profit = (double) sale.get("profit");

                if (productProfitMap.containsKey(productName)) {
                    double totalProfit = productProfitMap.get(productName) + profit;
                    productProfitMap.put(productName, totalProfit);
                } else {
                    productProfitMap.put(productName, profit);
                }
            }

            // Sort products by profit in descending order
            List<Map.Entry<String, Double>> sortedProducts = new ArrayList<>(productProfitMap.entrySet());
            Collections.sort(sortedProducts, (a, b) -> Double.compare(b.getValue(), a.getValue()));

            // Extract top 5 products or all if less than 5
            int topProductsCount = Math.min(5, sortedProducts.size());

            for (int i = 0; i < topProductsCount; i++) {
                Map.Entry<String, Double> entry = sortedProducts.get(i);
                labels.add(entry.getKey());
                barEntries.add(new BarEntry(i, entry.getValue().floatValue()));

                // Generate a random color for each bar
                int color = Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
                colors.add(color);
            }

            // Create the bar dataset
            BarDataSet barDataSet = new BarDataSet(barEntries, "Profit");
            barDataSet.setColors(colors);

            BarData barData = new BarData(barDataSet);

            // Customize the bar chart appearance
            barChart.setData(barData);
            barChart.setFitBars(true);
            barChart.getDescription().setEnabled(false);

            // X-Axis customization
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setLabelCount(labels.size());

            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int index = (int) value;
                    String productName = labels.get(index);

                    // Adjust product name length to fit within screen or truncate to 7 characters
                    if (productName.length() > 7) {
                        return productName.substring(0, 7);
                    } else {
                        return productName;
                    }
                }
            });

            // Y-Axis customization
            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMinimum(0f);

            // Disable the right Y-axis
            barChart.getAxisRight().setEnabled(false);

            // Refresh the chart
            barChart.invalidate();
        }

        private void generateLineChart(List<HashMap<String, Object>> salesList) {
            LineChart lineChart = findViewById(R.id.lineChart);

            List<Entry> entries = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            // Calculate sales count for each day
            HashMap<String, Integer> salesCountMap = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

            for (HashMap<String, Object> sale : salesList) {
                String date = (String) sale.get("date");

                if (salesCountMap.containsKey(date)) {
                    int count = salesCountMap.get(date) + 1;
                    salesCountMap.put(date, count);
                } else {
                    salesCountMap.put(date, 1);
                }
            }

            // Get the dates of the past 7 days and their corresponding day names
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -6); // Subtract 6 days to get the starting date

            for (int i = 0; i < 7; i++) {
                Date date = calendar.getTime();
                String formattedDate = dateFormat.format(date);
                String dayName = dayFormat.format(date);

                labels.add(dayName);

                // Get the sales count for the current date
                int salesCount = salesCountMap.containsKey(formattedDate) ? salesCountMap.get(formattedDate) : 0;

                entries.add(new Entry(i, salesCount));

                calendar.add(Calendar.DAY_OF_YEAR, 1); // Increment the calendar by 1 day
            }

            // Create the line dataset
            LineDataSet lineDataSet = new LineDataSet(entries, "Sales Count");
            lineDataSet.setColor(Color.rgb(104, 241, 175));
            lineDataSet.setCircleColor(Color.rgb(104, 241, 175));
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleRadius(4f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setValueTextSize(12f);
            lineDataSet.setDrawValues(true);

            LineData lineData = new LineData(lineDataSet);

            // Customize the line chart appearance
            lineChart.setData(lineData);
            lineChart.getDescription().setEnabled(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            lineChart.getAxisRight().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.setTouchEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setPinchZoom(true);

            // Set the line chart as the content view
            lineChart.invalidate();
        }

        private void createProductSalesLineChart(List<HashMap<String, Object>> salesList, String productName) {
            LineChart lineChart = findViewById(R.id.lineChart2);

            List<Entry> entries = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            // Calculate sales count for the product within the past week
            HashMap<String, Float> productSalesQuantityMap = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();

            for (int i = 6; i >= 0; i--) {
                Date date = calendar.getTime();
                String formattedDate = dateFormat.format(date);
                String dayName = dayFormat.format(date);

                labels.add(dayName);

                // Get the sales quantity for the product on the current date
                float salesQuantity = 0f;
                for (HashMap<String, Object> sale : salesList) {
                    String saleProductName = (String) sale.get("productName");
                    String saleDate = (String) sale.get("date");
                    float saleQuantity = ((Number) sale.get("quantity")).floatValue();

                    if (productName.equals(saleProductName) && formattedDate.equals(saleDate)) {
                        salesQuantity += saleQuantity;
                    }
                }

                entries.add(new Entry(i, salesQuantity));

                calendar.add(Calendar.DAY_OF_YEAR, -1); // Decrement the calendar by 1 day
            }

            // Reverse the entries and labels lists to display today's data on the rightmost side
            Collections.reverse(entries);
            Collections.reverse(labels);

            // Create the line dataset
            LineDataSet lineDataSet = new LineDataSet(entries, "Sales Quantity");
            lineDataSet.setColor(Color.rgb(104, 241, 175));
            lineDataSet.setCircleColor(Color.rgb(104, 241, 175));
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleRadius(4f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setValueTextSize(12f);
            lineDataSet.setDrawValues(true);

            LineData lineData = new LineData(lineDataSet);

            // Customize the line chart appearance
            lineChart.setData(lineData);
            lineChart.getDescription().setEnabled(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            lineChart.getAxisRight().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.setTouchEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setPinchZoom(true);

            // Set the line chart as the content view
            lineChart.invalidate();
        }
        private void createProductSalesBarChart(List<HashMap<String, Object>> salesList, String productName) {
            BarChart barChart = findViewById(R.id.barChart2);

            List<BarEntry> entries = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            List<Integer> colors = new ArrayList<>(); // List to store different colors for bars

            // Calculate sales quantity for the product within the past week
            HashMap<String, Float> productSalesQuantityMap = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();

            for (int i = 6; i >= 0; i--) {
                Date date = calendar.getTime();
                String formattedDate = dateFormat.format(date);
                String dayName = dayFormat.format(date);

                labels.add(dayName);

                // Get the sales quantity for the product on the current date
                float salesQuantity = 0f;
                for (HashMap<String, Object> sale : salesList) {
                    String saleProductName = (String) sale.get("productName");
                    String saleDate = (String) sale.get("date");
                    float saleQuantity = ((Number) sale.get("quantity")).floatValue();

                    if (productName.equals(saleProductName) && formattedDate.equals(saleDate)) {
                        salesQuantity += saleQuantity;
                    }
                }

                entries.add(new BarEntry(i, salesQuantity));

                // Assign a different color to each bar
                int color = getRandomColor();
                colors.add(color);

                calendar.add(Calendar.DAY_OF_YEAR, -1); // Decrement the calendar by 1 day
            }

            // Reverse the entries, labels, and colors lists to display today's data on the rightmost side
            Collections.reverse(entries);
            Collections.reverse(labels);
            Collections.reverse(colors);

            // Create the bar dataset
            BarDataSet barDataSet = new BarDataSet(entries, "Sales Quantity");
            barDataSet.setColors(colors); // Set the colors for the bars

            BarData barData = new BarData(barDataSet);

            // Customize the bar chart appearance
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            barChart.getAxisRight().setEnabled(false);
            barChart.getLegend().setEnabled(false);
            barChart.setTouchEnabled(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
            barChart.setPinchZoom(true);

            // Set the bar chart as the content view
            barChart.invalidate();
        }

        // Helper method to generate a random color
        private int getRandomColor() {
            Random rnd = new Random();
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }






    }

}