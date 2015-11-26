package info.mrmelon.nfctags;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

import info.mrmelon.nfctags.security.CommandDirectory;
import info.mrmelon.nfctags.tags.MifareClassicTag;
import info.mrmelon.nfctags.tags.MifareUltralightTag;
import info.mrmelon.nfctags.tags.NfcATag;
import info.mrmelon.nfctags.tags.TagManager;
import info.mrmelon.nfctags.tags.NdefTag;
import info.mrmelon.nfctags.ndef.NdefRecordCreator;

/**
 * Created by melon on 10/14/15
 * email - mahabub.melon@nuspay.com
 */
public class MainClass extends Activity {

    private TagHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        helper = new TagHelper(this);
        helper.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        TagManager manager = new TagManager(intent);

        Log.e("Tech", Arrays.toString(manager.getTag().getTechList()));


        if (manager.isTechSupported(TagManager.MIFARE_ULTRALIGHT)) {
            MifareUltralightTag ultralight = (MifareUltralightTag) manager.getTagClassAsObject(TagManager.MIFARE_ULTRALIGHT);
            ultralight.connectUltralightTag();
            int page = 4;
            //Log.e("wr", ultralight.writePage(page, new byte[]{1, 2, 3, 4}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(0)));
            //ultralight.lockPage(page, true);
            //Log.e("write", ultralight.writePage(page, new byte[]{1, 2, 3, 5}) + "");
            Log.e("read", ultralight.getUltralight().getType()+"");

            /*ultralight.lockPage(page, false);
            Log.e("write", ultralight.writePage(page, new byte[]{1, 2, 3, 4}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(page)));*/
            ultralight.closeUltralightTag();
        } else if (manager.isTechSupported(TagManager.MIFARE_CLASSIC)) {
            MifareClassicTag classic = (MifareClassicTag) manager.getTagClassAsObject(TagManager.MIFARE_CLASSIC);
            classic.connectMifareClassic();
            classic.authCardWithAnyKey(0);
            Log.e("classic", Arrays.deepToString(classic.getC(0)));
            classic.closeMifareClassic();
        } else if (manager.isTechSupported(TagManager.NDEF) || manager.isTechSupported(TagManager.NDEF_FORMATTABLE)) {
            NdefTag rw = new NdefTag(manager.getTag());
            NdefRecord[] record = new NdefRecord[2];
            record[0] = NdefRecordCreator.createTextRecord("Melon", Locale.ENGLISH, true);
            record[1] = NdefRecordCreator.createTelephoneRecord("+880172136");
            //rw.writeNdefRecords(record);

            NdefRecord[] rec = rw.getNdefMessage(false).getRecords();

            for (int i = 0; i < rec.length; i++) {
                try {
                    Log.e(i + "", rw.ndefRecordToString(rec[i]));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else if (manager.isTechSupported(TagManager.NFCA)) {
            NfcATag nfcA = (NfcATag) manager.getTagClassAsObject(TagManager.NFCA);
            nfcA.connect();
            Log.e("nfca", Arrays.toString(nfcA.read(CommandDirectory.getReadCommand(0, 0, 0))));
            nfcA.close();
        }

    }
}
