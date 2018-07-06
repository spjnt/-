package barcode.bar.update_app.listener;


public class ExceptionHandlerHelper {
    private static  ExceptionHandler instance;
    public static void init(ExceptionHandler exceptionHandler) {
        ExceptionHandler temp = instance;
        if (temp == null) {
            synchronized (ExceptionHandlerHelper.class) {
                temp = instance;
                if (temp == null) {
                    temp = exceptionHandler;
                    instance = temp;
                }
            }
        }
    }
    public static ExceptionHandler getInstance() {
        return instance;
    }
}
