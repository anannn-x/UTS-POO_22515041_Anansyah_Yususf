import java.util.ArrayList;


public class Queue<T> {
    private final ArrayList<T> data;   
    private final int kapasitas;

    
    public Queue() {
        this(10);
    }

   
    public Queue(int kapasitas) {
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas antrean harus > 0");
        }
        this.kapasitas = kapasitas;
        this.data = new ArrayList<>();
    }

    
    public void enqueue(T x) {
        if (x == null) {
            throw new IllegalArgumentException("Elemen antrean tidak boleh null");
        }
        if (isFull()) {
            throw new IllegalStateException("Antrean penuh");
        }
        data.add(x);
    }

    
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Antrean kosong");
        }
        return data.remove(0);
    }

   
    public T front() {
        if (isEmpty()) {
            throw new IllegalStateException("Antrean kosong");
        }
        return data.get(0);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean isFull() {
        return data.size() >= kapasitas;
    }

    public int size() {
        return data.size();
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < data.size(); i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(data.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
