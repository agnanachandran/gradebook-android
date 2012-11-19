package com.amrak.gradebook;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<EvalParentData> eval_parent;
	private ArrayList<ArrayList<EvalChildData>> eval_child;
	private LayoutInflater inflater;

	public ExpListAdapter(Context context,
			ArrayList<EvalParentData> eval_parent,
			ArrayList<ArrayList<EvalChildData>> eval_child) {
		this.context = context;
		this.eval_parent = eval_parent;
		this.eval_child = eval_child;
		inflater = LayoutInflater.from(context);
	}

	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return eval_child.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return (long) (groupPosition * 1024 + childPosition); // Max 1024 children per group
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_eval_child, parent, false);
		}

		EvalChildData evalChild = (EvalChildData) getChild(groupPosition, childPosition);
		TextView data1 = (TextView) v.findViewById(R.id.tvEvalChildData1);
		TextView data2 = (TextView) v.findViewById(R.id.tvEvalChildData2);

		data1.setText(evalChild.getData1());
		data2.setText(evalChild.getData2());

		return v;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return eval_child.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return eval_parent.get(groupPosition);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return eval_parent.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return (long) (groupPosition * 1024); // To be consistent with getChildId
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_eval_parent, parent, false);
		}
		
		EvalParentData evalParent = (EvalParentData) getGroup(groupPosition);
		TextView title = (TextView) v.findViewById(R.id.tvEvalParentTitle);
		TextView mark = (TextView) v.findViewById(R.id.tvEvalParentMark);

		title.setText(evalParent.getTitle());
		mark.setText(Integer.toString(evalParent.getMark()));

		return v;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
