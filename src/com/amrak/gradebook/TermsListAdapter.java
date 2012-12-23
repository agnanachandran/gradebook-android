package com.amrak.gradebook;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TermsListAdapter extends BaseAdapter {

	// context
	private Context context;

	// views
	private LayoutInflater inflater;

	// data
	private ArrayList<TermData> terms;

	public TermsListAdapter(Context context, ArrayList<TermData> terms) {
		this.context = context;
		this.terms = terms;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return terms.size();
	}

	public Object getItem(int position) {
		return terms.get(position);
	}

	public long getItemId(int position) {
		return (long) (position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_term, parent, false);
		}

		TermData term = (TermData) getItem(position);
		TextView title = (TextView) v.findViewById(R.id.tvTermTitle);
		TextView mark = (TextView) v.findViewById(R.id.tvTermMark);
		TextView date = (TextView) v.findViewById(R.id.tvTermDate);

		title.setText(term.getTitle());
		mark.setText(String.valueOf(term.getMark()) + " %");
		date.setText(term.getDateStart() + " - " + term.getDateEnd());
		return v;
	}

}
