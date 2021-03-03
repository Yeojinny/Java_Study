package ledger2;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

class Journal{
    private int Acct;
    private int CheckNum;
    private String Date;
    private String Description;
    private Double Amount;

    public Journal(int acct, int checkNum, String date, String description, Double amount) {
        Acct = acct;
        CheckNum = checkNum;
        Date = date;
        Description = description;
        Amount = amount;
    }

    public int getAcct() {
        return Acct;
    }

    public void setAcct(int acct) {
        Acct = acct;
    }

    public int getCheckNum() {
        return CheckNum;
    }

    public void setCheckNum(int checkNum) {
        CheckNum = checkNum;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "Acct=" + Acct +
                ", CheckNum=" + CheckNum +
                ", Date='" + Date + '\'' +
                ", Description='" + Description + '\'' +
                ", Amount=" + Amount +
                '}'+"\n";
    }
}

//계정과목별로 분류한 클래스
class Sort implements Comparable<Sort>{
    private int Acct;
    private String Title;
    private Double Prev_bal;
    private Double New_bal;
    private ArrayList<Journal> sortjournal;

    public int getAcct() {
        return Acct;
    }

    public void setAcct(int acct) {
        Acct = acct;
    }

    public Double getPrev_bal() {
        return Prev_bal;
    }

    public Double getNew_bal() {
        return New_bal;
    }

    public Sort(int acct, String title, Double prev_bal, Double new_bal, ArrayList<Journal> sortjournal) {
        Acct = acct;
        Title = title;
        Prev_bal = prev_bal;
        New_bal = (double)Math.round(new_bal*100)/100;
        this.sortjournal = sortjournal;
    }

    @Override
    public String toString() {
        return "Acct" + Acct + "Title: " + Title + " Prev_bal: " + Prev_bal + " New_bal: " + New_bal +"\n"+
                sortjournal +"\n";
    }

    @Override
    public int compareTo(Sort Acct0){
        int target = Acct0.getAcct();
        if(Acct==target) return 0;
        else if(Acct>target) return 1;
        else return -1;
    }
}

//월별 비용계정
class Posting{
    private int Acct;
    private String Title;
    private HashMap<Integer,Double> Balance;

    public Posting(int acct, String title, HashMap<Integer, Double> balance) {
        Acct = acct;
        Title = title;
        Balance = balance;
    }

    public int getAcct() {
        return Acct;
    }

    public String getTitle() {
        return Title;
    }

    public void setAcct(int acct) {
        Acct = acct;
    }

    public HashMap<Integer, Double> getBalance() {
        return Balance;
    }

    public void setBalance(HashMap<Integer, Double> balance) {
        Balance = balance;
    }

    public String getBal(){

        String str = "";
        for(int i=1;i<=getBalance().size();i++){
            if(getBalance().get(i+1)==null) {
                str+=getBalance().get(i);
                break;
            }
            else str+=getBalance().get(i)+",";
        }
        return str;
    }

    @Override
    public String toString() {
        return "Acct: " + Acct + " Title=: " + Title + " " +Balance+"\n";
    }
}

public class LedgerEx2 {

    //계정과목 hashmap 생성
    static void getLedger(HashMap ledger){
        FileReader reader = null;
        String line = "";
        try{
            reader = new FileReader("ledger1.txt");
            BufferedReader bufReader = new BufferedReader(reader);
            while((line=bufReader.readLine())!=null) {
               ledger.put(valueOf(line.split(",")[0]),line.split(",")[1]);
            }
            reader.close();
            bufReader.close();
        }catch(IOException e) {
            System.out.println("fail to read file");
        }
    }

    //expense_account파일 읽어서 배열에 저장하기
    static void getExpense_account(ArrayList expense){
        FileReader reader = null;
        String line = "";
        try{
            reader = new FileReader("expense_account.txt");
            BufferedReader bufReader = new BufferedReader(reader);
            while((line=bufReader.readLine())!=null) {
                String []arr = line.split(",");
                HashMap<Integer,Double> map=new HashMap<>();
                int j = 2;
                for(int i=1;i<arr.length-1;i++){
                        map.put(i,Double.parseDouble(arr[j]));
                        j++;
                }
                Posting post = new Posting(parseInt(arr[0]),arr[1],map);
                expense.add(post);
            }
            reader.close();
            bufReader.close();
        }catch(IOException e) {
            System.out.println("fail to read file");
        }

    }


