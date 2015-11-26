package info.mrmelon.nfctags.tags;

import android.annotation.TargetApi;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by melon on 11/23/15
 * email - mahabub.melon@nuspay.com
 */

public class NdefTag {
    private Ndef ndef = null;
    private NdefFormatable ndefFormatable = null;
    private Tag tag;

    public NdefTag(Tag tag) {
        this.tag = tag;
        List<String> techList = Arrays.asList(tag.getTechList());
        if(techList.contains(Ndef.class.getName())){
            this.ndef = Ndef.get(tag);
        }

        if(techList.contains(NdefFormatable.class.getName())){
            this.ndefFormatable = NdefFormatable.get(tag);
        }
    }

    public Ndef getNdef(){
        return ndef;
    }

    public String ndefRecordToString(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();
        if(payload.length>0) {
            String textEncoding = ((payload[0] & 128) == 0) ? new String("UTF-8") : "UTF-16";
            int languageCodeLength = payload[0] & 0063;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }
        else
            return null;
    }

    public NdefMessage getNdefMessage(boolean isCachedMessage){
        if(ndef!=null){
            try {
                ndef.connect();
                NdefMessage message;
                if(isCachedMessage)
                    message = ndef.getCachedNdefMessage();
                else
                    message = ndef.getNdefMessage();
                ndef.close();
                return message;
            } catch (IOException | FormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void writeNdefRecords(NdefRecord[] records){
        NdefMessage message = new NdefMessage(records);
        try {
            if(ndef!=null) {
                if (!ndef.isWritable()) {
                    Log.e("Tag","Tag is read-only");
                    return;
                }
                if(ndef.getMaxSize()>message.getByteArrayLength()) {
                    Log.e("Message size", "ndef message size exceeds");
                    return;
                }
                ndef.connect();
                ndef.writeNdefMessage(message);
                ndef.close();
            }
            else if(ndefFormatable!=null) {
                ndefFormatable.connect();
                ndefFormatable.format(message);
                ndefFormatable.close();
                if(ndef==null)
                    ndef = Ndef.get(tag);
            }
        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }
    }


}
