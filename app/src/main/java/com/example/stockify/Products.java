package com.example.stockify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Products extends AppCompatActivity {

    String ids;
    DrawerLayout drawerLayout;
    ImageView menu;
    TextView dname,demail;
    String uname,uemail;
    ListView lv;
    Spinner ctg;
    ListAdapter adapter;
    LinearLayout home,addProduct,products,addSale,sales,about,logout,reports;
    SpinKitView spinnerPP;

    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        spinnerPP = findViewById(R.id.spinnerPP);
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        addProduct=findViewById(R.id.addProduct);
        ctg=findViewById(R.id.spinnerProductCategory);
        products=findViewById(R.id.products);
        addSale=findViewById(R.id.addSales);
        search=findViewById(R.id.search);
        lv=findViewById(R.id.productsListView);
        sales=findViewById(R.id.salesT);
        about=findViewById(R.id.about);
        logout=findViewById(R.id.logout);
        reports=findViewById(R.id.reports);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinnerPP.setIndeterminateDrawable(threeBounce);
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
                redirectActivity(Products.this,MainActivity.class,ids,uname,uemail);
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Products.this,AddProduct.class,ids,uname,uemail);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Products.this,AddSales.class,ids,uname,uemail);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Products.this, SalesPage.class,ids,uname,uemail);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Products.this,About.class,ids,uname,uemail);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Products.this,Reports.class,ids,uname,uemail);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Products.this,Login.class,ids,uname,uemail);
            }
        });



        setSpinnerVisible(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        DatabaseReference productsRef = usersRef.child(ids).child("products");
        new FetchProductsTask(productsRef).execute();

    }
    private void setSpinnerVisible(boolean visible) {
        if (visible) {
            spinnerPP.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinnerPP.setVisibility(View.GONE);
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
    public static void redirectActivity(Activity acitivity,Class secondActivity,String s,String n,String e){
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
    public class FetchProductsTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {
        private DatabaseReference mProductsRef;

        public FetchProductsTask(DatabaseReference productsRef) {
            mProductsRef = productsRef;
        }

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(Void... voids) {
            ArrayList<HashMap<String, Object>> productList = new ArrayList<>();

            try {
                DataSnapshot dataSnapshot = Tasks.await(mProductsRef.get());
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
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
            } catch (ExecutionException | InterruptedException e) {
                // Handle exception
            }

            return productList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> productList) {


            setSpinnerVisible(false);
            // Do something with the productList
            adapter = new ListAdapter(Products.this, productList);
            lv.setAdapter(adapter);
            Set<String> uniqueCategories = new HashSet<>();
            uniqueCategories.add("All Categories");
            for (int x = 0; x < productList.size(); x++) {
                uniqueCategories.add(productList.get(x).get("category").toString());
            }
            List<String> categoryList = new ArrayList<>(uniqueCategories);
            ArrayAdapter<String> ad = new ArrayAdapter<>(Products.this, android.R.layout.simple_spinner_item, categoryList);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ctg.setAdapter(ad);

// Find the index of "All Categories" in the categoryList
            int defaultSelectionIndex = categoryList.indexOf("All Categories");

// Set the default selection to "All Categories"
            if (defaultSelectionIndex != -1) {
                ctg.setSelection(defaultSelectionIndex);
            }

            ctg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (ctg.getSelectedItem().toString().equals("All Categories")) {
                        adapter= new ListAdapter(Products.this, productList);
                        lv.setAdapter(adapter);
                    }
                    else if (!ctg.getSelectedItem().toString().equals("All Categories")){
                        String categoryToFilter =ctg.getSelectedItem().toString() ;
                        ArrayList<HashMap<String, Object>> filteredList = filterProductsByCategory(productList, categoryToFilter);
                        adapter = new ListAdapter(Products.this, filteredList);
                        lv.setAdapter(adapter);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Adapter a=lv.getAdapter();
                    // Retrieve the selected product from the productList using the position
                    HashMap<String, Object> product = (HashMap<String, Object>) a.getItem(position);

//                    HashMap<String, Object> product = productList.get(position);
                    // Extract the required data from the product HashMap

                    String pName =  product.get("name").toString();
                    String pPrice = product.get("price").toString();
                    String pDescription =  product.get("description").toString();
                    String pImageUrl = product.get("imageUrl").toString();
                    String pQuantity = product.get("quantity").toString();
                    String pDiscount = product.get("discount").toString();
                    String pCategory = product.get("category").toString();
                    String pProductKey = product.get("productKey").toString();


                    Intent intent = new Intent(Products.this, ProductDetailsActivity.class);

                    intent.putExtra("productName", pName);
                    intent.putExtra("productPrice", pPrice);
                    intent.putExtra("productDescription", pDescription);
                    intent.putExtra("imageUrl", pImageUrl);
                    intent.putExtra("quantity", pQuantity);
                    intent.putExtra("discount", pDiscount);
                    intent.putExtra("category", pCategory);
                    intent.putExtra("productKey", pProductKey);
                    intent.putExtra("ids",ids);
                    intent.putExtra("name",uname);
                    intent.putExtra("email",uemail);

                    startActivity(intent);


                }
            });
        }

        public  ArrayList<HashMap<String, Object>> filterProductsByCategory(ArrayList<HashMap<String, Object>> productList, String category) {
            ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();

            for (HashMap<String, Object> product : productList) {
                String productCategory = (String) product.get("category");
                if (productCategory.equals(category)) {
                    filteredList.add(product);
                }
            }

            return filteredList;
        }

    }


}
