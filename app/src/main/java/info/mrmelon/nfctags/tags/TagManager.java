package info.mrmelon.nfctags.tags;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;

import java.util.Arrays;
import java.util.List;

import info.mrmelon.nfctags.utils.Conversion;

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
    public static final int NDEF = 3;
    public static final int NDEF_FORMATTABLE = 4;
    public static final int UNKNOWN = 10;

    public static final int IN_HEX = 1000;
    public static final int IN_DECIMAL = 1001;

    public TagManager(Intent intent){
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    public final Tag getTag(){
        return tag;
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


    public boolean isTechSupported(int TECH_TYPE){
        List<String> techList = Arrays.asList(tag.getTechList());
        switch (TECH_TYPE){
            case MIFARE_CLASSIC: return techList.contains(MifareClassic.class.getName());
            case MIFARE_ULTRALIGHT: return techList.contains(MifareUltralight.class.getName());
            case NDEF: return techList.contains(Ndef.class.getName());
            case NDEF_FORMATTABLE: return techList.contains(NdefFormatable.class.getName());
            default: return false;
        }
    }

    public Object getTagClassAsObject(int TECH_TYPE){
        switch (TECH_TYPE){
            case MIFARE_CLASSIC: return new MifareClassicTag(tag);
            case MIFARE_ULTRALIGHT: return new MifareUltralightTag(tag);
            case NDEF: return new NdefTag(tag);
            case NDEF_FORMATTABLE: return new NdefTag(tag);
            default: return null;
        }
    }

}
