/**
 * 
 */
package com.gamesparks.sdk.android;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.text.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.gamesparks.sdk.*;
import com.gamesparks.sdk.api.*;
import com.gamesparks.sdk.api.autogen.*;

import android.app.*;
import android.content.*;
import android.util.*;
import android.os.*;
import android.provider.*;
import android.text.*;
import android.view.*;

/**
 * @author Giuseppe Perniola
 *
 */
public class GSAndroidPlatform implements IGSPlatform
{
	private static final String GS_CURR_STATUS = "GSCurrStatus";

	private static GS 	gs;
	private Handler 	mainHandler;
	private Context 	ctx;

	public static GS initialise(final Context ctx, String apiKey, String secret, String credential, boolean liveMode, boolean autoUpdate)
	{
		GSAndroidPlatform gsAndroidPlatform = new GSAndroidPlatform(ctx);

		if (!liveMode)
		{
			Intent intent = new Intent(ctx, GSActivity.class);

			ctx.startActivity(intent);
		}

		gs = new GS(apiKey, secret, credential, liveMode, autoUpdate, gsAndroidPlatform);

		return gs;
	}
	
	private GSAndroidPlatform(Context ctx)
	{
		this.ctx = ctx;
		
		mainHandler = new Handler(ctx.getMainLooper());

		/*GSData data = getDeviceStats();

		logMessage(Arrays.asList(data.getBaseData()).toString());*/
	}
	
	public static GS gs()
	{
		return gs;
	}
		
	/* (non-Javadoc)
	 * @see com.gamesparks.sdk.IGSPlatform#getWritableLocation()
	 */
	@Override
	public File getWritableLocation()
	{
		return ctx.getFilesDir();
	}
	
	public void storeValue(String key, String value)
	{
		try
		{
			SharedPreferences 			settings = ctx.getSharedPreferences(GS_CURR_STATUS, Context.MODE_PRIVATE);
			SharedPreferences.Editor 	editor = settings.edit();
			
			editor.putString(key, value);

			editor.commit();
		}
		catch (Exception e)
		{
			
		}
	}
	
	public String loadValue(String key)
	{   
		try
		{
			SharedPreferences settings = ctx.getSharedPreferences(GS_CURR_STATUS, Context.MODE_PRIVATE);
			
			return settings.getString(key, "");
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see com.gamesparks.sdk.IGSPlatform#executeOnMainThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnMainThread(Runnable job)
	{	
		if (mainHandler != null)
		{
			mainHandler.post(job);
		}
	}

	/* (non-Javadoc)
	 * @see com.gamesparks.sdk.IGSPlatform#getPlayerId()
	 */
	@Override
	public String getPlayerId()
	{
		return loadValue("playerId");
	}

	/* (non-Javadoc)
	 * @see com.gamesparks.sdk.IGSPlatform#getAuthToken()
	 */
	@Override
	public String getAuthToken()
	{
		return loadValue("authToken");
	}

	/* (non-Javadoc)
	 * @see com.gamesparks.sdk.IGSPlatform#setPlayerId()
	 */
	@Override
	public void setPlayerId(String value)
	{
		storeValue("playerId", value);
	}

	/* (non-Javadoc)
	 * @see com.gamesparks.sdk.IGSPlatform#setAuthToken()
	 */
	@Override
	public void setAuthToken(String value)
	{
		storeValue("authToken", value);
	}
	
	@Override
	public Object getHmac(String nonce, String secret)
	{
		try
		{
			Mac 			sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec 	secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");

			sha256_HMAC.init(secret_key);

			//return Base64.encodeBase64String(sha256_HMAC.doFinal(nonce.getBytes("UTF-8")));
			return Base64.encodeToString(sha256_HMAC.doFinal(nonce.getBytes("UTF-8")), Base64.NO_WRAP);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public void logMessage(String msg)
	{
		System.out.println(msg);
	}

	@Override
	public void logError(Throwable t)
	{
		System.out.println(t.getMessage());
	}

	@Override
	public String getDeviceId() {
		return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	@Override
	public String getDeviceOS() {
		return "ANDROID";
	}

	@Override
	public String getPlatform() {
		return "Android";
	}

	@Override
	public String getSDK() {
		return "Android";
	}

	@Override
	public String getDeviceType() {
		return "Handheld";
	}

	@Override
	public GSData getDeviceStats() {
		Map<String, Object> data = new HashMap<String, Object> ();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

		wm.getDefaultDisplay().getMetrics(displayMetrics);

		final String vmVersion = System.getProperty("java.vm.version");

		data.put ("manufacturer", Build.MANUFACTURER);
		data.put ("model", Build.MODEL);
		data.put ("memory", getTotalRAM());
		data.put ("os.name", "Android OS");
		data.put ("os.version", Integer.toString(Build.VERSION.SDK_INT));
		data.put ("cpu.cores", Integer.toString(getNumberOfCores()));
		data.put ("cpu.vendor", getCPUModel());
		data.put ("resolution", Integer.toString(displayMetrics.widthPixels).concat("x").concat(Integer.toString(displayMetrics.heightPixels)));
		data.put ("gssdk", "0.4.4");
		if (vmVersion != null && vmVersion.startsWith("2")) {
			data.put("engine", "ART");
		} else {
			data.put("engine", "Dalvik");
		}
		data.put ("engine.version", vmVersion);

		return new GSData (data);
	}

	private static String getTotalRAM() {
		RandomAccessFile reader = null;
		String load = null;
		DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
		double totRam = 0;
		String lastValue = "";

		try {
			reader = new RandomAccessFile("/proc/meminfo", "r");

			load = reader.readLine();

			// Get the Number value from the string
			Pattern p = Pattern.compile("(\\d+)");
			Matcher m = p.matcher(load);
			String value = "";

			while (m.find()) {
				value = m.group(1);
				// System.out.println("Ram : " + value);
			}
			reader.close();

			totRam = Double.parseDouble(value);
			// totRam = totRam / 1024;

			double mb = totRam / 1024.0;
			double gb = totRam / 1048576.0;
			double tb = totRam / 1073741824.0;

			if (tb > 1) {
				lastValue = twoDecimalForm.format(tb).concat(" TB");
			} else if (gb > 1) {
				lastValue = twoDecimalForm.format(gb).concat(" GB");
			} else if (mb > 1) {
				lastValue = twoDecimalForm.format(mb).concat(" MB");
			} else {
				lastValue = twoDecimalForm.format(totRam).concat(" KB");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Streams.close(reader);
		}

		return lastValue;
	}

	private static int getNumberOfCores() {
		if (Build.VERSION.SDK_INT >= 17) {
			return Runtime.getRuntime().availableProcessors();
		} else {
			return getNumCoresOldPhones();
		}
	}

	private static int getNumCoresOldPhones() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
					return true;
				}

				return false;
			}
		}

		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());

			return files.length;
		} catch(Exception e) {
			return 1;
		}
	}

	private static String getCPUModel() {
		HashMap<String, String> output = new HashMap<> ();

		try {
			BufferedReader br = new BufferedReader (new FileReader ("/proc/cpuinfo"));
			String str;

			while ((str = br.readLine ()) != null) {
				String[] data = str.split (":");

				if (data.length > 1) {
					String key = data[0].trim ().replace (" ", "_");

					if (key.equals ("model_name")) {
						key = "cpu_model";
					}

					String value = data[1].trim ();

					if (key.equals ("cpu_model")) {
						value = value.replaceAll("\\s+", " ");
					}

					output.put (key, value);
				}
			}

			br.close ();

		} catch (IOException e) {
			e.printStackTrace ();
		}

		return output.get("cpu_model");
	}
}
