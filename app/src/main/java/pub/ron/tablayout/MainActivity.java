package pub.ron.tablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

import pub.ron.tablayout.adapter.TitledFragmentPagerAdapter;
import pub.ron.tablayout.widget.TabLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewPager viewPager = findViewById(R.id.viewpager);
        List<Fragment> fragments = Arrays.<Fragment>asList(new TestFragment(), new TestFragment(), new TestFragment());
        viewPager.setAdapter(new TitledFragmentPagerAdapter(getSupportFragmentManager(),
                fragments,
                Arrays.asList("全部", "未处理", "已处理")));
        this.<TabLayout>findViewById(R.id.tablayout).setup(viewPager);
    }

}
