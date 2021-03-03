//n장의 로또 발행 -> n장 중 1장의 복권을 1등으로 지정 -> 해당 복권의 ball,bonus를 비교하여 폰번호+등수 기록 => 폰번호 입력시 등수 확인 가능
//복권 1장에 동일한 번호가 존재할 수 없기 때문에 hashset을 이용
//폰번호를 통해 등수를 확인 할 수 있도록 hashmap 

import java.util.Scanner;
import java.util.*;
import java.util.Random;


//로또 클래스
class Lotto{
    int number; //복권번호
    int bonus;
    HashSet<Integer> ball = new HashSet<Integer>();  //hashset : 저장되는 데이터가 중복될 수 없다.

    public Lotto(){}
    public Lotto(int number){
        this.number = number;
        Random random = new Random();
        bonus = random.nextInt(10); //0~9상이의 보너스볼
        while(ball.size() != 6){
            ball.add(random.nextInt(45)+1);
        }
    }

    public int getNumber() {
        return number;
    }

    public int getBonus() {
        return bonus;
    }

    public HashSet<Integer> getBall() {
        return ball;
    }


    @Override
    public String toString() {
        return "Lotto{" +
                "number=" + number +
                ", bonus=" + bonus +
                ", ball=" + ball +
                '}';
    }
}


class Main {
    //복권 발행
    static void Makelotto(ArrayList list){
        Random random = new Random();
        int ballnum = 0;

        for(int i = 1; i<=1000;i++){
           Lotto n = new Lotto(i); //i번 로또 생성
           list.add(n);
        }

    }


    //복권 자동 구매
    static void Buy_Lotto(ArrayList list, HashMap record){
        System.out.println("구매자수를 입력하세요(1000명이하)");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt(); //구매자수
        Collections.shuffle(list); //판매 복권 섞기

        //구매자만큼 해쉬맵에 기록
        Iterator<Lotto> it = list.iterator();
        int count = 0;
        while(count!=num){
            Lotto n = it.next();
            Random random = new Random();
            //폰번호 랜덤 발행
            String phone = "010";
            for(int i=1;i<=8;i++){
                phone = phone + Integer.toString(random.nextInt(10)); //0~9까지
            }

            int lottonum = n.getNumber();
            record.put(phone,lottonum); //기록
            //n.setIssold(true); //기록 후 해당 복권은 판매된 것으로 바꿔줌
            count++;
        }
    }

    //로또 추첨
    static HashMap Draw(ArrayList list,HashMap record){
        Random random = new Random();
        //win : 당첨 복권번호
        int win = random.nextInt(1000)+1;
        System.out.println("1등 당첨 로또 번호는 "+win);
        Lotto winner = ((Lotto)list.get(win-1)); //당첨 번호의 로또
        int bon = winner.bonus; //당첨 보너스 번호
        HashSet<Integer> winball = new HashSet<>(); //당첨 볼번호

        //당첨자 있는지 확인하여 hashmap에 기록(key:phone, value:rank)
        Iterator<String> keys = record.keySet().iterator(); //구매기록 탐색 iterator
        HashMap<String,String> result = new HashMap<>();    //추첨결과 기록을 위한 hashset

        while(keys.hasNext()){
            winball = winner.getBall(); //당첨 볼 hashset

            String key = keys.next();
            int num = (int)record.get(key); //해당 구매자의 복권 번호
            Lotto p = ((Lotto)list.get(num-1)); //해당 번호의 로또

            int bonus = 0;

            p.ball.retainAll(winball);

            if(p.getBonus()==bon) bonus++;
            //등수 찾기
            if(p.ball.size()==6) result.put(key,"1등");
            else if(p.ball.size()==5 && bonus==1) result.put(key,"2등");
            else if(p.ball.size()==5 && bonus==0) result.put(key,"3등");
            else if(p.ball.size()==4) result.put(key,"4등");
            else if(p.ball.size()==3) result.put(key,"5등");
            else result.put(key,"꽝");

            bonus = 0;

        }
        return result;
    }

    //당첨결과 폰번호로 조죄하기
    static void CheckResult(HashMap result){
        Scanner scanner = new Scanner(System.in);
        System.out.println("조회자의 폰번호를 입력하세요");
        String phone = scanner.next();
        String Result = (String)result.get(phone);
        System.out.println("당첨결과는? "+Result);
    }

    //오름차순 정렬
    static Comparator<Lotto> Order = new Comparator<Lotto>() {
        @Override
        public int compare(Lotto arg0, Lotto arg1) {
            return arg0.getNumber()-arg1.getNumber();
        }
    };

    //전체 로또 보여주
    static void ShowLotto(ArrayList list){

        Collections.sort(list,Order);
        Iterator<Lotto> it = list.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }

    }

    static void ShowResult(HashMap result,HashMap record, ArrayList list) {

        Iterator<String> keys = result.keySet().iterator(); //당첨기록 탐색 iterator

        while (keys.hasNext()) {
            String key = keys.next();
            String res = (String) result.get(key); //해당 구매자의 추첨 결과
            int num = (int)record.get(key); //해당 구매자의 복권 번호
            Lotto p = ((Lotto) list.get(num - 1)); //해당 번호의 로또
            System.out.println(key+" 복권번호"+num+" "+" - "+res);

        }
    }

    public static void main(String[] args) {
    //1)로또1000장 발행
        ArrayList<Lotto> Lotto_list = new ArrayList<>();
        Makelotto(Lotto_list);
    //2)로또 구매 = > 해쉬맵에 복권 구매자 저장
        HashMap<String,Integer> record = new HashMap<>();
        Buy_Lotto(Lotto_list,record);
        Collections.sort(Lotto_list,Order);

    //3)로또 추첨
        HashMap<String,String> result;
        result = Draw(Lotto_list,record);

    //4)폰번호로 당첨결과 조회
        ShowResult(result,record, Lotto_list);
        CheckResult(result);


    }


}
