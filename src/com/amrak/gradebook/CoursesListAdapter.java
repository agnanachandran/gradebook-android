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

public class CoursesListAdapter extends BaseAdapter {

	// context
	private Context context;

	// views
	private LayoutInflater inflater;

	// data
	private ArrayList<CourseData> courses;

	public CoursesListAdapter(Context context, ArrayList<CourseData> courses) {
		this.context = context;
		this.courses = courses;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return courses.size();
	}

	public Object getItem(int position) {
		return courses.get(position);
	}

	public long getItemId(int position) {
		return (long) (position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
			v = inflater.inflate(R.layout.layout_course, parent, false);
		}

		CourseData course = (CourseData) getItem(position);
		TextView title = (TextView) v.findViewById(R.id.tvCourseTitle);
		TextView mark = (TextView) v.findViewById(R.id.tvCourseMark);
		TextView code = (TextView) v.findViewById(R.id.tvCourseCode);

		title.setText(course.getTitle());
		mark.setText(String.valueOf(course.getMark()));
		code.setText(course.getCode());
		return v;

	}

}
