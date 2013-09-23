package ca.projectkarma.gradetrackr.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ca.projectkarma.gradetrackr.model.CategoryData;

import ca.projectkarma.gradetrackr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoriesListAdapter extends BaseAdapter {

    // views
    private LayoutInflater inflater;

    // data
    private ArrayList<CategoryData> categories;

    DecimalFormat twoDForm = new DecimalFormat("0.00");

    public CategoriesListAdapter(Context context, ArrayList<CategoryData> categories) {
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

    public void changeColor(TextView title, TextView mark, TextView weight, View lineColor,
            int targetColor) {
        lineColor.setBackgroundColor(targetColor);
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
            v = inflater.inflate(R.layout.layout_categories, parent, false);
        }

        double allWeight = 0;
        double allPercent = 0;

        v.setBackgroundColor(colors[position % 2]);
        CategoryData categories = (CategoryData) getItem(position);
        TextView title = (TextView) v.findViewById(R.id.tvCategoryTitle);
        TextView mark = (TextView) v.findViewById(R.id.tvCategoryMark);
        TextView weight = (TextView) v.findViewById(R.id.tvCategoryWeight);
        View lineColor = (View) v.findViewById(R.id.line_Color);

        title.setText(categories.getTitle());
        lineColor.setBackgroundColor(0xff159ceb);

        // TODO: refactor All to strings.xml
        if (!categories.getTitle().equals("All"))
            changeColor(title, mark, weight, lineColor, categories.getColor());

        // position of listview objects starts at 0 and ID starts at 1
        if (categories.getDatabaseID() == 0)
        {
            for (int i = 1; i < this.categories.size(); i++)
            {
                allWeight += this.categories.get(i).getWeight();
            }
            final double EPSILON = 0.001;
            if (Math.abs(allWeight) < EPSILON) {
            	mark.setText(String.valueOf(100.00));
            }
            else
            {
                for (int i = 1; i < this.categories.size(); i++)
                {
                    allPercent += this.categories.get(i).getMark()
                            * (this.categories.get(i).getWeight() / allWeight);
                }
                mark.setText(twoDForm.format(allPercent));
            }
        }
        else
        {
            mark.setText(twoDForm.format(categories.getMark()));
        }
        if (categories.getDatabaseID() == 0)
        {
            weight.setText("");
        }
        else
        {
            weight.setText(twoDForm.format(categories.getWeight()));
        }
        return v;

    }

}