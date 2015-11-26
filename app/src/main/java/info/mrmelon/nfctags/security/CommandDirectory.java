package info.mrmelon.nfctags.security;

/**
 * Created by melon on 11/26/15
 * email - mahabub.melon@nuspay.com
 */
public final class CommandDirectory {
    private static byte [] READ_COMMAND = {(byte) 0x00, (byte) 0x30, 0x00, 0x00, 0x00};

    public static byte [] getReadCommand(int startPage, int endPage, int length){
        READ_COMMAND[2] = (byte) startPage;
        READ_COMMAND[3] = (byte) endPage;
        READ_COMMAND[4] = (byte) length;
        return READ_COMMAND;
    }
}
