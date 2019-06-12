package ca.gc.inspection.scoop;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface SearchContract {
    interface View extends BaseView<SearchContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
