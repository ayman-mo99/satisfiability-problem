package sat_form;
import java.io.*;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sat_form {

    public static void main(String[] args) {
                       
            try {
            File file = new File("project.cnf");
            if (!file.exists()) {
                file.createNewFile();
            }

            PrintWriter pw = new PrintWriter(file);

            String lit  = JOptionPane.showInputDialog("enter the number of litrels");
            String clus = JOptionPane.showInputDialog("enter the number of clauses");
            String form = "p cnf " + lit + " "+clus+"\n";// write at the top of the file 
           
            int n = Integer.parseInt(clus); // TO get the number of clauses
            for (int i = 0; i < n; i++) {
                form = form + JOptionPane.showInputDialog("Enter the clause        without zero")+" 0" + "\n"; //write clause at single line
            }

            System.out.println(form);
            pw.print(form);// put the fomula on the file
            pw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Sat_form.class.getName()).log(Level.SEVERE, null, ex);
        }

        Process p = null;
        Runtime r = Runtime.getRuntime();
        try {
            p = r.exec("java -jar sat4j-maxsat.jar project.cnf");
            p.getOutputStream().close();
            InputStream processStdOutput = p.getInputStream();
            Reader rr = new InputStreamReader(processStdOutput);
            BufferedReader br = new BufferedReader(rr);
            String line;
            while ((line = br.readLine()) != null) {//go throwe the answer to find the answer
                if (line.charAt(0) == 'v') {                    
                    System.out.println(line);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Sat_form.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}