package ca.gc.inspection.scoop.settings;

import android.support.annotation.NonNull;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsInteractor mInteractor;
    private SettingsContract.View mView;

    SettingsPresenter(@NonNull SettingsContract.View view) {
        mInteractor = new SettingsInteractor(this);
        mView = checkNotNull(view);
    }

    @Override
    public void start() {

    }
}
