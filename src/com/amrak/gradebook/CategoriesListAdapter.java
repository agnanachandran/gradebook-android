package com.amrak.gradebook;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoriesListAdapter extends BaseAdapter {
	
	// context
	private Context context;

	// views
	private LayoutInflater inflater;

	// data
	private ArrayList<CategoryData> categories;
	
	public CategoriesListAdapter(Context context, ArrayList<CategoryData> categories) {
		this.context = context;
		this.categories = categories;
		inflater = LayoutInflater.from(context);
	}
	
	public int getCount() {
		return categories.size();
	}

	public Object getItem(int position) {
		return categories.get(position);
	}

	public long getItemId(int position) {
		return (long) (position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_categories, parent, false);
		}

		CategoryData categories = (CategoryData) getItem(position);
		TextView title = (TextView) v.findViewById(R.id.tvCategoryTitle);
		TextView mark = (TextView) v.findViewById(R.id.tvCategoryMark);
		TextView weight = (TextView) v.findViewById(R.id.tvCategoryWeight);

		title.setText(categories.getTitle());
		mark.setText(String.valueOf(categories.getMark()));
		weight.setText(String.valueOf(categories.getWeight()));
		return v;

	}

}