package info.mrmelon.nfctags;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by melon on 11/23/15
 * email - mahabub.melon@nuspay.com
 */

public class NDEF {
    private Ndef Ndef;

    public NDEF(Ndef ndefTech) {
        this.Ndef = ndefTech;
    }


    public NdefRecord createNdefAppRecord(String applicationPackageName) {
        return NdefRecord.createApplicationRecord(applicationPackageName);
    }

    public NdefRecord createTextRecord(String payload, Locale locale, boolean isEncodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = isEncodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = isEncodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public NdefRecord createUrlRecord(String url) throws UrlInvalidException {
        if(!url.startsWith("http"))
            url = "http://" + url;
        Pattern pattern = Pattern.compile("(http://|https://)(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
        if(pattern.matcher(url).matches())
            return NdefRecord.createUri(url);
        else{
            throw new UrlInvalidException("Invalid URL");
        }
    }

    public NdefRecord createEmailRecord(String to, String cc, String bcc, String subject, String message){
        String emailPayload = "mailto:" + to;
        if(!subject.isEmpty())
        {
            emailPayload+="?subject="+subject;
        }
        if(!cc.isEmpty())
        {
            emailPayload+="&cc="+cc;
        }
        if(!bcc.isEmpty())
        {
            emailPayload+="&bcc="+bcc;
        }
        if(!message.isEmpty())
        {
            emailPayload+="&body="+message;
        }
        return NdefRecord.createUri(emailPayload);
    }

    public NdefRecord createSMSRecord(String [] numbers, String message){
        String toNumbers = "";
        for (String number: numbers){
            toNumbers+=(number+";");
        }
        return NdefRecord.createUri("sms:"+toNumbers+"?body="+message);
    }

    public NdefRecord createTelephoneRecord(String telephoneNumber){
        return NdefRecord.createUri("tel:"+telephoneNumber);
    }

    public NdefRecord createLocationRecord(double latitude, double longitude){
        return NdefRecord.createUri("geo:"+latitude+","+longitude);
    }

    public NdefRecord createVCardRecord(String name, String [] emails, String [] phoneNumbers, String address){
        String vcardString = "";
        vcardString =  vcardString + "N:"+name+"\n";
        for(String email:emails){
            vcardString =  vcardString + "EMAIL:"+email+"\n";;
        }
        vcardString =  vcardString + "ADR:"+address+"\n";
        for(String phoneNumber:phoneNumbers){
            vcardString = vcardString + "TEL:"+phoneNumber+"\n";
        }
        vcardString = "BEGIN:VCARD\nVERSION:2.1\n" + vcardString + "END:VCARD";
        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/x-vcard".getBytes(), new byte[0],
                vcardString.getBytes(Charset.forName("US-ASCII")));
    }

    public void writeNdefRecords(NdefRecord[] records){
        NdefMessage message = new NdefMessage(records);
        try {
            Ndef.connect();
            Ndef.writeNdefMessage(message);
            Ndef.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class UrlInvalidException extends Exception{
        public UrlInvalidException(String message){
            super(message);
        }
    }
}
