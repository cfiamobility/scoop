package ca.gc.inspection.scoop.ProfileComment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.*;

public class ProfileCommentAdapter extends RecyclerView.Adapter<ProfileCommentViewHolder> {

    private ProfileCommentContract.Presenter mProfileCommentPresenter;
    private ProfileCommentFragment mProfileCommentView;

    public void setView (ProfileCommentFragment profileCommentView){
        mProfileCommentView = profileCommentView;
    }

    /**
     * Constructor for the adapter
     */
    public ProfileCommentAdapter(ProfileCommentFragment profileCommentView, ProfileCommentContract.Presenter presenter) {
        mProfileCommentView = profileCommentView;
        mProfileCommentPresenter = presenter;
    }

    /**
     * Creates the viewholder that contains all the parts of the adapter
     * @param viewGroup: ??
     * @param i: item iterator for each row of the recycler view
     * @returns a new view holder
     */
    @NonNull
    @Override
    public ProfileCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_profile_comments, viewGroup, false);
        return new ProfileCommentViewHolder(v);
    }

    /**
     * Binds new data to the viewholder
     *
     * @param profileCommentViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ProfileCommentViewHolder profileCommentViewHolder, int i) {
        //TODO remove commented lines
//        mProfileCommentPresenter = new ProfileCommentPresenter(mProfileCommentView, comments, images, i, profileCommentViewHolder);
//        try {
//            mProfileCommentPresenter.displayPost();
//            mProfileCommentPresenter.displayImages();
//            mProfileCommentPresenter.formPostTitle();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        mProfileCommentPresenter.onBindProfileCommentViewHolderAtPosition(profileCommentViewHolder, i);
    }

    @Override
    public int getItemCount() {
        return mProfileCommentPresenter.getItemCount();
    }

}
