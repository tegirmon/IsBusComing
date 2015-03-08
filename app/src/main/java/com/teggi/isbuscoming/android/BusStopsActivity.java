package com.teggi.isbuscoming.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.teggi.isbuscoming.R;
import com.teggi.isbuscoming.comm.SmsSender;
import com.teggi.isbuscoming.dba.BusStopsSchema;
import com.teggi.isbuscoming.helpers.BusStopsHelper;
import com.teggi.isbuscoming.helpers.TimeHelper;


public class BusStopsActivity extends ActionBarActivity implements AddBusStopActivity.NoticeDialogListener{
    static TextView ds;
    static TextView dsTime;
    public void listBusStops(){
        Cursor c = BusStopsHelper.listBusStops(getApplicationContext());
        ListView listOfStops = (ListView) findViewById(R.id.busStopList);

        if (c.getCount() > 0) {
            String[] fromColumns = {BusStopsSchema.Favorites.COLUMN_NAME,
                    BusStopsSchema.Favorites.COLUMN_ID};
            int[] toViews = {R.id.stopName, R.id.stopNumber};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.item_bus_stop, c, fromColumns, toViews, 0);

            listOfStops.setAdapter(adapter);
            listOfStops.setClickable(true);
            listOfStops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView stopName = (TextView) view.findViewById(R.id.stopName);
                    TextView stopNumber = (TextView) view.findViewById(R.id.stopNumber);

                    SmsSender.checkStatus(stopNumber.getText().toString());
                    displayStatusActivity(stopNumber.getText().toString(),
                            stopName.getText().toString());
                }

            });
            listOfStops.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView stopName = (TextView) view.findViewById(R.id.stopName);
                    final TextView stopNumber = (TextView) view.findViewById(R.id.stopNumber);
                    //TODO implement edit/delete option
                    displayConfirmDeleteDialog(stopNumber.getText().toString(), stopName.getText().toString());
                    return true;
                }
            });
        } else {
            String[] noResult = {"No Bus Stops. \n\nClick 'Add New Stop' to add a new bus stop to the list."};
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.item_bus_stop_no_result,
                    noResult);
            listOfStops.setAdapter(adapter);
        }
    }

    public void displayConfirmDeleteDialog(String busStop, String busStopName){
        final String busStopNumber = busStop;
        AlertDialog.Builder builder = new AlertDialog.Builder(BusStopsActivity.this);
        builder.setTitle(R.string.deleteTitle);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(R.string.deleteMessage + busStopName + R.string.questionMark);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onUserDismissDeleteDialog(true, busStopNumber);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Cancel function
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void onUserDismissDeleteDialog(boolean allow, String busStop){
        Toast.makeText(getApplicationContext(), "Deleting..", Toast.LENGTH_SHORT).show();
        BusStopsHelper.deleteBusStop(getApplicationContext(), busStop);
        listBusStops();
    }
    public void showNoticeDialog() {
        DialogFragment dialog = new AddBusStopActivity();
        dialog.show(getSupportFragmentManager(), "AddBusStopActivity");
    }

    public static void displayStatus(String status){
        ds.setText(status);
        String currTime = "Last updated: " + TimeHelper.currTime();
        dsTime.setText(currTime);
    }

    public void displayStatusActivity(final String code, String title){
        setContentView(R.layout.activity_display_status);
        TextView dsBusStop = (TextView) findViewById(R.id.busStopCode);
        TextView dsTitle = (TextView) findViewById(R.id.displayStatusTitle);
        ds = (TextView) findViewById(R.id.displayStatus);
        dsTime = (TextView) findViewById(R.id.displayStatusTime);
        dsBusStop.setText(code);
        dsTitle.setText("Bus Stop: " + title);
        String status = getText(R.string.statusLoading).toString();
        displayStatus(status);

        Button refresh = (Button) findViewById(R.id.refreshStatus);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsSender.checkStatus(code);
                displayStatus("Updating..");
            }
        });
    }

    public void displayHome(){
        setContentView(R.layout.activity_my_stops);
        listBusStops();

        final Button button = (Button) findViewById(R.id.addBusStop);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showNoticeDialog();
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        final EditText name = (EditText)dialog.getDialog().findViewById(R.id.busStopName);
        final EditText number = (EditText)dialog.getDialog().findViewById(R.id.busStopNumber);

        if (name != null && number != null){
            String sName = name.getText().toString();
            String sNumber = number.getText().toString();

            if (sName.isEmpty() || sNumber.isEmpty()){
                showNoticeDialog();
                Toast.makeText(getApplicationContext(), R.string.enterBusInfo, Toast.LENGTH_SHORT).show();
            } else {
                int iNumber = Integer.parseInt(sNumber);
                BusStopsHelper.addBusStop(getApplicationContext(), iNumber, sName);
                listBusStops();
            }
        } else {
            showNoticeDialog();
            Toast.makeText(getApplicationContext(), R.string.enterBusInfo, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayHome();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bus_stops, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            setContentView(R.layout.activity_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO add a back button to the app
    @Override
    public void onBackPressed() {
        displayHome();
    }
}
