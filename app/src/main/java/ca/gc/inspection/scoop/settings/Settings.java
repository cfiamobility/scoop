package ca.gc.inspection.scoop.settings;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;

import ca.gc.inspection.scoop.MyApplication;
import ca.gc.inspection.scoop.R;

/**
 * This class contains the names of all columns in the usersettings table in the scoop database (except for userid)
 * These values are used as unique identify keys for all settings in the app
 * We need to do it this way because keys MUST be consisted between the app and the server/database
 */
public abstract class Settings {
    public static final ArrayList<String> LANGUAGES = new ArrayList<>(Arrays.asList("English", "French"));

    public static final String PUSH_NOTIFICATION_SETTING = "push_notifications";
    public static final String LANGUAGE_SETTING = "language";
}
