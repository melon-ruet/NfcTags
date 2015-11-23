package info.mrmelon.nfctags.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by melon on 10/14/15
 * email - mahabub.melon@nuspay.com
 */
public final class Messages {

    public static void showWirelessSettingsDialog(final Context conn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(conn);
        builder.setMessage("NFC disabled");
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(
                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        conn.startActivity(intent);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((Activity)conn).finish();
                    }
                });
        builder.create().show();
    }

}
