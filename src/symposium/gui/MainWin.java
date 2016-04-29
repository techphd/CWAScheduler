package symposium.gui;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import symposium.DummyScheduler;
import symposium.Main;
import symposium.Parser;
import symposium.Report;
import symposium.model.ScheduleData;


public class MainWin extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainWin() {
        this.setLocationRelativeTo (null);
        initComponents();
        //
        resultWindow = new ResultWin(this, true);
        resultWindow.setLocationRelativeTo(this);
        //optimizeWindow = new OptimizeWin(this, true);
       // optimizeWindow.setLocationRelativeTo(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inputChooser = new javax.swing.JFileChooser();
        inputLabel = new javax.swing.JLabel();
        inputPathTxt = new javax.swing.JTextField();
        chooseInputBtn = new javax.swing.JButton();
        scheduleBtn = new javax.swing.JButton();
        helpBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        inputLabel.setText("Input :");

        inputPathTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputPathTxtActionPerformed(evt);
            }
        });

        chooseInputBtn.setText("...");
        chooseInputBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseInputBtnActionPerformed(evt);
            }
        });

        scheduleBtn.setText("Schedule");
        scheduleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scheduleBtnActionPerformed(evt);
            }
        });

        helpBtn.setText("About");
        helpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inputLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputPathTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chooseInputBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scheduleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                        .addComponent(helpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputPathTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chooseInputBtn)))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(helpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scheduleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseInputBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseInputBtnActionPerformed
        if( this.inputChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.inputPathTxt.setText(this.inputChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_chooseInputBtnActionPerformed

    private void inputPathTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputPathTxtActionPerformed
        scheduleBtnActionPerformed(evt);
    }//GEN-LAST:event_inputPathTxtActionPerformed

    private void scheduleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scheduleBtnActionPerformed
        File f = new File(this.inputPathTxt.getText());
        if(f.exists() && !f.isDirectory()) { 
            ScheduleData.deleteScheduleData();
        
            int[] diffValues = new int[5];
            diffValues[0] = 10;
            diffValues[1] = 100000;
            diffValues[2] = 100000;
            diffValues[3] = 10000000;
            diffValues[4] = 100; // panelist

            // Reading parsing json files
            Parser.parse(f.getAbsolutePath());
            // Schedule data is initiated

            DummyScheduler bs = new DummyScheduler(diffValues);
            bs.makeSchedule();
            //long elapsedTime = System.nanoTime() - initTime;

            // Print report
            //System.out.println(Report.INSTANCE.toString());
            //System.out.println(__debugStats());
            //System.out.println("\n\n\nTIME= " + (elapsedTime/(double)1000000000) + " ± 0.05 s");
            this.resultWindow.showSchedule();
        } else {
            JOptionPane.showMessageDialog(this, "File " + f.toString() + " does not exist", "Problem!", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_scheduleBtnActionPerformed

    private void helpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpBtnActionPerformed
        JOptionPane.showMessageDialog(this, "Input should have the location of the input json file.\n" +
                "\n" +
                "The three dots button will give you an interactive window to choose the input file. \n" +
                "\n" +
                "Schedule button when pressed will schedule.", "About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_helpBtnActionPerformed

    public static void saveFile(Component parent, File f, String str) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(f);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Can't save file "+f.toString(), "Error", JOptionPane.ERROR_MESSAGE);  
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        if(args.length > 0) {
            File inFile = new File(args[0]);
            if(!inFile.exists()) {
                System.err.println("Error: cannot find "+ args[0]);
            }
            //
            
            int[] diffValues = new int[5];
            diffValues[0] = 10;
            diffValues[1] = 100000;
            diffValues[2] = 100000;
            diffValues[3] = 10000000;
            diffValues[4] = 100; // panelist

            // Reading parsing json files
            Parser.parse(inFile.getAbsolutePath());
            // Schedule data is initiated

            DummyScheduler bs = new DummyScheduler(diffValues);
            bs.makeSchedule();
            //long elapsedTime = System.nanoTime() - initTime;

            //
            if(args.length > 1) {
                MainWin.saveFile(null, new File(args[1]), Report.INSTANCE.toJson().toString());
            } else {
                System.out.println(Report.INSTANCE.toJson());
            }
        } else {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new MainWin().setVisible(true);
                }
            });
        }
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseInputBtn;
    private javax.swing.JButton helpBtn;
    private javax.swing.JFileChooser inputChooser;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JTextField inputPathTxt;
    private javax.swing.JButton scheduleBtn;
    // End of variables declaration//GEN-END:variables
    private final ResultWin resultWindow;
    //private final OptimizeWin optimizeWindow;
}
