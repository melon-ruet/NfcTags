package info.mrmelon.nfctags.tags;

import android.nfc.Tag;
import android.nfc.tech.NfcA;

import java.io.IOException;

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

    public void read(){

    }

}
