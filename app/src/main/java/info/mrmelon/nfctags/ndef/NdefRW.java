package info.mrmelon.nfctags.ndef;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by melon on 11/23/15
 * email - mahabub.melon@nuspay.com
 */

public class NdefRW {
    private Ndef Ndef;

    public NdefRW(Ndef ndefTech) {
        this.Ndef = ndefTech;
    }

    public Ndef getNdef(){
        return Ndef;
    }

    public void NdefConnect(){
        try {
            Ndef.connect();
        } catch (IOException e) {
            Log.e("Ndef Connect", "Ndef Connection failed");
        }
    }

    public void NdefClose(){
        try {
            Ndef.close();
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
            Ndef.writeNdefMessage(message);
        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }
    }


}
