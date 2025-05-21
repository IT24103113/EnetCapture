package com.enetcapture.service;

public class CustomArray<T> {
    private Object[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public CustomArray() {
        array = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void add(T element) {
        if (size == array.length) {
            resize();
        }
        array[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) array[index];
    }

    public boolean remove(T element) {
        int index = indexOf(element);
        if (index == -1) {
            return false;
        }
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null;
        return true;
    }

    public boolean removeByPredicate(Predicate<T> predicate) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return false;
        }
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null;
        return true;
    }

    private int indexOf(T element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void resize() {
        Object[] newArray = new Object[array.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    public CustomArray<T> copy() {
        CustomArray<T> copy = new CustomArray<>();
        for (int i = 0; i < size; i++) {
            copy.add(get(i));
        }
        return copy;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray(T[] type) {
        T[] result = (T[]) java.lang.reflect.Array.newInstance(type.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) array[i];
        }
        return result;
    }

    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T t);
    }
}