    //journal 파일 읽어서 배열에 저장하기
    static void readJournal(String filename, ArrayList journal){
        FileReader reader = null;
        String line = "";
        try{
            reader = new FileReader(filename);
            BufferedReader bufReader = new BufferedReader(reader);
            while((line=bufReader.readLine())!=null) {
                Journal jou = new Journal(parseInt(line.split(",")[0]),parseInt(line.split(",")[1]),line.split(",")[2],
                        line.split(",")[3],Double.parseDouble(line.split(",")[4]));
                journal.add(jou);
            }
            reader.close();
            bufReader.close();
        }catch(IOException e) {
            System.out.println("fail to read file");
        }
    }

    //계정과목 리스트랑 당월 journalentry를 받아서 정리하기
    static void Sort(ArrayList sort,HashMap ledger, ArrayList journal,ArrayList expense,int month) {
        Iterator<Integer> it = ledger.keySet().iterator();
        while(it.hasNext()){
            int led = it.next();
            Stream<Journal> jou = journal.stream();
            ArrayList<Journal> jou1 = new ArrayList<>();
            jou.filter((n)->n.getAcct()==led).forEach(n->jou1.add(n));

            Iterator<Posting> pos = expense.iterator();
            HashMap<Integer,Double> map=new HashMap<>();
            while(pos.hasNext()){
                Posting pos1 = pos.next();
                if(pos1.getAcct()==led)  map = pos1.getBalance();
            }

            Optional<Double> new_bal = jou1.stream().map((n)->n.getAmount()).reduce((a,b)->(a+b));
            //optinal 객체 값 반환받으려면 get() 값이 없으면 noSuchElement발생
            Double bal =map.get(month-1)+new_bal.orElse(0.0);
            Sort sort1 = new Sort(led,(String)ledger.get(led), map.get(month-1),bal,jou1);
            sort.add(sort1);

            }
        sort.sort(Comparator.naturalOrder());

    }

    //계정과목에 월데이터 업데이트하기
    static void Update(ArrayList posting, ArrayList sort,int month){
        Iterator<Posting> it = posting.iterator();

        while(it.hasNext()){
            Posting po = it.next();
            HashMap map = po.getBalance();
            Stream<Sort> st = sort.stream();
            //expense의 계정과목에 알맞은 new bal를 기입한다.
            st.filter((n)->po.getAcct()==n.getAcct()).forEach((n)->map.put(month,n.getNew_bal()));
        }
        //expense_account배열 및 파일 업데이트
        Write(posting);
    }

    //expense_account 파일 쓰기
    static void Write(ArrayList<Posting> expense){
        FileWriter writer = null;
        Iterator<Posting> ex = expense.iterator();
        try{
            writer = new FileWriter("expense_account.txt",false);
            while(ex.hasNext()){
                Posting ex1 = ex.next();
                String str = "";
                str = Integer.toString(ex1.getAcct())+","+ex1.getTitle()+","+ex1.getBal();
                writer.write(str,0,str.length());
                writer.write("\r\n"); //개행
            }
            writer.close();
        }catch(IOException e){
            System.out.println("fail to write");
        }

    }

    public static void main(String[] args){
        //계정과목 파일 읽어서 hashmap 만들기
        HashMap<Integer,String> ledger = new HashMap<>();
        getLedger(ledger);
        Object m;
        HashSet<String> h;
        LinkedList<String> l;


       // lombok.val  = h;

        //expense account 파일 읽어서 배열에 저장하기
        ArrayList<Posting> expense_account = new ArrayList<>();
        getExpense_account(expense_account);
//=========================================================================
        //journal 파일 읽어서 배열에 저장하기
        ArrayList<Journal> journals_Apr = new ArrayList<>();
        readJournal("journal1.txt",journals_Apr);

        //계정별로 sort해서 new bal 구하기
        //4월 journal entry 정리하기
        ArrayList<Sort> sort_Apr = new ArrayList<>();
        Sort(sort_Apr,ledger,journals_Apr,expense_account,4);

        //월별 Expense account 업데이트하기
        Update(expense_account,sort_Apr,4);
        System.out.println("업데이트 ");
        System.out.println(expense_account);
        
        //5월 journal 파일 정리해서 업데이트 하기
//        ArrayList<Journal> journals_May = new ArrayList<>();
//        readJournal("journal2.txt",journals_May);
//        ArrayList<Sort> sort_May= new ArrayList<>();
//        Sort(sort_May,ledger,journals_May,expense_account,5);
//        Update(expense_account,sort_May,5);
//        System.out.println("5월 저널 업데이트 ");
//        System.out.println(expense_account);
    }

}
