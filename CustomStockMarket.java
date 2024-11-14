import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class CustomStockMarket {
    private final Map<String, Double> prices = new HashMap<>();
    private final Map<String, ReadWriteLock> locks = new HashMap<>();
    private final LongAdder readOps = new LongAdder();
    private final LongAdder writeOps = new LongAdder();
    private final LongAdder failedOps = new LongAdder();

    public CustomStockMarket() {
        String[] stocks = {"AAPL", "GOOGL", "MSFT", "AMZN"};
        for (String stock : stocks) {
            prices.put(stock, 100.0);
            locks.put(stock, new ReentrantReadWriteLock());
        }
    }

    public Double getPrice(String s) {
        ReadWriteLock lock = locks.get(s);
        lock.readLock().lock();
        try {
            return prices.get(s);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updatePrice(String s, Double p) {
        ReadWriteLock lock = locks.get(s);
        lock.writeLock().lock();
        try {
            prices.put(s, p);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

class StandardStockMarket {
    private final ConcurrentHashMap<String, Double> prices = new ConcurrentHashMap<>();

    public StandardStockMarket() {
        String[] stocks = {"AAPL", "GOOGL", "MSFT", "AMZN"};
        for (String stock : stocks) {
            prices.put(stock, 100.0);
        }
    }

    public Double getPrice(String s) {
        return prices.get(s);
    }

    public void updatePrice(String s, Double p) {
        prices.put(s, p);
    }
}

