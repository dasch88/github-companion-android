package com.aptera.githubcompanion.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.BaseActivity;
import com.aptera.githubcompanion.lib.businesslogic.IUserManager;
import com.aptera.githubcompanion.lib.model.User;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    // UI references.
    private TextView mTxtUsername;
    private TextView mTxtName;
    private TextView mTxtEmail;
    private TextView mTxtFollowerCount;
    private TextView mTxtFollowingCount;

    @Inject
    public IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtUsername = (TextView) findViewById(R.id.txtUsername);
        mTxtName = (TextView) findViewById(R.id.txtName);
        mTxtEmail = (TextView) findViewById(R.id.txtEmail);
        mTxtFollowerCount = (TextView) findViewById(R.id.txtFollowerCount);
        mTxtFollowingCount = (TextView) findViewById(R.id.txtFollowingCount);

        //make sure that there is a currently logged in user. if not, load up the login activity
        if(userManager.getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else {
            setCurrentUserInfo();
        }
    }

    private void setCurrentUserInfo() {
        User currentUser = userManager.getCurrentUser();
        mTxtUsername.setText(currentUser.getLogin());
        mTxtName.setText(currentUser.getName());
        mTxtEmail.setText(currentUser.getEmail());
        mTxtFollowerCount.setText(currentUser.getFollowers().toString());
        mTxtFollowingCount.setText(currentUser.getFollowing().toString());
    }
}
