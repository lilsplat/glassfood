// Copyright 2007-2014 metaio GmbH. All rights reserved.
package com.metaio.Example;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.Example.TutorialVisualSearch.MetaioSDKCallbackHandler;
import com.metaio.Example.TutorialVisualSearch.VisualSearchCallbackHandler;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.tools.io.AssetsManager;

public class TutorialHelloWorld extends ARViewActivity {
	/**
	 * Geometry to display visual search result each result has a model
	 */
	private final IGeometry[] mModels = new IGeometry[10];
	/**
	 * Paint object used to draw text on the images
	 */
	Paint mPaint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPaint = new Paint();
	}

	@Override
	protected int getGUILayout() {
		// Attaching layout to the activity
		return R.layout.tutorial_hello_world;
	}

	public void onButtonClick(View v) {
		finish();
	}

	@Override
	protected void loadContents() {
		try {
			// Getting a file path for tracking configuration XML file
			File trackingConfigFile = AssetsManager
					.getAssetPathAsFile(getApplicationContext(),
							"TutorialHelloWorld/Assets/TrackingData_MarkerlessFast.xml");

			// Assigning tracking configuration
			boolean result = metaioSDK
					.setTrackingConfiguration(trackingConfigFile);
			MetaioDebug.log("Tracking data loaded: " + result);
		} catch (Exception e) {
			MetaioDebug.printStackTrace(Log.ERROR, e);
		}
	}

	@Override
	public void onDrawFrame() {
		super.onDrawFrame();

		if (metaioSDK != null) {
			// get all detected poses/targets
			TrackingValuesVector poses = metaioSDK.getTrackingValues();

			// if we have detected one, attach our metaio man to this coordinate
			// system Id
			for (int i = 0; i < poses.size(); i++) {
				// load an image geometry to display the result on the pattern
				final String texturePath = AssetsManager.getAssetPath(
						getApplicationContext(),
						"TutorialVisualSearch/Assets/poi.png");
				if (texturePath != null) {
					// remove the file extension
					final String name = Integer.toString(poses.get(i).getCoordinateSystemID());

					// create a billboard texture that highlights the file name
					// of the searched image
					final String imagePath = createTexture(name, texturePath);

					if (imagePath != null) {
						IGeometry mModel = mModels[i];
						if (mModel == null) {
							// create new geometry
							mModel = metaioSDK
									.createGeometryFromImage(imagePath);
							mModel.setScale(1.5f);
							mModel.setCoordinateSystemID(poses.get(i).getCoordinateSystemID());
							MetaioDebug
									.log("The image has been loaded successfully");
						} else {
							// update texture with new image
							mModel.setTexture(imagePath);
						}
					} else {
						MetaioDebug.log(Log.ERROR,
								"Error creating image texture");
					}
				}
			}

		}
	}

	/**
	 * Create a texture with the title on the background image
	 * 
	 * @param title
	 *            Title string
	 * @param backgroundImagePath
	 *            Full path to the background image
	 * @return Full path of the created texture, or null if failed
	 */
	private String createTexture(String title, String backgroundImagePath) {
		try {
			final String texturepath = getCacheDir() + "/" + title + ".png";

			// Load background image and make a mutable copy
			Bitmap backgroundImage = BitmapFactory
					.decodeFile(backgroundImagePath);
			Bitmap image = backgroundImage.copy(Bitmap.Config.ARGB_8888, true);
			backgroundImage.recycle();
			backgroundImage = null;

			Canvas c = new Canvas(image);

			mPaint.setColor(Color.WHITE);
			mPaint.setTextSize(66);
			mPaint.setTypeface(Typeface.DEFAULT);

			// Draw title string
			if (title != null && title.length() > 0) {
				String n = title.trim();

				final int maxWidth = 10000;

				int i = mPaint.breakText(n, true, maxWidth, null);
				mPaint.setTextAlign(Align.CENTER);

				c.drawText(n.substring(0, i), c.getWidth() / 2,
						c.getHeight() / 2 + 25.0f, mPaint);

			}

			// writing to the file
			try {
				FileOutputStream out = new FileOutputStream(texturepath);
				image.compress(Bitmap.CompressFormat.PNG, 90, out);
				MetaioDebug.log("Texture file is saved to " + texturepath);
				return texturepath;
			} catch (Exception e) {
				MetaioDebug.log("Failed to save texture file");
				e.printStackTrace();
			}

			image.recycle();
			image = null;

		} catch (Exception e) {
			MetaioDebug.log(Log.ERROR,
					"Error creating billboard texture: " + e.getMessage());
			MetaioDebug.printStackTrace(Log.ERROR, e);
			return null;
		}
		return null;
	}

	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// Not used in this tutorial
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		// No callbacks needed in this tutorial
		return null;
	}
}
