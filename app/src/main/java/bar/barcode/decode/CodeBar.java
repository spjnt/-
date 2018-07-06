package bar.barcode.decode;


/*This class can only be in this package,
 *
 *otherwise it will fail and need to recompile JNI,
 *
 *and no  JNI  in this  project, you  should  find sound code*/
public class CodeBar {
    static {
        System.loadLibrary("CodeBar");
    }

    public native static String getString();//just  for test

    public native static int getByte(byte[] szFile, byte[] out);
}
