package ca.gc.inspection.scoop.base;

import android.support.annotation.Nullable;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}

