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
    static int Suffix[];
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
            Suffix=new int[10010];
            PCR=new double[10010];
            for (int i=1;i<=n;i++) {
                Weight[i] = In.nextInt();
                Value[i] = In.nextInt();
                Suffix[i] = i;
                PCR[i] = (double) Value[i] / (double) Weight[i];
                System.out.printf("重量: %4d 价值: %4d 性价比: %4.3f\n", Weight[i], Value[i], PCR[i]);
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
                    SwapInt(Suffix,i,j);
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

    static int Res;
    static int Vectors[];

    public static void Greedy() {
        Vectors=new int[10010];
        for (int i=1;i<=n;i++) Vectors[i]=0;
        int Size=m;
        int Ans=0;
        for (int i=1;i<=n;i++) {
            if (Size>Weight[i]) {
                Size-=Weight[i];
                Ans+=Value[i];
                Vectors[Suffix[i]]=1;
            } else {
                break;
            }
        }
        Res=Ans;
        System.out.println("求得的解:"+Ans);
        System.out.print("解向量: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) System.out.print(Vectors[i]+",");
            else System.out.println(Vectors[i]+"}");
        }
    }

    static int Path[];
    static int f[];

    public static void FindPath(int Size) {
        while (Size>0) {
            for (int i=1;i<=n;i++) {
                if (Path[Suffix[i]]==0) {
                    if (Size-Weight[i]>=0) {
                        if (f[Size-Weight[i]]+Value[i]==f[Size]) {
                            Path[Suffix[i]]=1;
                            Size-=Weight[i];
                            break;
                        }
                    }
                }
            }
            Size--;
        }
    }

    public static void DP() {
        f=new int[10010];
        for (int i=1;i<=n;i++) {
            for (int j=m;j>=Weight[i];j--) {
                f[j]=Math.max(f[j],f[j-Weight[i]]+Value[i]);
            }
        }
        Res=f[m];
        System.out.println("求得的解:"+f[m]);
        Path=new int[10010];
        for (int i=1;i<=n;i++) Path[i]=0;
        FindPath(m);
        System.out.print("解向量: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) System.out.print(Path[i]+",");
            else System.out.println(Path[i]+"}");
        }
    }

    static int Ans=0;
    static int CW=0; //Current Weight
    static int CV=0; //Current Value
    static int Flag[];

    public static double Bound(int Index) {
        double RemainW=m-CW;
        double CurrentV=CV;
        while (Index<n && Weight[Index]<=RemainW) {
            RemainW-=Weight[Index];
            CurrentV+=Value[Index];
            Index++;
        }
        if (Index<=n) {
            CurrentV+=PCR[Index]*RemainW;
        }
        return CurrentV;
    }

    public static void BackTrack(int Index) {
        if (Index>n) {
            Ans=CV;
            return;
        }
        if (CW+Weight[Index]<=m) {
            CW+=Weight[Index];
            CV+=Value[Index];
            Flag[Suffix[Index]]=1;
            BackTrack(Index+1);
            CW-=Weight[Index];
            CV-=Value[Index];
        }
        if (Bound(Index+1)>Ans) {
            BackTrack(Index+1);
        }
    }

    public static void WriteFile(int Ans, double RunTime, int AnsRoute[]) throws FileNotFoundException {
        PrintStream Cout=new PrintStream("res.txt");
        Cout.println("求得的解: "+Ans);
        Cout.println("运行时间: "+RunTime+"s");
        Cout.close();
    }

    public static void SelectSolution() {
        System.out.println("请选择一种算法解决D{0-1}问题 (输入1-3)");
        System.out.println("1:贪心\t2:动态规划\t3:回溯");
        int Operation;
        Operation=Cin.nextInt();
        double RunTime=0.0;
        if (Operation==1) {
            long StartTime=System.nanoTime();
            Greedy();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            System.out.println("运行时间: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Vectors);
            } catch (IOException e) {

            }
        }
        if (Operation==2) {
            long StartTime=System.nanoTime();
            DP();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            System.out.println("运行时间: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Path);
            } catch (IOException e) {

            }
        }
        if (Operation==3) {
            Flag=new int[10010];
            for (int i=1;i<=n;i++) Flag[i]=0;
            long StartTime=System.nanoTime();
            BackTrack(1);
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            Res=Ans;
            System.out.print("解向量: {");
            for (int i=1;i<=n;i++) {
                if (i!=n) System.out.print(Flag[i]+",");
                else System.out.println(Flag[i]+"}");
            }
            System.out.println("求得的解: "+Ans);
            System.out.println("运行时间: "+RunTime+"ms");
            try {
                WriteFile(Res,RunTime,Flag);
            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ReadFile();
        PlottingScatterPlots();
        DataSort();
        SelectSolution();
    }
}