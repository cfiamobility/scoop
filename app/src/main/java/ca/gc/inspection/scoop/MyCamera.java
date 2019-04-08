package ca.gc.inspection.scoop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class MyCamera {

	// Takes the bitmap and converts it to encoded base64 string
	// 0 compression
	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		return Base64.encodeToString(imageBytes, Base64.DEFAULT);
	}

	// Takes the encoded base64 string and converts it into a bitmap.
	public static Bitmap stringToBitmap(String string) {
		byte[] decodedImage = Base64.decode(string, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
	}
}
