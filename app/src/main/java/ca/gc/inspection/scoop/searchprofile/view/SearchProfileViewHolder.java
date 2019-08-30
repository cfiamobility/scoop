package ca.gc.inspection.scoop.searchprofile.view;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.searchprofile.SearchProfileContract;
import ca.gc.inspection.scoop.searchprofile.UserProfileListener;
import ca.gc.inspection.scoop.util.CameraUtils;
import ca.gc.inspection.scoop.util.TextFormat;
import de.hdodenhof.circleimageview.CircleImageView;

import static ca.gc.inspection.scoop.util.TextFormat.POST_TEXT_FORMAT_BOLD_COLOUR;
import static ca.gc.inspection.scoop.util.TextFormat.POST_TEXT_FORMAT_HIGHLIGHT_COLOUR;
import static ca.gc.inspection.scoop.util.TextFormat.TEXT_FORMAT_FOOTER_SEPARATOR;

/**
 * ViewHolder which displays a user's profile in the SearchProfileFragment
 */
public class SearchProfileViewHolder extends RecyclerView.ViewHolder implements
        SearchProfileContract.View.ViewHolder,
        UserProfileListener {

    SearchProfileContract.Presenter.ViewHolderAPI mPresenter;

    public TextView username, position, division, location;
    public CircleImageView profileImage;

    public SearchProfileViewHolder(View v, SearchProfileContract.Presenter.ViewHolderAPI presenter) {
        super(v);
        username = v.findViewById(R.id.item_search_profile_txt_name);
        position = v.findViewById(R.id.item_search_profile_txt_position);
        division = v.findViewById(R.id.item_search_profile_txt_division);
        location = v.findViewById(R.id.item_search_profile_txt_location);
        profileImage = v.findViewById(R.id.item_search_profile_img);

        mPresenter = presenter;
    }

    public SearchProfileContract.View.ViewHolder setFullName(String fullName) {
        this.username.setText(fullName);
        return this;
    }

    public SearchProfileContract.View.ViewHolder setPosition(String position) {
        this.position.setText(position);
        return this;
    }

    public SearchProfileContract.View.ViewHolder setDivision(String division) {
        this.division.setText(division);
        return this;
    }

    public SearchProfileContract.View.ViewHolder setLocation(String location) {
        this.location.setText(location);
        return this;
    }

    public SearchProfileContract.View.ViewHolder setUserImageFromString(String image) {
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

    /**
     * Uses the TextFormat to highlight relevant words and to show an italicized footer
     * @param text
     * @param textFormat
     * @return
     */
    public static SpannableStringBuilder getSpannableStringBuilderWithFormat(String text, TextFormat textFormat) {
        String fullText = text;
        String footer = textFormat.getFooter();
        if (footer != null && !footer.isEmpty())
            fullText += TEXT_FORMAT_FOOTER_SEPARATOR + footer;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(fullText);

        // bold words in text
        for (Pair<Integer, Integer> pair : textFormat.getBoldTextPositions()) {
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),
                    pair.first, pair.second, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            spannableStringBuilder.setSpan(new ForegroundColorSpan(POST_TEXT_FORMAT_BOLD_COLOUR),
                    pair.first, pair.second, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            spannableStringBuilder.setSpan(new BackgroundColorSpan(POST_TEXT_FORMAT_HIGHLIGHT_COLOUR),
                    pair.first, pair.second, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        // set footer format
        if (footer != null && !footer.isEmpty()) {
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC),
                    text.length(), fullText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return spannableStringBuilder;
    }

    @Override
    public SearchProfileContract.View.ViewHolder setFullNameWithFormat(String fullName, TextFormat textFormat) {
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilderWithFormat(fullName, textFormat);
        this.username.setText(spannableStringBuilder);
        return this;
    }

    @Override
    public SearchProfileContract.View.ViewHolder setPositionWithFormat(String position, TextFormat textFormat) {
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilderWithFormat(position, textFormat);
        this.position.setText(spannableStringBuilder);
        return this;
    }

    @Override
    public SearchProfileContract.View.ViewHolder setDivisionWithFormat(String division, TextFormat textFormat) {
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilderWithFormat(division, textFormat);
        this.division.setText(spannableStringBuilder);
        return this;
    }

    @Override
    public SearchProfileContract.View.ViewHolder setLocationWithFormat(String location, TextFormat textFormat) {
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilderWithFormat(location, textFormat);
        this.location.setText(spannableStringBuilder);
        return this;
    }
}
