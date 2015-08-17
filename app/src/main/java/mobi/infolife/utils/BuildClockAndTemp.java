package mobi.infolife.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

import mobi.infolife.cwwidget.R;

public class BuildClockAndTemp {

	String content;
	static int textSize;
	static int textColor;
	static int textOffset;
	static boolean isUseShadow;
	static Typeface ttf;
	static String textSimple = "0";
	static int length = 1;

	public static Bitmap buildTempBitMap(Context c, String s) {
		ttf = Typeface.createFromAsset(c.getAssets(), "temp text typeface.ttf");
		textColor = c.getResources().getColor(
				R.color.clockweather_42_theme1_temptext_text1_textcolor);
		textSize = c.getResources().getInteger(
				R.integer.clockweather_42_theme1_temptext_text1_textsize);
		isUseShadow = c.getResources().getBoolean(
				R.bool.is_42_temprature_text1_use_shadow);
		textOffset = c.getResources().getInteger(
				R.integer.clockweather_42_theme1_temptext_text1_offset);
		Paint paint = new Paint();
		Rect bounds = new Rect();
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
		paint.setTypeface(ttf);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTextAlign(Align.CENTER);
		paint.getTextBounds(textSimple, 0, length, bounds);

		int shadowX = 0;
		int shadowY = 0;
		int shadowColor = 0xFF123456;
		float shadowradius = 2.0f;
		float shadowdx = 0.0f;
		float shadowdy = 2.0f;
		if (isUseShadow) {
			shadowColor = c.getResources().getColor(
					R.color.clockweather_42_theme1_temptext_text1_shadowcolor);
			shadowY = c
					.getResources()
					.getInteger(
							R.integer.clockweather_42_theme1_temptext_text1_shadowheight);
			shadowX = c
					.getResources()
					.getInteger(
							R.integer.clockweather_42_theme1_temptext_text1_shadowheight);
			shadowradius = c.getResources().getDimension(
					R.dimen.clockweather_42_theme1_temptext_text1_shadowradius);
			shadowdx = c.getResources().getInteger(
					R.integer.clockweather_42_theme1_temptext_text1_shadowdx);
			shadowdy = c.getResources().getInteger(
					R.integer.clockweather_42_theme1_temptext_text1_shadowdy);
			paint.setShadowLayer(shadowradius, shadowdx, shadowdy, shadowColor);
		}
		// int width=bounds.right-bounds.left+shadowX*2;
		int width = bounds.width() + shadowX * 2;
		int height = bounds.height() + shadowY;

		Bitmap myBitmap = null;
		// Utils.log("BuildBitmapFactory::"+setting.content+"++"+(height
		// +setting.textOffset + shadowY)+"+++"+width);
		if ((height + textOffset + shadowY <= 0) || width <= 0 || " ".equals(s))
			myBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		else
			myBitmap = Bitmap.createBitmap(width,
					height + textOffset + shadowY, Bitmap.Config.ARGB_8888);
		Canvas myCanvas = new Canvas(myBitmap);
		myCanvas.drawText(s, width / 2, height, paint);
		return myBitmap;
	}

	public static Bitmap buildClockBitMap(Context c, String s) {
		ttf = Typeface
				.createFromAsset(c.getAssets(), "clock text typeface.ttf");
		textColor = c.getResources().getColor(
				R.color.clockweather_42_theme1_clocktext_text1_textcolor);
		textSize = c.getResources().getInteger(
				R.integer.clockweather_42_theme1_clocktext_text1_textsize);
		isUseShadow = c.getResources().getBoolean(
				R.bool.is_42_clock_text1_use_shadow);
		textOffset = c.getResources().getInteger(
				R.integer.clockweather_42_theme1_clocktext_text1_offset);
		Paint paint = new Paint();
		Rect bounds = new Rect();
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
		paint.setTypeface(ttf);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTextAlign(Align.CENTER);
		paint.getTextBounds(textSimple, 0, length, bounds);

		int shadowX = 0;
		int shadowY = 0;
		int shadowColor = 0xFF123456;
		float shadowradius = 2.0f;
		float shadowdx = 0.0f;
		float shadowdy = 2.0f;
		if (isUseShadow) {
			shadowColor = c.getResources().getColor(
					R.color.clockweather_42_theme1_clocktext_text1_shadowcolor);
			shadowY = c
					.getResources()
					.getInteger(
							R.integer.clockweather_42_theme1_clocktext_text1_shadowheight);
			shadowX = c
					.getResources()
					.getInteger(
							R.integer.clockweather_42_theme1_clocktext_text1_shadowheight);
			shadowradius = c
					.getResources()
					.getDimension(
							R.dimen.clockweather_42_theme1_clocktext_text1_shadowradius);
			shadowdx = c.getResources().getInteger(
					R.integer.clockweather_42_theme1_clocktext_text1_shadowdx);
			shadowdy = c.getResources().getInteger(
					R.integer.clockweather_42_theme1_clocktext_text1_shadowdy);
			paint.setShadowLayer(shadowradius, shadowdx, shadowdy, shadowColor);
		}

		// int width=bounds.right-bounds.left+shadowX*2;
		int width = bounds.width() + shadowX * 2;
		int height = bounds.height() + shadowY;

		Bitmap myBitmap = null;
		// Utils.log("BuildBitmapFactory::"+setting.content+"++"+(height
		// +setting.textOffset + shadowY)+"+++"+width);
		if ((height + textOffset + shadowY <= 0) || width <= 0 || " ".equals(s))
			myBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		else
			myBitmap = Bitmap.createBitmap(width,
					height + textOffset + shadowY, Bitmap.Config.ARGB_8888);
		Canvas myCanvas = new Canvas(myBitmap);
		myCanvas.drawText(s, width / 2, height, paint);

		return myBitmap;
	}

}
