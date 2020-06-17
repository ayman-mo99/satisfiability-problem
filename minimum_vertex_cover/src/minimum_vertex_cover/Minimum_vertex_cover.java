package minimum_vertex_cover;

import java.io.*;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

public class Minimum_vertex_cover {
    static int n1;
    static int n2;
    public static void main(String[] args) {

        try {
            File file = new File("project.wcnf");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(file);
            n1 = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of vertices"));
            int mat[][] = new int[n1][n1];                 //adjacency matrix
            n2 = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of edges"));

            int v1;
            int v2;
            for (int i = 0; i < n2; i++) {// take the edges
                String e = JOptionPane.showInputDialog("Enter the number of the two vertices with space betwen them");
                v1 = Integer.parseInt(e.substring(0, e.indexOf(' ')));
                v2 = Integer.parseInt(e.substring(e.indexOf(' ') + 1, e.length()));
                //  System.out.println(v1+" "+v2);
                mat[v1 - 1][v2 - 1] = 1;
                mat[v2 - 1][v1 - 1] = 1;
            }
            
            String form = "p wcnf " + n1 + " " + (n1 + n2) + "\n"; // the header
            
            for (int i = 0; i < n1; i++) {
                form += "1 " + (-1 * (i + 1)) + " 0\n";//soft clauses
            }

            for (int i = 0; i < n1; i++) {
                for (int j = i; j < n1; j++) {
                    if (mat[i][j] == 1) {
                        form += (n1 + 1) + " " + (i + 1) + " " + (j + 1) + " " + 0 + "\n"; // hard clauses
                    }
                }
            }

            System.out.println(form);
            
            pw.print(form);// witre the formula
            pw.close();

        } catch (IOException ex) {
            Logger.getLogger(Minimum_vertex_cover.class.getName()).log(Level.SEVERE, null, ex);
        }

        Process p = null;
        Runtime r = Runtime.getRuntime();
        try {
            p = r.exec("java -jar sat4j-maxsat.jar project.wcnf");
            p.getOutputStream().close();
            InputStream processStdOutput = p.getInputStream();
            Reader r2 = new InputStreamReader(processStdOutput);
            BufferedReader br = new BufferedReader(r2);
            String targt;
            while ((targt = br.readLine()) != null) {
                if (targt.charAt(0) == 'v') {
                    System.out.println(targt);
                    out(targt);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Minimum_vertex_cover.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

     static void out(String l) {
        if (n2 == 0) {//if no edges
            System.out.print("the set of vertices is : ");
            for (int j = 1; j <= n1; j++) {               
                System.out.print("v" + j + " ");
            }
            System.out.println("which is all the vertcis because there is no edges in the graph ");
        } else {
            
            ArrayList<String> vertx = new ArrayList<>();
            l = l.substring(2, l.length());
            l = l.substring(0, l.length() - 1);
            
            int i = 0;
            String temp;
            while (true) {// this loop to put every result to each boolean variable in the array list
                i = l.indexOf(" ");
                temp = l.substring(0, i);
                l = l.substring(i + 1, l.length());
                vertx.add(temp);
                if (l.isEmpty()) {
                    break;
                }
            }
            
            System.out.print("the set of vertices is : ");
            for (int j = 0; j < vertx.size(); j++) {
                if (vertx.get(j).charAt(0) != '-') { 
                    System.out.print("v" + vertx.get(j) + " ");
                }

            }
            System.out.println();
        }
    }
}
