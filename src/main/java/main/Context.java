package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by usr on 21.11.15.
 */
public final class Context {
    private Map<Class<?>, Object> context = new HashMap<>();
    static Context instance = new Context();

    private Context(){}

    public static Context getInstance(){
        return instance;
    }

    public boolean add(Class<?> clazz, Object obj){
        if(context.containsKey(clazz)){
            return false;
        }
        else{
            context.put(clazz, obj);
            return true;
        }
    }

    public Object get(Class<?> clazz){

        return context.get(clazz);
    }

    public boolean remove(Class<?> clazz){
        if(context.containsKey(clazz)){
            context.remove(clazz);
            return true;
        }
        else{
            return false;
        }
    }

}
