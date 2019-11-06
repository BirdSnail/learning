package com.github.effective.java.training.classandinterface;

import java.util.Collection;
import java.util.Set;

/**
 * @author BirdSnail
 * @date 2019/10/29
 */
public class InstrumentedSet<E> extends ForwardingSet<E> {

    private int addCount;

    public InstrumentedSet(Set<E> set) {
        super(set);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}
