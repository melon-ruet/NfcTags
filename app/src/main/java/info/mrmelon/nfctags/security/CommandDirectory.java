package info.mrmelon.nfctags.security;

import android.util.Log;

import info.mrmelon.nfctags.utils.Conversion;

/**
 * Created by melon on 11/26/15
 * email - mahabub.melon@nuspay.com
 */
public final class CommandDirectory {
    private static byte [] READ_COMMAND = {(byte) 0xFF, (byte) 0xCA, 0x00, 0x00, 0x00};

    public static byte [] getReadCommand(int startPage, int endPage, int length){
        READ_COMMAND[2] = (byte) startPage;
        READ_COMMAND[3] = (byte) endPage;
        READ_COMMAND[4] = (byte) length;
        return READ_COMMAND;
    }


        static char __crcBDefault = (char) 0xffff;

        private static char UpdateCrc(byte b, char crc)
        {

                byte ch = (byte)(b^(byte)(crc & 0x00ff));
                ch = (byte)(ch ^ (ch << 4));
                return (char)((crc >> 8)^(ch << 8)^(ch << 3)^(ch >> 4));

        }

        public static byte [] ComputeCrc(byte[] bytes)
        {
            char res = __crcBDefault;
            for (byte b : bytes)
                res = UpdateCrc(b, res);

            return String.valueOf(res).getBytes();
        }

}
