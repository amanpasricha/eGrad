package com.egrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AppCompatActivity implements ForgotPasswordBottomSheet.ForgotPasswordListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(new ViewPagerLoginAdapter(getSupportFragmentManager(), getLifecycle()));

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == LoginFragment.STUDENT_LOGIN)
                tab.setText(R.string.student);
            else if (position == LoginFragment.FACULTY_LOGIN)
                tab.setText(R.string.faculty);
        }).attach();
    }

    @Override
    public void sendPasswordResetLink(String emailAddress) {
//        TODO:
        Toast.makeText(this, "Password reset link has been sent to your email address", Toast.LENGTH_LONG).show();
    }
}

class ViewPagerLoginAdapter extends FragmentStateAdapter {

    public ViewPagerLoginAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return LoginFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
