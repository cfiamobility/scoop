package ca.gc.inspection.scoop.settings;

import android.support.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

class SettingsInteractor {

    private SettingsPresenter mPresenter;

    SettingsInteractor(@NonNull SettingsPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
