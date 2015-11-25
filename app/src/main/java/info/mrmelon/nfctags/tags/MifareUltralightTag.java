package info.mrmelon.nfctags.tags;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

import java.io.IOException;

/**
 * Created by melon on 10/14/15
 * email - melon.ruet@gmail.com
 */

public class MifareUltralightTag {

    private MifareUltralight ultralight;

    public MifareUltralightTag(Tag tag){
        ultralight = MifareUltralight.get(tag);
    }

    public MifareUltralight getUltralight(){
        return ultralight;
    }

    public boolean connectUltralightTag(){
        try {
            ultralight.connect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeUltralightTag(){
        try {
            ultralight.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte [] readPage(int pageOffset){
        try {
            return ultralight.readPages(pageOffset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean writePage(int pageOffset, byte [] data){
        try {
            ultralight.writePage(pageOffset, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void makeReadonly(int pageIndex){
        lockPage(pageIndex, true);
        blockLocking(pageIndex);
    }

    public void blockLocking(int pageIndex){
        if(pageIndex>=3 && pageIndex<=15) {
            byte[] getLockPage = readPage(2);
            if (pageIndex == 3) {
                getLockPage[2] = setLock(7, true, getLockPage[2]);
            } else if (pageIndex >= 4 && pageIndex <= 9) {
                getLockPage[2] = setLock(6, true, getLockPage[2]);
            } else {
                getLockPage[2] = setLock(5, true, getLockPage[2]);
            }

            byte [] writeLockBytes = new byte[4];
            System.arraycopy(getLockPage, 0, writeLockBytes, 0, 4);
            writePage(2, writeLockBytes);
        }
        else if(pageIndex>=16 && pageIndex<=47 && pageIndex!=40){
            byte[] getLockPage = readPage(40);
            if(pageIndex>=16 && pageIndex<=27){
                getLockPage[0] = setLock(7, true, getLockPage[0]);
            }
            else if(pageIndex >= 28 && pageIndex <= 39){
                getLockPage[0] = setLock(3, true, getLockPage[0]);
            }
            else if(pageIndex>=44 && pageIndex<=47){
                getLockPage[1] = setLock(4, true, getLockPage[1]);
            }
            else {
                getLockPage[1] = setLock(48-pageIndex, true, getLockPage[1]);
            }

            byte [] writeLockBytes = new byte[4];
            System.arraycopy(getLockPage, 0, writeLockBytes, 0, 4);
            writePage(40, writeLockBytes);
        }
        else {
            Log.e("Page Index", "Page index is not valid");
        }
    }

    public void lockPage(int pageIndex, boolean isSetLocked){
        if(pageIndex>=3 && pageIndex<=15){
            byte [] getLockPage = readPage(2);
            if(pageIndex<=7){
                getLockPage[2] = setLock(8-pageIndex, isSetLocked, getLockPage[2]);
            }
            else {
                getLockPage[3] = setLock(15-pageIndex, isSetLocked, getLockPage[2]);
            }
            byte [] writeLockBytes = new byte[4];
            System.arraycopy(getLockPage, 0, writeLockBytes, 0, 4);
            writePage(2, writeLockBytes);
        }
        else if(pageIndex>=16 && pageIndex<=47 && pageIndex != 40){
            byte [] getLockPage = readPage(40);
            if(pageIndex>=28 && pageIndex<=39){
                getLockPage[0] = setLock(9-pageIndex/4, isSetLocked, getLockPage[0]);
            }
            else if(pageIndex>=16 && pageIndex<=27){
                getLockPage[0] = setLock(10-pageIndex/4, isSetLocked, getLockPage[0]);
            }
            else if(pageIndex>=44 && pageIndex<=47){
                getLockPage[1] = setLock(0, isSetLocked, getLockPage[1]);
            }
            else {
                getLockPage[1] = setLock(44-pageIndex, isSetLocked, getLockPage[1]);
            }

            byte [] writeLockBytes = new byte[4];
            System.arraycopy(getLockPage, 0, writeLockBytes, 0, 4);
            writePage(40, writeLockBytes);
        }
        else {
            Log.e("Page Index", "Page index is not valid");
        }
    }

    private byte setLock(int index, boolean isLock, byte lockByte){
        if(isLock)
            lockByte = (byte) (lockByte | (1 << index));
        else
            lockByte = (byte) (lockByte & ~(1 << index));
        return lockByte;
    }
}
