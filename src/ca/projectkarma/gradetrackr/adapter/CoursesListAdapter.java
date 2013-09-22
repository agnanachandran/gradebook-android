package ca.projectkarma.gradetrackr.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ca.projectkarma.gradetrackr.model.CourseData;

import ca.projectkarma.gradetrackr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CoursesListAdapter extends BaseAdapter {

    // context
    private Context context;

    // views
    private LayoutInflater inflater;

    // data
    private ArrayList<CourseData> courses;

    DecimalFormat twoDForm = new DecimalFormat("0.00");

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
        int[] colors = new int[] { 0x30ffffff, 0x30404040 };

        View v = null;
        if (convertView != null)
        {
            v = convertView;
        }
        else
        {
            v = inflater.inflate(R.layout.layout_course, parent, false);
        }
        v.setBackgroundColor(colors[position % 2]);

        CourseData course = (CourseData) getItem(position);
        TextView title = (TextView) v.findViewById(R.id.tvCourseTitle);
        TextView mark = (TextView) v.findViewById(R.id.tvCourseMark);
        TextView code = (TextView) v.findViewById(R.id.tvCourseCode);

        title.setText(course.getTitle());
        mark.setText(twoDForm.format(course.getMark()));
        code.setText(course.getCode());
        return v;

    }

}