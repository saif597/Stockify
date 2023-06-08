    package com.example.stockify;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.view.GravityCompat;
    import androidx.drawerlayout.widget.DrawerLayout;

    import android.app.Activity;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.content.pm.ResolveInfo;
    import android.net.Uri;
    import android.os.Bundle;
    import android.view.View;
    import android.webkit.WebView;
    import android.webkit.WebViewClient;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.maps.MapView;
    import com.google.android.gms.maps.OnMapReadyCallback;
    import com.google.android.gms.maps.GoogleMap;
    import com.google.android.gms.maps.CameraUpdateFactory;
    import com.google.android.gms.maps.model.LatLng;
    import com.google.android.gms.maps.model.MarkerOptions;

    import java.util.List;

    public class About extends AppCompatActivity implements OnMapReadyCallback {

    String ids;
        DrawerLayout drawerLayout;

        TextView dname,demail;
        String uname,uemail;
        ImageView menu;
        WebView webView;
        private MapView mapView;
        private GoogleMap googleMap;
        private final LatLng destinationLatLng = new LatLng(33.5695366, 73.1470420);

        LinearLayout home,addProduct,products,addSale,sales,about,logout,reports;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);
            drawerLayout=findViewById(R.id.drawerLayout);
            menu=findViewById(R.id.menu);
            home=findViewById(R.id.home);
            addProduct=findViewById(R.id.addProduct);
            products=findViewById(R.id.products);
            addSale=findViewById(R.id.addSales);
            sales=findViewById(R.id.salesT);
            about=findViewById(R.id.about);
            logout=findViewById(R.id.logout);
            reports=findViewById(R.id.reports);
            webView = findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("https://mysamdbucket.s3.eu-north-1.amazonaws.com/index.html");
            mapView = findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
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
                    redirectActivity(About.this,MainActivity.class,ids,uname,uemail);
                }
            });
            addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity(About.this,AddProduct.class,ids,uname,uemail);
                }
            });
            products.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity(About.this,Products.class,ids,uname,uemail);
                }
            });
            addSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity(About.this,AddSales.class,ids,uname,uemail);
                }
            });
            sales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity(About.this, SalesPage.class,ids,uname,uemail);
                }
            });
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeDrawer(drawerLayout);
                }
            });
            reports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity(About.this,Reports.class,ids,uname,uemail);
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity(About.this,Login.class,ids,uname,uemail);
                }
            });


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
            mapView.onPause();
        }
        @Override
        public void onMapReady(GoogleMap map) {
            googleMap = map;
            googleMap.addMarker(new MarkerOptions().position(destinationLatLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 12));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    launchGoogleMaps(destinationLatLng);
                }
            });
        }

    //    private void launchGoogleMaps(LatLng destination) {
    //        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination.latitude + "," + destination.longitude);
    //        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
    //        mapIntent.setPackage("com.google.android.apps.maps");
    //
    //        PackageManager packageManager = getPackageManager();
    //        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
    //        boolean isIntentSafe = activities.size() > 0;
    //
    //        if (isIntentSafe) {
    //            startActivity(mapIntent);
    //        } else {
    //            Toast.makeText(this, "Google Maps is not installed on this device", Toast.LENGTH_SHORT).show();
    //        }
    //    }
    private void launchGoogleMaps(LatLng destination) {
        String uri = "http://maps.google.com/maps?q=" + destination.latitude + "," + destination.longitude;

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(mapIntent);
        }
    }


        @Override
        protected void onResume() {
            super.onResume();
            mapView.onResume();
        }



        @Override
        protected void onDestroy() {
            super.onDestroy();
            mapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mapView.onLowMemory();
        }

    }