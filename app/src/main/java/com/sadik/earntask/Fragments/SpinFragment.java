package com.sadik.earntask.Fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.sadik.earntask.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SpinFragment extends Fragment {

    // ==================== CONFIGURATION ====================
    // TODO: Configure your spin settings
    private static final int MAX_DAILY_SPINS = 10;
    private static final long SPIN_COOLDOWN_MS = 5 * 60 * 1000; // 5 minutes in milliseconds

    // TODO: Configure your rewards (must match the number of segments in your wheel)
    private static final int[] REWARD_VALUES = {10, 25, 50, 100, 5, 15, 20, 200};
    private static final String[] REWARD_NAMES = {"10 Coins", "25 Coins", "50 Coins", "100 Coins",
            "5 Coins", "15 Coins", "20 Coins", "200 Coins"};

    // Animation settings
    private static final int SPIN_DURATION_MS = 4000;
    private static final int SPIN_ROTATIONS = 5;
    // =====================================================

    private static final String PREFS_NAME = "spin_prefs";
    private static final String KEY_SPINS_LEFT = "spins_left";
    private static final String KEY_LAST_SPIN_DATE = "last_spin_date";
    private static final String KEY_NEXT_SPIN_TIME = "next_spin_time";

    private ImageView imgWheel;
    private TextView tvSpinsLeft, tvTimer, tvWheelMessage, tvSpinReward;
    private CardView btnSpin;
    private SharedPreferences prefs;
    private CountDownTimer countDownTimer;
    private boolean isSpinning = false;
    private int currentRewardIndex = -1;
    private long nextSpinTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spin, container, false);

        initViews(view);
        checkAndResetDailySpins();
        updateSpinCount();
        startTimerIfNeeded();

        return view;
    }

    private void initViews(View view) {
        imgWheel = view.findViewById(R.id.imgWheel);
        tvSpinsLeft = view.findViewById(R.id.tvSpinsLeft);
        tvTimer = view.findViewById(R.id.tvTimer);
        tvWheelMessage = view.findViewById(R.id.tvWheelMessage);
        tvSpinReward = view.findViewById(R.id.tvSpinReward);
        btnSpin = view.findViewById(R.id.btnSpin);

        prefs = requireContext().getSharedPreferences(PREFS_NAME, requireContext().MODE_PRIVATE);

        btnSpin.setOnClickListener(v -> {
            if (!isSpinning) {
                startSpin();
            }
        });

        // Long click to show configuration info (for development/debugging)
        btnSpin.setOnLongClickListener(v -> {
            showConfigurationInfo();
            return true;
        });
    }

    private void showConfigurationInfo() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Spin Configuration")
                .setMessage("Max Daily Spins: " + MAX_DAILY_SPINS +
                        "\nCooldown: " + (SPIN_COOLDOWN_MS / 1000 / 60) + " minutes" +
                        "\n\nAd Integration: None (Ready for your preferred ad network)")
                .setPositiveButton("OK", null)
                .show();
    }

    private void checkAndResetDailySpins() {
        String lastSpinDate = prefs.getString(KEY_LAST_SPIN_DATE, "");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (!lastSpinDate.equals(currentDate)) {
            // It's a new day, reset spins
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_SPINS_LEFT, MAX_DAILY_SPINS);
            editor.putString(KEY_LAST_SPIN_DATE, currentDate);
            editor.putLong(KEY_NEXT_SPIN_TIME, 0); // Reset cooldown
            editor.apply();
        }
    }

    private void updateSpinCount() {
        int spinsLeft = prefs.getInt(KEY_SPINS_LEFT, MAX_DAILY_SPINS);
        tvSpinsLeft.setText(String.valueOf(spinsLeft));

        if (spinsLeft <= 0) {
            btnSpin.setEnabled(false);
            btnSpin.setAlpha(0.5f);
            tvWheelMessage.setText("No spins left today. Come back tomorrow!");
        } else {
            btnSpin.setEnabled(true);
            btnSpin.setAlpha(1.0f);
            tvWheelMessage.setText("Tap SPIN to try your luck");
        }
    }

    private void startTimerIfNeeded() {
        nextSpinTime = prefs.getLong(KEY_NEXT_SPIN_TIME, 0);

        if (nextSpinTime > System.currentTimeMillis()) {
            // There's still time left in the cooldown
            long timeLeft = nextSpinTime - System.currentTimeMillis();
            startCountdownTimer(timeLeft);
        } else {
            // No cooldown
            tvTimer.setText("00:00:00");
            btnSpin.setEnabled(true);
            btnSpin.setAlpha(1.0f);
        }
    }

    private void startCountdownTimer(long timeLeft) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        btnSpin.setEnabled(false);
        btnSpin.setAlpha(0.5f);

        countDownTimer = new CountDownTimer(timeLeft, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                long totalSeconds = millisUntilFinished / 1000;

                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                tvTimer.setText(String.format(
                        Locale.getDefault(),
                        "%02d:%02d:%02d",
                        hours, minutes, seconds
                ));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00:00");
                btnSpin.setEnabled(true);
                btnSpin.setAlpha(1.0f);
                tvWheelMessage.setText("Tap SPIN to try your luck");
            }

        }.start();

    }

    private void startSpin() {
        int spinsLeft = prefs.getInt(KEY_SPINS_LEFT, MAX_DAILY_SPINS);

        if (spinsLeft <= 0) {
            Toast.makeText(getContext(), "No spins left today. Come back tomorrow!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nextSpinTime > System.currentTimeMillis()) {
            Toast.makeText(getContext(), "Please wait for the cooldown to end", Toast.LENGTH_SHORT).show();
            return;
        }

        isSpinning = true;
        btnSpin.setEnabled(false);
        tvWheelMessage.setText("Spinning...");

        // Determine the reward
        Random random = new Random();
        currentRewardIndex = random.nextInt(REWARD_VALUES.length);

        // Calculate the rotation angle (multiple full rotations + final position)
        int segmentAngle = 360 / REWARD_VALUES.length;
        int targetAngle = 360 * SPIN_ROTATIONS + (currentRewardIndex * segmentAngle) + (segmentAngle / 2);

        // Create and start the rotation animation
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imgWheel, "rotation", 0f, targetAngle);
        rotateAnimator.setDuration(SPIN_DURATION_MS);
        rotateAnimator.setInterpolator(new DecelerateInterpolator());
        rotateAnimator.addUpdateListener(animation -> {
            // You can add additional effects during rotation if needed
        });

     //   rotateAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
     //       @Override
     //       public void onAnimationEnd(android.animation animation) {
                // Animation ended
                // ==================== AD INTEGRATION POINT ====================
                // TODO: Integrate your preferred ad network here
                // Examples:
                // - AdMob: showRewardedAd()
                // - AppLovin: AppLovinAds.showRewardedAd()
                // - Unity Ads: UnityAds.show("rewardedVideo")
                // - IronSource: IronSource.showRewardedVideo()
                //
                // After the ad is successfully watched, call:
                // giveRewardToUser();
                //
                // For now, we'll give the reward directly:
      //          showAdAndGiveReward();
                // =============================================================
      //      }
      //  });

        rotateAnimator.start();
    }

    // ==================== AD INTEGRATION METHODS ====================
    // TODO: Replace these methods with your preferred ad network implementation

    private void showAdAndGiveReward() {
        // This is a placeholder method showing where to integrate ads
        // Currently, it simulates watching an ad and gives the reward directly

        // Simulate ad loading/watching delay (remove this when integrating real ads)
        tvWheelMessage.setText("Loading reward...");

        // Simulate 2-second ad watch time (remove this when integrating real ads)
        new android.os.Handler().postDelayed(() -> {
            giveRewardToUser();
        }, 2000);
    }

    /*
    // Example AdMob integration (uncomment and modify for AdMob):
    private void loadRewardedAd() {
        rewardedAd = new RewardedAd(requireContext(), YOUR_ADMOB_AD_UNIT_ID);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() { }
            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) { }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    private void showRewardedAd() {
        if (rewardedAd != null && rewardedAd.isLoaded()) {
            rewardedAd.show(requireActivity(), new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(RewardItem reward) {
                    giveRewardToUser();
                }
                @Override
                public void onRewardedAdClosed() { }
                @Override
                public void onRewardedAdFailedToShow(int errorCode) { }
            });
        }
    }
    */

    /*
    // Example AppLovin integration (uncomment and modify for AppLovin):
    private void showAppLovinRewardedAd() {
        AppLovinAds.getInstance().showRewardedAd(requireActivity(), new AppLovinAdRewardListener() {
            @Override
            public void userRewarded(String adUnitIdentifier, AppLovinReward reward) {
                giveRewardToUser();
            }
            @Override
            public void adHidden(AppLovinAd ad) { }
            @Override
            public void adFailedToDisplay(AppLovinAd ad, AppLovinAdError error) { }
        });
    }
    */
    // =============================================================

    private void giveRewardToUser() {
        // Update spins left
        int spinsLeft = prefs.getInt(KEY_SPINS_LEFT, MAX_DAILY_SPINS) - 1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SPINS_LEFT, spinsLeft);

        // Set next spin time (cooldown)
        nextSpinTime = System.currentTimeMillis() + SPIN_COOLDOWN_MS;
        editor.putLong(KEY_NEXT_SPIN_TIME, nextSpinTime);

        editor.apply();

        // Update UI
        updateSpinCount();
        startCountdownTimer(SPIN_COOLDOWN_MS);

        // Show reward message
        String rewardMessage = "Congratulations! You won " + REWARD_NAMES[currentRewardIndex] + "!";
        tvWheelMessage.setText(rewardMessage);
        tvSpinReward.setText(REWARD_NAMES[currentRewardIndex] + " 🎉");

        // Here you would add the reward to the user's balance
        // For example: updateUserBalance(REWARD_VALUES[currentRewardIndex]);

        // Reset spinning state
        isSpinning = false;

        // Show a toast
        Toast.makeText(getContext(), rewardMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}