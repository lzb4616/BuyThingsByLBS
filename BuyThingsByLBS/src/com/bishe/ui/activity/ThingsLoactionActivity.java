package com.bishe.ui.activity;

import java.util.List;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bishe.MyApplication;
import com.bishe.buythingsbylbs.R;
import com.bishe.logic.UserLogic;
import com.bishe.model.Location;
import com.bishe.model.Things;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LocationUtils;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
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
 * @date 2015-4-29 Copyright 2015 The robin . All rights reserved
 */
public class ThingsLoactionActivity extends MapActivity implements OnMarkerPressListener,OnInforWindowClickListener {

	public static final String TAG = "ThingsLoactionActivity";

	private MapView mMapView;

	private MapController mapController;
	private Marker markerFix;

	private List<Things> mThings = null;

	private List<Bitmap> mBitmaps = null;
	
	private UserLogic muUserLogic;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_location_map);
		muUserLogic = new UserLogic(getApplicationContext());
		mThings = MyApplication.getInstance().getShowLocationThings();
		if (null == mThings) {
			ActivityUtils.toastShowBottom(this, "数据为空，请添加数据");
			return;
		}
		initMapView();
		myLocation();
		
	}

	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.mapviewOverlay);
		mMapView.getController().setZoom(13);
		mapController = mMapView.getController();

		// 标注点击监听
		mapController.setOnMarkerClickListener(this);
		// InfoWindow点击监听
		mapController.setOnInforWindowClickListener(this);

		for (Things things : mThings) {
			
		}
		for (int i = 0; i < mThings.size(); i++) {
			Things things = mThings.get(i);
			
			showThingsloaction(mMapView, things,i);
		}
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("商品位置");
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.empty_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void showThingsloaction(MapView mapView,Things things,int index) {

		 mapView.addMarker(new MarkerOptions()
				.position(new LatLng(things.getThingsLocation().getLatitude(), things.getThingsLocation().getLongitude()))
				.title("卖家："+things.getAuthor().getUsername()+"   价格："+things.getPrice()+"元").snippet("商品描述:"+things.getContent()+"  距离："+(int)things.getThingsLocation().distanceInKilometersTo(muUserLogic.getCurrentUser().getLocation())+"km")
				.anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.red_location)));
		//markerFix.showInfoWindow();
	}

	public void myLocation() {
		Location currentLocation = muUserLogic.getCurrentUser().getLocation();
		if (null == currentLocation ) {
			ActivityUtils.toastShowBottom(this, "当前地址获取失败");
			return;
		}
		mapController.animateTo(LocationUtils.ofBmobLocation(currentLocation));
		OverlayItem mylay = new OverlayItem(LocationUtils.ofBmobLocation(currentLocation), "我的位置", "");
		mylay.setDragable(false);
		mMapView.add(mylay);
	}
	@Override
	public void onMarkerPressed(Marker arg0) {
		arg0.showInfoWindow();
	}

	@Override
	public void onInforWindowClick(Marker arg0) {
		ActivityUtils.toastShowCenter(this, "第"+arg0.getId()+"个") ;
		arg0.showInfoWindow();
	}

}
