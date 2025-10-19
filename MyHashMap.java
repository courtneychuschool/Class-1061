import java.util.LinkedList;

public class MyHashMap<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.5;

    private LinkedList<Entry<K, V>>[] buckets;
    private int size = 0;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new LinkedList[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(K key, V value) {
        if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }

        int bucketIndex = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
    }

    public V get(K key) {
        int bucketIndex = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return (value != null) ? value : defaultValue;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        LinkedList<Entry<K, V>>[] oldBuckets = buckets;

        this.capacity *= 2;
        this.buckets = new LinkedList[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            this.buckets[i] = new LinkedList<>();
        }

        this.size = 0;

        for (LinkedList<Entry<K, V>> bucket : oldBuckets) {
            for (Entry<K, V> entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }

    public int size() {
        return size;
    }
}
