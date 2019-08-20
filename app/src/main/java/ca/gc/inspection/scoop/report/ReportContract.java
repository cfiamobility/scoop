package ca.gc.inspection.scoop.report;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

/**
 * This interface is the contract that communicates the methods between the Report
 * View (ReportDialogFragment) and the Report Presenter (ReportPresenter)
 */
public interface ReportContract {

    /**
     * View interface implemented by ReportFragment
     */
    interface View extends BaseView<ReportContract.Presenter> {
        void reportConfirmation();

        void setReportFailMessage(String message);
    }


    /**
     * Presenter interface implemented by ReportPresenter
     */
    interface Presenter extends BasePresenter {
        void submitReport(String activityId, String posterId, String userId, String reportReason, String reportBody);
    }

}
