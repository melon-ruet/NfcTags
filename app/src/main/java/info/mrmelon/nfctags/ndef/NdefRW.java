package info.mrmelon.nfctags.ndef;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.TagTechnology;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by melon on 11/23/15
 * email - mahabub.melon@nuspay.com
 */

public class NdefRW {
    private Ndef ndef = null;
    private NdefFormatable ndefFormatable = null;

    public NdefRW(Tag tag) {
        List<String> techList = Arrays.asList(tag.getTechList());
        if(techList.contains(Ndef.class.getName())){
            this.ndef = Ndef.get(tag);
        }
        else if(techList.contains(NdefFormatable.class.getName())){
            this.ndefFormatable = NdefFormatable.get(tag);
        }
    }

    public Ndef getNdef(){
        return ndef;
    }

    public NdefFormatable getNdefFormatable(){
        return ndefFormatable;
    }

    public void NdefConnect(){
        try {
            if(ndef!=null)
                ndef.connect();
            else
                ndefFormatable.connect();
        } catch (IOException e) {
            Log.e("Ndef Connect", "Ndef Connection failed");
        }
    }

    public void NdefClose(){
        try {
            if(ndef!=null)
                ndef.close();
            else
                ndefFormatable.close();
        } catch (IOException e) {
            Log.e("Ndef Close", "Ndef Closing failed");
        }
    }

    public String readNdefRecord(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? new String("UTF-8") : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    public void writeNdefRecords(NdefRecord[] records){
        NdefMessage message = new NdefMessage(records);
        try {
            if(ndef!=null)
                ndef.writeNdefMessage(message);
            else
                ndefFormatable.format(message);
        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }
    }


}
