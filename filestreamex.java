package filestream;
import java.util.*;
import java.io.*;
import java.lang.String; //String 메서드를 쓰기위
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

class FilestreamEx {
    //파일 생성하는 함수
    static void Makefile(String list, String filename){
        FileWriter writer = null;

        try {
            writer = new FileWriter(filename); //list1을 쓰는 객체
            writer.write(list,0,list.length());
            System.out.println("success to make file");
            writer.close();
        } catch (IOException e) {
            System.out.println("fail to make file");
        }

    }

    static void Makefile(Vector<String> vec , String filename){
        FileWriter writer = null;

        String str = vec.toString(); //벡터(배열)을 문자열로 변환
        String st = str.substring(1,str.length()-1); //앞뒤 삭제
//        String str = "";
//        Iterator<String> it = vec.iterator();
//        while(it.hasNext()){
//            str += (it.next()+" ");
//        }

        try {
            writer = new FileWriter(filename); //list1을 쓰는 객체
            writer.write(st,0,st.length());
            System.out.println("success to make file");
            writer.close();
        } catch (IOException e) {
            System.out.println("fail to make file");
        }

    }

    static void Readfile(String filename){
//        InputStreamReader in = null;
//        FileInputStream fin = null;
          FileReader reader = null;
        try{
            reader = new FileReader(filename);
//            fin = new FileInputStream(filename);
//            in = new InputStreamReader(fin,"utf-8");
            int c;
            while((c=reader.read())!=-1){
                System.out.print((char)c);
            }
            System.out.println();
            reader.close();
        }
        catch(IOException e) {
            System.out.println("fail to read file");
        }
    }

    static void Readfile(String filename, Vector<String> vec){
        //1) filereader를 이용한 방법 (문자단위로 읽어오기)
        FileReader reader = null;
        //2) fileinputstream을 이요한 방법(바이트 단위로 읽어오기)ex. 이미지파일, 음악파일, 실행파일
        //FileInputStream input = null;
        try{
            reader = new FileReader(filename);
            char[] arr = new char[1024];
            //reader.read(char[] buf,offset,len) 0(offset):쓰여질 버퍼 시작 위치
            reader.read(arr,0,arr.length);
            //1) split으로 분리하기
            String [] str = String.valueOf(arr).split(","); //string.valueOf : 문자열로 형변환하는 메서드 trim : 빈칸 삭제
            for(int i=0;i<str.length;i++){
                vec.add(str[i]);
            }

            //2)StringTokenizer로 분리하기
//            String query = String.valueOf(arr);
//            StringTokenizer st = new StringTokenizer(query,",");
//            while(st.hasMoreTokens()){ //다음 토이 있을 때까지
//                String token = st.nextToken();
//                vec.add(token);
//            }

        }catch(IOException e){
            System.out.println("fail to read file");
        }
    }

    static void Readvec(Vector<String> vec){
        Iterator<String> it = vec.iterator();
        while(it.hasNext()){
            System.out.print(it.next()+" ");
        }
        System.out.println();
    }

    static Vector<String> Compare(Vector<String> vec1,Vector<String> vec2 ){
        //리턴할 벡터
        Vector<String> vec3 = new Vector<>();

        //vec1스트림생성 -> vec2와 값이 교차되는 것만 거르기 -> 컬렉션 변환(리스트로)
        vec1.stream().filter(vec2::contains).collect(Collectors.toList())
                .forEach((n)->vec3.add(n));
        return vec3;
    }

    public static void main(String[] args) {
        String list1 = "ADAMS,CARTER,CHIN,DAVIS,FOSTER,GARWICK,JAMES,JOHNSON,KARNS,LAMBERT,PETERS,RESTON,ROSEWALD,TURNER";
        String list2 = "ADAMS,ANDERSON,ANDREWS,BECH,BURNS,CARTER,DAVIS,DEMPSEY,GRAY,JAMES,JOHNSON,KATZ,PETERS,ROSEWALD,SCHMIDT,THAYER,WALKER,WILLIS";
        //파일 만들기
        //Makefile(list1,"list1.txt");
        //Makefile(list2,"list2.txt");

        //파일 읽어서 벡터에 저장하기
        Vector<String> vec1 = new Vector();
        Vector<String> vec2 = new Vector();
        Readfile("list1.txt",vec1);
        Readfile("list2.txt",vec2);
        System.out.print("vec1: ");Readvec(vec1);
        System.out.print("vec2: ");Readvec(vec2);

        //vec1, vec2 비교하여 같은 것은 vec3에 저장
        Vector<String> vec3 = new Vector<>();
        vec3=Compare(vec1,vec2);
        System.out.print("두개의 리스트중 중복되는 이름은 : ");
        //vec3 파일로 쓰고 읽기
        Readvec(vec3);
        Makefile(vec3,"result.txt");
        Readfile("result.txt");
    }
}
