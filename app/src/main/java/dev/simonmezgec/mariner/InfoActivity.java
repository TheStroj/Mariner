package dev.simonmezgec.mariner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/** Activity with information about the app. */
@SuppressWarnings("unused")
public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.shipsWheelImageView)
    ImageView mShipsWheelImageView;
    @BindView(R.id.quoteTextView)
    TextView mQuoteTextView;
    @BindView(R.id.quoteAuthorTextView)
    TextView mQuoteAuthorTextView;
    @BindView(R.id.marinerTitleTextView)
    TextView mMarinerTitleTextView;
    @BindView(R.id.marinerDescriptionTextView)
    TextView mMarinerDescriptionTextView;
    @BindView(R.id.dataAssetNumberTextView)
    TextView mDataAssetNumberTextView;
    @BindView(R.id.opDescriptionTextView)
    TextView mOpDescriptionTextView;
    @BindView(R.id.opCopyrightTextView)
    TextView mOpCopyrightTextView;
    @BindView(R.id.thanksTextView)
    TextView mThanksTextView;
    @BindView(R.id.fontTextView)
    TextView mFontTextView;
    @BindView(R.id.madeByTextView)
    TextView mMadeByTextView;
    @BindView(R.id.emailTextView)
    TextView mEmailTextView;

    private AnimatedVectorDrawable avdWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        parseTextViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            avdWheel = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_wheel);
            mShipsWheelImageView.setImageDrawable(avdWheel);
            avdWheel.start();
        }

        SharedPreferences prefs = getSharedPreferences(Constant.SHARED_PREFERENCES, MODE_PRIVATE);
        int totalDataAssets = prefs.getInt(Constant.PREFERENCES_TOTAL_DATA_ASSETS, 0);
        mDataAssetNumberTextView.setText(
                String.format(getString(R.string.mariner_data_assets_total), totalDataAssets));

        String[] dataQuotes = getResources().getStringArray(R.array.data_quotes);
        String[] dataQuotesAuthors = getResources().getStringArray(R.array.data_quotes_authors);
        int randomNumber = new Random().nextInt(dataQuotes.length);
        String randomQuote = dataQuotes[randomNumber];
        String randomQuoteAuthor = dataQuotesAuthors[randomNumber];
        mQuoteTextView.setText(randomQuote);
        mQuoteAuthorTextView.setText(
                String.format(getString(R.string.quote_author), randomQuoteAuthor));
    }

    /** Parses the TextViews in the Activity, making the text selectable and links clickable. */
    private void parseTextViews() {
        mMarinerDescriptionTextView.setTextIsSelectable(true);
        mOpDescriptionTextView.setTextIsSelectable(true);
        mMadeByTextView.setTextIsSelectable(true);
        mQuoteTextView.setTextIsSelectable(true);
        mQuoteAuthorTextView.setTextIsSelectable(true);
        mEmailTextView.setTextIsSelectable(true);
        mOpCopyrightTextView.setTextIsSelectable(true);
        mDataAssetNumberTextView.setTextIsSelectable(true);
        mThanksTextView.setTextIsSelectable(true);
        mFontTextView.setTextIsSelectable(true);
        mMarinerTitleTextView.setTextIsSelectable(true);
        mOpDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mEmailTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mFontTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /** Repeats the ship's wheel animation and displays a different data quote. */
    public void repeatAnimation(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            avdWheel.start();
        }
        String[] dataQuotes = getResources().getStringArray(R.array.data_quotes);
        String[] dataQuotesAuthors = getResources().getStringArray(R.array.data_quotes_authors);
        String previousQuote = mQuoteTextView.getText().toString();
        String previousQuoteAuthor = mQuoteAuthorTextView.getText().toString();
        String randomQuote = previousQuote;
        String randomQuoteAuthor = previousQuoteAuthor;
        int randomNumber;

        while (randomQuote.equals(previousQuote)) {
            randomNumber = new Random().nextInt(dataQuotes.length);
            randomQuote = dataQuotes[randomNumber];
            randomQuoteAuthor = dataQuotesAuthors[randomNumber];
        }

        mQuoteTextView.setText(randomQuote);
        mQuoteAuthorTextView.setText(
                String.format(getString(R.string.quote_author), randomQuoteAuthor));
    }
}