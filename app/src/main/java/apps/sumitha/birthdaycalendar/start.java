package apps.sumitha.birthdaycalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matthewtamlin.sliding_intro_screen_library.background.BackgroundManager;
import com.matthewtamlin.sliding_intro_screen_library.background.ColorBlender;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.transformers.MultiViewParallaxTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class start extends IntroActivity {

    /**
     * Colors to use for the blended background: blue, pink, purple.
     */
    private static final int[] BACKGROUND_COLORS = {0xff304FFE, 0xffcc0066, 0xff9900ff};

    /**
     * Name of the shared preferences which hold a key for preventing the intro screen from
     * displaying again once completed.
     */
    public static final String DISPLAY_ONCE_PREFS = "display_only_once_spfile";

    /**
     * Key to use in {@code DISPLAY_ONCE_PREFS} to prevent the intro screen from displaying again
     * once completed.
     */
    public static final String DISPLAY_ONCE_KEY = "display_only_once_spkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoActionBar); // Looks good when the status bar is hidden

        super.onCreate(savedInstanceState);

// Skip to the next Activity if the user has previously completed the introduction
        if (introductionCompletedPreviously()) {
            final Intent nextActivity = new Intent(this, getUserdetails.class);
            finish();
            startActivity(nextActivity);
        }

        // hideStatusBar();
        configureTransformer();
        configureBackground();

        //setContentView(R.layout.activity_start);
    }

    @Override
    protected Collection<Fragment> generatePages(Bundle savedInstanceState) {
        // This variable holds the pages while they are being created
        final ArrayList<Fragment> pages = new ArrayList<>();


        List<String> x = new ArrayList<>(Arrays.asList("Birthday Reminder", "Super Easy", "Notifies you"));
        List<String> y = new ArrayList<>(Arrays.asList("Be thoughtful! Remember your friends on their birthday",
                "Easily add friends birthdays and let the app do the rest!", "Get yourself notified on your loved ones birthdays!"));

        ArrayList<Integer> z = new ArrayList<>(Arrays.asList(R.drawable.i1, R.drawable.i2, R.drawable.i3));
        // Create as many pages as there are background colors
        for (int i = 0; i < BACKGROUND_COLORS.length; i++) {
            final NumberFragment fragment = new NumberFragment();
            fragment.settitle(y.get(i));
            fragment.setdesc(x.get(i));
            fragment.setimg(z.get(i));
            pages.add(fragment);
        }

        return pages;
    }

    @Override
    @SuppressLint("CommitPrefEdits") // Expected behaviour here
    protected IntroButton.Behaviour generateFinalButtonBehaviour() {
        /* The pending changes to the shared preferences editor will be applied when the
         * introduction is successfully completed. By setting a flag in the pending edits and
		 * checking the status of the flag when the activity starts, the introduction screen can
		 * be skipped if it has previously been completed.
		 */
        final SharedPreferences sp = getSharedPreferences(DISPLAY_ONCE_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor pendingEdits = sp.edit().putBoolean(DISPLAY_ONCE_KEY, true);

        // Define the next activity intent and create the Behaviour to use for the final button

        final Intent nextActivity = new Intent(this, getUserdetails.class);

        return new IntroButton.ProgressToNextActivity(nextActivity, pendingEdits);

    }

    /**
     * Checks for a shared preference flag indicating that the introduction has been completed
     * previously.
     *
     * @return true if the introduction has been completed before, false otherwise
     */
    private boolean introductionCompletedPreviously() {
        final SharedPreferences sp = getSharedPreferences(DISPLAY_ONCE_PREFS, MODE_PRIVATE);
        return sp.getBoolean(DISPLAY_ONCE_KEY, false);
    }

    /**
     * Sets this IntroActivity to use a MultiViewParallaxTransformer page transformer.
     */
    private void configureTransformer() {
        final MultiViewParallaxTransformer transformer = new MultiViewParallaxTransformer();
        transformer.withParallaxView(R.id.titleda, 1.2f);
        setPageTransformer(false, transformer);
    }

    /**
     * Sets this IntroActivity to use a ColorBlender background manager.
     */
    private void configureBackground() {
        final BackgroundManager backgroundManager = new ColorBlender(BACKGROUND_COLORS);
        setBackgroundManager(backgroundManager);
    }
}
