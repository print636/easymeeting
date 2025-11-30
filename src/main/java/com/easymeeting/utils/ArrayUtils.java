package com.easymeeting.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayUtils {

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static boolean isEmpty(int[] array) { return array == null || array.length == 0; }
    public static boolean isEmpty(long[] array) { return array == null || array.length == 0; }
    public static boolean isEmpty(double[] array) { return array == null || array.length == 0; }
    public static boolean isEmpty(boolean[] array) { return array == null || array.length == 0; }

    public static <T> boolean contains(T[] array, T value) {
        if (array == null) return false;
        for (T element : array) {
            if (value == null ? element == null : value.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public static int contains(int[] array, int value) { return indexOf(array, value) >= 0 ? 1 : 0; }

    public static <T> int indexOf(T[] array, T value) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(value, array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(int[] array, int value) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) if (array[i] == value) return i;
        return -1;
    }

    public static <T> int lastIndexOf(T[] array, T value) {
        if (array == null) return -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (value == null ? array[i] == null : value.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static <T> T[] append(T[] array, T element) {
        int newLength = (array == null ? 0 : array.length) + 1;
        T[] newArray = newArrayLike(array, newLength);
        if (array != null && array.length > 0) System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[newLength - 1] = element;
        return newArray;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        if (isEmpty(first)) return clone(second);
        if (isEmpty(second)) return clone(first);
        T[] newArray = newArrayLike(first, first.length + second.length);
        System.arraycopy(first, 0, newArray, 0, first.length);
        System.arraycopy(second, 0, newArray, first.length, second.length);
        return newArray;
    }

    public static <T> T[] removeAt(T[] array, int index) {
        if (array == null || index < 0 || index >= array.length) return array;
        T[] newArray = newArrayLike(array, array.length - 1);
        if (index > 0) System.arraycopy(array, 0, newArray, 0, index);
        if (index < array.length - 1) System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
        return newArray;
    }

    public static <T> T[] remove(T[] array, T value) {
        int idx = indexOf(array, value);
        return idx >= 0 ? removeAt(array, idx) : array;
    }

    public static <T> List<T> toList(T[] array) {
        return array == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(array));
    }

    public static <T> Set<T> toSet(T[] array) {
        return array == null ? new HashSet<>() : new HashSet<>(Arrays.asList(array));
    }

    public static <T> T[] distinct(T[] array) {
        if (array == null) return null;
        List<T> list = new ArrayList<>();
        Set<T> seen = new HashSet<>();
        for (T t : array) {
            if (seen.add(t)) list.add(t);
        }
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), list.size());
        return list.toArray(result);
    }

    public static <T> T[] filter(T[] array, Predicate<T> predicate) {
        if (array == null) return null;
        List<T> list = new ArrayList<>();
        for (T t : array) if (predicate.test(t)) list.add(t);
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), list.size());
        return list.toArray(result);
    }

    public static <T, R> R[] map(T[] array, Function<T, R> mapper, Class<R> targetComponent) {
        if (array == null) return null;
        @SuppressWarnings("unchecked")
        R[] result = (R[]) Array.newInstance(targetComponent, array.length);
        for (int i = 0; i < array.length; i++) result[i] = mapper.apply(array[i]);
        return result;
    }

    public static <T> boolean anyMatch(T[] array, Predicate<T> predicate) {
        if (array == null) return false;
        for (T t : array) if (predicate.test(t)) return true;
        return false;
    }

    public static <T> boolean allMatch(T[] array, Predicate<T> predicate) {
        if (array == null) return false;
        for (T t : array) if (!predicate.test(t)) return false;
        return true;
    }

    private static <T> T[] newArrayLike(T[] like, int newLength) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(like != null ? like.getClass().getComponentType() : Object.class, newLength);
        return arr;
    }

    private static <T> T[] clone(T[] source) {
        if (source == null) return null;
        return Arrays.copyOf(source, source.length);
    }
}


