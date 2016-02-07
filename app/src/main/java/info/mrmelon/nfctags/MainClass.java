package info.mrmelon.nfctags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import info.mrmelon.nfctags.enums.TagType;
import info.mrmelon.nfctags.nfc.NFChelper;
import info.mrmelon.nfctags.security.CommandDirectory;
import info.mrmelon.nfctags.tags.NfcATag;
import info.mrmelon.nfctags.nfc.TagManager;

/**
 * Created by melon on 10/14/15
 * email - mahabub.melon@nuspay.com
 */
public class MainClass extends Activity {

    private NFChelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        helper = new NFChelper(this);
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


        /*if (manager.isTechSupported(TagManager.MIFARE_ULTRALIGHT)) {
            MifareUltralightTag ultralight = (MifareUltralightTag) manager.getTagClassAsObject(TagManager.MIFARE_ULTRALIGHT);
            ultralight.connectUltralightTag();
            int page = 4;
            //Log.e("wr", ultralight.writePage(page, new byte[]{1, 2, 3, 4}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(0)));
            //ultralight.lockPage(page, true);
            //Log.e("write", ultralight.writePage(page, new byte[]{1, 2, 3, 5}) + "");
            Log.e("read", ultralight.getUltralight().getType()+"");

            *//*ultralight.lockPage(page, false);
            Log.e("write", ultralight.writePage(page, new byte[]{1, 2, 3, 4}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(page)));*//*
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
        } else*/ if (manager.isTechSupported(TagType.NFCA)) {
            NfcATag nfcA = (NfcATag) manager.getTagClassAsObject(TagType.NFCA);
            nfcA.connect();
            byte [] command = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            System.arraycopy(manager.getTagID(), 0, command, 3, manager.getTagID().length);

            Log.e("CRC", Arrays.toString(CommandDirectory.ComputeCrc(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00})) + "   " + Arrays.toString(command));

            byte [] command2 = new byte[command.length+2];
            System.arraycopy(command, 0, command2, 0, command.length);
            System.arraycopy(CommandDirectory.ComputeCrc(command), 0, command2, command.length, 2);
            //Log.e("comm2", Arrays.toString(command2));
            Log.e("comm3", Arrays.toString(nfcA.read(new byte[]{0x26})));
            Log.e("nfca", Arrays.toString(nfcA.read(command2)));
            nfcA.close();
        }

    }
}
