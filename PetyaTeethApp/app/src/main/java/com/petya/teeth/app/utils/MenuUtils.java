package com.petya.teeth.app.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.petya.teeth.app.AffirmationActivity;
import com.petya.teeth.app.MainActivity;
import com.petya.teeth.app.R;

public class MenuUtils {

    public static Intent handleMenuItemClick(MenuItem item, Context context) {
        int id = item.getItemId();

        if (id == R.id.affirmationMenu) {
           return new Intent(context, AffirmationActivity.class);
        }
        else if (id == R.id.homeMenu) {
            return new Intent(context, MainActivity.class);
        }

        return null;
    }
}
