package ca.gc.inspection.scoop.searchpeople;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;
import ca.gc.inspection.scoop.searchpost.view.SearchPostContract;
import ca.gc.inspection.scoop.util.TextFormat;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchPeopleViewHolder extends RecyclerView.ViewHolder
        implements SearchPeopleContract.View.ViewHolder {

    SearchPeopleContract.Presenter.ViewHolderAPI mPresenter;

    public TextView username, positionDivision, location;
    public CircleImageView profileImage;

    public SearchPeopleViewHolder(View v, SearchPeopleContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        username = v.findViewById(R.id.fragment_search_profile_txt_name);
        positionDivision = v.findViewById(R.id.fragment_search_profile_txt_position_division);
        location = v.findViewById(R.id.fragment_search_profile_txt_location);
        profileImage = v.findViewById(R.id.fragment_search_profile_img);

        mPresenter = presenter;
    }

}
