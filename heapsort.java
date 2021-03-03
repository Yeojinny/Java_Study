
// 힙소트 최소힙 => 내림차순정렬
//1.Scanner로 입력받은 정수들로 min heap을 생성
//2.Sorting함수로 시작 a[1],a[n]바꾸고 n-1로 heapify함수들어감
//3.heapify함수를 통해서 루트에 최솟값 위치하게함=>sorting
//반복
import java.util.Scanner;
class Main{

    static int[] Scanner(){
        Scanner scanner = new Scanner(System.in);
        int []arr=new int[30];
        int count = 0;
        int input;
        System.out.println("빈칸으로 구분하여 정수를 입력하세요");
        while ((input = scanner.nextInt()) >0) {
            count+=1;
            ///heap을 형성하며 input값 받기
            makeheap(arr,count,input); //arr배열에 count번째 input값을 넣을거
        }

        arr[0]=-count;
        return arr;
    }

    //입력 배열을 min heap으로 구현
    static void makeheap(int []a, int idx,int value){

        a[idx]=value; // 일단 idx번째 자리에 값을 넣는다.
        for(int i=idx;i>=2;i/=2){
            if(a[i]<a[i/2]){ //입력한 node가 부모 노드보다 작다면 자리 바꾸기
                int temp = a[i/2];
                a[i/2]=a[i];
                a[i]=temp;
            }
        }
    }

    static void Sorting(int []a,int n){
        //현재트리의 최솟값인 heap의 루트 노드를 배열의 맨 끝 원소(a[n])와 서로 맞 바꾼다.
        // n =1 노드갯수가 1이되면 종료시킨다.
        if(n==1) {
            return;
        }
        int temp = a[1];
        a[1]=a[n];
        a[n]=temp;

        //마지막 인덱스에 위치한 최솟값은 더 이상 위치가 변하지 않으며, 이 노드는 더 이상 heap의 노드로 취급x
        //n-1 크기의 tree에 대한 heapify를 진행한다.
        Heapify(a,1,n-1); //항상 루트에서 부터 비교 시작(루트의 자식끼리 비교)

    }
    //부모 노드 자식 노드 바꾸기
    static void Swap(int[]a, int parent, int child){
        int temp = a[child];
        a[child]=a[parent];
        a[parent]=temp;
    }

    //root가 제거된 heap을 min heap성질을 만족하도록 수선
    static void Heapify(int []a,int l,int n) { //n: 노드의 갯수 l:루트

        int left = l*2;
        int right = l*2+1;
        //두 자식 노드 중 작은 노드의 인덱스를 smaller에 저장
        int smaller;
        //자식이 둘 다 있는 경우
        if(right<=n){
            if(a[left]<a[right]) smaller = left;
            else smaller = right;
        }
        //자식 노드가 하나 존재하는 경우(left만 있는 경우)
        else if(left<=n) smaller=left; //일단 smaller를 left라고 치고(밑에서 부모랑 비교)
        //자식이 없는 노드인 경우 => a[1]과 a[n]을 바꾸고 현재 함수 끝냄 => 노드 줄이고 다시 heapify하도록 sorting함수로 go!
        else {Sorting(a,n); return;}
        //smaller가 부모노드보다 작으면 부모랑 바꾸고 heapify재진행
        if(a[smaller]<a[l]){
            Swap(a,l,smaller); 
            Heapify(a,smaller,n); //부모랑 자식이랑 바꿨으면 smaller의 자식들을 비교해야함 smaller를 l로 
        }
        else Sorting(a,n); //a[smaller]보다 부모가 더작으면 걍 그대로 두고 sorting함수로 간다. 

    }

    //오름차순 출력
    static void PrintAs(int[]a,int start,int end){
        for(int i=start;i<=end;i++){
            System.out.print(a[i]+" ");
        }
        System.out.println(" ");
    }
    //내림차순 출력
    static void PrintDs(int []a,int start,int end){
        for(int i=start;i>=end;i--){
            System.out.print(a[i]+" ");
        }
        System.out.println(" ");
    }

    public static void main(String[] args) {
        // 1. 정수 입력 받기(0보다 작거나 같은 수가 입력되면 멈춤)
        int []arr;
        arr = Scanner();
        int count = Math.abs(arr[0]);

        // input 배열 출력
        PrintAs(arr,0,count);

        int []output;
        // 힙정렬 시작
        Sorting(arr,count);

        // 힙출력
        PrintAs(arr,0,count);

    }

}
