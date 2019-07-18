package ca.gc.inspection.scoop.searchpeople.view;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.searchpeople.SearchPeopleContract;
import ca.gc.inspection.scoop.searchpeople.UserProfileListener;
import ca.gc.inspection.scoop.util.CameraUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchPeopleViewHolder extends RecyclerView.ViewHolder implements
        SearchPeopleContract.View.ViewHolder,
        UserProfileListener {

    SearchPeopleContract.Presenter.ViewHolderAPI mPresenter;

    public TextView username, position, division, location;
    public CircleImageView profileImage;

    public SearchPeopleViewHolder(View v, SearchPeopleContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        username = v.findViewById(R.id.item_search_profile_txt_name);
        position = v.findViewById(R.id.item_search_profile_txt_position);
        division = v.findViewById(R.id.item_search_profile_txt_division);
        location = v.findViewById(R.id.item_search_profile_txt_location);
        profileImage = v.findViewById(R.id.item_search_profile_img);

        mPresenter = presenter;
    }

    public SearchPeopleContract.View.ViewHolder setFullName(String fullName) {
        this.username.setText(fullName);
        return this;
    }

    public SearchPeopleContract.View.ViewHolder setPosition(String position) {
        this.position.setText(position);
        return this;
    }

    public SearchPeopleContract.View.ViewHolder setDivision(String division) {
        this.division.setText(division);
        return this;
    }

    public SearchPeopleContract.View.ViewHolder setLocation(String location) {
        this.location.setText(location);
        return this;
    }

    public SearchPeopleContract.View.ViewHolder setUserImageFromString(String image) {
        if (image != null && !image.isEmpty()) {
            Bitmap bitmap = CameraUtils.stringToBitmap(image); //converts image string to bitmap
            Log.i("image", image);
            profileImage.setImageBitmap(bitmap);
        }
        return this;
    }

    @Override
    public TextView getUserName() {
        return username;
    }

    @Override
    public ImageView getProfileImage() {
        return profileImage;
    }
}
