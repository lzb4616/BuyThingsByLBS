package com.bishe.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bishe.buythingsbylbs.R;
import com.bishe.model.Location;
import com.bishe.utils.LocationUtils;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;

/**
 * @author robin
 * @date 2015-4-29 Copyright 2015 The robin . All rights reserved
 */
public class MyLoactionActivity extends MapActivity implements
		TencentLocationListener {

	public static final String TAG = "MyLoactionActivity";

	private MapView mMapView;

	private TencentLocation mLocation;
	private TencentLocationManager mLocationManager;
	private Marker markerFix;
	
	private int mRetrySearchTimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_map);
		initMapView();
		mRetrySearchTimes = 3;
		mLocationManager = TencentLocationManager.getInstance(this);
		mLocationManager
				.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
	}

	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.mapviewOverlay);
		mMapView.getController().setZoom(16);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("定位");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.publish_action_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_things_publish:
			Location location = new Location();
			location.setLatitude(mLocation.getLatitude());
			location.setLongitude(mLocation.getLongitude());
			Intent intent = new Intent();
			intent.setClass(this, ThingsDetailActivity.class);
			intent.putExtra("location", location);
			intent.putExtra("locationName", mLocation.getAddress());
			MyLoactionActivity.this.setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		startLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();
	}

	public void myLocation() {
		if (mLocation != null) {
			mMapView.getController().animateTo(LocationUtils.of(mLocation));
			tagMyloaction(mMapView, mLocation);
		}
	}

	@Override
	public void onLocationChanged(TencentLocation location, int error,
			String reason) {
		if (error == TencentLocation.ERROR_OK) {
			mLocation = location;
			mMapView.invalidate();
			mRetrySearchTimes -- ;
			myLocation();
		}
		if (mRetrySearchTimes == 0) {
			stopLocation();
		}
	}

	@Override
	public void onStatusUpdate(String name, int status, String desc) {
	}

	private void startLocation() {
		TencentLocationRequest request = TencentLocationRequest.create();
		request.setInterval(5000);
		mLocationManager.requestLocationUpdates(request, this);
	}

	private void stopLocation() {
		mLocationManager.removeUpdates(this);
	}

	private void tagMyloaction(MapView mapView, TencentLocation location) {
		markerFix = mapView.addMarker(new MarkerOptions()
				.position(
						new LatLng(location.getLatitude(), location
								.getLongitude()))
				.title("当前的位置")
				.snippet(location.getAddress())
				.anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.red_location)));
		markerFix.showInfoWindow();
	}
}