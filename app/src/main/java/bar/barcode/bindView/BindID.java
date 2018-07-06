package bar.barcode.bindView;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解会在class字节码中存在,在编译时通过反射获取
@Retention(RetentionPolicy.RUNTIME)
//@Target——指明该类型的注解可以注解的程序元素的范围。该元注解的取值可以为TYPE,METHOD,CONSTRUCTOR,FIELD等。
// 如果Target元注解没有出现，那么定义的注解可以应用于程序的任何元素。
@Target({ElementType.FIELD, ElementType.TYPE})

public @interface BindID {

    /**
     * Used to mark a View that has no ID.
     */
    int value() default View.NO_ID;

    boolean click() default true;
}
