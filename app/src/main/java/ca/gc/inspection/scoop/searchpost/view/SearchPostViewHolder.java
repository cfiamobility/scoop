package ca.gc.inspection.scoop.searchpost.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.searchpost.SearchPostContract;
import ca.gc.inspection.scoop.util.TextFormat;
import ca.gc.inspection.scoop.profilepost.ProfilePostViewHolder;

public class SearchPostViewHolder extends ProfilePostViewHolder
        implements SearchPostContract.View.ViewHolder {
    /**
     * ViewHolder for viewing a search post.
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

    /**
     * Override to allow method chaining
     * @param postTitle
     * @param textFormat
     * @return
     */
    @Override
    public SearchPostContract.View.ViewHolder setPostTitleWithFormat(String postTitle, TextFormat textFormat) {
        super.setPostTitleWithFormat(postTitle, textFormat);
        return this;
    }

    /**
     * Override to allow method chaining
     * @param postText
     * @param textFormat
     * @return
     */
    @Override
    public SearchPostContract.View.ViewHolder setPostTextWithFormat(String postText, TextFormat textFormat) {
        super.setPostTextWithFormat(postText, textFormat);
        return this;
    }
}
