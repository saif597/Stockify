package com.example.stockify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Random;

public class MainActivity  extends AppCompatActivity   {

    String ids;

    DrawerLayout drawerLayout;
Button addsaleBtn;
    ImageView menu;
    PieChart pieChart;
    SpinKitView spinnerMain;
    TextView dname,demail;
    String uname,uemail;
    LinearLayout home, addProduct, products, addSale, salesPage, about, logout, reports;

    TextView lowQuantityText, salesTodayText, totalProfitText, revenueText, highestQuantityText, highestSellingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        ids = i.getStringExtra("userId");
        uname=i.getStringExtra("name");
        uemail=i.getStringExtra("email");
        pieChart=findViewById(R.id.salesChartHome);
        dname=findViewById(R.id.drawerName);
        demail=findViewById(R.id.drawerEmail);
        dname.setText(uname);
        demail.setText(uemail);
        lowQuantityText = findViewById(R.id.lowQuantityText);
        spinnerMain = findViewById(R.id.spinnerMain);
        salesTodayText = findViewById(R.id.salesTodayText);
        addsaleBtn=findViewById(R.id.addSaleBtn);
        totalProfitText = findViewById(R.id.totalProfitText);
        revenueText = findViewById(R.id.totalRevenueText);
        highestQuantityText = findViewById(R.id.highestQuantityText);
        highestSellingText = findViewById(R.id.highestSellingText);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        addProduct = findViewById(R.id.addProduct);
        products = findViewById(R.id.products);
        addSale = findViewById(R.id.addSales);
        salesPage = findViewById(R.id.salesT);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);
        reports = findViewById(R.id.reports);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinnerMain.setIndeterminateDrawable(threeBounce);
        addsaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AddSales.class);
                i.putExtra("userId",ids);
                i.putExtra("name",uname);
                i.putExtra("email",uemail);
                startActivity(i);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, AddProduct.class, ids,uname,uemail);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Products.class, ids,uname,uemail);
            }
        });
        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, AddSales.class, ids,uname,uemail);
            }
        });
        salesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, SalesPage.class, ids,uname,uemail);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, About.class, ids,uname,uemail);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Reports.class, ids,uname,uemail);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Login.class, ids,uname,uemail);
            }
        });
        setSpinnerVisible(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        DatabaseReference productsRef = usersRef.child(ids).child("products");
        DatabaseReference salesRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ids).child("Sales");
        FetchDataAsyncTask asyncTask = new FetchDataAsyncTask(productsRef, salesRef);
        asyncTask.execute();

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity acitivity, Class secondActivity, String s,String n, String e) {
        Intent intent = new Intent(acitivity, secondActivity);
        intent.putExtra("userId", s);
        intent.putExtra("name",n);
        intent.putExtra("email",e);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        acitivity.startActivity(intent);
        acitivity.finish();
    }
    private void setSpinnerVisible(boolean visible) {
        if (visible) {
            spinnerMain.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinnerMain.setVisibility(View.GONE);
            // Remove the content blur
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
    private class FetchDataAsyncTask extends AsyncTask<Void, Void, Pair<List<HashMap<String, Object>>, List<HashMap<String, Object>>>> {
        private DatabaseReference productsRef;
        private DatabaseReference salesRef;

        public FetchDataAsyncTask(DatabaseReference productsRef, DatabaseReference salesRef) {
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
            List<PieEntry> pieEntryList ;
            pieEntryList=new ArrayList<>();
           //set low quantity
            int count=0;
            List<String> productsWithQuantityLess = new ArrayList<>();
            for (int i = 0; i <productList.size(); i++) {
                System.out.println("labgya"+productList.get(i).get("name"));
                System.out.println("labgya saman"+productList.get(i).get("quantity"));
                int c=Integer.parseInt(productList.get(i).get("quantity").toString());
                String s=productList.get(i).get("name").toString();
                if (c<10){
                    count++;
                     productsWithQuantityLess.add(s);
                }
            }
            System.out.println("yebhi"+count);
            String uploadCount=Integer.toString(count);
            lowQuantityText.setText(uploadCount);
            //set numbers of sale today and profit and revenue
            double totalRevenue = 0.0;
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayDate = today.format(formatter);
            double totalProfit = 0.0;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            int countSales = 0;
            for (HashMap<String, Object> sale : salesList) {
                double revenue = (double) sale.get("revenue");
                totalRevenue += revenue;
                String saleDate = (String) sale.get("date");
                if (saleDate.equals(todayDate)) {
                    countSales++;
                }
                double profit = (double) sale.get("profit");
                totalProfit += profit;
            }
            String productNameWithHighestQuantity = "";
            int highestQuantity = Integer.MIN_VALUE;

            for (HashMap<String, Object> product : productList) {
                int quantity = (int) product.get("quantity");
                if (quantity > highestQuantity) {
                    highestQuantity = quantity;
                    productNameWithHighestQuantity = (String) product.get("name");
                }
            }
            HashMap<String, Integer> productQuantities = new HashMap<>();

// Calculate total quantity for each product
            for (HashMap<String, Object> sale : salesList) {
                String productName = (String) sale.get("productName");
                int quantity = (int) sale.get("quantity");

                if (productQuantities.containsKey(productName)) {
                    int currentQuantity = productQuantities.get(productName);
                    productQuantities.put(productName, currentQuantity + quantity);
                } else {
                    productQuantities.put(productName, quantity);
                }
            }

// Find the product with the greatest quantity
            String mostSellingProductName = "";
            int maxQuantity = Integer.MIN_VALUE;

            for (String productName : productQuantities.keySet()) {
                int quantity = productQuantities.get(productName);
                if (quantity > maxQuantity) {
                    maxQuantity = quantity;
                    mostSellingProductName = productName;
                }
            }

            HashMap<String, Integer> productSalesCount = new HashMap<>();

            for (HashMap<String, Object> sale : salesList) {
                String productName = (String) sale.get("productName");
                int quantity = (int) sale.get("quantity");

                // Update the total quantity for the current product
                int totalQuantity = productSalesCount.getOrDefault(productName, 0);
                productSalesCount.put(productName, totalQuantity + quantity);
            }

            for (Map.Entry<String, Integer> entry : productSalesCount.entrySet()) {
                String productName = entry.getKey();
                int totalQuantity = entry.getValue();
                PieEntry pieEntry = new PieEntry(totalQuantity, productName);
                pieEntryList.add(pieEntry);
            }

            // Use the pieEntryList to create the pie chart
            PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Sales chart");

            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(10f);
            PieData pieData = new PieData(pieDataSet);

            Description desc=new Description();
            desc.setText("");

            pieChart.setDescription(desc);

            pieChart.setData(pieData);
            pieChart.invalidate();




            System.out.println("Most Selling Product Name: " + mostSellingProductName);
            highestSellingText.setText(mostSellingProductName);
            highestQuantityText.setText(productNameWithHighestQuantity);
            salesTodayText.setText(Integer.toString(countSales));
            totalProfit=Double.parseDouble(decimalFormat.format(totalProfit));
            totalRevenue=Double.parseDouble(decimalFormat.format(totalRevenue));
            totalProfitText.setText(Double.toString(totalProfit));
            revenueText.setText(Double.toString(totalRevenue));
//            for (int i = 0; i < salesList.size(); i++) {
//                System.out.println("khajoor"+salesList.get(i).get("date"));
//            }


            // Use the lists in your activity
            // ...
        }



    }


}
