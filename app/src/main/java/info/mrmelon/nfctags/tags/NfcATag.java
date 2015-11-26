package info.mrmelon.nfctags.tags;

import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.util.Log;

import java.io.IOException;

import info.mrmelon.nfctags.security.CommandDirectory;

/**
 * Created by melon on 11/26/15.
 */

public class NfcATag {
    private NfcA nfcA;
    private Tag tag;

    public NfcATag(Tag tag){
        this.tag = tag;
        nfcA = NfcA.get(tag);
    }

    public NfcA getNfcA() {
        return nfcA;
    }

    public void connect(){
        try {
            nfcA.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            nfcA.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(int page){

    }

    public byte [] read(byte [] command){
        try {
            return nfcA.transceive(command);
        } catch (IOException e) {
            Log.e("NfcA Read", ""+e.toString());
        }
        return null;
    }

}
