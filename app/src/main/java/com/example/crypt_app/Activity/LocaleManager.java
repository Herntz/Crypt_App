package com.example.crypt_app.Activity;import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import java.util.Locale;

public class LocaleManager {

    private static final String LANGUAGE_KEY = "language_key";

    public static Context setLocale(Context c) {
        return updateResources(c, getLanguage(c));
    }

    public static Context setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        return updateResources(c, language);
    }

    public static String getLanguage(Context c) {
        SharedPreferences preferences = c.getSharedPreferences("language_prefs", Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE_KEY, Locale.getDefault().getLanguage());
    }

    private static void persistLanguage(Context c, String language) {
        SharedPreferences preferences = c.getSharedPreferences("language_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE_KEY, language);
        editor.apply();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Configuration applyOverrideConfiguration(Context context, Configuration overrideConfiguration) {
        String language = getLanguage(context);
        if (!language.isEmpty()) {
            Configuration config = new Configuration(overrideConfiguration);
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            config.setLocale(locale);
            return config;
        }
        return overrideConfiguration;
    }
}
