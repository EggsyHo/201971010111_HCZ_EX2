import javax.swing.*;
import java.io.*;
import java.util.*;

public class main extends JPanel {
    static Scanner Cin=new Scanner(System.in);
    static int Weight[];
    static int Value[];
    static double PCR[]; //Price-Cost Ratio
    static int DP[];
    static int m,n;
    public static void ReadFile() {
        int Option;
        System.out.println("请选择要处理的数据 (输入0-9):");
        Option=Cin.nextInt();
        if (Option>10 || Option<0) {
            System.out.println("没有该组数据");
        }
        String Data="data\\beibao"+String.valueOf(Option)+".in";
        System.out.println(Data);
        try {
            Scanner In=new Scanner(new FileReader(Data));
            m=In.nextInt();
            n=In.nextInt();
            Weight=new int[10010];
            Value=new int[10010];
            PCR=new double[10010];
            for (int i=1;i<=n;i++) {
                Weight[i] = In.nextInt();
                Value[i] = In.nextInt();
                PCR[i] = (double) Value[i] / (double) Weight[i];
                System.out.printf("%d %d %.3f\n", Weight[i], Value[i], PCR[i]);
            }
        } catch (IOException e) {
            System.out.println("没有该文件");
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        ReadFile();
    }
}