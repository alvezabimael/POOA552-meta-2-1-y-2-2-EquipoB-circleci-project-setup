package cliente.gui;

import flujodetrabajo.Actividad;
import flujodetrabajo.Fase;
import flujodetrabajo.FlujoDeTrabajo;
import flujodetrabajo.Tarea;
import externalSources.TextPrompt;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TableroGUI extends JDialog {
    private FlujoDeTrabajo flujoDeTrabajo;
    private DefaultTableModel modelo;
    private JPanel contentPane;
    private JPanel panelPrincipal;
    private JPanel panelBotones;
    private JPanel panelSecundario;
    private JTable tableTablero;
    private JButton buttonOK;
    private JButton buttonCancel;
    //private JButton buttonTest;/**/
    private JButton buttonAgregarFase;
    private JButton buttonAgregarActividad;
    private JButton buttonAgregarTarea;
    private JButton buttonDelete;
    //private JButton buttonActualizarTablero;
    private JTextField textFieldFase;
    private JTextField textFieldActividad;
    private JTextField textFieldTarea;
    private JComboBox comboBoxFase;
    private JComboBox comboBoxActividad;
    private JComboBox comboBoxTarea;


    public TableroGUI() {
        flujoDeTrabajo = new FlujoDeTrabajo("Mi flujo de trabajo");

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //setResizable(false);

        //placeHolders for TextFields
        TextPrompt placeHFase = new TextPrompt("Fase...", textFieldFase);
        TextPrompt placeHAct = new TextPrompt("Actividad...", textFieldActividad);
        TextPrompt placeHTarea = new TextPrompt("Tarea...", textFieldTarea);
        placeHFase.changeAlpha(0.75f);
        placeHFase.changeStyle(Font.ITALIC);
        placeHAct.changeAlpha(0.75f);
        placeHAct.changeStyle(Font.ITALIC);
        placeHTarea.changeAlpha(0.75f);
        placeHTarea.changeStyle(Font.ITALIC);

        //promttext for combobox

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonAgregarFase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textFieldFase.getText().equals(""))
                {
                    Fase fase = new Fase(textFieldFase.getText(), flujoDeTrabajo);
                    flujoDeTrabajo.getFases().add(fase);
                    actualizarTablero();
                } else
                {
                    JOptionPane.showMessageDialog(null, "Favor de asignar nombre a la fase", "Fase sin nombre", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonAgregarActividad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textFieldActividad.getText().equals(""))
                {
                    Actividad actividad = new Actividad(textFieldActividad.getText(), flujoDeTrabajo);
                    flujoDeTrabajo.getActividades().add(actividad);
                    actualizarTablero();
                } else
                {
                    JOptionPane.showMessageDialog(null, "Favor de asignar nombre a la actividad", "Actividad sin nombre", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonAgregarTarea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textFieldTarea.getText().equals(""))
                {
                    Fase fase = flujoDeTrabajo.getFases().get(comboBoxFase.getSelectedIndex());
                    Actividad actividad = flujoDeTrabajo.getActividades().get(comboBoxActividad.getSelectedIndex());

                    Tarea tarea = new Tarea(textFieldTarea.getText(), actividad, fase, flujoDeTrabajo);
                    flujoDeTrabajo.getTareas().add(tarea);
                    actividad.getTareas().add(tarea);
                    fase.getTareas().add(tarea);
                    actualizarTablero();
                } else
                {
                    JOptionPane.showMessageDialog(null, "Favor de asignar nombre a la tarea", "Tarea sin nombre", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Codigo para borrar una tarea
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void actualizarTablero(){
        comboBoxFase.removeAllItems();
        for (int i = 0; i < flujoDeTrabajo.getFases().size(); i++) {
            comboBoxFase.addItem(flujoDeTrabajo.getFases().get(i).getNombre());
        }

        comboBoxActividad.removeAllItems();
        for (int j = 0; j < flujoDeTrabajo.getActividades().size(); j++) {
            comboBoxActividad.addItem(flujoDeTrabajo.getActividades().get(j).getNombre());
        }

        comboBoxTarea.removeAllItems();
        for (int j = 0; j < flujoDeTrabajo.getTareas().size(); j++) {
            comboBoxTarea.addItem(flujoDeTrabajo.getTareas().get(j).getNombre());
        }

        modelo = new DefaultTableModel();
        tableTablero.setModel(modelo);

        for (int k = 0; k < flujoDeTrabajo.getFases().size(); k++) {
            modelo.addColumn(flujoDeTrabajo.getFases().get(k).getNombre(), flujoDeTrabajo.getFases().get(k).getTareas());
        }
    }

    public static void main(String[] args) {
        TableroGUI dialog = new TableroGUI();
        dialog.pack();
        dialog.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        dialog.setTitle("Tablero Kanban - 0.1.1c");
        dialog.setResizable(false);
        try{
            dialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        dialog.setVisible(true);
        System.exit(0);
    }

}
