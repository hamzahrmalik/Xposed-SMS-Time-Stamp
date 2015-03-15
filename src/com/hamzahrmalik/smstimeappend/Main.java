package com.hamzahrmalik.smstimeappend;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.SpannableStringBuilder;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage {

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
						Field f = XposedHelpers.findField(clazz, "mText");

						SpannableStringBuilder text = (SpannableStringBuilder) f
								.get(param.thisObject);
						String s = text.toString();

						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",
								Locale.ENGLISH);
						String date = sdf.format(new Date());

						f.set(param.thisObject, s + " " + date);

					}
				});
	}

}