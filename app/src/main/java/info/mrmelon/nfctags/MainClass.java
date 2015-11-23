package info.mrmelon.nfctags;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import info.mrmelon.nfctags.Tags.MifareClassicTag;
import info.mrmelon.nfctags.Tags.MifareUltralightTag;
import info.mrmelon.nfctags.Tags.TagManager;

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

        if(manager.getTagType() == TagManager.MIFARE_ULTRALIGHT){
            MifareUltralightTag ultralight = (MifareUltralightTag) manager.getTagClassAsObject();
            ultralight.connectUltralightTag();
            int page =4;
            /*Log.e("wr", ultralight.writePage(page, new byte[]{1, 2, 3, 4}) + "");
            Log.e("read", Arrays.toString(ultralight.readPage(4)));*/
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
        }
        

    }
}
