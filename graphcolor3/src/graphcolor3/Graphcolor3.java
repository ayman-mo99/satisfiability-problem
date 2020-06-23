package graphcolor3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/*
In this code ,we try to convert a graph coloring probem into a satisfiability problem.
By knowing the edges and the number of colors and vertices we create a satisfiability formula then make 'maxsat' solve it
*/
public class Graphcolor3 {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        File file = new File("project.cnf");
        if (!file.exists()) {
            file.createNewFile();
        }

        int k = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of colors"));
        String v = JOptionPane.showInputDialog("Enter the vertices");
        String e = JOptionPane.showInputDialog("Enter the edges");
        v=v.replaceAll(" ", "");
        e=e.replaceAll(" ", "");
       
        int x[] = new int[k * v.length()];
        int y[] = new int[k * v.length()];
        int z[] = new int[k * e.length()];
        for (int i = 0; i < x.length; i++) {
            x[i] = i + 1;
            y[i] = (-1) * i - 1;
        }
       
        int j = -1;
        for (int i = 0; i < e.length();) {
            int t1, t2;
            t1 = v.indexOf(e.charAt(i));
            t2 = v.indexOf(e.charAt(i + 1));
            for (int q = 0; q < k; q++) {
                z[++j] = t1 * k + q;
                z[++j] = t2 * k + q;
            }
            i = i + 2;
        }

        for (int i = 0; i < z.length; i++) {
            z[i] = (z[i] * -1) - 1;
        }
        // now we write the satisfiability formula that corresponds to this graph and this number of colors
        String result = "";
        result = "p cnf " + (v.length() * k) + " " + ((v.length()) + (v.length() * (k * (k - 1) / 2)) + (e.length() / 2) * k);//*****
        result += "\n";

        int c = 0;
        for (int i = 0; i < v.length(); i++) {
            for (int w = 0; w < k; w++) {
                result = result + x[c++] + " ";
            }
            result = result + 0;
            result += "\n";
        }
        c = 0;

        for (int i = 0; i < v.length(); i++) {
            if (k != 1) {
                for (int w = c; w < k + c; w++) {
                    for (int q = w + 1; q < k + c; q++) {                        
                        result = result + y[w] + " " + y[q] + " 0";
                        result += "\n";
                    }
                }
                c = c + k;
            } else {
                result = result + y[i] + " ";
                result = result + 0;
                result += "\n";
            }
        }

        for (int i = 0; i < z.length; i++) {
            result = result + z[i] + " " + z[++i] + " " + 0;
            result += "\n";
        }

        System.out.println(result);
        
        PrintWriter pw = new PrintWriter(file);
        pw.print(result);
        pw.close();
        String ww = "";
        Process p = null;
        Runtime r = Runtime.getRuntime();
        try {
            p = r.exec("java -jar sat4j-maxsat.jar project.cnf");
            p.getOutputStream().close();
            InputStream processStdOutput = p.getInputStream();
            Reader rr = new InputStreamReader(processStdOutput);
            BufferedReader br = new BufferedReader(rr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.charAt(0) == 'v') {
                    System.out.println(line);
                    ww=line;
                }

            }
       clear_output(ww,k);    
        } catch (IOException ex) {
            Logger.getLogger(Graphcolor3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // this function will show The assumptions for each vertex more clearly
    //where we have a clause for each vertex
    //each clause have n numbers where n is the number if colors
    //if the number 'i' where negative than this vertex will not be colored with the color 'i'
    //he first number in each clause is the same in the other clauses and the second number and so on ...
    public static void clear_output(String ww , int k ){
        String ss=ww;
        int x =1 ;
        ss = ss.substring(2, ss.length() - 1);
        for (int i = 0; i < ss.length(); i++) {
            if (ss.charAt(i) != '-' && ss.charAt(i) != ' ') {
                x++;
            }
            if (x > k) {
                ss = ss.substring(0, i + 1) + "x" + ss.substring(i + 1, ss.length());
                x = 1;
                i++;
            }
        }
        ss = ss.replaceAll("x", " )&&( ");
        ss = "( " + ss + " )";
        ss = ss.substring(0, ss.length() - 7);
        System.out.println(ss);
    }
}
