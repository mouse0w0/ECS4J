package com.github.mouse0w0.ecs.util;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class IntQueue {

    private int[] elements;
    private int beginIndex;
    private int size = 0;

    public IntQueue() {
        this(64);
    }

    public IntQueue(int capacity) {
        elements = new int[capacity];
    }

    public void push(int e) {
        if (size == elements.length) {
            grow();
        }

        elements[index(size++)] = e;
    }

    public int pop() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }

        int value = elements[beginIndex];
        beginIndex = (beginIndex + 1) % elements.length;
        size--;
        return value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        beginIndex = 0;
    }

    private void grow() {
        int oldCapacity = elements.length;
        int[] newElements = new int[oldCapacity + oldCapacity << 1];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[index(i)];
        }

        elements = newElements;
        beginIndex = 0;
    }

    private int index(int relativeIndex) {
        return (beginIndex + relativeIndex) % elements.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntQueue intQueue = (IntQueue) o;
        return Arrays.equals(elements, intQueue.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IntQueue{elements=[");
        for (int i = 0; size > i; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[index(i)]);
        }
        return sb.append("]}").toString();
    }
}
