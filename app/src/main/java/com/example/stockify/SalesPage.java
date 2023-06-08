package com.example.stockify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class SalesPage extends AppCompatActivity {

    String ids;
    DrawerLayout drawerLayout;
    Spinner salesSpin;
    TextView dname,demail;
    String uname,uemail;
    ImageView menu;
    ListView salesListView;
    Button generatePDFButton ;

    SpinKitView spinnerSalesPage;
    LinearLayout home,addProduct,products,addSale,sales,about,logout,reports;
     public  SalesPage(){
        System.out.println("xcvcx");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_page);
        salesSpin=findViewById(R.id.spinnerSales);
        salesListView = findViewById(R.id.salesListView);
        drawerLayout=findViewById(R.id.drawerLayout);
        generatePDFButton = findViewById(R.id.downloadPDFButton);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        addProduct=findViewById(R.id.addProduct);
        products=findViewById(R.id.products);
        addSale=findViewById(R.id.addSales);
        sales=findViewById(R.id.salesT);
        about=findViewById(R.id.about);
        logout=findViewById(R.id.logout);
        reports=findViewById(R.id.reports);
        spinnerSalesPage= findViewById(R.id.spinnerSalesPage);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinnerSalesPage.setIndeterminateDrawable(threeBounce);
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
                redirectActivity(SalesPage.this,MainActivity.class,ids,uname,uemail);
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SalesPage.this,AddProduct.class,ids,uname,uemail);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SalesPage.this,Products.class,ids,uname,uemail);
            }
        });
        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SalesPage.this,AddSales.class,ids,uname,uemail);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SalesPage.this,About.class,ids,uname,uemail);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SalesPage.this,Reports.class,ids,uname,uemail);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SalesPage.this,Login.class,ids,uname,uemail);
            }
        });

        setSpinnerVisible(true);
        DatabaseReference salesRef = FirebaseDatabase.getInstance().getReference().child("Users").child(ids).child("Sales");
        FetchSalesTask fetchSalesTask = new FetchSalesTask(salesRef);
        fetchSalesTask.execute();

    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity acitivity,Class secondActivity,String s,String n,String e){
        Intent intent=new Intent(acitivity,secondActivity);
        intent.putExtra("userId",s);
        intent.putExtra("name",n);
        intent.putExtra("email",e);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        acitivity.startActivity(intent);
        acitivity.finish();
    }

    private void setSpinnerVisible(boolean visible) {
        if (visible) {
            spinnerSalesPage.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinnerSalesPage.setVisibility(View.GONE);
            // Remove the content blur
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout) ;
    }


    public class FetchSalesTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

        private DatabaseReference salesRef;

        public FetchSalesTask(DatabaseReference salesRef) {
            this.salesRef = salesRef;
        }

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(Void... voids) {
            ArrayList<HashMap<String, Object>> salesList = new ArrayList<>();

            try {
                DataSnapshot dataSnapshot = Tasks.await(salesRef.get());
                for (DataSnapshot saleSnapshot : dataSnapshot.getChildren()) {
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
            } catch (ExecutionException | InterruptedException e) {
                // Handle exception
            }

            return salesList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> salesList) {

            setSpinnerVisible(false);

            SalesAdapter salesAdapters = new SalesAdapter(SalesPage.this, R.layout.sales_row, salesList);
            salesListView.setAdapter(salesAdapters);

            String[] salesArray = {"All Sales", "Today's Sales", "Week Sales", "Month Sales", "High To Low Revenue", "Low To High Revenue", "High To Low Quantity", "Low To High Quantity"};
// Create the adapter
            ArrayAdapter<String> salesAdapter = new ArrayAdapter<>(SalesPage.this, android.R.layout.simple_spinner_item, salesArray);
            salesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Attach
            salesSpin.setAdapter(salesAdapter);

            salesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedType=salesSpin.getSelectedItem().toString();
                    if(selectedType.equals("All Sales")){
                        SalesAdapter salesAdapter = new SalesAdapter(SalesPage.this, R.layout.sales_row, salesList);
                        salesListView.setAdapter(salesAdapter);
                    }
                    else if (selectedType.equals("Today's Sales")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForToday = filterSalesByToday(salesList);
                        SalesAdapter salesAdapterToday = new SalesAdapter(SalesPage.this, R.layout.sales_row, filteredSalesListForToday);
                        salesListView.setAdapter(salesAdapterToday);
                    }
                    else if (selectedType.equals("Week Sales")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForWeek = filterSalesByWeek(salesList);
                        SalesAdapter salesAdapterWeek = new SalesAdapter(SalesPage.this, R.layout.sales_row, filteredSalesListForWeek);
                        salesListView.setAdapter(salesAdapterWeek);
                    }
                    else if (selectedType.equals("Month Sales")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForMonth= filterSalesByMonth(salesList);
                        SalesAdapter salesAdapterMonth = new SalesAdapter(SalesPage.this, R.layout.sales_row, filteredSalesListForMonth);
                        salesListView.setAdapter(salesAdapterMonth);
                    }
                    else if (selectedType.equals("High To Low Revenue")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForHighToLowRevenue= filterAndSortByRevenueHighToLow(salesList);
                        SalesAdapter salesAdapterHighToLowRevenue = new SalesAdapter(SalesPage.this, R.layout.sales_row, filteredSalesListForHighToLowRevenue);
                        salesListView.setAdapter(salesAdapterHighToLowRevenue);
                    }
                    else if (selectedType.equals("Low To High Revenue")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForLowToHighRevenue= filterAndSortByRevenueLowToHigh(salesList);
                        SalesAdapter salesAdapterHighToLowRevenue = new SalesAdapter(SalesPage.this, R.layout.sales_row, filteredSalesListForLowToHighRevenue);
                        salesListView.setAdapter(salesAdapterHighToLowRevenue);

                    }
                    else if (selectedType.equals("High To Low Quantity")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForHighToLowQuantity= filterAndSortByQuantityHighToLow(salesList);
                        SalesAdapter salesAdapterHighToQuantity = new SalesAdapter(SalesPage.this, R.layout.sales_row, filteredSalesListForHighToLowQuantity);
                        salesListView.setAdapter(salesAdapterHighToQuantity);

                    }
                    else if (selectedType.equals("Low To High Quantity")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForLowToHighQuantity= filterAndSortByQuantityLowToHigh(salesList);
                        SalesAdapter salesAdapterLowToHighQuantity = new SalesAdapter(SalesPage.this, R.layout.sales_row, filteredSalesListForLowToHighQuantity);
                        salesListView.setAdapter(salesAdapterLowToHighQuantity);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            generatePDFButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedType=salesSpin.getSelectedItem().toString();
                    if(selectedType.equals("All Sales")){
                        PDFGenerator.generatePDF(SalesPage.this, salesList);
                    }
                    else if (selectedType.equals("Today's Sales")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForToday = filterSalesByToday(salesList);
                        PDFGenerator.generatePDF(SalesPage.this, filteredSalesListForToday);
                    }
                    else if (selectedType.equals("Week Sales")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForWeek = filterSalesByWeek(salesList);
                        PDFGenerator.generatePDF(SalesPage.this, filteredSalesListForWeek);
                    }
                    else if (selectedType.equals("Month Sales")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForMonth= filterSalesByMonth(salesList);
                        PDFGenerator.generatePDF(SalesPage.this, filteredSalesListForMonth);
                    }
                    else if (selectedType.equals("High To Low Revenue")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForHighToLowRevenue= filterAndSortByRevenueHighToLow(salesList);
                        PDFGenerator.generatePDF(SalesPage.this, filteredSalesListForHighToLowRevenue);
                    }
                    else if (selectedType.equals("Low To High Revenue")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForLowToHighRevenue= filterAndSortByRevenueLowToHigh(salesList);
                        PDFGenerator.generatePDF(SalesPage.this, filteredSalesListForLowToHighRevenue);

                    }
                    else if (selectedType.equals("High To Low Quantity")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForHighToLowQuantity= filterAndSortByQuantityHighToLow(salesList);
                        PDFGenerator.generatePDF(SalesPage.this, filteredSalesListForHighToLowQuantity);

                    }
                    else if (selectedType.equals("Low To High Quantity")) {
                        ArrayList<HashMap<String, Object>> filteredSalesListForLowToHighQuantity= filterAndSortByQuantityLowToHigh(salesList);
                        PDFGenerator.generatePDF(SalesPage.this, filteredSalesListForLowToHighQuantity
                        );
                    }

                }
            });

            // Use the salesList here or notify any adapters/observers of the data change

        }

        public ArrayList<HashMap<String, Object>> filterSalesByToday(ArrayList<HashMap<String, Object>> salesList) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            // Get today's date as a string
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String todayDate = dateFormat.format(new Date());

            // Iterate over each sale in the salesList
            for (HashMap<String, Object> sale : salesList) {
                String saleDate = (String) sale.get("date");

                // Compare the sale's date with today's date
                if (saleDate != null && saleDate.equals(todayDate)) {
                    // Add the sale to the filtered list
                    filteredList.add(sale);
                }
            }

            return filteredList;
        }

        public ArrayList<HashMap<String, Object>> filterSalesByWeek(ArrayList<HashMap<String, Object>> salesList) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            // Get today's date
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();

            // Calculate the date 7 days ago
            calendar.add(Calendar.DATE, -7);
            Date sevenDaysAgo = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Iterate over each sale in the salesList
            for (HashMap<String, Object> sale : salesList) {
                String saleDate = (String) sale.get("date");

                try {
                    Date date = dateFormat.parse(saleDate);

                    // Check if the sale date falls within the last 7 days
                    if (date != null && date.after(sevenDaysAgo) && date.before(today)) {
                        // Add the sale to the filtered list
                        filteredList.add(sale);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return filteredList;
        }

        public ArrayList<HashMap<String, Object>> filterSalesByMonth(ArrayList<HashMap<String, Object>> salesList) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            // Get today's date
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();

            // Calculate the date 30 days ago
            calendar.add(Calendar.DATE, -30);
            Date thirtyDaysAgo = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Iterate over each sale in the salesList
            for (HashMap<String, Object> sale : salesList) {
                String saleDate = (String) sale.get("date");

                try {
                    Date date = dateFormat.parse(saleDate);

                    // Check if the sale date falls within the last 30 days
                    if (date != null && date.after(thirtyDaysAgo) && date.before(today)) {
                        // Add the sale to the filtered list
                        filteredList.add(sale);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return filteredList;
        }

        public ArrayList<HashMap<String, Object>> filterAndSortByRevenueHighToLow(ArrayList<HashMap<String, Object>> salesList) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            // Filter the salesList to exclude any entries with null revenue
            for (HashMap<String, Object> sale : salesList) {
                if (sale.get("revenue") != null) {
                    filteredList.add(sale);
                }
            }

            // Sort the filteredList by revenue in descending order
            Collections.sort(filteredList, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> sale1, HashMap<String, Object> sale2) {
                    double revenue1 = (double) sale1.get("revenue");
                    double revenue2 = (double) sale2.get("revenue");

                    // Sort in descending order (high to low revenue)
                    return Double.compare(revenue2, revenue1);
                }
            });

            return filteredList;
        }

        public ArrayList<HashMap<String, Object>> filterAndSortByRevenueLowToHigh(ArrayList<HashMap<String, Object>> salesList) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            // Filter the salesList to exclude any entries with null revenue
            for (HashMap<String, Object> sale : salesList) {
                if (sale.get("revenue") != null) {
                    filteredList.add(sale);
                }
            }

            // Sort the filteredList by revenue in ascending order
            Collections.sort(filteredList, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> sale1, HashMap<String, Object> sale2) {
                    double revenue1 = (double) sale1.get("revenue");
                    double revenue2 = (double) sale2.get("revenue");

                    // Sort in ascending order (low to high revenue)
                    return Double.compare(revenue1, revenue2);
                }
            });

            return filteredList;
        }
        public ArrayList<HashMap<String, Object>> filterAndSortByQuantityHighToLow(ArrayList<HashMap<String, Object>> salesList) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            // Filter the salesList to exclude any entries with null quantity
            for (HashMap<String, Object> sale : salesList) {
                if (sale.get("quantity") != null) {
                    filteredList.add(sale);
                }
            }

            // Sort the filteredList by quantity in descending order
            Collections.sort(filteredList, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> sale1, HashMap<String, Object> sale2) {
                    int quantity1 = (int) sale1.get("quantity");
                    int quantity2 = (int) sale2.get("quantity");

                    // Sort in descending order (high to low quantity)
                    return Integer.compare(quantity2, quantity1);
                }
            });

            return filteredList;
        }
        public ArrayList<HashMap<String, Object>> filterAndSortByQuantityLowToHigh(ArrayList<HashMap<String, Object>> salesList) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            // Filter the salesList to exclude any entries with null quantity
            for (HashMap<String, Object> sale : salesList) {
                if (sale.get("quantity") != null) {
                    filteredList.add(sale);
                }
            }

            // Sort the filteredList by quantity in ascending order
            Collections.sort(filteredList, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> sale1, HashMap<String, Object> sale2) {
                    int quantity1 = (int) sale1.get("quantity");
                    int quantity2 = (int) sale2.get("quantity");

                    // Sort in ascending order (low to high quantity)
                    return Integer.compare(quantity1, quantity2);
                }
            });

            return filteredList;
        }
    }
}