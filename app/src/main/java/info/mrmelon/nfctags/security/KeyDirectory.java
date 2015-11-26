package info.mrmelon.nfctags.security;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.BitSet;

/**
 * Author melon on 6/2/15.
 * email melon.ruet@gmail.com
 */

public final class KeyDirectory {

    // C[block_position][access bits - C1, C2, C3]
    public static final boolean C[][] = {
            {false, false, false}, // block 0
            {false, false, false}, // block 1
            {false, false, false}, // block 2
            {false, false, true}   // block 3
    };


    public static final byte [] DEFAULT_ACCESS_BYTES = setAccessBytes(C, 0x69);


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static byte [] setAccessBytes(boolean [][] C, int userData) {
        BitSet bitSet = new BitSet(8);

        for (int i = 2, k = 23; i >= 1; i--)
            for (int j = 3; j >= 0; j--, k--)
                bitSet.set(k, C[j][i]);

        bitSet.set(15, C[3][0]);
        bitSet.set(14, C[2][0]);
        bitSet.set(13, C[1][0]);
        bitSet.set(12, C[0][0]);

        bitSet.set(11, !C[3][2]);
        bitSet.set(10, !C[2][2]);
        bitSet.set(9, !C[1][2]);
        bitSet.set(8, !C[0][2]);

        for (int i = 1, k = 7; i >= 0; i--)
            for (int j = 3; j >= 0; j--, k--)
                bitSet.set(k, !C[j][i]);


        byte [] accessBytes = new byte[4];
        System.arraycopy(bitSet.toByteArray(), 0, accessBytes, 0, 3);

        if(userData >=0 && userData<= 0xFF)
            accessBytes[3] = (byte) userData;
        else
            accessBytes[3] = (byte) 0x69;

        return accessBytes;
    }
}
