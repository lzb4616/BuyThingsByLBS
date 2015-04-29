package com.bishe.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.bishe.buythingsbylbs.R;
import com.bishe.model.Things;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OnInforWindowClickListener;
import com.tencent.tencentmap.mapsdk.map.OnMarkerDragedListener;
import com.tencent.tencentmap.mapsdk.map.OnMarkerPressListener;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;

/**
 * @author robin
 * @date 2015-4-29
 * Copyright 2015 The robin . All rights reserved
 */
public class ThingsLoactionActivity extends MapActivity implements OnMarkerDragedListener,
 OnMarkerPressListener,
		OnInforWindowClickListener {
	
	public static final String TAG = "ThingsLoactionActivity";
	
	private MapView mMapView;
	
	private MapController mapController;
	private Marker markerFix;
	
	private List<Things> mThings = new ArrayList<Things>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_location_map);
		
	}
	
	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.mapviewOverlay);
//		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(9);
		mapController = mMapView.getController();
		
		//标注点击监听
		mapController.setOnMarkerClickListener(this);
		//标注拖动监听
		mapController.setOnMarkerDragListener(this);
		//InfoWindow点击监听
		mapController.setOnInforWindowClickListener(this);
		
		addMarkers(mMapView);
		
	}

	private static GeoPoint of(Location location) {
		GeoPoint ge = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
		return ge;
	}
	
	private void showThingsloaction(MapView mapView,GeoPoint geoPoint)
	{
		OverlayItem mylay = new OverlayItem(geoPoint, "我的位置", "我的东西");
		mylay.setDragable(true);
		mapView.add(mylay);
		
		markerFix = mapView.addMarker(new MarkerOptions()
		.position(new LatLng(31.238068, 121.501654))
		.title("上海")
		.anchor(0.5f, 0.5f)
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.red_location)));
		markerFix.showInfoWindow();// 设置默认显示一个infowinfow
	}
	
	// ====== util methods

	private static GeoPoint of(TencentLocation location) {
		GeoPoint ge = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
		return ge;
	}
	
	private void tagMyloaction(MapView mapView,GeoPoint geoPoint)
	{
		OverlayItem mylay = new OverlayItem(geoPoint, "我的位置", "我的东西");
		mylay.setDragable(true);
		mapView.add(mylay);
		
		markerFix = mapView.addMarker(new MarkerOptions()
		.position(new LatLng(31.238068, 121.501654))
		.title("上海")
		.anchor(0.5f, 0.5f)
		.icon(BitmapDescriptorFactory
				.defaultMarker())
				.draggable(true));
		addHundredMarkers(mapView);
		markerFix.showInfoWindow();// 设置默认显示一个infowinfow
	}

	/**
	 * 通过添加OverlayItem添加标注物
	 * @param mapView
	 */
	private void addMarkers(MapView mapView) {
		GeoPoint p1 = new GeoPoint((int)(39.90403 * 1E6), (int)(116.407525 * 1E6));
		GeoPoint p2 = new GeoPoint((int)(38.5 * 1E6), (int)(114.955 * 1E6));
		LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
		Drawable marker = getResources().getDrawable(R.drawable.route_start);

		OverlayItem oiFixed = new OverlayItem(p1, "标注1", "不可拖拽");

		oiFixed.setDragable(false);
		OverlayItem oiDrag = new OverlayItem(p2, "标注2", "可拖拽,自定义图标", marker);

		mapView.add(oiFixed);		
		mapView.add(oiDrag);
		
		markerFix = mapView.addMarker(new MarkerOptions()
		.position(SHANGHAI)
		.title("上海")
		.anchor(0.5f, 0.5f)
		.icon(BitmapDescriptorFactory
				.defaultMarker())
				.draggable(true));
		addHundredMarkers(mapView);
		markerFix.showInfoWindow();// 设置默认显示一个infowinfow
		
		//BitmapDescriptorFactory相关方法使用
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 100))
		.icon(BitmapDescriptorFactory.fromAsset("green_location.ico"))
		.title("from asset"));

		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 102.0))
		.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.route_start)))
		.title("from bitmap"));
		
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 104.0))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_location))
		.title("from resource"));
		
		File file = new File(getFilesDir(), "myicon.ico");
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromAsset("green_location.ico");
		Bitmap bmp = bitmapDescriptor.getBitmap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		FileOutputStream fos;
		try {
			fos = openFileOutput(file.getName(), MODE_PRIVATE);
			try {
				fos.write(baos.toByteArray());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 106.0))
		.icon(BitmapDescriptorFactory.fromPath(getFilesDir() + "/" + file.getName()))
		.title("from path"));
		
		mapView.addMarker(new MarkerOptions()
		.position(new LatLng(32.01, 108.0))
		.icon(BitmapDescriptorFactory.fromFile(file.getName()))
		.title("from file"));
	}

	private void addHundredMarkers(MapView mapView) {
		double lat = 20.5;
		double lng = 90.955;
		LatLng latLng = new LatLng(lat, lng);
		int i = 0;
		while (i < 100) {
			mapView.addMarker(new MarkerOptions()
			.position(latLng)
			.title("marker" + i++)
			.icon(BitmapDescriptorFactory.
					defaultMarker())
					.draggable(false));
			latLng = new LatLng(lat += 0.5, lng += 0.5);
			Log.e("draw", "marker:" + i);
		}
	}
	
	@Override
	public void onMarkerPressed(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.showInfoWindow();
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.setSnippet(arg0.getPosition().toString());
		arg0.showInfoWindow();
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInforWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.setSnippet("InfoWindow Clicked!");
		arg0.showInfoWindow();
	}

}
