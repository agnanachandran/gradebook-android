package ca.projectkarma.gradetrackr.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ca.projectkarma.gradetrackr.R;
import ca.projectkarma.gradetrackr.model.TaskData;

public class TaskListAdapter extends BaseAdapter {

    // views
    private LayoutInflater inflater;

    // data
    private List<TaskData> tasks;

    public TaskListAdapter(Context context, List<TaskData> tasks) {
        this.tasks = tasks;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return tasks.size();
    }

    public Object getItem(int position) {
        return tasks.get(position);
    }

    public long getItemId(int position) {
        return (long) (position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (convertView != null)
        {
            v = convertView;
        }
        else
        {
            v = inflater.inflate(R.layout.layout_task, parent, false);
        }

        TaskData task = (TaskData) getItem(position);
        TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
        TextView dateDue = (TextView) v.findViewById(R.id.tvTaskDateDue);
        TextView dateDueTime = (TextView) v.findViewById(R.id.tvTaskDateDueTime);

        title.setText(task.getTitle());
        dateDue.setText(task.getDateDue());
        dateDueTime.setText(task.getDateDueTime());
        return v;
    }

}