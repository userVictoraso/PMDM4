package com.example.victoraso.pmdm4.EjercicioUno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.victoraso.pmdm4.R;
import com.example.victoraso.pmdm4.databinding.ActivityMalagaBinding;

public class MalagaActivity extends AppCompatActivity {
    ActivityMalagaBinding binding;
    Animation fadeIn, rotate;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private int gallery[] = {R.drawable.imageone, R.drawable.imagetwo, R.drawable.imagethree, R.drawable.imagefour};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMalagaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //SET INITIAL BUTTON
        startAnimation();
    }

    private void startAnimation() {
        binding.buttonStartAnimation.setOnClickListener(v -> {
            binding.buttonStartAnimation.setEnabled(false);
            fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
            binding.textViewAnimated.setVisibility(View.VISIBLE);
            binding.textViewAnimated.startAnimation(fadeIn);
            binding.textViewAnimated.startAnimation(rotate);

            //Terminar la animación tras 3 segundos.
            new Handler().postDelayed(() -> {
                binding.textViewAnimated.clearAnimation();
                showViewPager();
            }, 3000);
        });
    }

    private void showViewPager() {
        /**Hacemos no visible el boton y el textView**/
        binding.textViewAnimated.setVisibility(View.INVISIBLE);
        binding.buttonStartAnimation.setVisibility(View.INVISIBLE);
        viewPager = binding.viewpagger;
        /**set images and rate**/
        viewPagerAdapter = new ViewPagerAdapter(MalagaActivity.this, gallery);
        viewPager.setAdapter(viewPagerAdapter);
        binding.editTextRate.setVisibility(View.VISIBLE);
        /**set audio and rate empty**/
        ViewPager.OnPageChangeListener player = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg1) {
                /**sonido**/
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
                mp.start();
                /**vaciar campo**/
                binding.editTextRate.setText("");
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        };
        viewPager.addOnPageChangeListener(player);
    }
}
