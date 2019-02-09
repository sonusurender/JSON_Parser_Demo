package com.json_demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	private static Button localFile, serverData;
	private static ListView showParsedData;
	private static TextView showdatatext;
	private static JSONData_Adapter adapter;

	private static String call_url = "http://androhub.com/api/get_recent_posts/";// Call
																					// url
																					// that
																					// contain
																					// json
																					// data

	// IDs to for both type of parsers
	private static final int LocalFileId = 0;
	private static final int ServerFileID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		setListeners();
		hideListView();
	}

	// Initiaize all views in activity
	private void init() {
		localFile = (Button) findViewById(R.id.read_data_from_localfile);

		serverData = (Button) findViewById(R.id.read_data_from_server);
		showParsedData = (ListView) findViewById(R.id.show_jsondata);
		showdatatext = (TextView) findViewById(R.id.parsed_showdata_text);
	}

	// Set Listeners on all three buttons
	private void setListeners() {
		localFile.setOnClickListener(this);

		serverData.setOnClickListener(this);
	}

	// Show listview and text
	private void showListView() {
		showdatatext.setVisibility(View.VISIBLE);
		showParsedData.setVisibility(View.VISIBLE);
	}

	// Hide listview and text
	private void hideListView() {
		showdatatext.setVisibility(View.GONE);
		showParsedData.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.read_data_from_localfile:

			// Execute the Asynctask
			new ReadJSONDataTask(MainActivity.this, LocalFileId).execute();

			break;

		case R.id.read_data_from_server:
			// Execute the Asynctask
			new ReadJSONDataTask(MainActivity.this, ServerFileID).execute();
			break;

		}

	}

	// Background task to parse json Data
	private class ReadJSONDataTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;
		Context context;
		int id; // Id to execute the particular task in background like..local
				// or server
		ArrayList<JSONData_Model> jsonData = new ArrayList<JSONData_Model>();// Arraylist
																				// for
																				// setter

		public ReadJSONDataTask(Context context, int id) {
			this.context = context;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			// Progress Dialog to show while task is running
			pd = new ProgressDialog(context);
			pd.setIndeterminate(true);
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.setMessage("Please wait..\nLoading data");
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			// Check the send id and call the method according to it
			if (id == LocalFileId) {
				jsonData = ReadDataFromLocalFile();

			} else if (id == ServerFileID) {
				jsonData = ReadDataFromServer();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);

			// After background task the post method will set adapter over
			// listview
			if (jsonData != null) {

				// the adapter will be called according to send IDs
				if (id == LocalFileId) {
					adapter = new JSONData_Adapter(context, jsonData, "Local");
					showParsedData.setAdapter(adapter);
					adapter.notifyDataSetChanged();

					// Show listview after setting adapter
					showListView();
				}

				else if (id == ServerFileID) {
					adapter = new JSONData_Adapter(context, jsonData, "Server");
					showParsedData.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					showListView();
				}

			}

			// After setting adapter the dilaog should be dismissed
			if (pd.isShowing())
				pd.dismiss();
		}
	}

	// Method that will return the jsonData in arraylist form after parsing data
	// from local text file
	private ArrayList<JSONData_Model> ReadDataFromLocalFile() {
		ArrayList<JSONData_Model> jsonArrayList = new ArrayList<JSONData_Model>();
		try {
			JSONObject jsonObject = new JSONObject(loadJSONFromAsset());// JsonObject
																		// get
																		// from
																		// local
																		// assests
																		// text
																		// file
			JSONArray jsonArray = jsonObject.getJSONArray("data");// "data"
																	// keyvalue
																	// contains
																	// the
																	// jsonarray

			// Check if jsonArray is null or not and length should be greater
			// than 0
			if (jsonArray != null) {
				if (jsonArray.length() > 0) {

					// Loop to all jsonarray items
					for (int i = 0; i < jsonArray.length(); i++) {
						String[] contactNumberArray = null;// String array that
															// will store phone
															// numbers
						JSONObject newJsonObject = jsonArray.getJSONObject(i);// Now
																				// jsonarray
																				// contains
																				// jsonObject
																				// so
																				// it
																				// should
																				// pe
																				// parsed
																				// again
																				// jsonobject
						String name = newJsonObject.getString("name");// "name"
																		// keyvalue
																		// contain
																		// name
						String location = newJsonObject.getString("location");// "location"
																				// keyvalue
																				// conatin
																				// location
						String contact_number = newJsonObject
								.getString("phone");// "phone" keyvalue conatins
													// phonearray
						JSONArray phoneArray = new JSONArray(contact_number);// so
																				// the
																				// contact_number
																				// string
																				// will
																				// parse
																				// into
																				// jsonArray
						if (phoneArray.length() > 0) {
							contactNumberArray = new String[phoneArray.length()];
							for (int j = 0; j < phoneArray.length(); j++) {
								contactNumberArray[j] = phoneArray.getString(j);// string
																				// array
																				// will
																				// store
																				// the
																				// number
																				// array

							}
						}

						jsonArrayList.add(new JSONData_Model(name, location,
								contactNumberArray));// finally add the data to
														// arrayList
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			// If an exception occurs the arraylist will return null
			return null;

		}
		return jsonArrayList;
	}

	// Method that will return the jsonData in arraylist form after parsing data
	// from server
	private ArrayList<JSONData_Model> ReadDataFromServer() {
		ArrayList<JSONData_Model> jsonArrayList = new ArrayList<JSONData_Model>();
		try {
			HttpClient client = new DefaultHttpClient();// Default httpclient
														// for request
			HttpGet httpget = new HttpGet(call_url);// HttpGet method to get the
													// data from URL

			HttpResponse response = client.execute(httpget);// HttpResponse
															// after executing
															// httpGet method
															// via client
			HttpEntity entity = response.getEntity();// Entity will get the
														// respone data
			String data = EntityUtils.toString(entity);// now finally entity
														// will convert into
														// string and data will
														// fetched in string
														// format
			Log.e("data", data);// Log for fetched data
			JSONObject jsonObject = new JSONObject(data);// Main data is in
															// JsonObject form

			if (jsonObject != null) {
				// The data contains many values but we'll parse only needed
				// values

				String posts = jsonObject.getString("posts");// Posts contains
																// the all posts
				JSONArray postsArray = new JSONArray(posts);// "posts" is in
															// array so
															// jsonArray will
															// used to parse
				if (postsArray != null) {
					if (postsArray.length() > 0) {
						for (int i = 0; i < postsArray.length(); i++) {
							JSONObject postsObject = postsArray
									.getJSONObject(i);// Now jsonArray contains
														// josnObject

							// Now this posts jsonObject contains many values
							// but we'll fetch required data
							String title = postsObject.getString("title");// "title"
																			// contains
																			// title
																			// of
																			// post
							String status = postsObject.getString("status");// "status"
																			// contains
																			// status
																			// of
																			// post
							String published_date = postsObject
									.getString("date");// "date" contains
														// published_date of
														// post
							jsonArrayList.add(new JSONData_Model(title, status,
									published_date));// finally add the data to
														// arrayList

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return jsonArrayList;
	}

	// Read file from assets folder and return data in json string
	private String loadJSONFromAsset() {// this method will read the text file
										// from assests folder and return the
										// file in string format
		String json = null;
		try {
			InputStream is = getAssets().open("dummyjson.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

}
