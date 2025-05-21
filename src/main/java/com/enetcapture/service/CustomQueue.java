package com.enetcapture.service;

public class CustomQueue<T> {
    private Object[] array;
    private int front;
    private int rear;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public CustomQueue() {
        array = new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = -1;
        size = 0;
    }

    public void enqueue(T element) {
        if (size == array.length) {
            resize();
        }
        rear = (rear + 1) % array.length;
        array[rear] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T element = (T) array[front];
        array[front] = null;
        front = (front + 1) % array.length;
        size--;
        return element;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return (T) array[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize() {
        Object[] newArray = new Object[array.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[(front + i) % array.length];
        }
        array = newArray;
        front = 0;
        rear = size - 1;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray(T[] type) {
        T[] result = (T[]) java.lang.reflect.Array.newInstance(type.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            result[i] = (T) array[(front + i) % array.length];
        }
        return result;
    }

    public void clear() {
        array = new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = -1;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public CustomQueue<T> copy() {
        CustomQueue<T> copy = new CustomQueue<>();
        for (int i = 0; i < size; i++) {
            copy.enqueue((T) array[(front + i) % array.length]);
        }
        return copy;
    }
}