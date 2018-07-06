package bar.barcode.util;

import com.synqe.Barcode.Barcode;
import com.synqe.Barcode.ImageType.EarMark;
import com.synqe.Barcode.ResultObject.DecodeImageData_Result;

public class DecodeUtil {

    private static EarMark em;

    public static EarMark getEar(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            EarMark earMark = new EarMark();
            DecodeImageData_Result DR = Barcode.DecodeImageData(bytes, earMark, 0);
            int result = DR.Result;
            if (result == 0) {
                em = DR.EM;
            } else {
                // do  nothing
            }
        }

        return em;
    }
}
