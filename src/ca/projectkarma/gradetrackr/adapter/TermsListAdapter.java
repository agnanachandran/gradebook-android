package ca.projectkarma.gradetrackr.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ca.projectkarma.gradetrackr.Utils;
import ca.projectkarma.gradetrackr.model.TermData;

import ca.projectkarma.gradetrackr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TermsListAdapter extends BaseAdapter {

    // context
    private Context context;

    // views
    private LayoutInflater inflater;

    // data
    private ArrayList<TermData> terms;

    DecimalFormat twoDForm = new DecimalFormat("0.00");

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

        int[] colors = new int[] { 0x30ffffff, 0x30e0e0e0 };
        View v = null;
        if (convertView != null)
        {
            v = convertView;
        }
        else
        {
            v = inflater.inflate(R.layout.layout_term, parent, false);
        }

        v.setBackgroundColor(colors[position % 2]);

        TermData term = (TermData) getItem(position);
        TextView title = (TextView) v.findViewById(R.id.tvTermTitle);
        TextView mark = (TextView) v.findViewById(R.id.tvTermMark);
        TextView date = (TextView) v.findViewById(R.id.tvTermDate);

        title.setText(term.getTitle());
        mark.setText(twoDForm.format(term.getMark()));
        date.setText(Utils.toMonthYear(term.getDateStart()) + " - "
                + Utils.toMonthYear(term.getDateEnd()));
        return v;
    }

}
