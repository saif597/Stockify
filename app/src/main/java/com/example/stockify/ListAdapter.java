package com.example.stockify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListAdapter extends BaseAdapter implements Filterable {

    private ArrayList<HashMap<String, Object>> productList;
    private ArrayList<HashMap<String, Object>> filteredList;
    private LayoutInflater layoutInflater;
    private ItemFilter itemFilter;

    public ListAdapter(Context context, ArrayList<HashMap<String, Object>> productList) {
        this.productList = productList;
        this.filteredList = productList;
        this.layoutInflater = LayoutInflater.from(context);
        this.itemFilter = new ItemFilter();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.product_list_row, parent, false);
        }

        // Get references to views in the row layout
        ImageView rowImg = convertView.findViewById(R.id.rowImg);
        TextView rowName = convertView.findViewById(R.id.rowName);
        TextView rowPrice = convertView.findViewById(R.id.rowPrice);
        TextView rowQuantity = convertView.findViewById(R.id.rowQuantity);
        TextView rowCategory = convertView.findViewById(R.id.rowCategory);
        TextView rowDiscount = convertView.findViewById(R.id.rowDiscount);
        TextView rowDescription = convertView.findViewById(R.id.rowDescription);

        // Get the data for this row
        HashMap<String, Object> product = filteredList.get(position);

        String imgU = product.get("imageUrl").toString();
        Picasso.get().load(imgU).into(rowImg);
        rowName.setText(product.get("name").toString());
        rowPrice.setText(product.get("price").toString());
        rowQuantity.setText(product.get("quantity").toString());
        rowCategory.setText(product.get("category").toString());
        rowDiscount.setText(product.get("discount").toString());
        rowDescription.setText(product.get("description").toString());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<HashMap<String, Object>> filteredItems = new ArrayList<>();
                for (HashMap<String, Object> item : productList) {
                    String itemName = item.get("name").toString().toLowerCase();
                    if (itemName.contains(constraint.toString().toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                results.count = filteredItems.size();
                results.values = filteredItems;
            } else {
                results.count = productList.size();
                results.values = productList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<HashMap<String, Object>>) results.values;
            notifyDataSetChanged();
        }
    }
}
