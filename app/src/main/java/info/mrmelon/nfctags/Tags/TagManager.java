package info.mrmelon.nfctags.Tags;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;

import info.mrmelon.nfctags.Helper.Conversion;

/**
 * Created by melon on 10/14/15
 * email - melon.ruet@gmail.com
 */

public class TagManager {

    //Variables
    private Tag tag;

    //Constants
    public static final int MIFARE_CLASSIC = 1;
    public static final int MIFARE_ULTRALIGHT = 2;
    public static final int UNKNOWN = 10;

    public static final int IN_HEX = 1000;
    public static final int IN_DECIMAL = 1001;

    public TagManager(Intent intent){
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    public final Tag getTag(){
        return tag;
    }

    public final Ndef getNdefTag(){
        return Ndef.get(tag);
    }

    public byte [] getTagID(){
        return tag.getId();
    }

    public String getTagID(int conversionType){
        if(conversionType == IN_HEX)
            return Conversion.ByteArrayToHexString(getTagID());
        else if (conversionType == IN_DECIMAL)
            return String.valueOf(Conversion.byte2Number(getTagID()));
        return null;
    }

    /*public String [] getTechList(){
        return tag.getTechList();
    }*/

    public int getTagType(){
        if(MifareClassic.get(tag) != null)
            return MIFARE_CLASSIC;
        else if (MifareUltralight.get(tag) != null)
            return MIFARE_ULTRALIGHT;
        return UNKNOWN;
    }

    public Object getTagClassAsObject(){
        switch (getTagType()){
            case MIFARE_CLASSIC: return new MifareClassicTag(tag);
            case MIFARE_ULTRALIGHT: return new MifareUltralightTag(tag);
            default: return null;
        }
    }

}
