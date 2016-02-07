package info.mrmelon.nfctags.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;

import info.mrmelon.nfctags.utils.Messages;


public class NFChelper {

    private NfcAdapter _nfcAdapter;
    private PendingIntent _nfcPendingIntent;
    private Context context;

    public NFChelper(Context context){
        this.context = context;
    }

    public void onCreate() {
        try {
            _nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }

        _nfcPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,
                context.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    public final void onResume(){
        if (_nfcAdapter != null) {
            if (!_nfcAdapter.isEnabled()) {
                Messages.showWirelessSettingsDialog(context);
            }
            _nfcAdapter.enableForegroundDispatch((Activity) context, _nfcPendingIntent, null,
                    null);
        }
    }

    public final void onPause(){
        if (_nfcAdapter != null) {
            _nfcAdapter.disableForegroundDispatch((Activity) context);
        }
    }

}
