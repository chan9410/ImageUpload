package com.example.imageupload.utils;

import static java.lang.String.format;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {

	public final static String LOG = "INSISEOL Inventory";
	
	private final static String STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private final static String ROOT_FOLDER_NAME = LOG;
	
	private final static String DATABASE_FOLDER_NAME = "Database";
	private final static String XML_FOLDER_NAME = "XML";
	private final static String IMAGE_FOLDER_NAME = "Image";

	private final static String ROOT_PATH = STORAGE_PATH + "/" + ROOT_FOLDER_NAME;
	public final static String DATABASE_PATH = ROOT_PATH + "/" + DATABASE_FOLDER_NAME;
	public final static String XML_PATH = ROOT_PATH + "/" + XML_FOLDER_NAME;
	public final static String IMAGE_PATH = ROOT_PATH + "/" + IMAGE_FOLDER_NAME;


	private static String mUserKey = "";

	public static void setUserKey(String key) {
		mUserKey = key;
	}

	public static String getUserKey() {
		return mUserKey;
	}

	public static String getDatabaseFileName(Context context) {
		String name = "/data/data/"+context.getPackageName()+"/databases/" + LOG + ".db";
		return name;
	}
	
	public static boolean isNull(String s) {
		if (s == null || s.equals("") || s.equalsIgnoreCase("null"))
			return true;
		
		return false;
	}
	
	public static String fixNull(String s1, String s2) {
		if (isNull(s1))
			return s2;

		return s1.trim();
	}
	
	public static boolean existFile(String path) {
		return new File(path).exists(); 
	}
	
	public static void makeDirectory(String path) {
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	} 
	
	public static String getCurrentDateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		return sdf.format(new Date());
	}

	public static String getCurrentDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd", Locale.KOREA);
		return sdf.format(new Date());
	}
	
	public static String getPrefixPrice(String price) {
		if (isNull(price))
			return price;
		
		return new DecimalFormat("#,##0").format(Double.parseDouble(price));
	}

	
	public static String getRawQuery(Context context, int rawID) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		
		try {
			InputStream is = context.getResources().openRawResource(rawID);
			br = new BufferedReader(new InputStreamReader(is));
			
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
		
		return sb.toString().trim();
	}
	
	public static String convertCodeToNoTest(String code) {
		String rfidNo = "";
		
		if (isNull(code))
			return code;
		  //System.out.println("readcod1 : " +rfidNo);
		  //System.out.println("code1 : " +code);
		//if (code.contains("3000")) {
		//占쏙옙占쏙옙占� 
		//  if (code.contains("4000")) {
			if (code.length() >= 28) {
		//占쏙옙占쏙옙占�	
			//  if (code.length() >= 36) {
				try {
					//rfidNo = code.substring(4, 26);
					rfidNo = code.substring(4, 12);
					//占쏙옙占쏙옙占�
		//			rfidNo = code.substring(22, 34);
					//System.out.println("readcod 2: " +rfidNo);
				} catch (StringIndexOutOfBoundsException e) {
					// TODO: handle exception
					e.getStackTrace();
				}
			}
		//}
		
		return rfidNo.trim();
	}
	
	public static String convertCodeToNo(String code) {
		String rfidNo = "";
		
		if (isNull(code))
			return code;
	//	  System.out.println("readcod1 : " +rfidNo);
		  //System.out.println("code1 : " +code);
		//if (code.contains("3000")) {
		//占쏙옙占쏙옙占� 
		 // if (code.contains("4000")) {
			if (code.length() >= 28) {
		//占쏙옙占쏙옙占�	
			//  if (code.length() >= 36) {
				try {
					rfidNo = code.substring(4, 28);
				//	rfidNo = code.substring(4, 12);
					//占쏙옙占쏙옙占�
					//rfidNo = code.substring(22, 34);
					//System.out.println("readcod 2: " +rfidNo);
				} catch (StringIndexOutOfBoundsException e) {
					// TODO: handle exception
					e.getStackTrace();
				}
			}
		//}
		
		return rfidNo.trim();
	}

	public static String convertCodeToNoApulse(String code) {
		String rfidNo = "";

		if (isNull(code))
			return code;

		if (code.length() >= 24) {
			try {
				rfidNo = code.substring(0, 24);
			} catch (StringIndexOutOfBoundsException e) {
				// TODO: handle exception
				e.getStackTrace();
			}
		}

		return rfidNo.trim();
	}

	public static String hexToString(String hex) {
		String val = "";
		try {
			byte[] bytes = new java.math.BigInteger(hex, 16).toByteArray();
			val = new String(bytes, "UTF-8");
		} catch (Exception e) {

		}
		return val;
	}

	public static String stringToHex(String val) {
		String hex = "";
		try {
			byte[] bytes = val.getBytes("UTF-8");
			hex = new java.math.BigInteger(bytes).toString(16);
		} catch (Exception e) {
			//DebugLog.d("CommonUtils", "stringToHex:" + e);
		}
		return hex.toUpperCase().trim();
	}

    public static void copyFile(File source, File target) {
        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            //DebugLog.d("CommonUtils", "copyFile error : " + e);
        }
    }

	public static Bitmap resizeFile(File source) {
		try {

			Bitmap srcBitmap = BitmapFactory.decodeFile(source.getPath());

			//get its orginal dimensions
			int bmOriginalWidth = srcBitmap.getWidth();
			int bmOriginalHeight = srcBitmap.getHeight();
			double originalWidthToHeightRatio =  1.0 * bmOriginalWidth / bmOriginalHeight;
			double originalHeightToWidthRatio =  1.0 * bmOriginalHeight / bmOriginalWidth;
			//choose a maximum height
			int maxHeight = 1200;
			//choose a max width
			int maxWidth = 1200;
			//call the method to get the scaled bitmap
			srcBitmap = getScaledBitmap(srcBitmap, bmOriginalWidth, bmOriginalHeight,
					originalWidthToHeightRatio, originalHeightToWidthRatio,
					maxHeight, maxWidth);

			return srcBitmap;
		} catch (Exception e) {
			//DebugLog.d("CommonUtils", "copyFile error : " + e);
		}

		return null;
	}

	public static void saveResizeFile(File source, File target) {
		try {

            Bitmap srcBitmap = BitmapFactory.decodeFile(source.getPath());

            //get its orginal dimensions
            int bmOriginalWidth = srcBitmap.getWidth();
            int bmOriginalHeight = srcBitmap.getHeight();
            double originalWidthToHeightRatio =  1.0 * bmOriginalWidth / bmOriginalHeight;
            double originalHeightToWidthRatio =  1.0 * bmOriginalHeight / bmOriginalWidth;
            //choose a maximum height
            int maxHeight = 1200;
            //choose a max width
            int maxWidth = 1200;
            //call the method to get the scaled bitmap
            srcBitmap = getScaledBitmap(srcBitmap, bmOriginalWidth, bmOriginalHeight,
                    originalWidthToHeightRatio, originalHeightToWidthRatio,
                    maxHeight, maxWidth);

            saveBitmapToFile(srcBitmap, target);
		} catch (Exception e) {
			//DebugLog.d("CommonUtils", "copyFile error : " + e);
		}
	}

	public static void saveBitmapToFile(Bitmap bitmap, File file) {

		try {
			OutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
		} catch (Exception e) {
			//DebugLog.d("CommonUtils", "saveBitmapToFile error : " + e);
		}

	}

    private static Bitmap getScaledBitmap(Bitmap bm, int bmOriginalWidth, int bmOriginalHeight, double originalWidthToHeightRatio, double originalHeightToWidthRatio, int maxHeight, int maxWidth) {
        if(bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
            //DebugLog.d("CommonUtils", format("RESIZING bitmap FROM %sx%s ", bmOriginalWidth, bmOriginalHeight));

            if(bmOriginalWidth > bmOriginalHeight) {
                bm = scaleDeminsFromWidth(bm, maxWidth, bmOriginalHeight, originalHeightToWidthRatio);
            } else {
                bm = scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio);
            }

            //DebugLog.d("CommonUtils", format("RESIZED bitmap TO %sx%s ", bm.getWidth(), bm.getHeight()));
        }
        return bm;
    }



    private static Bitmap scaleDeminsFromHeight(Bitmap bm, int maxHeight, int bmOriginalHeight, double originalWidthToHeightRatio) {
        int newHeight = (int) Math.min(maxHeight, bmOriginalHeight * .55);
        int newWidth = (int) (newHeight * originalWidthToHeightRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    private static Bitmap scaleDeminsFromWidth(Bitmap bm, int maxWidth, int bmOriginalWidth, double originalHeightToWidthRatio) {
        //scale the width
        int newWidth = (int) Math.min(maxWidth, bmOriginalWidth * .75);
        int newHeight = (int) (newWidth * originalHeightToWidthRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    public static boolean isTabletDevice(Context context) {

        if (Build.VERSION.SDK_INT >= 19){
            return checkTabletDeviceWithScreenSize(context) &&
                    checkTabletDeviceWithProperties() &&
                    checkTabletDeviceWithUserAgent(context);
        }else{
            return checkTabletDeviceWithScreenSize(context) &&
                    checkTabletDeviceWithProperties() ;

        }
    }

    private static boolean checkTabletDeviceWithScreenSize(Context context) {
        boolean device_large = ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        if (device_large) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkTabletDeviceWithProperties()
    {
        try
        {
            InputStream ism = Runtime.getRuntime().exec("getprop ro.build.characteristics").getInputStream();
            byte[] bts = new byte[1024];
            ism.read(bts);
            ism.close();

            boolean isTablet = new String(bts).toLowerCase().contains("tablet");
            return isTablet;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            return false;
        }
    }

    private static boolean checkTabletDeviceWithUserAgent(Context context) {
        WebView webView = new WebView(context);
        String ua=webView.getSettings().getUserAgentString();
        webView = null;
        if(ua.contains("Mobile Safari")){
            return false;
        }else{
            return true;
        }
    }

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

}
