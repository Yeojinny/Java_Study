//=====링크드리스트를 활용한 머지소트==========

//Chainlist 배열을 만든다.
//1)랜덤의 정수로 링크드리스트를 만든다. => Chainlist[0]에 해당 배열을 넣는다.
//2)split : Chainlist[0]의 링크드리스트를 반으로 잘라 [1][2]배열에 넣는 방식으로 노드가 1개가 될때까지 split한다.
//3)merge : split한것과 반대로 노드 한개짜리 배열의 노드들끼리 대소를 비교하여 빈배열에 채우며 위로 올라간다. [0]에는 오름차순으로 배열된 링크드 리스트가 들어간다.
//4)iterator : iterator는 링크드리스트에서 옮겨다니는 p가 들어있는 class이다. 해당 클래스내 next함수를 사용할때마다 p가 다음노드로 이동한다. => while문을 통해 최대 최솟값을 찾는데 활용


import java.util.Random;

class ListNode{
    private int data;
    ListNode next;
    ListNode(){}
    public ListNode(int data) {
        this.data = data;
        next =null;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }


}

class Chainlist{
    private ListNode first;
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Chainlist(){
        first = null;
    }

    public void Insert(int data){
        if(first==null) {
            //first가 null인 경우 새로운 노드를 참조하도록
            first = new ListNode(data);
        }else{
            //head노드가 null이 아닌 경우 temp노드가 head를 잠조
            //tempnode는 마지막 노드를 찾아서 참조하기 위해 사용
            ListNode tempNode = first;
            while(tempNode.next!=null){
                tempNode=tempNode.next;
            }
            tempNode.next = new ListNode(data);
        }
    }
    
    public void show(){//리스트 하나만 출력
        ListNode p = this.first;
        while(p != null){
            System.out.print(p.getData()+" ");
            p  = p.next;
        }
        System.out.println();
    }

    public Chainlist merge(Chainlist b){
        Chainlist output = new Chainlist();

        ListNode p = this.getFirst();
        ListNode q = b.getFirst();

        while(p!=null || q!=null){ //둘중에 하나라도 null이 아니면 반복
            if(p==null){  //p값이 다들어간 경우 => q를 다 넣어준다.
                output.Insert(q.getData());
                q=q.next;
                continue;
            }
            if(q==null){
                output.Insert(p.getData());
                p=p.next;
                continue;
            }
            if(p.getData()<=q.getData()){
                output.Insert(p.getData());
                p=p.next;
                continue;
            }
            if(p.getData()>q.getData()){
                output.Insert(q.getData());
                q=q.next;
                continue;
            }
        }
        return output;
}

    public Chainlist split(Chainlist b){
        Chainlist c = new Chainlist();

        c.setFirst(this.getFirst());

        int left = (int)Math.ceil(this.size/2.0);
        c.setSize(left);
        int right = this.size-left;
        b.setSize(right);
        ListNode p = c.getFirst();
        for(int i=1;i<left;i++){
            p = p.next;
        }
        b.setFirst(p.next);
        p.next = null;

        return c;
    }

    public ListNode getFirst() {
        return first;
    }

    public void setFirst(ListNode first) {
        this.first = first;
    }
}

class ChainIterator{

    private Chainlist chain;
    private ListNode current;
    ChainIterator(Chainlist a){
        this.chain = a;
        this.current = a.getFirst();
    }
    public int next(){
        if(current==null) {return 0;}
        int temp = current.getData();
        current = current.next;
        return temp;
    }
}

class Main {

    static Chainlist getData(Chainlist [] l){
        Random random = new Random();
        int nodenum = (int)(Math.random()*4+4);

        for(int i=0;i<(nodenum*2);i++){
            l[i] = new Chainlist();
        }

        System.out.println(nodenum+"개의 데이터");
        Chainlist chain = new Chainlist();
        chain.setSize(nodenum);
        for(int i=1;i<=nodenum;i++){
            chain.Insert((int)(Math.random()*99)+1);
        }

        return chain;
    }

    static void splitList(Chainlist []l){
        int k=0;
        // a를 스플릿해서 왼쪽은 c에 오른쪽은 b에 담아서 출
        for (int i = 0; i <l[0].getSize() ; i++) {
            if(l[i].getSize()==1) {
                continue;
            }
            l[k+1] = l[i].split(l[k+2]);
            k+=2;
            l[i].setFirst(null);
        }
        showList(l);
    }


    static void mergeList(Chainlist[]l) {

        int first = (l[0].getSize() * 2) - 2;  //시작할 배열 위치 인덱스
        int[] cnt = new int[first + 1];        //input을 했는지 안했는지 확인하기 위한 배열 input한 인덱스는 1로 바꿔줌
        for (int i = 0; i <= first; i++) {
            cnt[i] = 0;
        }

        int k = first - 2;

        for (int i = first; i >= 2; i -= 2) {
            int inputidx = (k / 2) + 1;

            if (l[inputidx].getSize() == 1 || cnt[inputidx] != 0) {
                inputidx--;
            }
            l[inputidx]=l[i].merge(l[i-1]);
            cnt[inputidx]=1;
            k -= 2;
        }
        showList(l);
    }


    static int FindMax(Chainlist a){
        ChainIterator ci = new ChainIterator(a);
        int max =0;
        int p =0;
        while((p = ci.next())!=0){
            if(p>max) max = p;
        }
        System.out.println("최댓값: "+max);
        return max;
    }

    static int FindMin(Chainlist a){
        ChainIterator ci = new ChainIterator(a);
        int min =100;
        int p =0;
        while((p = ci.next())!=0){
            if(p<min) min = p;
        }
        System.out.println("최솟값: "+min);
        return min;
    }

    static void showList(Chainlist[] l){
        int i=0;

        while(l[i]!=null) {
            System.out.print("[" + i + "] ");
            l[i].show();
            i++;
        }
        System.out.println("");

    }

    public static void main(String[] args){
        //리스트 배열 만들기 + l[0]데이터 초기
        Chainlist [] l = new Chainlist[20];
        l[0]=getData(l);
        l[0].show();
        splitList(l);
        mergeList(l);
//            l[2].show();
//            l[1].show();
//            a.merge(c,b);
//            a.show();
            FindMax(l[0]);
            FindMin(l[0]);



    }

}
