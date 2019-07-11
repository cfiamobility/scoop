package ca.gc.inspection.scoop.searchpost;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;

public class SearchPostViewHolder extends ProfilePostViewHolder
        implements SearchPostContract.View.ViewHolder {
    /**
     * ViewHolder for viewing a profile post.
     */

    SearchPostContract.Presenter.ViewHolderAPI mPresenter;

    public TextView commentCount;
    public ImageView optionsMenu;


    public SearchPostViewHolder(View v, SearchPostContract.Presenter.ViewHolderAPI presenter) {
        super(v, presenter);
        commentCount = v.findViewById(R.id.comment_count);
        optionsMenu = v.findViewById(R.id.options_menu);

        mPresenter = presenter;
    }
}
