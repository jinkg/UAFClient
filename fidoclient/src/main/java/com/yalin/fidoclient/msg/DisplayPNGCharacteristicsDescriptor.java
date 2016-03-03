package com.yalin.fidoclient.msg;

/**
 * Created by YaLin on 2016/1/21.
 */
public class DisplayPNGCharacteristicsDescriptor {
	public long width;
	public long height;
	public String bitDepth;
	public String colorType;
	public String compression;
	public String filter;
	public String interlace;
	public rgbPalletteEntry[] plte;
}
