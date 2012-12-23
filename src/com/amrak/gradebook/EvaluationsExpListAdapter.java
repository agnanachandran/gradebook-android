package com.amrak.gradebook;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class EvaluationsExpListAdapter extends BaseExpandableListAdapter {

	// context
	private Context context;

	// views
	private LayoutInflater inflater;

	// data
	private ArrayList<EvalData> eval_parent;
	private ArrayList<ArrayList<EvalData>> eval_child;
	
	DecimalFormat twoDForm = new DecimalFormat("0.00");

	public EvaluationsExpListAdapter(Context context,
			ArrayList<EvalData> eval_parent,
			ArrayList<ArrayList<EvalData>> eval_child) {
		this.context = context;
		this.eval_parent = eval_parent;
		this.eval_child = eval_child;
		inflater = LayoutInflater.from(context);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return eval_child.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return (long) (groupPosition * 1024 + childPosition); // Max 1024
																// children per
																// group
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_eval_child, parent, false);
		}
		
		EvalData evalChild = (EvalData) getChild(groupPosition, childPosition);
		TextView tvEvalChildWeight = (TextView) v.findViewById(R.id.tvEvalChildWeight);
		TextView tvEvalChildDate = (TextView) v.findViewById(R.id.tvEvalChildDate);
		TextView tvEvalChildCategory = (TextView) v.findViewById(R.id.tvEvalChildCategory);
		
		TextView tvEvalChildWeightValue = (TextView) v.findViewById(R.id.tvEvalChildWeightValue);
		TextView tvEvalChildDateValue = (TextView) v.findViewById(R.id.tvEvalChildDateValue);
		TextView tvEvalChildCategoryValue = (TextView) v.findViewById(R.id.tvEvalChildCategoryValue);
		
		tvEvalChildWeight.setText("Weight:");
		tvEvalChildDate.setText("Date:");
		tvEvalChildCategory.setText("Category:");
		
		tvEvalChildWeightValue.setText(String.valueOf((evalChild.getWeight())));
		tvEvalChildDateValue.setText(evalChild.getDate());
		tvEvalChildCategoryValue.setText(String.valueOf(evalChild.getCategory()));
	
		return v;
	}

	public int getChildrenCount(int groupPosition) {
		return eval_child.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return eval_parent.get(groupPosition);
	}

	public int getGroupCount() {
		return eval_parent.size();
	}

	public long getGroupId(int groupPosition) {
		return (long) (groupPosition * 1024); // To be consistent with
												// getChildId
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_eval_parent, parent, false);
		}
		
		EvalData evalParent = (EvalData) getGroup(groupPosition);
		TextView title = (TextView) v.findViewById(R.id.tvEvalParentTitle);
		TextView mark = (TextView) v.findViewById(R.id.tvEvalParentMark);

		title.setText(evalParent.getTitle());
		
		//mark.setText(Double.toString(evalParent.getMark()) + "/" + Integer.toString(evalParent.getOutOfNoDecimal()));
		//Display mark in percentage
		double percent = (evalParent.getMark()/evalParent.getOutOf())*100;
		mark.setText(twoDForm.format(percent) + " %");

		return v;
		
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
}