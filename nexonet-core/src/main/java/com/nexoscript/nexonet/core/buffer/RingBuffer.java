package com.nexoscript.nexonet.core.buffer;

public final class RingBuffer<T> {
    private final Slot<T>[] buffer;
    private final int mask;
    private final long capacity;
    private volatile long head = 0;
    private volatile long tail = 0;

    @SuppressWarnings("unchecked")
    public RingBuffer(int sizePowerOfTwo) {
        if (Integer.bitCount(sizePowerOfTwo) != 1) {
            throw new IllegalArgumentException("Size must be power of two");
        }
        this.capacity = sizePowerOfTwo;
        this.buffer = new Slot[sizePowerOfTwo];
        for (int i = 0; i < sizePowerOfTwo; i++) {
            Slot<T> slot = new Slot<>();
            slot.sequence = i;
            buffer[i] = slot;
        }
        this.mask = sizePowerOfTwo - 1;
    }

    public void offer(T value) {
        long pos;
        Slot<T> slot;

        while (true) {
            pos = tail;
            slot = buffer[(int) (pos & mask)];

            long seq = slot.sequence;
            long diff = seq - pos;

            if (diff == 0) {
                if (compareAndSetTail(pos, pos + 1)) {
                    break;
                }
            } else {
                Thread.onSpinWait();
            }
        }

        slot.value = value;
        slot.sequence = pos + 1;
    }

    public T poll() {
        long pos = head;
        Slot<T> slot = buffer[(int) (pos & mask)];

        if (slot.sequence != pos + 1) {
            return null;
        }

        T value = slot.value;
        slot.value = null;
        slot.sequence = pos + capacity;
        head = pos + 1;

        return value;
    }

    private synchronized boolean compareAndSetTail(long expected, long update) {
        if (tail == expected) {
            tail = update;
            return true;
        }
        return false;
    }

    private static final class Slot<T> {
        volatile long sequence;
        volatile T value;
    }
}
