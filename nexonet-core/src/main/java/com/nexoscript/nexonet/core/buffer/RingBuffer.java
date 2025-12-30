package com.nexoscript.nexonet.core.buffer;

import com.nexoscript.nexonet.api.packet.IPacket;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class RingBuffer {
    private static final class Slot {
        volatile long sequence;
        volatile IPacket value;
    }

    private final Slot[] buffer;
    private final int mask;

    private final AtomicLong head = new AtomicLong(0);
    private final AtomicLong tail = new AtomicLong(0);

    public RingBuffer(int sizePowerOfTwo) {
        if (Integer.bitCount(sizePowerOfTwo) != 1) {
            throw new IllegalArgumentException("Size must be power of two");
        }
        buffer = new Slot[sizePowerOfTwo];
        for (int i = 0; i < sizePowerOfTwo; i++) {
            Slot s = new Slot();
            s.sequence = i;
            buffer[i] = s;
        }
        mask = sizePowerOfTwo - 1;
    }

    public void offer(IPacket packet) {
        long pos;
        Slot slot;

        while (true) {
            pos = tail.get();
            slot = buffer[(int) (pos & mask)];

            long seq = slot.sequence;
            long diff = seq - pos;
            if (diff == 0) {
                if (tail.compareAndSet(pos, pos + 1)) {
                    break;
                }
                continue;
            }
            Thread.onSpinWait();
        }
        slot.value = packet;
        slot.sequence = pos + 1;
    }

    public IPacket poll() {
        long pos = head.get();
        Slot slot = buffer[(int) (pos & mask)];
        if (slot.sequence != pos + 1) {
            return null;
        }
        IPacket value = slot.value;
        slot.value = null;
        slot.sequence = pos + buffer.length;
        head.lazySet(pos + 1);
        return value;
    }
}
