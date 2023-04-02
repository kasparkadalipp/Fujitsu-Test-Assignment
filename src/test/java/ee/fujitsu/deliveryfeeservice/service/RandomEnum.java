package ee.fujitsu.deliveryfeeservice.service;

import java.util.Random;

class RandomEnum<E> {
    Random RND = new Random();
    E[] values;

    public RandomEnum(Class<E> token) {
        values = token.getEnumConstants();
    }

    public E random() {
        return values[RND.nextInt(values.length)];
    }
}