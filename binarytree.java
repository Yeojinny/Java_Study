//숫자 입력받아서 바이너리 트리 만들기 root는 첫 입력 숫자로 고정
//값이 작으면 왼쪽 크면 오른쪽으로 Insert
//delete
//iterator를 통해 최대,최솟값 구하기
//트리가 치우친 경우 depth구해서 split하기 

import java.util.Scanner;
import java.util.Stack;

class Node{
    private int data;
    Node left;
    Node right;
    public Node(){};
    public Node(int data){
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

}

class Tree{
    private Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Tree(){
        this.root=null;
    }


    public void Insert(int data){
        if(this.root==null){
            this.root = new Node(data);
        }
        else{
            Node temp = root;
            //같은 데이터가 있다면 while 나감
            while(temp.getData() != data) {
                //부모보다 작고, left가 null값이라면 넣기
                if(data<temp.getData() && temp.left==null) {
                    Node n = new Node(data);
                    temp.left=n;
                    break;
                }
                //부모보다 작고, left가 null이 아니라면 계속 비교
                if(data<temp.getData()) {
                    temp = temp.left;
                    continue;
                }
                //부모보다 크고, right가 null값이라면 넣기
                if(data>temp.getData() && temp.right==null){
                    Node n = new Node(data);
                    temp.right=n;
                    break;
                }
                if(data>temp.getData()){
                    temp = temp.right;
                    continue;
                }
            }
        }

    }

    private void inorder(Node p){
        if (p != null)
        {
            inorder(p.left);
            System.out.print(p.getData()+" > ");
            inorder(p.right);
        }
    }
    public void Show(){
        Node p = root;
        inorder(p);
        System.out.println("");
    }

    public void Delete(int a){
        Node findNode = root;
        Node delNodeparent = findNode; //지우려는 노드의 부모

        //일단 트리에 삭제하려는 데이터 a가 있는지 확인
        while(findNode.getData()!=a){

            //삭제하려는 노드가 탐색노드보다 작다면 => 왼쪽으로 탐색
            if(findNode.getData() > a){
                delNodeparent = findNode;
                findNode = findNode.left;
            }
            //삭제하려는 노드가 탐색노드보다 크다면 => 오른쪽으로 탐색
            if(findNode.getData() < a){
                delNodeparent = findNode;
                findNode = findNode.right;
            }
            //삭제하려는 노드가 없다면 리턴
            if(findNode == null){
                System.out.println("삭제하려는 데이터가 트리안에 없습니다.");
                return;
            }
        }
        //=> 트리내에 a가 존재하는 경우 while문을 빠져나와 일로옴
        Node delNode = findNode;
        //자식이 없는 경우 => 걍삭제
        if(delNode.left==null && delNode.right==null){
            if(delNode==root) root = null;
            if(delNodeparent.getData() > a) delNodeparent.left = null;
            if(delNodeparent.getData() < a) delNodeparent.right = null;
            return;
        }

        //자식이 하나인 경우 => 삭제하려는 노드와 바꿈
        //1) 왼쪽자식만 있는 경우
        if(delNode.right==null){
            if(delNode==root) root=delNode.left;
            //1)-1 del노드가 부모보다 큰경우
            if(delNodeparent.getData() < a) delNodeparent.right = delNode.left;
            //1)-2 del노드가 부모보다 작은 경우
            if(delNodeparent.getData() > a) delNodeparent.left = delNode.left;
            return;
        }
        //2) 오른쪽 자식만 있는 경우
        if(delNode.left==null){
            if(delNode==root) root=delNode.right;
            if(delNodeparent.getData() < a) delNodeparent.right = delNode.right;
            if(delNodeparent.getData() > a) delNodeparent.left = delNode.right;
            return;
        }

        //자식이 둘인 경우 => 오른쪽 subtree에서 가장 작은 노드를 찾아 바꿈 => 바꾸려는 노드한테 자식이 달려있으면 밀어올림
        Node min = inorderSucc(delNode);
        Node delright = delNode.right;
        Node minright = min.right;

        //inorersucc이 없을 때(삭제하려는 노드 오른쪽의 왼쪽이 없을 때)
        if(delright==min){
            delNode.setData(min.getData());
            delNode.right = min.right;
            return;
        }

        //inorderSucc이 리프노드가 아닐 때
        while(min.right!=null) {
            if (minright.left == null) {
                if (delNode == root) {
                    root.setData(min.getData());
                    delright.left = min.right;
                    return;
                }
                delNode.setData(min.getData());
                delright.right = min.right;
                return;
            }
            Node temp = Succprev(min);
            delNode.setData(min.getData());
            delNode = min;
            min = inorderSucc(delNode);
        }
        //inorderSucc이 리프노드일 때
        Node temp = Succprev(min);
        delNode.setData(min.getData());
        temp.left = null;
        return;

    }

