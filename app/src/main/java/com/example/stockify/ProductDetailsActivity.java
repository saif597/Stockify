package com.example.stockify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    TextInputEditText pName,pDescription,pQuantity,pPrice,editTextDiscount;
    ImageView imageViewSelectedImage;
    String imgurl="";
    String uname,uemail;
    Uri selectedImageUri;
    String selectedImageUrl;
    Button updateProductButton;
    ImageView menu;
    String ids;
    private int REQUEST_CODE_SELECT_IMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        menu=findViewById(R.id.menu);
        updateProductButton=findViewById(R.id.buttonAddProduct);
        pName=findViewById(R.id.editTextProductName);
        pDescription=findViewById(R.id.editTextDescription);
        pQuantity=findViewById(R.id.editTextQuantity);
        pPrice=findViewById(R.id.editTextPrice);
        editTextDiscount = findViewById(R.id.editTextDiscount);
        imageViewSelectedImage = findViewById(R.id.imageViewProduct);

        Intent intent = getIntent();
        ids=intent.getStringExtra("ids");
        uname=intent.getStringExtra("name");
        uemail=intent.getStringExtra("email");
        String productName = intent.getStringExtra("productName");
        String productPrice = intent.getStringExtra("productPrice");
        String productDescription = intent.getStringExtra("productDescription");
        String imageUrl = intent.getStringExtra("imageUrl");
        String quantity = intent.getStringExtra("quantity");
        String discount = intent.getStringExtra("discount");
        String category = intent.getStringExtra("category");
        String productKey = intent.getStringExtra("productKey");
        pName.setText(productName);
        pQuantity.setText(quantity);
        pDescription.setText(productDescription);
        pPrice.setText(productPrice);
        editTextDiscount.setText(discount);
        Picasso.get().load(imageUrl).into(imageViewSelectedImage);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProductDetailsActivity.this,Products.class);
                i.putExtra("userId",ids);
                i.putExtra("name",uname);
                i.putExtra("email",uemail);
                startActivity(i);
            }
        });
        imageViewSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the image selection process when the ImageView is clicked
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }

        });

        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseStorage storage = FirebaseStorage.getInstance();

// Create a reference to the image file in Firebase Storage

                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("productImages").child(productKey + ".jpg");

// Upload the file to Firebase Storage
                if (TextUtils.isEmpty(pName.getText().toString())){
                    Toast.makeText(ProductDetailsActivity.this, "Enter Product name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pDescription.getText().toString())){
                    Toast.makeText(ProductDetailsActivity.this, "Enter Product Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pQuantity.getText().toString())){
                    Toast.makeText(ProductDetailsActivity.this, "Enter Product Quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pPrice.getText().toString())){
                    Toast.makeText(ProductDetailsActivity.this, "Enter Price Per Unit of Product", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(editTextDiscount.getText().toString())){
                    Toast.makeText(ProductDetailsActivity.this, "Enter Discount if not Enter 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    if(selectedImageUrl!=null && !selectedImageUrl.isEmpty()){
                        UploadTask uploadTask = storageRef.putFile(selectedImageUri);

                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            // Image uploaded successfully, retrieve the download URL
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                imgurl = uri.toString();

                                // Store the image URL in the Realtime Database
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String path = "Users/" + ids + "/products/" + productKey;
                                DatabaseReference productRef = database.getReference(path);
                                String pname=pName.getText().toString();
                                String pdescription=pDescription.getText().toString();
                                int quantity=Integer.parseInt(pQuantity.getText().toString());
                                double pprice=Double.parseDouble(pPrice.getText().toString());

                                int disc=Integer.parseInt(editTextDiscount.getText().toString());
                                String categry=category;

                                ProductHelperClass product=new ProductHelperClass(pname,pdescription,quantity,pprice,imgurl,disc,categry);


                                productRef.setValue(product,new DatabaseReference.CompletionListener(){

                                    @Override
                                    public void onComplete(@androidx.annotation.Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null) {
                                            Toast.makeText(ProductDetailsActivity.this, "success", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(ProductDetailsActivity.this,Products.class);
                                            i.putExtra("userId",ids);
                                            i.putExtra("name",uname);
                                            i.putExtra("email",uemail);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(ProductDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();                        }
                                    }
                                });

                            });
                        }).addOnFailureListener(e -> {
                            // Handle any errors during image upload
                        });



                    }
                    else if (selectedImageUrl==null||selectedImageUrl.isEmpty()){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String path = "Users/" + ids + "/products/" + productKey;
                        DatabaseReference productRef = database.getReference(path);
                        String pname=pName.getText().toString();
                        String pdescription=pDescription.getText().toString();
                        int quantity=Integer.parseInt(pQuantity.getText().toString());
                        double pprice=Double.parseDouble(pPrice.getText().toString());

                        imgurl=imageUrl;

                        int disc=Integer.parseInt(editTextDiscount.getText().toString());
                        String categry=category;

                        ProductHelperClass product=new ProductHelperClass(pname,pdescription,quantity,pprice,imgurl,disc,categry);


                        productRef.setValue(product,new DatabaseReference.CompletionListener(){

                            @Override
                            public void onComplete(@androidx.annotation.Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
                                    Toast.makeText(ProductDetailsActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(ProductDetailsActivity.this,Products.class);
                                    i.putExtra("userId",ids);
                                    i.putExtra("name",uname);
                                    i.putExtra("email",uemail);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(ProductDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();                        }
                            }
                        });
                    }

                }


// Listen for upload success/failure


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            selectedImageUri = data.getData();

            // Generate the URL of the selected image
            String imageUrl = getRealPathFromUri(selectedImageUri);

            // Set the selected image in the ImageView
            imageViewSelectedImage.setImageURI(selectedImageUri);

            // Store the URL and URI in strings
            selectedImageUrl = imageUrl;
            String selectedImageUriString = selectedImageUri.toString();

            // Use the selectedImageUrl and selectedImageUriString as needed
            // ...
        }
    }
    private String getRealPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return "";
    }


}