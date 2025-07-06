package Symbol;

import java.util.HashMap;
import java.util.Enumeration;

public class Table<Tau> {
    private class Binder<Tau>
    {
        Tau data;
        Binder<Tau> tail;
        String top;
        
        public Binder(Tau t, Binder tail, String s)
        {
            this.data = t;
            this.tail = tail;
            this.top = s;
        }
    }
    private HashMap<String, Binder<Tau>> table = new HashMap<String, Binder<Tau>>();
    private String top; //most recent binding added
    private Binder<Tau> marks;

    public Tau put(String key, Tau data)
    {
        //put is table's put (hashmap)
        Binder<Tau> b = table.put(key.intern(), new Binder<Tau>(data, table.get(key.intern()), top));
        top = key;
        if(b == null)
        {
            return null;
        }
        return b.data;
    }

    public Tau get(String key)
    {
        Binder<Tau> b = table.get(key.intern());
        if(b == null)
        {
            return null;
        }
        return b.data;
    }

    public void beginScope()
    {
        marks = new Binder<Tau>(null, marks, top);
        top = null;
    }

    public void endScope()
    {
        while(top != null)
        {
            Binder<Tau> b = table.get(top);//most recent thing in table
            if(b.tail != null)
            {
                table.put(top, b.tail);//pop top binding out of the table from that name
            }
            else//this is the first binding for the key
            {
                table.remove(top);
            }
            top = b.top;            
        }
        top = marks.top;
        marks = marks.tail;
    }

    public void printTable()
    {
        System.out.println("-Start Table-");
        for(String t : table.keySet())
        {
            System.out.println(t);
        }
        System.out.println("-End Table-");
    }
}