package info.mrmelon.nfctags;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

import info.mrmelon.nfctags.Tags.TagManager;
import info.mrmelon.nfctags.ndef.NdefRW;
import info.mrmelon.nfctags.ndef.NdefRecordCreator;

/**
 * Created by melon on 10/14/15
 * email - mahabub.melon@nuspay.com
 */
public class MainClass extends Activity{

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

        /*if(manager.getTagType() == TagManager.MIFARE_ULTRALIGHT){
            MifareUltralightTag ultralight = (MifareUltralightTag) manager.getTagClassAsObject();
            ultralight.connectUltralightTag();
            int page =4;
            *//*Log.e("wr", ultralight.writePage(page, new byte[]{1, 2, 3, 4}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(4)));*//*
            //ultralight.lockPage(page, true);
            Log.e("write", ultralight.writePage(page, new byte[]{1, 2, 3, 5}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(page)));

            ultralight.lockPage(page, false);
            Log.e("write", ultralight.writePage(page, new byte[]{1, 2, 3, 4}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(page)));
            ultralight.closeUltralightTag();
        }
        else if(manager.getTagType() == TagManager.MIFARE_CLASSIC){
            MifareClassicTag classic = (MifareClassicTag) manager.getTagClassAsObject();
            classic.connectMifareClassic();
            classic.authCardWithAnyKey(0);
            Log.e("classc", Arrays.deepToString(classic.getC(0)));
            classic.closeMifareClassic();
        }*/

        Log.e("Tech", Arrays.toString(manager.getTag().getTechList()));

        /*NdefFormatable formatable = NdefFormatable.get(manager.getTag());
        try {
            formatable.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NdefRecord [] record = new NdefRecord[2];
        record[0] = NdefRecordCreator.createTextRecord("Melon", Locale.ENGLISH, true);
        record[1] = NdefRecordCreator.createTelephoneRecord("+8801721366503");

        try {
            formatable.format(new NdefMessage(record));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }


        try {
            formatable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/



        NdefRW rw = new NdefRW(manager.getTag());
        NdefRecord [] record = new NdefRecord[2];
        record[0] = NdefRecordCreator.createTextRecord("Melon", Locale.ENGLISH, true);
        record[1] = NdefRecordCreator.createTelephoneRecord("+8801721366503");
        rw.NdefConnect();
        rw.writeNdefRecords(record);


        NdefRecord[] rec  = rw.getNdef().getCachedNdefMessage().getRecords();
        for (int i=0; i<rec.length; i++){
            try {
                Log.e(i+"", rw.readNdefRecord(rec[i]));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        rw.NdefClose();
    }
}
