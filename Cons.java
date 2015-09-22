/**
 * Created by DilanHira on 9/18/15.
 */
interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class Cons {
    // instance variables
    private Object car;   // traditional name for first
    private Cons cdr;     // "could-er", traditional name for rest
    private Cons(Object first, Cons rest)
    { car = first;
        cdr = rest; }

    // Cons is the data type.
    // cons() is the method that makes a new Cons and puts two pointers in it.
    // cons("a", null) = (a)
    // cons puts a new thing on the front of an existing list.
    // cons("a", list("b","c")) = (a b c)
    public static Cons cons(Object first, Cons rest)
    { return new Cons(first, rest); }

    // consp is true if x is a Cons, false if null or non-Cons Object
    public static boolean consp (Object x)
    { return ( (x != null) && (x instanceof Cons) ); }

    // first returns the first thing in a list.
    // first(list("a", "b", "c")) = "a"
    // safe, first(null) = null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }

    // rest of a list after the first thing.
    // rest(list("a", "b", "c")) = (b c)
    // safe, rest(null) = null
    public static Cons rest(Cons lst) {
        return ( (lst == null) ? null : lst.cdr  ); }

    // second thing in a list
    // second(list("+", "b", "c")) = "b"
    public static Object second (Cons x) { return first(rest(x)); }

    // third thing in a list
    // third(list("+", "b", "c")) = "c"
    public static Object third (Cons x) { return first(rest(rest(x))); }

    // destructively change the first() of a cons to be the specified object
    // setfirst(list("a", "b", "c"), 3) = (3 b c)
    public static void setfirst (Cons x, Object i) { x.car = i; }

    // destructively change the rest() of a cons to be the specified Cons
    // setrest(list("a", "b", "c"), null) = (a)
    // setrest(list("a", "b", "c"), list("d","e")) = (a d e)
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }

    // make a list of the specified items
    // list("a", "b", "c") = (a b c)
    // list() = null
    public static Cons list(Object ... elements) {
        Cons list = null;
        for (int i = elements.length-1; i >= 0; i--) {
            list = cons(elements[i], list);
        }
        return list;
    }

    // convert a list to a string in parenthesized form for printing
    public String toString() {
        return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
        return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
        return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                + ((rest(lst) == null) ? ")"
                : " " + toStringb(rest(lst)) ) ); }

    public static int square(int x) { return x*x; }

    // ****** your code starts here ******
    public static int length(Cons lst){
        int n = 0;
        for(Cons arg = lst;arg != null; arg = rest(arg) ){
            n++;
        }
        return n;
    }

    // add up elements of a list of numbers
    public static int sum (Cons lst) {
        if(lst == null){
            return 0;
        }
        return (Integer)first(lst) + sum(rest(lst));
    }

    // mean = (sum x[i]) / n
    public static double mean (Cons lst) {
        int n = length(lst);
        return (double)(sum(lst)) / n;
    }

    // square of the mean = mean(lst)^2

    // mean square = (sum x[i]^2) / n
    public static double meansq (Cons lst) {
        //square sum
        int sumsq= 0;
        for(Cons arg = lst; arg != null; arg = rest(arg)){
            sumsq += square((Integer)first(arg));
        }
        return (double)(sumsq) / (length(lst));
    }

    public static double variance (Cons lst) {
        return meansq(lst) - Math.pow(mean(lst), 2.0);
    }

    public static double stddev (Cons lst) {
        return Math.sqrt(variance(lst));
    }

    public static double sine (double x) {
        return sineb(x, 1, true, x, 1, x);
    }

    //starts at second term on tailor series. First term is always x
    // n = which term we are on
    public static double sineb (double x, double n, boolean neg, double num, double denom, double ans){
        if(n==21){
            return ans;
        }
        num *= (x*x);
        denom *= ((n+1)*(n+2));
        if(neg)
            ans -= (num/denom);
        else
            ans += (num/denom);
        return sineb(x, n + 2, !neg, num, denom, ans);
    }

    public static Cons nthcdr (int n, Cons lst) {
        if(n==0){
            return lst;
        }
        else if(lst == null){
            return lst;
        }
        return nthcdr(n - 1, rest(lst));
    }

    //return the nth element in the lst
    public static Object elt (Cons lst, int n) {
        for(int i = 0; i != n; i++){
            if(lst == null){
                return null;
            }
            lst = rest(lst);
        }
        return first(lst);
    }

    public static double interpolate (Cons lst, double x) {
        int i = (int) x;
        int bottom = (Integer)elt(lst,i);
        int top = (Integer)elt(lst,i+1);
        return bottom + (x-i)*(top-bottom);


    }

    // Make a list of Binomial coefficients
    // binomial(2) = (1 2 1)
    public static Cons binomial(int n) {
        return binomialB(n, new Cons(1, null));
    }

    public static Cons binomialB(int n,Cons lst){
        if(n == 0) return lst;
        return binomialB(n - 1, getNextRow(lst, new Cons(1, null), n));
    }

    public static Cons getNextRow(Cons lst, Cons ans, int i){
        if(rest(lst) == null){
            ans = cons(1,ans);
            return ans;
        }
        return getNextRow(rest(lst), cons((Integer) first(lst) + ((second(lst) == null) ? 0 : ((Integer) second(lst))), ans), i++);
    }

    // adds lists, even if there are embeded lists
    //through each iteration x either takes on the value at the location or the sum of the embedded list at that location
    public static int sumtr (Cons lst) {
        if(lst == null){
            return 0;
        }
        int x = 0;
        if(consp(first(lst))){
            x = sumtr((Cons)first(lst));
        }
        else x = (Integer)first(lst);
        return x + sumtr(rest(lst));
    }

    // use auxiliary functions as you wish.
    public static Cons subseq (Cons lst, int start, int end) {
        Cons ans = null;
        //find start element
        for(int i = 0; i != start;i++){
            lst = rest(lst);
        }
        for(;start < end;start++){
            ans = cons(first(lst),ans);
            lst = rest(lst);
        }
        return reverse(ans);
    }





    public static Cons reverse(Cons lst){
        Cons answer = null;
        for (;lst != null; lst = rest(lst)){
            answer = cons(first(lst),answer);
        }
        return answer;
    }
    public static Cons posfilter (Cons lst) {
        return posfilterB(lst,null);
    }

    public static Cons posfilterB(Cons Olist, Cons ans){
        if(Olist == null){
            return reverse(ans);
        }
        if((Integer)first(Olist) >= 0){
            return posfilterB(rest(Olist),cons(first(Olist),ans));
        }
        else return posfilterB(rest(Olist),ans);
    }

    public static Cons subset (Predicate p, Cons lst) {
        return subsetB(p, lst, null);
    }

    public static Cons subsetB(Predicate p, Cons lst, Cons ans){
        if(lst == null){
            return reverse(ans);
        }
        if(p.pred(first(lst))){
            ans = cons(first(lst),ans);
        }
        return subsetB(p, rest(lst),ans);
    }

    public static Cons mapcar (Functor f, Cons lst) {
        return mapcarB(f, lst, null);
    }

    public static Cons mapcarB(Functor f, Cons lst, Cons ans){
        if(lst == null){
            return reverse(ans);
        }
        return mapcarB(f, rest(lst),cons(f.fn(first(lst)),ans));
    }
    public static Object some (Predicate p, Cons lst) {
        if(lst == null){
            return null;
        }
        else if(p.pred(first(lst))){
            return first(lst);
        }
        return some(p,rest(lst));
    }

    public static boolean every (Predicate p, Cons lst) {
        if (lst == null){
            return true;
        }
        else if(!p.pred(first(lst))){
            return false;
        }
        return every(p, rest(lst));
    }

    // ****** your code ends here ******

    public static void main( String[] args )
    {
        Cons mylist =
                list(95, 72, 86, 70, 97, 72, 52, 88, 77, 94, 91, 79,
                        61, 77, 99, 70, 91 );
        Cons thislist = list(1,1,1,1);
        System.out.println("mylist = " + mylist.toString());
        System.out.println("sum = " + sum(mylist));
        System.out.println("mean = " + mean(mylist));
        System.out.println("meansq = " + meansq(mylist));
        System.out.println("variance = " + variance(mylist));
        System.out.println("stddev = " + stddev(mylist));
        System.out.println("sine(0.5) = " + sine(0.5));  // 0.47942553860420301
        System.out.print("nthcdr 5 = ");
        System.out.println(nthcdr(5, mylist));
        System.out.print("nthcdr 18 = ");
        System.out.println(nthcdr(18, mylist));
        System.out.println("elt 5 = " + elt(mylist, 5));

        Cons mylistb = list(0, 30, 56, 78, 96);
        System.out.println("mylistb = " + mylistb.toString());
        System.out.println("interpolate(3.4) = " + interpolate(mylistb, 3.4));
        Cons binom = binomial(12);
        System.out.println("binom = " + binom.toString());
        System.out.println("interpolate(3.4) = " + interpolate(binom, 3.4));

        Cons mylistc = list(1, list(2, 3), list(list(list(list(4)),
                        list(5)),
                6));


        System.out.println("mylistc = " + mylistc.toString());
        System.out.println("sumtr = " + sumtr(mylistc));
        Cons mylistcc = list(1, list(7, list(list(2), 3)),
                list(list(list(list(list(list(list(4)))), 9))),
                list(list(list(list(5), 4), 3)),
                list(6));

        System.out.println("mylistcc = " + mylistcc.toString());
        System.out.println("sumtr = " + sumtr(mylistcc));

        Cons mylistd = list(0, 1, 2, 3, 4, 5, 6 );
        System.out.println("mylistd = " + mylistd.toString());
        System.out.println("subseq(2 5) = " + subseq(mylistd, 2, 5));

        Cons myliste = list(3, 17, -2, 0, -3, 4, -5, 12 );
        System.out.println("myliste = " + myliste.toString());
        System.out.println("posfilter = " + posfilter(myliste));

        final Predicate myp = new Predicate()
        { public boolean pred (Object x)
            { return ( (Integer) x > 3); }};

        System.out.println("subset = " + subset(myp, myliste).toString());

        final Functor myf = new Functor()
        { public Integer fn (Object x)
            { return  (Integer) x + 2; }};

        System.out.println("mapcar = " + mapcar(myf, mylistd).toString());

        System.out.println("some = " + some(myp, myliste).toString());

        System.out.println("every = " + every(myp, myliste));

    }

}
