package ca.gc.inspection.scoop;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface MainContract {
    interface View extends BaseView<MainContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
