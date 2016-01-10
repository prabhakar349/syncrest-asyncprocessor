package com.iamiddy;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Observable ob;
        ConcurrentHashMap<String,P> m = new ConcurrentHashMap<>();
        P p = new P("Iddy","Magohe");
        m.putIfAbsent(p.f,p);
        System.out.println(m.size());
        P pt = m.get("Iddy");

        System.out.println( "before" );
        System.out.println(pt.f + " ----" + pt.l);
        System.out.println( "after" );
        m.remove(pt.f);
        System.out.println(m.size());
        System.out.println(pt.f + " ----" + pt.l);
    }


   static class P{
        public String f;
        public String l;

        public P(String f, String l) {
            this.f = f;
            this.l = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            P p = (P) o;

            if (f != null ? !f.equals(p.f) : p.f != null) return false;
            return !(l != null ? !l.equals(p.l) : p.l != null);

        }

        @Override
        public int hashCode() {
            int result = f != null ? f.hashCode() : 0;
            result = 31 * result + (l != null ? l.hashCode() : 0);
            return result;
        }
    }
}
