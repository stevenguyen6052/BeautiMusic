package com.example.windows10gamer.beautimusic.view.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.windows10gamer.beautimusic.R;

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setToolbar(titleToolbar());
        initView();
        initData();
    }

    public void setToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {

            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);

            if (getLayoutResourceId() != R.layout.activity_home)// màn hình home k có action back
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    public abstract void initView();

    public abstract void initData();

    public abstract int getLayoutResourceId();

    public abstract String titleToolbar();


}
