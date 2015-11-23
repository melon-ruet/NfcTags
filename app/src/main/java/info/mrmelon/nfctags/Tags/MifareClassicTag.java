package info.mrmelon.nfctags.Tags;


import android.annotation.TargetApi;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.BitSet;


/**
 * Created by melon on 6/25/15
 * email - melon.ruet@gmail.com
 */

public class MifareClassicTag {

    private MifareClassic mifare;

    public final String KEY_LENGTH_ERROR = "Key length must be 6 bytes";
    public final String KEY_CHANGE_SUCCESSFUL = "Key changed";
    public final String KEY_CHANGE_FAILED = "Key change failed";
    public final String SECTOR_TRAILER_READ_ERROR = "Can not read sector trailer";

    public final String ACCESS_BYTES_LENGTH_ERROR = "Access bytes must be 4 bytes";
    public final String ACCESS_BYTES_CHANGE_FAILED = "Access bytes change failed";
    public final String ACCESS_BYTES_CHANGE_SUCCESSFUL = "Access bytes changed";

    public final String SECTOR_TRAILER_CHANGE_FAILED = "Sector trailer change failed";
    public final String SECTOR_TRAILER_CHANGE_SUCCESSFUL = "Sector trailer changed";

    public MifareClassicTag(Tag tag) {
        mifare = MifareClassic.get(tag);
    }

    public MifareClassic getMifareClassicTag() {
        return mifare;
    }

    public boolean connectMifareClassic() {
        try {
            mifare.connect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean closeMifareClassic() {
        if (mifare != null) {
            try {
                mifare.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public byte[] readBlock(int block) {
        try {
            return mifare.readBlock(block);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean writeBlock(int blockIndex, byte[] data) {
        try {
            mifare.writeBlock(blockIndex, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authCardWithAnyKey(int sector){
        return authCardWithAorB(sector, true)
                || authCardWithAorB(sector, true)
                || authCardWithAorB(sector, true)
                || authCardWithAorB(sector, false)
                || authCardWithAorB(sector, false)
                || authCardWithAorB(sector, false);
    }

    public boolean authCardWithAorB(int sector, boolean isKeyA){
        return authWithKey(sector, isKeyA, MifareClassic.KEY_DEFAULT)
                || authWithKey(sector, isKeyA, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)
                || authWithKey(sector, isKeyA, MifareClassic.KEY_NFC_FORUM);
    }

    public boolean authWithKey(int sector, boolean isKeyA, byte[] key) {
        try {
            if (isKeyA) return mifare.authenticateSectorWithKeyA(sector, key);
            return mifare.authenticateSectorWithKeyB(sector, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getSectorBlock(int sector){
        if(sector>31)  return mifare.sectorToBlock(sector) + 7;
        else return mifare.sectorToBlock(sector) + 3;
    }

    public String changeClassicKey(int sector, boolean isKeyA, byte [] newKey){
        if(newKey.length != 6) return KEY_LENGTH_ERROR;

        int sectorBlock = getSectorBlock(sector);
        byte [] sectorData = readBlock(sectorBlock);
        if (sectorData.length != 16) return SECTOR_TRAILER_READ_ERROR;

        if(isKeyA){
            System.arraycopy(newKey, 0, sectorData, 0, newKey.length);
        }
        else {
            System.arraycopy(newKey, 0, sectorData, 10, newKey.length);
        }

        if(writeBlock(sectorBlock, sectorData))
            return KEY_CHANGE_SUCCESSFUL;
        else
            return KEY_CHANGE_FAILED;
    }

    public String changeAccessBytes(int sector, byte [] newAccessBytes){
        if(newAccessBytes.length != 4) return ACCESS_BYTES_LENGTH_ERROR;

        int sectorBlock = getSectorBlock(sector);
        byte [] sectorData = readBlock(sectorBlock);
        if (sectorData.length != 16) return SECTOR_TRAILER_READ_ERROR;

        System.arraycopy(newAccessBytes, 0, sectorData, 4, newAccessBytes.length);

        if(writeBlock(sectorBlock, sectorData))
            return ACCESS_BYTES_CHANGE_SUCCESSFUL;
        else
            return ACCESS_BYTES_CHANGE_FAILED;
    }

    public String changeSectorTrailer(int sector, byte [] keyA, byte [] accessBytes, byte [] keyB){
        if(keyA.length != 6 || keyB.length != 6) return KEY_LENGTH_ERROR;
        if(accessBytes.length != 4) return ACCESS_BYTES_LENGTH_ERROR;

        byte [] sectorTrailer = new byte[16];
        System.arraycopy(keyA, 0, sectorTrailer, 0, keyA.length);
        System.arraycopy(accessBytes, 0, sectorTrailer, keyA.length, accessBytes.length);
        System.arraycopy(keyB, 0, sectorTrailer, keyA.length+accessBytes.length, keyB.length);

        if(writeBlock(getSectorBlock(sector), sectorTrailer))
            return SECTOR_TRAILER_CHANGE_SUCCESSFUL;
        else
            return SECTOR_TRAILER_CHANGE_FAILED;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean [][] getC(int sector){
        byte [] accessBytes = new byte[4];
        try {
            System.arraycopy(readBlock(mifare.sectorToBlock(sector) + 3), 6, accessBytes, 0, 4);
        }catch (ArrayIndexOutOfBoundsException ex){
            Log.e("Access bits read error", ex.getMessage());
        }

        BitSet bitSetC = BitSet.valueOf(accessBytes);

        boolean C[][] = new boolean[4][3];
        int baseC1 = 11, baseC2 = 19, baseC3 = 23;
        C[0] = new boolean[]{bitSetC.get(baseC1), bitSetC.get(baseC2), bitSetC.get(baseC3)};
        C[1] = new boolean[]{bitSetC.get(baseC1-1), bitSetC.get(baseC2-1), bitSetC.get(baseC3-1)};
        C[2] = new boolean[]{bitSetC.get(baseC1-2), bitSetC.get(baseC2-2), bitSetC.get(baseC3-2)};
        C[3] = new boolean[]{bitSetC.get(baseC1-3), bitSetC.get(baseC3-3), bitSetC.get(baseC3-3)};

        return C;
    }
}

