package info.mrmelon.nfctags.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import info.mrmelon.nfctags.enums.ConversionType;
import info.mrmelon.nfctags.enums.TagType;
import info.mrmelon.nfctags.tags.MifareClassicTag;
import info.mrmelon.nfctags.tags.MifareUltralightTag;
import info.mrmelon.nfctags.tags.NdefTag;
import info.mrmelon.nfctags.tags.NfcATag;
import info.mrmelon.nfctags.utils.Conversion;

/**
 * Created by melon on 10/14/15
 * email - melon.ruet@gmail.com
 */

public class TagManager {

    private Tag tag;


    public TagManager(Intent intent){
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    public final Tag getTag(){
        return tag;
    }

    public byte [] getTagID(){
        return tag.getId();
    }

    public String getTagID(ConversionType conversionType){
        switch (conversionType){
            case HEX: return Conversion.byteArrayToHexString(getTagID());
            case DECIMAL: return String.valueOf(Conversion.byte2Number(getTagID()));
            case BINARY: return new BigInteger(getTagID()).toString(2);
            default: return null;
        }
    }


    public boolean isTechSupported(TagType TECH_TYPE){
        List<String> techList = Arrays.asList(tag.getTechList());
        switch (TECH_TYPE){
            case MIFARE_CLASSIC: return techList.contains(MifareClassic.class.getName());
            case MIFARE_ULTRALIGHT: return techList.contains(MifareUltralight.class.getName());
            case NDEF: return techList.contains(Ndef.class.getName());
            case NDEF_FORMATTABLE: return techList.contains(NdefFormatable.class.getName());
            case NFCA: return techList.contains(NfcA.class.getName());
            default: return false;
        }
    }

    public Object getTagClassAsObject(TagType TECH_TYPE){
        switch (TECH_TYPE){
            case MIFARE_CLASSIC: return new MifareClassicTag(tag);
            case MIFARE_ULTRALIGHT: return new MifareUltralightTag(tag);
            case NDEF: return new NdefTag(tag);
            case NDEF_FORMATTABLE: return new NdefTag(tag);
            case NFCA: return new NfcATag(tag);
            default: return null;
        }
    }

}
