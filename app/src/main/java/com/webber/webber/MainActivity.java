package com.webber.webber;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.webber.webber.authenticator.AuthenticatorActivity;
import com.webber.webber.general.PreferenceManager;
import com.webber.webber.mainui.WebberActivity;


public class MainActivity extends Activity {

    public static final String PREFS_NAME = "WebberPrefsFile";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if it is possible to login without asking for username/password
        //if only one account and password is saved, authenticate

        Intent intentAuthenticationService = new Intent(this, com.webber.webber.authenticator.AuthenticationService.class);
        startService(intentAuthenticationService);

        //try to get account from account manager
        AccountManager accountManager = AccountManager.get(this);

        Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE);

        //clearAccounts(accounts,accountManager);

        if (accounts.length == 0) {
            startActivity(new Intent(this, AuthenticatorActivity.class));
        } else if (accounts.length == 1) {
            //TODO: load the data
            String uid = accountManager.getUserData(accounts[0], "uid");
            Intent intent = new Intent(this, WebberActivity.class);
            intent.putExtra("uid", uid);
            PreferenceManager.setCurrentUID(this, uid);
            startActivity(intent);
        } else {
            Intent intent = AccountManager.newChooseAccountIntent(null, null,
                    new String[]{Constants.ACCOUNT_TYPE}, true, null, null,
                    null, null);
            startActivityForResult(intent, 0x20);

        }
    }

    private void clearAccounts(Account[] accounts, AccountManager accountManager) {
        for (int index = 0; index < accounts.length; index++) {
            accountManager.removeAccount(accounts[index], null, null);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