    //오른쪽에서 가장 작은 값
    public Node inorderSucc(Node p){
        Node t= p.right;
        while(t.left!=null){
            t=t.left;
        }
        return t;
    }
    //후임자의 부모
    public Node Succprev(Node p){
        Node findNode = root;
        Node Succprev = findNode;

        while(findNode!=p) {
            //찾는 노드가 탐색노드보다 작다면 => 왼쪽으로 탐색
            if (findNode.getData() > p.getData()) {
                Succprev = findNode;
                findNode = findNode.left;
            }
            //삭제하려는 노드가 탐색노드보다 크다면 => 오른쪽으로 탐색
            if (findNode.getData() < p.getData()) {
                Succprev = findNode;
                findNode = findNode.right;
            }
        }
        return Succprev;
    }

    //깊이 구하기
    public int Depth(Node p){
        if(p==null) {
            return 0;
        }
        return 1+ Math.max(Depth(p.left),Depth(p.right));
    }

    //깊이가
    public void Split() {
        //depth가 음수:오른쪽이 큼, 양수: 왼쪽이 큼
        int depth = Depth(root.left) - Depth(root.right);
        int dep = Math.abs(depth / 2);

        Node nr = root;
        Node nl = nr;
        //차이가 2미만일 때
        if(Math.abs(depth)<2){
            System.out.println("split이 불가능 합니다.");
            return;
        }
        //오른쪽 트리가 더 클 때
        if (depth < 0) {
            for (int i = 0; i < dep; i++) {
                nl = nr;
                nr = nr.right;
            }

            nl.right = null;
            nr.left = root;
            this.setRoot(nr);
        }

        //왼쪽 트리가 더 클 때
        if (depth > 0) {
            for (int i = 0; i < dep; i++) {
                nl = nr;
                nr = nr.left;
            }

            nl.left = null;
            nr.right = root;
            this.setRoot(nr);
        }
    }

}

//Iterator클래스 만들기
class InorderIterator{
    public Node current;
    private Stack<Node> s = new Stack<>();

    //생성자 => current노드를 root로 지정
    public InorderIterator(Tree tree){
        current = tree.getRoot();
    }

    //next함수
    public int next(){
        while(current!=null){
            s.push(current);
            current = current.left;
        }
        //스택이 비어있지 않다면
        if(!s.isEmpty()){
            current = s.pop();
            int temp = current.getData();
            current = current.right;
            return temp;
        }
        else return 0;
    }

}


class Main {

    static void GetTree(Tree t){
        Scanner scan = new Scanner(System.in);
        System.out.println("트리를 구성할 정수를 입력하세요.");
        int data = 0;

        while((data=scan.nextInt())!=0){
            t.Insert(data);
        }

    }

    static void Findmax(Tree tree){
        InorderIterator it = new InorderIterator(tree);
        int max = 0;
        int s = it.next();
        while(s!=0){
            if(s>max) max = s;
            s = it.next();
        }
        System.out.println("최댓값은: "+max);
    }

    static void Findmin(Tree tree){
        InorderIterator it = new InorderIterator(tree);
        int min = 100;
        int s = it.next();
        while(s!=0){
            if(s<min) min = s;
            s = it.next();
        }
        System.out.println("최솟값은: "+min);
    }

    public static void main(String[] args){
        Tree T = new Tree();
        GetTree(T);
        T.Show();
//      T.Delete(5);
//      T.Show();
//      Findmax(T);
//      Findmin(T);
//      System.out.println(T.Depth(T.getRoot()));
        T.Split();
        T.Show();
    }
}
