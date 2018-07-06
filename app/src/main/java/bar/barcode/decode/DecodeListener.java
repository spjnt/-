package bar.barcode.decode;

public interface DecodeListener {
     void onDecodeSuccess( byte[] bytes, Lumin source);

     void onDecodeFailed(Lumin source);
}
