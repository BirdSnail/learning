package training.yanghuadong.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BirdSnail
 * @date 2020/3/16
 */
public class MyRunTestAnnotationTool {

    public static void main(String[] args)  {
        final Class<DoSomething> doSomethingClass = DoSomething.class;
        final Constructor<?> constructor = doSomethingClass.getConstructors()[0];
        Object instance = null;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        List<Exception> exceptionList = new ArrayList<>();
        final Method[] methods = doSomethingClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyRunTest.class)) {
                try {
                    method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    exceptionList.add(e);
                }
            }
        }

        if (!exceptionList.isEmpty()) {
            System.out.println("程序由错误：" + exceptionList.size());
            exceptionList.forEach(System.out::println);
        }
    }
}
