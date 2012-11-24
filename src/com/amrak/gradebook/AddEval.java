package com.amrak.gradebook;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class AddEval extends Activity {
	EditText etEvalName;
	EditText date;
	Button bDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_eval);
		etEvalName = (EditText) findViewById(R.id.etEvalName);
		bDone = (Button) findViewById(R.id.bDone);
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		date.setText(today.month + 1 + "/" + today.monthDay + "/" + today.year);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, EVALUATIONS);
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.acTextCategory);
		textView.setAdapter(adapter);
		
		bDone.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				
			}
		});

	}
	

	private static final String[] EVALUATIONS = new String[] { "Quiz", "Test",
			"Unit Test", "Midterm", "Final", "Final Exam", "Assignment",
			"Presentation" };
}