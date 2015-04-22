package com.bishe.model;

import cn.bmob.v3.datatype.BmobGeoPoint;

public class Location extends BmobGeoPoint {
	
	private static final long serialVersionUID = -7881589012584412494L;

	public double distanceInKilometersTo(Location point) {
		return super.distanceInKilometersTo(point);
	}

	public double distanceInMilesTo(Location point) {
		return super.distanceInMilesTo(point);
	}

	public double distanceInRadiansTo(Location point) {
		return super.distanceInRadiansTo(point);
	}

	@Override
	public double getLatitude() {
		return super.getLatitude();
	}

	@Override
	public double getLongitude() {
		return super.getLongitude();
	}

	@Override
	public void setLatitude(double latitude) {
		super.setLatitude(latitude);
	}

	@Override
	public void setLongitude(double longitude) {
		super.setLongitude(longitude);
	}
}
