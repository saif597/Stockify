package com.example.stockify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {

    String imageUrl;
    String ids,productName,description,quantity,price,discount;
    DrawerLayout drawerLayout;
    SpinKitView spinnerAddProduct;
    TextView dname,demail;
    String uname,uemail;
    ImageView menu;

    Button addProductButton;
    TextInputEditText pName,pDescription,pQuantity,pPrice,editTextDiscount;
    Spinner categorySpinner;


    String categorySelected;
    ArrayList<String> items=new ArrayList<String>();

    Uri selectedImageUri;
    LinearLayout home,addProduct,products,addSale,sales,about,logout,reports;

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private Button buttonSelectImage;
    private ImageView imageViewSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        addProductButton=findViewById(R.id.buttonAddProduct);
        addProduct=findViewById(R.id.addProduct);
        products=findViewById(R.id.products);
        spinnerAddProduct = findViewById(R.id.spinnerAddProduct);
        addSale=findViewById(R.id.addSales);
        sales=findViewById(R.id.salesT);
        pName=findViewById(R.id.editTextProductName);
        pDescription=findViewById(R.id.editTextDescription);
        pQuantity=findViewById(R.id.editTextQuantity);
        pPrice=findViewById(R.id.editTextPrice);
        categorySpinner = findViewById(R.id.spinnerCategory);
        editTextDiscount = findViewById(R.id.editTextDiscount);
        about=findViewById(R.id.about);
        logout=findViewById(R.id.logout);
        reports=findViewById(R.id.reports);
        buttonSelectImage = findViewById(R.id.buttonAddProductImage);
        imageViewSelectedImage = findViewById(R.id.imageViewProduct);
        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.dblue)); // Change color to desired value
        spinnerAddProduct.setIndeterminateDrawable(threeBounce);

        // Create an ArrayAdapter for the categories array and set it to the spinner

        items.add("Category");
        items.add("Watches");
        items.add("Bags");
        items.add("Men's Fashion");
        items.add("Women's Fashion");
        items.add("Home Appliances");
        items.add("Sports");
        items.add("Electronic Devices");
        items.add("Grocery");
        items.add("Health");
        items.add("Beauty");

        categorySpinner.setAdapter(new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,items));
        categorySpinner.setOnItemSelectedListener(new
                                                  AdapterView.OnItemSelectedListener() {
                                                      @Override
                                                      public void onItemSelected(AdapterView<?> adapterView, View
                                                              view, int i, long l) {
                                                          categorySelected = categorySpinner.getItemAtPosition(i).toString();
                                                          if(categorySelected.equals("Category")){
                                                              Toast.makeText(AddProduct.this, "Select a Category please", Toast.LENGTH_SHORT).show();
                                                          }
                                                          Toast.makeText(AddProduct.this, categorySelected, Toast.LENGTH_SHORT).show();

                                                      }
                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> adapterView) {
                                                      }
                                                  });

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
                redirectActivity(AddProduct.this,MainActivity.class,ids,uname,uemail);
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddProduct.this,Products.class,ids,uname,uemail);
            }
        });
        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddProduct.this,AddSales.class,ids,uname,uemail);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddProduct.this, SalesPage.class,ids,uname,uemail);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddProduct.this,About.class,ids,uname,uemail);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddProduct.this,Reports.class,ids,uname,uemail);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AddProduct.this,Login.class,ids,uname,uemail);
            }
        });




        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
            }
        });
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImageUri!=null) {
                    imageUrl = selectedImageUri.toString();
                }
                if (TextUtils.isEmpty(pName.getText().toString())){
                    Toast.makeText(AddProduct.this, "Enter Product name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pDescription.getText().toString())){
                    Toast.makeText(AddProduct.this, "Enter Product Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedImageUri==null ){
                    Toast.makeText(AddProduct.this, "select an image please", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pQuantity.getText().toString())){
                    Toast.makeText(AddProduct.this, "Enter Product Quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pPrice.getText().toString())){
                    Toast.makeText(AddProduct.this, "Enter Price Per Unit of Product", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(editTextDiscount.getText().toString())){
                    Toast.makeText(AddProduct.this, "Enter Discount if not Enter 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (imageUrl.isEmpty()){
                    Toast.makeText(AddProduct.this, "Please Upload Product Image", Toast.LENGTH_SHORT).show();
                return;
                }
                else if (categorySelected.equals("Category")) {
                    Toast.makeText(AddProduct.this, "Select Product Category", Toast.LENGTH_SHORT).show();
                return;
                }
//                }
                else{


                    setSpinnerVisible(true);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRef = database.getReference().child("Users").child(ids);
                DatabaseReference newProductRef = userRef.child("products").push();
                String productId = newProductRef.getKey();
                productName=pName.getText().toString();
                description=pDescription.getText().toString();
                quantity=pQuantity.getText().toString();
                price=pPrice.getText().toString();
                discount=editTextDiscount.getText().toString();




                    System.out.println("xxxyyy" + categorySelected + "xxx" + discount);
                    ProductHelperClass product = new ProductHelperClass();
                    product.setName(productName);
                    product.setDiscount(Integer.parseInt(discount));
                    product.setCategory(categorySelected);
                    product.setDescription(description);
                    product.setQuantity(Integer.parseInt(quantity));
                    product.setPrice(Integer.parseInt(price));
                    product.setImageUrl(imageUrl);


                    newProductRef.setValue(product);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("productImages").child(productId + ".jpg");

                    UploadTask uploadTask = storageRef.putFile(selectedImageUri);
                    setSpinnerVisible(true);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL for the uploaded image
                            Task<Uri> downloadUrlTask = storageRef.getDownloadUrl();
                            downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    // Save the image URL to the product node in the database
                                    newProductRef.child("imageUrl").setValue(downloadUrl.toString());
                                    Intent i = new Intent(AddProduct.this, MainActivity.class);
                                    i.putExtra("userId", ids);
                                    i.putExtra("name",uname);
                                    i.putExtra("email",uemail);
                                    startActivity(i);
                                }
                            });
                        }
                    });

                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            System.out.println("uuu"+selectedImageUri);
            imageViewSelectedImage.setImageURI(selectedImageUri);
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
    public static void redirectActivity(Activity acitivity,Class secondActivity,String s,String n, String e){
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
            spinnerAddProduct.setVisibility(View.VISIBLE);
            // Blur the content
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            spinnerAddProduct.setVisibility(View.GONE);
            // Remove the content blur
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout) ;
    }
}