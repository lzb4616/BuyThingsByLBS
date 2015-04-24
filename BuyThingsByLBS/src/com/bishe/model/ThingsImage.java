package com.bishe.model;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;

/**
 * @author robin
 * @date 2015-4-24
 * Copyright 2015 The robin . All rights reserved
 */
public class ThingsImage extends BmobFile {

	private static final long serialVersionUID = -2805955851181925415L;

	public ThingsImage(File file)
	{
		super(file);
	}
	
	public ThingsImage() {
		super();
	}

	@Override
	protected void setFilename(String filename) {
		super.setFilename(filename);
	}

	@Override
	protected void setGroup(String group) {
		super.setGroup(group);
	}

}
