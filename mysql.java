package sql;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.*;
import java.util.Scanner;

//insert
public class DbTest01 {

    //1)데이터 스캔 받아서 파일로 저장하기
    static void makeFile(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("저장할 파일 이름을 입력하세요");
        String filename = scanner.nextLine();

        System.out.println("데이터를 입력할 테이블을 입력하세요");
        String tablename =scanner.nextLine();

        System.out.println("입력할 row 수를 입력하세요");
        int input = scanner.nextInt();

        if(tablename.equals("s")) System.out.println("sno,sname,status,city 순서대로 입하세요(콤바로구분)");
        else if(tablename.equals("p")) System.out.println("pno,pname,color,weight,city 순서대로 입력하세요(콤마로구분)");
        else if(tablename.equals("sp"))  System.out.println("sno,pno,qty 순서대로 입력하세요(콤마로구분)");

        Scanner scan = new Scanner(System.in);
        FileWriter writer = null;
        try{
            writer = new FileWriter(filename,false);
            for(int i=0;i<input;i++) {
                String str = scan.nextLine();
                writer.write(str,0,str.length());
                writer.write("\r\n");
            }
            writer.close();
            System.out.println("파일에 저장되었습니다.");
        }catch(IOException e){
            System.out.println("fail to make file");
        }

        Insert(filename,tablename);
    }

    //2)파일 읽어서 db에 insert
    public static void Insert(String filename,String tablename){
        //1) db연결
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        //sql문을 데이터베이스에 보내기 위한 객체
        PreparedStatement pstmt = null;
        String SQL = "";

        try {
            Class.forName(JDBC_DRIVER); //jdbc 드라이버 로딩
            Connection conn = DriverManager.getConnection( //db연결
                    "jdbc:mysql://localhost:3306/javadb?autoReconnect=true&useSSL=false&serverTimezone=UTC",
                    "root",
                    "tiger"
            );
            //2)파일 읽기
            FileReader reader = null;
            String line = "";
            try{
                reader = new FileReader(filename);
                BufferedReader bufreader = new BufferedReader(reader);

                if(tablename.equals("s")) {
                    SQL = "insert into s(sno,sname,status,city) values(?,?,?,?)";
                    pstmt = conn.prepareStatement(SQL);
                    int r=0;
                    int rows=0;
                    while ((line = bufreader.readLine()) != null) {
                        pstmt.setString(1, line.split(",")[0]);
                        pstmt.setString(2, line.split(",")[1]);
                        pstmt.setInt(3, Integer.parseInt(line.split(",")[2]));
                        pstmt.setString(4, line.split(",")[3]);
                        r = pstmt.executeUpdate();
                        rows++;
                    }
                    //sql문장을 실행하고 결과를 리턴 - 변경된 row 수 int type으로 리턴
                    System.out.println("updated rows = " + rows);
                }
                else if(tablename.equals("p")) {
                    SQL = "insert into p(pno,pname,color,weight,city) values(?,?,?,?,?)";
                    pstmt = conn.prepareStatement(SQL);
                    int r=0;
                    int rows=0;
                    while ((line = bufreader.readLine()) != null) {
                        pstmt.setString(1, line.split(",")[0]);
                        pstmt.setString(2, line.split(",")[1]);
                        pstmt.setString(3, line.split(",")[2]);
                        pstmt.setFloat(4, Float.parseFloat(line.split(",")[3]));
                        pstmt.setString(5, line.split(",")[4]);
                        r = pstmt.executeUpdate();
                        rows++;
                    }
                    System.out.println("updated rows = " + rows);
                }
                else if(tablename.equals("sp")) {
                    SQL = "insert into sp(sno,pno,Qty) values(?,?,?)";
                    pstmt = conn.prepareStatement(SQL);
                    int r=0;
                    int rows=0;
                    while ((line = bufreader.readLine()) != null) {
                        pstmt.setString(1, line.split(",")[0]);
                        pstmt.setString(2, line.split(",")[1]);
                        pstmt.setString(3, line.split(",")[2]);
                        r = pstmt.executeUpdate();
                        rows++;
                    }
                    System.out.println("updated rows = " + rows);
                }
                reader.close();
                bufreader.close();
            }catch(IOException e){
                System.out.println("fail to read file");
            }

        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
    }

    //조회하기(JOIN)
    static void Select(){
        //상품X를 공급하는 모든 공급자의 이름과 도시를 찾아라
        System.out.println("조회하려는 상품명을 기입하세요");
        Scanner scanner = new Scanner(System.in);
        String product = scanner.nextLine();

        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        PreparedStatement pstmt = null;
        String SQL = "select s.sname,s.city from s,p,sp where s.sno=sp.sno and p.pno=sp.pno and p.pname =?";
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/javadb?autoReconnect=true&useSSL=false&serverTimezone=CTT",
                    "root",
                    "tiger"
            );
             //pstmt는 객체를 캐시에 담아 재사용한다.
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1,product); //인덱스를 string으로 위에서 선언한 sql문장 첫번쨰 물음표자리에 product를 넣어서 string으로 만든다.
            ResultSet rs1 = pstmt.executeQuery();

            System.out.println(product+"를 공급한 공급자 이름과 도시");
            while (rs1.next()) {
                String sname = rs1.getString("sname");
                String city = rs1.getString("city");
                System.out.println(sname + " - " + city );
            }
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }

    }

    //3) 모든 푸품의 부품별 공급 수량을 찾아 부품 이름, 부품번호, 총집계 수량을 file에 쓰기
    static void TotalQty(){

        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        PreparedStatement pstmt = null;
        String SQL = "select p.pname,p.pno,sum(sp.qty) from p,sp where p.pno=sp.pno group by sp.pno";

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/javadb?autoReconnect=true&useSSL=false&serverTimezone=CTT",
                    "root",
                    "tiger"
            );
            //pstmt는 객체를 캐시에 담아 재사용한다.
            pstmt = conn.prepareStatement(SQL);
            ResultSet rs1 = pstmt.executeQuery();

            while (rs1.next()) {
                String pname = rs1.getString("pname");
                String pno = rs1.getString("pno");
                String qty = Integer.toString(rs1.getInt("sum(sp.qty)"));
                String str = pname+","+pno+","+qty;
                FileWriter writer = null;
                try {
                    writer = new FileWriter("Total_Quantity.txt",true);
                    writer.write(str,0,str.length());
                    writer.write("\r\n");
                    writer.close();
                }catch(IOException e){
                    System.out.println("fail to make file");
                }
            }
            System.out.println("상품별 총 공급수량이 정상적으로 파일에 저장되었습니다.");
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }

    }


    public static void main(String[] args) {
        //1) 데이터 스캔 받아서 파일로 저장 -> 파일 읽어서 db에 저장
        //makeFile();
        //2) 상품x를 입력받아 x를 공급하는 모든 공급자의 이름과 도시 찾기
        //Select();
        //3) 모든 푸품의 부품별 공급 수량을 찾아 부품 이름, 부품번호, 총집계 수량을 file에 쓰기
        TotalQty();
    }
}
