package ca.gc.inspection.scoop.searchprofile;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.search.SearchContract;
import ca.gc.inspection.scoop.util.TextFormat;

public interface SearchProfileContract extends SearchContract {
    /**
     * A contract between the View (layer) and Presenter for the viewing a profile post
     * action. This contract communicates the how the View and Presenter will
     * interact with each other.
     *
     * Since RecyclerView is being used in this package, we must additionally specify the interaction
     * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
     *
     * View = SearchProfileFragment - is the main View responsible for creating the Presenter and Adapter
     *      View.Adapter = SearchPostAdapter is the subview responsible for creating ViewHolders
     *      View.ViewHolder = SearchProfileViewHolder is the subview which displays an individual profile comment
     *
     * Presenter = SearchProfilePresenter - there's only one object which implements:
     *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
     *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
     *
     * If communication is required between the View layer and Presenter layer, interface methods must be used.
     *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
     * If communication is required within the View only or Presenter only, the object itself should be passed in
     * to avoid leaking access to internal methods in the contract.
     *
     * See PostCommentContract for inheritance hierarchy for Posts/Comments
     */

    interface View extends BaseView<Presenter> {
        /**
         * Implemented by the main View (ie. SearchProfileFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        void onLoadedDataFromDatabase();
        void setResultsInfo(int resultsCount);

        interface Adapter {
            void refreshAdapter();
        }

        interface ViewHolder {
            ViewHolder setFullName(String fullName);
            ViewHolder setPosition(String position);
            ViewHolder setDivision(String division);
            ViewHolder setLocation(String location);

            ViewHolder setUserImageFromString(String image);

            ViewHolder setFullNameWithFormat(String fullName, TextFormat textFormat);
            ViewHolder setPositionWithFormat(String position, TextFormat textFormat);
            ViewHolder setDivisionWithFormat(String division, TextFormat textFormat);
            ViewHolder setLocationWithFormat(String location, TextFormat textFormat);
        }
    }

    interface Presenter extends BasePresenter {

        void loadDataFromDatabase(String search);

        interface AdapterAPI {
            void setAdapter(SearchProfileContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    SearchProfileContract.View.ViewHolder searchProfileViewHolder, int i);
            int getItemCount();
            String getProfileUserIdByIndex(int i);
        }

        interface ViewHolderAPI {
        }
    }
}
