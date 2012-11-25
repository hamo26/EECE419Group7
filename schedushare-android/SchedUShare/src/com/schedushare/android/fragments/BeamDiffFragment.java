package com.schedushare.android.fragments;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import com.schedushare.android.BeamActivity;
import com.schedushare.android.EditScheduleActivity;
import com.schedushare.android.MainMenuActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.util.ScheduleTimeBlockWrapper;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class BeamDiffFragment extends Fragment {
	private TableLayout diffTable;

	private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa");
	private ScheduleTimeBlockWrapper receivedSchedule = null;
	private ArrayList<ScheduleData> userSchedules;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set so that fragment does not get recreated every time configuration changes.
		// (e.g. orientation change)
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_diff, container, false);


		Bundle bundle = getArguments();
		if (bundle != null) {
			//get received schedule wrapper
			try {
				this.receivedSchedule =  ScheduleTimeBlockWrapper.deserialize(bundle.getByteArray("receivedSchedule"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.userSchedules = new ArrayList<ScheduleData>();
			userSchedules.add(receivedSchedule.schedule);

			SharedPreferences p = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
			SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
			dataSource.open();
			this.userSchedules.add(dataSource.getScheduleFromId(p.getLong(getString(R.string.settings_owner_active_schedule_id), 1)));

			// Initialize all rows in the diffTable.
			this.diffTable = (TableLayout)view.findViewById(R.id.diff_table_layout);
			addAllRows();

			// Calendars used for comparison.
			Calendar currentTime = Calendar.getInstance();
			Calendar startTime = Calendar.getInstance();
			Calendar endTime = Calendar.getInstance();

			// Layout params used by block.
			LayoutParams blockParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
			blockParams.setMargins(1, 0, 1, 0);

			// Iterate through all days. Colour all free blocks.
			for (int j = 0; j < 7; j++) {
				// Get all the time blocks of each schedule for the given day.
				ArrayList<ArrayList<TimeBlockData>> scheduleTimeBlocks = new ArrayList<ArrayList<TimeBlockData>>();
				for (ScheduleData s : this.userSchedules) {	    			
					ArrayList<TimeBlockData> t = dataSource.getScheduleDayTimeBlocks(s.id, j);

					if (t != null) {
						scheduleTimeBlocks.add(dataSource.getScheduleDayTimeBlocks(s.id, j));
					}
				}

				// Go through each time block for the day and perform a diff between all schedules.
				for (int k = 0; k < EditScheduleActivity.TIME_DATA.length; k++) {
					TableRow row = (TableRow)view.findViewWithTag(EditScheduleActivity.TIME_DATA[k]);
					TextView tv = new TextView(getActivity());
					tv.setLayoutParams(blockParams);

					// Set calendar object as basis for comparison.
					try {
						currentTime.setTime(this.timeFormat.parse(EditScheduleActivity.TIME_DATA[k]));

						// Count the number of schedules that are free for the time block.
						int numSchedulesFree = 0;
						boolean isBlockFree = true;

						for (ArrayList<TimeBlockData> timeBlocks : scheduleTimeBlocks) {		    				
							isBlockFree = true;

							for (TimeBlockData timeBlock : timeBlocks) {
								startTime.setTime(this.timeFormat.parse(timeBlock.startTime));
								endTime.setTime(this.timeFormat.parse(timeBlock.endTime));

								// Found time block that is not free.
								if ((currentTime.getTime().getTime() >= startTime.getTime().getTime()) &&
										(currentTime.getTime().getTime() < endTime.getTime().getTime())) {
									isBlockFree = false;
									break;
								}
							}

							if (isBlockFree)
								numSchedulesFree++;
						}

						// All schedules are free at that time.
						if (numSchedulesFree == scheduleTimeBlocks.size()) {
							tv.setBackgroundColor(Color.GREEN);
						}

						row.addView(tv);
					} catch (ParseException e) {
						e.printStackTrace();
					}	    				    			
				}
			}
			dataSource.close();
		} else {
			System.out.println("Bundle is null.");
		}
		return view;
	}

	private void addAllRows() {
		LayoutParams rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LayoutParams blockParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		blockParams.setMargins(1, 0, 1, 0);

		Calendar currentTime = Calendar.getInstance();
		for (int i = 0; i < EditScheduleActivity.TIME_DATA.length; i++) {
			TableRow row = new TableRow(getActivity());
			row.setLayoutParams(rowParams);
			row.setTag(EditScheduleActivity.TIME_DATA[i]);

			// Create a block in table to display the time.
			TextView timeBlock = new TextView(getActivity());
			timeBlock.setLayoutParams(blockParams);
			timeBlock.setTypeface(Typeface.MONOSPACE);
			try {
				currentTime.setTime(this.timeFormat.parse(EditScheduleActivity.TIME_DATA[i]));

				Formatter formatter = new Formatter();
				String hour = Integer.toString(currentTime.get(Calendar.HOUR));
				if (hour.equals("0")) hour = "12";
				if (currentTime.get(Calendar.AM_PM) == Calendar.AM) {
					timeBlock.setText(formatter.format("%2s:%02d AM", hour, currentTime.get(Calendar.MINUTE)).toString());
				} else {
					timeBlock.setText(formatter.format("%2s:%02d PM", hour, currentTime.get(Calendar.MINUTE)).toString());
				}

				formatter.close();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			row.addView(timeBlock);

			this.diffTable.addView(row);
		}
	}
}
