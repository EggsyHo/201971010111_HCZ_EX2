import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.time.Year;
import java.util.*;


public class main extends JPanel {
    static Scanner Cin=new Scanner(System.in);
    static int Weight[];
    static int Value[];
    static double PCR[]; //Price-Cost Ratio
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

    public static void SwapInt(int Data[],int a,int b) {
        int temp=Data[a];
        Data[a]=Data[b];
        Data[b]=temp;
    }

    public static void SwapDouble(double Data[],int a,int b) {
        double temp=Data[a];
        Data[a]=Data[b];
        Data[b]=temp;
    }

    public static void DataSort() {
        for (int i=1;i<=n-1;i++) {
            for (int j=i+1;j<=n;j++) {
                if (PCR[i]<PCR[j]) {
                    SwapDouble(PCR,i,j);
                    SwapInt(Weight,i,j);
                    SwapInt(Value,i,j);
                }
            }
        }
    }

    final int Space=20;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D=(Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int Width=getWidth();
        int Height=getHeight();
        g2D.draw(new Line2D.Double(Space,Space,Space,Height-Space));
        g2D.draw(new Line2D.Double(Space,Height-Space,Width-Space,Height-Space));
        Font font=new Font("Sarasa Fixed HC",Font.PLAIN,10);
        g2D.setFont(font);
        g2D.drawString("0",Space-10,Height-Space+10);
        g2D.drawString("Weight",Width-Space-20,Height-Space+10);
        g2D.drawString("Value",Space-10,Space-5);
        double xAxis=(double)(Width-2*Space)/getMaxWeight();
        double yAxis=(double)(Height-2*Space)/getMaxValue();
        g2D.setPaint(Color.pink);
        for (int i=1;i<=n;i++) {
            double x=Space+xAxis*Weight[i];
            double y=Height-Space-yAxis*Value[i];
            g2D.fill(new Ellipse2D.Double(x-2,y-2,4,4));
        }
    }

    private int getMaxWeight() {
        int MaxW=-Integer.MAX_VALUE;
        for (int i=1;i<=n;i++) {
            if (Weight[i]>MaxW) MaxW=Weight[i];
        }
        return MaxW;
    }

    private int getMaxValue() {
        int MaxV=-Integer.MAX_VALUE;
        for (int i=1;i<=n;i++) {
            if (Value[i]>MaxV) MaxV=Value[i];
        }
        return MaxV;
    }

    public static void PlottingScatterPlots() {
        JFrame Frame=new JFrame();
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.add(new main());
        Frame.setSize(300,300);
        Frame.setLocation(200,200);
        Frame.setVisible(true);
    }

    public static void Greedy() {
        int Size=m;
        int Ans=0;
        for (int i=1;i<=n;i++) {
            if (Size>Weight[i]) {
                Size-=Weight[i];
                Ans+=Value[i];
            } else {
                break;
            }
        }
        System.out.println("求得的解:"+Ans);
    }

    public static void DP() {
        int Size=m;
        int f[];
        f=new int[10010];
        for (int i=1;i<=n;i++) {
            for (int j=m;j>=Weight[i];j--) {
                f[j]=Math.max(f[j],f[j-Weight[i]]+Value[i]);
            }
        }
        System.out.println("求得的解:"+f[m]);
    }

    public static void SelectSolution() {
        System.out.println("请选择一种算法解决D{0-1}问题 (输入1-3)");
        System.out.println("1:贪心\t2:动态规划\t3:回溯");
        int Operation;
        Operation=Cin.nextInt();
        double RunTime;
        if (Operation==1) {
            long StartTime=System.nanoTime();
            Greedy();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000.0;
            System.out.println("运行时间: "+RunTime+"ms");
        }
        if (Operation==2) {
            long StartTime=System.nanoTime();
            DP();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000.0;
            System.out.println("运行时间: "+RunTime+"ms");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ReadFile();
        PlottingScatterPlots();
        DataSort();
        SelectSolution();
    }
}