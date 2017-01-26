package com.monash.ZweiSteele;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.TreeMap;

/**
 * Created by Kin To Pang on 15/06/14.
 * http://www.mysamplecode.com/2012/07/android-listview-custom-layout-filter.html
 * http://nickcharlton.net/posts/building-custom-android-listviews.html
 */
public class CardListAdapter extends BaseAdapter implements Filterable {

    private TreeMap<String, String[]> data = new TreeMap<String, String[]>();
    private TreeMap<String, String[]> originalData = new TreeMap<String, String[]>();
    // store the context (as an inflated layout)
    private LayoutInflater inflater;
    // store the resource (typically list_item.xml)
    private int resource;
    private String[] keys;
    private CardListFilter cardListFilter;

    public CardListAdapter(Context context, int resource, TreeMap<String, String[]> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.data = data;
        this.originalData = data;
        keys = this.data.keySet().toArray(new String[data.size()]);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String[] getItem(int position) {
        return data.get(keys[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        if (cardListFilter == null) {

            cardListFilter = new CardListFilter();
        }

        return cardListFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // reuse a given view, or inflate a new one from the xml
        View view;

        if (convertView == null) {
            view = this.inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        // bind the data to the view object
        return this.bindData(view, position);
    }

    public View bindData(View view, int position) {

        String key = keys[position];
        String[] value = getItem(position);


        // make sure it's worth drawing the view
        if (value == null) {
            return view;
        }
        String[] details = value[0].split("/");
        // extract the view object
        TextView cardNameEngtv = (TextView) view.findViewById(R.id.cardList_cardNameEng);
        // set the value
        cardNameEngtv.setText(details[2]);

        TextView cardNameJptv = (TextView) view.findViewById(R.id.cardList_cardNameJp);
        cardNameJptv.setText(details[3]);

        TextView cardIDtv = (TextView) view.findViewById(R.id.cardList_cardID);
        cardIDtv.setText(key);

        TextView cardTypetv = (TextView) view.findViewById(R.id.cardList_cardType);
        cardTypetv.setText(details[4]);

        LinearLayout row = (LinearLayout) view.findViewById(R.id.cardList_ItemRow);
        row.setBackgroundColor(Color.parseColor("#" + details[5]));

        // return the final view object
        return view;
    }

    private class CardListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                TreeMap<String, String[]> filterList = new TreeMap<String, String[]>();
                for (String key : originalData.keySet()) {
                    //get the card name from the data string
                    String name = originalData.get(key)[0].split("/")[0];
                    //match either the card serial or card name from user input
                    if (key.toUpperCase().contains(constraint.toString().toUpperCase()) || name.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.put(key, originalData.get(key));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = originalData.size();
                results.values = originalData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (TreeMap<String, String[]>) results.values;
            keys = data.keySet().toArray(new String[data.size()]);
            notifyDataSetChanged();
            notifyDataSetInvalidated();
        }
    }
}
