package com.example.stockify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class AddSales extends AppCompatActivity {
    String ids;
    DrawerLayout drawerLayout;
    SpinKitView spinnerSaleP;
    SpinKitView spinnerAddSale;
    TextView dname,demail;
    String uname,uemail;
    ImageView menu;
    Spinner productSpinner;
    LinearLayout home,addProduct,products,addSale,sales,about,logout,reports;
    TextInputEditText IntakePrice,QuantityAvaialable,SaleQuantity,SalePrice,Discount;
    Button addSaleButton;
    Boolean selectedTrue=false;
    String productKey,name,description,quantity,category,discount,imageUrl,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
        spinnerSaleP = findViewById(R.id.spinnerSaleP);
        spinnerAddSale = findViewById(R.id.spinnerAddSale);
        IntakePrice=findViewById(R.id.editTextIntakePrice);
        IntakePrice.setEnabled(false);
        QuantityAvaialable=findViewById(R.id.editTextQuantityAvailable);
        QuantityAvaialable.setEnabled(false);
        SaleQuantity=findViewById(R.id.editTextSaleQuantity);
        SalePrice=findViewById(R.id.editTextSalePrice);
        Discount=findViewById(R.id.editTextDiscount);
        Discount.setEnabled(false);
        addSaleButton=findViewById(R.id.buttonAddSale);
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        addProduct=findViewById(R.id.addProduct);
        products=findViewById(R.id.products);
        addSale=findViewById(R.id.addSales);
        sales=findViewById(R.id.salesT);
        about=findViewById(R.id.about);
        logout=findViewById(R.id.logout);
        productSpinner=findViewById(R.id.spinnerProduct);
        reports=findViewById(R.id.reports);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinnerSaleP.setIndeterminateDrawable(threeBounce);
        spinnerAddSale.setIndeterminateDrawable(threeBounce);
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
                redirectActivity(AddSales.this,MainActivity.class,ids,uname,uemail);
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddSales.this,AddProduct.class,ids,uname,uemail);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddSales.this,Products.class,ids,uname,uemail);
            }
        });
        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddSales.this, SalesPage.class,ids,uname,uemail);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddSales.this,About.class,ids,uname,uemail);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddSales.this,Reports.class,ids,uname,uemail);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddSales.this,Login.class,ids,uname,uemail);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        DatabaseReference productsRef = usersRef.child(ids).child("products");
        new AddSales.FetchProductsForSaleTask(productsRef).execute();
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
    private void setSpinnerVisible(final boolean visible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visible) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    spinnerSaleP.setVisibility(View.VISIBLE);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    spinnerSaleP.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setSpinnerVisibleTwo(boolean visible) {
        if (visible) {
            spinnerAddSale.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinnerAddSale.setVisibility(View.GONE);
            // Remove the content blur
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout) ;
    }

    private class FetchProductsForSaleTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {
        private DatabaseReference mProductsRef;

        public FetchProductsForSaleTask(DatabaseReference productsRef) {
            mProductsRef = productsRef;
        }

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(Void... voids) {
            setSpinnerVisible(true);
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
            String [] spinArray=new String[productList.size()];
            for (int x = 0; x < productList.size(); x++) {
                spinArray[x]=productList.get(x).get("name").toString();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddSales.this, android.R.layout.simple_spinner_item, spinArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productSpinner.setAdapter(adapter);
            productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    
                    String productNameSelected=productSpinner.getSelectedItem().toString();
                   
                    for (int j = 0; j <productList.size() ; j++) {
                        HashMap<String, Object> product = new HashMap<>();
                        product=productList.get(j);
                        if (product.get("name").equals(productNameSelected)){
                            productKey=product.get("productKey").toString();
                            name=product.get("name").toString();
                            description=product.get("description").toString();
                            quantity=product.get("quantity").toString();
                            category=product.get("category").toString();
                            imageUrl=product.get("imageUrl").toString();
                            discount=product.get("discount").toString();
                            price=product.get("price").toString();
                            QuantityAvaialable.setText(quantity);
                            Discount.setText(discount);
                            IntakePrice.setText(price);
                            selectedTrue=true;
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            addSaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedTrue){
                        boolean priceOk=true;
                        boolean quantityOk=true;
                       if (!TextUtils.isEmpty(SaleQuantity.getText().toString())&&!TextUtils.isEmpty(SalePrice.getText().toString())){
                           
                        double salePrice=Double.parseDouble(SalePrice.getText().toString());

                  int saleQuantity=Integer.parseInt(SaleQuantity.getText().toString());
                           double sp=salePrice;

                           int sq=saleQuantity;
                           if(sp<Double.parseDouble(price)){
                               Toast.makeText(AddSales.this, "Sale price cannot be less then Intake Price", Toast.LENGTH_SHORT).show();
                               priceOk=false;

                           }
                           if (sq>Integer.parseInt(quantity)){
                               Toast.makeText(AddSales.this, "Quantity Not Available", Toast.LENGTH_SHORT).show();
                               quantityOk=false;
                             }
                           if (quantityOk&&priceOk){
                               setSpinnerVisibleTwo(true);
                               FirebaseDatabase database = FirebaseDatabase.getInstance();
                               String path = "Users/" + ids + "/products/" + productKey;
                               DatabaseReference productRef = database.getReference(path);
                               String pname=name;
                               String pdescription=description;
                               int pquantity=Integer.parseInt(quantity)-sq;
                               double pprice=Double.parseDouble(price);
                               String imgurl=imageUrl;
                               int disc=Integer.parseInt(discount);
                               String pcategry=category;


                               ProductHelperClass product=new ProductHelperClass(pname,pdescription,pquantity,pprice,imgurl,disc,pcategry);


                               productRef.setValue(product,new DatabaseReference.CompletionListener(){

                                   @Override
                                   public void onComplete(@androidx.annotation.Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                       setSpinnerVisibleTwo(false);
                                       if (error == null) {

                                           FirebaseDatabase database = FirebaseDatabase.getInstance();
                                           DatabaseReference usersRef = database.getReference("Users");
                                           String userId = ids;
                                           String productId =productKey;
                                           int squantity = sq;
                                           String date="";
                                           double t=(salePrice*sq);
                                           double d=((Double.parseDouble(discount))/100)*t;
                                           System.out.println("salesalesale"+d);
                                           double revenue = (salePrice*sq)-d;
                                           double profit=revenue-(pprice*sq);
                                           LocalDate today = null;

                                           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                               today = LocalDate.now();
                                           }
                                           DateTimeFormatter formatter = null;
                                           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                               formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                           }
                                           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                               date= today.format(formatter);
                                           }
                                           String productName =name;
                                           double totalPrice = salePrice * sq;

                                           DatabaseReference salesRef = usersRef.child(userId).child("Sales").push();
                                           String saleId = salesRef.getKey();

                                           // Create a Sale object to hold the sale data
                                           SaleHelperClass sale = new SaleHelperClass(productId, squantity, revenue, date, productName, totalPrice,profit);

                                           // Set the sale details
                                           salesRef.setValue(sale);
                                           Toast.makeText(AddSales.this, "success", Toast.LENGTH_SHORT).show();
                                           Intent i=new Intent(AddSales.this, SalesPage.class);
                                           i.putExtra("userId",ids);
                                           i.putExtra("name",uname);
                                           i.putExtra("email",uemail);
                                           startActivity(i);
                                       } else {

                                           Toast.makeText(AddSales.this, "Error", Toast.LENGTH_SHORT).show();                        }
                                   }
                               });

                           } else if (!quantityOk&&!priceOk) {
                               Toast.makeText(AddSales.this, "Quantity not available and price not valid", Toast.LENGTH_SHORT).show();
                           }

                       }
                       else if(TextUtils.isEmpty(SaleQuantity.getText().toString()) || TextUtils.isEmpty(SalePrice.getText().toString())){

                           Toast.makeText(AddSales.this, "Fill Complete Sale Details", Toast.LENGTH_SHORT).show();
                       }
                        
                    } else if (!selectedTrue) {
                        Toast.makeText(AddSales.this, "SelectProduct", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}