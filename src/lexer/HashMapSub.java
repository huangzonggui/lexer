package lexer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hzg on 2016/11/21.
 */
public class HashMapSub<K,V> extends HashMap {
    @Override
    public String toString() {
        Iterator<Map.Entry<K,V>> i = entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        for (;;) {
            Map.Entry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key   == this ? "(this Map)" : key);
            sb.append("\t\t\t\t\t");
            sb.append(value == this ? "(this Map)" : value);
            if (! i.hasNext())
                return sb.toString();
            sb.append("\r\n").append(' ');
        }
    }
}
