package ca.gc.inspection.scoop.searchbuilding;

import android.app.Activity;
import android.content.Context;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface SearchBuildingContract {
    interface View extends BaseView<Presenter>{

        void addBuilding(String toString);

        void updateRecyclerView();
    }
    
    interface Presenter extends BasePresenter{

        void populateBuildingNamesList();


        int getBuildingID(String item);
    }
}
