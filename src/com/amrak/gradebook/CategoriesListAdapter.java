package com.amrak.gradebook;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
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
	
	DecimalFormat twoDForm = new DecimalFormat("0.00");
	
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
	
	public void changeColor(TextView title, TextView mark, TextView weight, int targetColor) {
		String colorCode = new String("#000000");
		if (targetColor == 0)
			colorCode = new String("#000000");
		else if (targetColor == 1)
			colorCode = new String("#B22222");
		else if (targetColor == 2)
			colorCode = new String("#FFA500");
		else if (targetColor == 3)
			colorCode = new String("#1E90FF");
		else if (targetColor == 4)
			colorCode = new String("#228B22");
		
		title.setTextColor(Color.parseColor(colorCode));
		mark.setTextColor(Color.parseColor(colorCode));
		weight.setTextColor(Color.parseColor(colorCode));
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_categories, parent, false);
		}
		
		double allWeight = 0;
		double allPercent = 0;

		CategoryData categories = (CategoryData) getItem(position);
		TextView title = (TextView) v.findViewById(R.id.tvCategoryTitle);
		TextView mark = (TextView) v.findViewById(R.id.tvCategoryMark);
		TextView weight = (TextView) v.findViewById(R.id.tvCategoryWeight);
		
		title.setText(categories.getTitle());

		if (!categories.getTitle().equals("All"))
			changeColor(title, mark, weight, categories.getColor());		
		
		// position of listview objects starts at 0 and ID starts at 1
		if (categories.getID() == 0){
			for (int i = 1; i < this.categories.size(); i++){
				allWeight +=  this.categories.get(i).getWeight();
			}
			if (allWeight > -0.001 && allWeight < 0.001)
				mark.setText(String.valueOf(100.00));
			else {
				for (int i = 1; i < this.categories.size(); i++){
					allPercent += this.categories.get(i).getMark() * (this.categories.get(i).getWeight()/allWeight);
				}
				mark.setText(String.valueOf(twoDForm.format(allPercent)));				
			}
		}
		else {
			mark.setText(String.valueOf(twoDForm.format(categories.getMark())));		
		}
		if (categories.getID() == 0){
			weight.setText("");
		}
		else {
			weight.setText(String.valueOf(categories.getWeight()));
		}
		return v;

	}

}