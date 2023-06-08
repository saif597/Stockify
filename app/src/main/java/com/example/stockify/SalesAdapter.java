package com.example.stockify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SalesAdapter extends ArrayAdapter<HashMap<String, Object>> {

    private Context context;
    private int resource;
    private ArrayList<HashMap<String, Object>> salesList;

    public SalesAdapter(Context context, int resource, ArrayList<HashMap<String, Object>> salesList) {
        super(context, resource, salesList);
        this.context = context;
        this.resource = resource;
        this.salesList = salesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sales_row, parent, false);
        }

        // Get the current sale HashMap from the list
        HashMap<String, Object> sale = getItem(position);

        // Retrieve the TextViews from the convertView

        TextView nameTextView = convertView.findViewById(R.id.rowSaleName);
        TextView dateTextView = convertView.findViewById(R.id.rowSaleDate);
        TextView quantityTextView = convertView.findViewById(R.id.rowSaleQuantity);
        TextView profitTextView = convertView.findViewById(R.id.rowSaleProfit);
        TextView revenueTextView = convertView.findViewById(R.id.rowSaleRevenue);

        // Set the values from the sale HashMap to the TextViews
        nameTextView.setText(String.valueOf(sale.get("productName")));
        dateTextView.setText(String.valueOf(sale.get("date")));
        quantityTextView.setText(String.valueOf(sale.get("quantity")));
        profitTextView.setText(String.valueOf(sale.get("profit")));
        revenueTextView.setText(String.valueOf(sale.get("revenue")));

        return convertView;
    }
}

