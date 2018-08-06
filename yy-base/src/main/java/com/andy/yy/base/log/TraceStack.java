package com.andy.yy.base.log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author richard
 * @since 2018/1/31 17:08
 */
public class TraceStack<T> {
    
    private final ThreadLocal<List<T>> stack = new ThreadLocal<>();

    public void push(T entry) {
        List<T> list = stack.get();
        if(list == null) {
            list = new ArrayList<>();
            stack.set(list);
        }
        list.add(entry);
    }
    
    public T pop() {
        List<T> list = stack.get();
        if(list == null || list.isEmpty()) {
            return null;
        }
        T entry = list.remove(list.size() - 1);
        return entry;
    }
    
    public T last() {
        List<T> list = stack.get();
        return list == null || list.isEmpty() ? null : list.get(list.size() - 1);
    }
    
    public int size() {
        List<T> list = stack.get();
        return list == null ? 0 : list.size();
    }
    
    public boolean isEmpty() {
        List<T> list = stack.get();
        return list == null || list.isEmpty();
    }

    public T first() {
        List<T> list = stack.get();
        return list == null || list.isEmpty() ? null : list.get(0);
    }

}
