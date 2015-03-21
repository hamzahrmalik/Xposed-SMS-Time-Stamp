package com.hamzahrmalik.smstimeappend;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.SpannableStringBuilder;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage {

	XSharedPreferences pref;
	final String SEPARATOR = " | ";// may be configurable later
	final String PNAME = "com.hamzahrmalik.smstimeappend";

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.android.mms"))
			return;
		final Class<?> clazz = XposedHelpers.findClass(
				"com.android.mms.data.WorkingMessage", lpparam.classLoader);
		XposedHelpers.findAndHookMethod(clazz, "send", String.class,
				new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param)
							throws Throwable {
						// get prefs
						if (pref == null)
							pref = new XSharedPreferences(PNAME, Keys.PREF);
						else
							pref.reload();
						// get original message
						Field f = XposedHelpers.findField(clazz, "mText");
						SpannableStringBuilder text = (SpannableStringBuilder) f
								.get(param.thisObject);
						String origMsg = text.toString();
						// get date in prefrred format
						String format = "hh:mm";
						if (pref.getBoolean(Keys.USE_24H, false))
							format = "HH:mm";
						SimpleDateFormat sdf = new SimpleDateFormat(format,
								Locale.ENGLISH);
						String date = sdf.format(new Date());
						// check if we should add separator, otherwise just a
						// space
						if (pref.getBoolean(Keys.USE_SEPARATOR, true))
							origMsg += SEPARATOR;
						else
							origMsg += " ";
						// set new value
						f.set(param.thisObject, origMsg + date);

					}
				});
	}

}