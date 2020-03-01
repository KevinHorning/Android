package com.example.lab3;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class MainActivity extends Activity {

    private static final String database_url = "jdbc:mysql://studentcoded.com:3306/csc4360_db";
    private static final String database_user = "csc4360dbviewer";
    private static final String database_pass = "p;fw3X2K!ab,Q";
    // placeholder that you will be updating with the database data
    private static TextView getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find the screen element that you need
        getData = (TextView) findViewById(R.id.queryResults1);
        Button loadDataButton = (Button) findViewById(R.id.getDataButton1);
        //set the onClick listener for the button
        loadDataButton.setOnClickListener(new View.OnClickListener()
                                          {
                                              @Override
                                              public void onClick(View v){
                                                  new getDataFromDatabase().execute();
                                              }//end getDataFromDatabase
                                          }//end OnClickListener
        );//end loadDataButton.setOnClickListener
    }//end onCreate

    public static class getDataFromDatabase extends AsyncTask<Void, Void, Void> {
        //references: http://developer.android.com/reference/android/os/AsyncTask.html
        //            https://www.youtube.com/watch?v=N0FLT5NdSNU (about the 7 min mark)
        private String queryResult;
        protected Void doInBackground(Void... arg0)  {
            try {
                queryResult = "Database connection success\n";

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(database_url, database_user, database_pass);
                String queryString = "select * from film limit 5";

                Statement st = con.createStatement();
                final ResultSet rs = st.executeQuery(queryString);
                ResultSetMetaData rsmd = rs.getMetaData();

                //do some things with the data you've retrieved
                while (rs.next()) {
                    queryResult += rsmd.getColumnName(1) + ": " + rs.getString(1) + "\n";
                    queryResult += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                }

                con.close(); //close database connection
            } catch (Exception e) {
                e.printStackTrace();
                //put the error into the TextView on the app screen
                queryResult = "Database connection failure\n" +  e.toString();
            }

            return null;
        }//end database connection via doInBackground

        //after processing is completed, post to the screen
        protected void onPostExecute(Void result) {
            //put the results into the TextView on the app screen
            getData.setText(queryResult);
        }
    }//end getDataFromDatabase()

}