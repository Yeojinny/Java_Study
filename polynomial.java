//랜덤으로 다항식 2개를 생성하여 덧셈, 곱셈 

import java.util.Scanner;
import java.util.Random;


class Node{
    private int coef; //계수
    private int expo; //지수
    Node next;
    public Node(){}
    public Node(int coef,int expo){
        this.coef = coef;
        this.expo = expo;
        this.next = null;
    }

    public int getCoef() {
        return coef;
    }

    public void setCoef(int coef) {
        this.coef = coef;
    }

    public int getExpo() {
        return expo;
    }

    public void setExpo(int expo) {
        this.expo = expo;
    }
}

class Polynomial {
    private Node first;

    public Node getFirst() {
        return first;
    }

    public void setFirst(Node first) {
        this.first = first;
    }

    public Polynomial() {
        this.first = null;
    }

    public void Set(int coef, int expo) {
        if (this.first == null) {
            this.first = new Node(coef, expo);
        } else {
            Node tempnode = this.first;
            while (tempnode.next != null) {
                tempnode = tempnode.next;
            }
            tempnode.next = new Node(coef, expo);
        }
    }

    public void Delete(int coef, int expo){

        Node p = this.getFirst();
        while(p.next.getExpo()!=expo){ //a의 지수와 같은 노드를 삭제
            p=p.next;
        }
        //p = a의 지수과 같은 노드의 앞;
        p.next = p.next.next;

    }


    public void Insert(int coef, int expo) { //지수 비교해서 넣기 //지수가 같으면 더해줘야함!
        if (first == null) {
            first = new Node(coef, expo);
        } else {
            Node tt = first;
            int a=0;
            //일단 같은 지수의 노드가 있는지 확인
            while(tt.next!=null){
                if(expo == tt.getExpo()){
                    if(coef+tt.getCoef()==0) {
                        this.Delete(coef,expo);
                        a++;
                        break;
                    }
                    tt.setCoef(coef+tt.getCoef());
                    a++;
                    break;
                }
                if(a == 1) break;
                tt=tt.next;
            }
            Node tempnode = first;
            while (tempnode != null) {
                if (expo > tempnode.getExpo()) { //first의 지수보다 크다면 앞에 생성
                    //head로 시작하는 링크드 리스트가 있을 때 노드n을 생성
                    Node n = new Node(coef, expo);
                    //새로운 노드가 head를 가리키도록 변경
                    n.next = first;
                    //노드n이 head가 되게 바꿔줌 => 맨앞에 노드가 추가된 것처럼 됨.
                    first = n;
                    break;
                }
                if (expo < tempnode.getExpo()) { //first의 지수보다 작다면 그 다음노드랑 비교
                    //fist 다음 노드가 null이라면 first뒤에 바로 넣기
                    if (tempnode.next == null) {
                        tempnode.next = new Node(coef, expo);
                        break;
                    }
                    //first다음 노드가 null이 아니라면 first다음 노드랑 비교
                    //다음노드보다 크면 다음노드자리에 넣고 다음노드를 뒤로보
                    if (expo > tempnode.next.getExpo()) {
                        Node temp = tempnode.next;
                        tempnode.next = new Node(coef, expo);
                        tempnode = tempnode.next;
                        tempnode.next = temp;
                    }
                    //다음노드보다 작다면 다음노드 다음자리에 넣음
                    if (expo < tempnode.next.getExpo()) {
                        tempnode = tempnode.next;
                        continue;
                    }
                    break;
                }
            }
        }
    }

    public void show() {
        Node p = this.first;
        while (p != null) {
            if (p.getExpo() == 0) {
                System.out.print(p.getCoef());
            } else System.out.print(p.getCoef() + "x^" + p.getExpo());
            if (p.next != null) {
                System.out.print(" (+) ");
            }
            p = p.next;
        }
        System.out.println();

    }

    public Polynomial add(Polynomial b) {
        Polynomial c = new Polynomial();
        Node p = this.getFirst();
        Node q = b.getFirst();
        int coef = 0;

        while (p != null || q != null) {
            if (p == null) {
                c.Insert(q.getCoef(), q.getExpo());
                q = q.next;
                continue;
            }
            if (q == null) {
                c.Insert(p.getCoef(), p.getExpo());
                p = p.next;
                continue;
            }
            //지수가 같으면 계수끼리 더하고 다음 노드 비교
            if (p.getExpo() == q.getExpo()) {
                coef = p.getCoef() + q.getCoef();
                c.Insert(coef, p.getExpo());
                p = p.next;
                q = q.next;
                continue;
            }
            if (p.getExpo() < q.getExpo()) {
                c.Insert(q.getCoef(), q.getExpo());
                q = q.next;
                continue;
            }
            if (p.getExpo() > q.getExpo()) {
                c.Insert(p.getCoef(), p.getExpo());
                p = p.next;
                continue;
            }

        }

        return c;
    }


    public Polynomial multiply(Polynomial b) {
        Polynomial c = new Polynomial();
        Node p = this.getFirst();
        Node q = b.getFirst();
        int coef = 0;
        int expo = 0;

        while (q != null) {
            //p가 한바퀴 다돌면 q한칸 next해주고 p를 다시 맨처음부터 시작
            if(p==null){
                q=q.next;
                p=this.getFirst();
            }
            if(p!=null && q!=null) {
                //p가 null이 될때까지 q1이랑 곱한다.
                //p의 지수가 0인경우 q의 지수 반영
                if(p.getExpo()==0){
                    coef = p.getCoef() * q.getCoef();
                    expo = q.getExpo();
                    c.Insert(coef, expo);
                    p = p.next;
                    continue;
                }
                if(q.getExpo()==0){
                    coef = p.getCoef() * q.getCoef();
                    expo = p.getExpo();
                    c.Insert(coef, expo);
                    p = p.next;
                    continue;
                }
                coef = p.getCoef() * q.getCoef();
                expo = p.getExpo() + q.getExpo();
                c.Insert(coef, expo);
                p = p.next;
                }
            }
        return c;
        }

    }



class Main {

    static Polynomial getData() {
        Polynomial poly = new Polynomial();
        Random random = new Random();

        int num = random.nextInt(10)+1; //다항식의 갯수 1~10

        int []cnt = new int[num]; //이미 들어간 지수인지 확인


        for (int i = 0; i < num; i++) {
            cnt[i]=10;
            int a =0;
            int coef = random.nextInt(201)-100; //계수 -100~100사이의 정수
            int expo = random.nextInt(10); //지수 0~9
            //들어간 지수가 아니라면 넣는다.
            for(int j = 0;j<num;j++){
                if(expo==cnt[j]) { a++; break;}
            }
            if(a == 1) continue;
            poly.Insert(coef, expo);
            cnt[i]=expo;
        }
        return poly;
    }


    public static void main(String[] args) {
        Polynomial a,b,c,d = new Polynomial();

        a = getData();
        b = getData();
        System.out.print("a= ");a.show();
        System.out.print("b= ");b.show();
        System.out.println(" ");
        c=a.add(b);
        System.out.print("a+b= ");c.show();
        d=a.multiply(b);
        System.out.print("a*b= ");d.show();
    }


}
