package com.amrak.gradebook;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class Evaluations extends Activity {

	private ExpListAdapter expListAdapter;
	ExpandableListView expListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluations);
        
        TextView title = (TextView) findViewById(R.id.tvEvalTitle);
		TextView mark = (TextView) findViewById(R.id.tvEvalMark);

		title.setText("Introduction to Methods of Software Engineering");
		mark.setText(Integer.toString(100));

		expListView = (ExpandableListView) findViewById(R.id.elvEvalList);

		// parent rows
		ArrayList<EvalParentData> eval_parent = new ArrayList<EvalParentData>();
		eval_parent.add(new EvalParentData("Assignment 1", 100));
		eval_parent.add(new EvalParentData("Assignment 2", 90));
		eval_parent.add(new EvalParentData("Assignment 3", 80));
		eval_parent.add(new EvalParentData("Assignment 4", 70));

		// child rows
		ArrayList<ArrayList<EvalChildData>> eval_childs = new ArrayList<ArrayList<EvalChildData>>();
		ArrayList<EvalChildData> eval_child = new ArrayList<EvalChildData>();
		eval_child.add(new EvalChildData("Item 1 Data 1", "Item 1 Data 2"));
		eval_childs.add(eval_child);
		eval_child = new ArrayList<EvalChildData>();
		eval_child.add(new EvalChildData("Item 2 Data 1", "Item 2 Data 2"));
		eval_childs.add(eval_child);
		eval_child = new ArrayList<EvalChildData>();
		eval_child.add(new EvalChildData("Item 3 Data 1", "Item 3 Data 2"));
		eval_childs.add(eval_child);
		eval_child = new ArrayList<EvalChildData>();
		eval_child.add(new EvalChildData("Item 4 Data 1", "Item 4 Data 2"));
		eval_childs.add(eval_child);

		expListAdapter = new ExpListAdapter(this, eval_parent, eval_childs);
		expListView.setAdapter(expListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_evaluations, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.preferences:
			Intent i = new Intent("com.amrak.gradebook.SETTINGS");
			startActivity(i);
			break;
		case R.id.addeval:
			Intent j = new Intent("com.amrak.gradebook.ADDEVAL");
			startActivity(j);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}