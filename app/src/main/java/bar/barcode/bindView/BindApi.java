package bar.barcode.bindView;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BindApi {


//    public static void BindIDEasy(Activity obj) {
//        Class<? extends Activity> aClass = obj.getClass();
//        Field[] fields = aClass.getDeclaredFields();
//        if (fields != null && fields.length > 0)
//            for (Field field : fields) {
//                if (field.isAnnotationPresent(BindID.class)) {
//                    BindID id = field.getAnnotation(BindID.class);
//                    int value1 = id.value();
//                    try {
//                        field.setAccessible(true);
//                        field.set(obj, (obj).findViewById(value1));
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//    }

    public static void BindIDEasy(Object obj) {
        Class<? extends Object> aClass = obj.getClass();
        //使用反射调用contentView
        if (aClass.isAnnotationPresent(BindID.class)) {
            //得到这个类的注解
            BindID bindID = aClass.getAnnotation(BindID.class);

            int value = bindID.value();
            try {
                Method method = aClass.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(obj, value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        Field[] fields = aClass.getDeclaredFields();
        if (fields != null && fields.length > 0)
            for (Field field : fields) {
                if (field.isAnnotationPresent(BindID.class)) {
                    BindID id = field.getAnnotation(BindID.class);
                    int value1 = id.value();
                    try {
                        Method viewById = aClass.getMethod("findViewById", int.class);
                        viewById.setAccessible(true);
                        Object invoke = viewById.invoke(obj, value1);
                        field.setAccessible(true);
                        field.set(obj, invoke);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

            }
    }

//    public static void BindIDEasy(Dialog obj) {
//        Class<? extends Dialog> aClass = obj.getClass();
//        Field[] fields = aClass.getDeclaredFields();
//        if (fields != null && fields.length > 0)
//            for (Field field : fields) {
//                if (field.isAnnotationPresent(BindID.class)) {
//                    BindID id = field.getAnnotation(BindID.class);
//                    int value1 = id.value();
//                    try {
//                        field.setAccessible(true);
//                        field.set(obj, (obj).findViewById(value1));
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//    }

    public static void BindOnClick(final Activity obj) {
        Class<? extends Activity> aClass = obj.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            if (method.isAnnotationPresent(OnClick.class)) {
                OnClick onClick = method.getAnnotation(OnClick.class);
                int[] value = onClick.value();
                for (int j = 0; j < value.length; j++) {
                    final View id = obj.findViewById(value[j]);
                    id.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置true 才能调用私有的方法
                            method.setAccessible(true);
                            try {
                                method.invoke(obj, id);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    public static void BindOnClick(final Dialog obj) {
        Class<? extends Dialog> aClass = obj.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            if (method.isAnnotationPresent(OnClick.class)) {
                OnClick onClick = method.getAnnotation(OnClick.class);
                int[] value = onClick.value();
                for (int j = 0; j < value.length; j++) {
                    final View id = obj.findViewById(value[j]);
                    id.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置true 才能调用私有的方法
                            method.setAccessible(true);
                            try {
                                method.invoke(obj, id);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }


}
