package org.example.view;

// MonstrGUI.java


import org.example.handlers.FileImportExportHandler;
import org.example.handlers.JsonHandler;
import org.example.handlers.XmlHandler;
import org.example.handlers.YamlHandler;
import org.example.model.Monstr;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MonstrGUI extends JFrame {

    private FileImportExportHandler fileHandlerChain;

    private List<Monstr> monsters;
    private DefaultMutableTreeNode rootNode;
    private DefaultMutableTreeNode yamlNode;
    private DefaultMutableTreeNode xmlNode;
    private DefaultMutableTreeNode jsonNode;
    private JTree tree;

    public MonstrGUI() {
        setupHandlers();
        initMonstGUI();
    }

    // Контроллер
    // Настройка цепочки обработчиков
    private void setupHandlers() {
        YamlHandler yamlHandler = new YamlHandler();
        XmlHandler xmlHandler = new XmlHandler();
        JsonHandler jsonHandler = new JsonHandler();

        yamlHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(jsonHandler);
        fileHandlerChain = yamlHandler;
    }

    // Инициализация главного экрана
    private void initMonstGUI() {
        setTitle("Bestiarum");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Меню
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem importItem = new JMenuItem("Импорт");
        JMenuItem exportItem = new JMenuItem("Экспорт");
        JMenuItem exitItem = new JMenuItem("Выход");

        importItem.addActionListener(e -> importData());
        exportItem.addActionListener(e -> exportData());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Дерево
        rootNode = new DefaultMutableTreeNode("Чудовища");
        yamlNode = new DefaultMutableTreeNode("YAML");
        xmlNode = new DefaultMutableTreeNode("XML");
        jsonNode = new DefaultMutableTreeNode("JSON");
        rootNode.add(yamlNode);
        rootNode.add(xmlNode);
        rootNode.add(jsonNode);

        tree = new JTree(rootNode);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //tree.addTreeSelectionListener(e -> showDetails());
        tree.addTreeSelectionListener(e -> showDetailsNew());


        add(new JScrollPane(tree), BorderLayout.CENTER);
        setVisible(true);
    }

    // Контроллер
    // Функция меню для импорта чудовищь
    private void importData() {
        JFileChooser fileChooser = new JFileChooser( new File("./data/") );
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                monsters = fileHandlerChain.importData(file);

                System.out.println("Monstr imported: " + monsters);

                DefaultMutableTreeNode targetNode =
                        switch (file.getName().split("\\.")[1].toLowerCase()) {
                            case "yaml" -> yamlNode;
                            case "xml" -> xmlNode;
                            case "json" -> jsonNode;
                            default -> rootNode;
                        };
                yamlNode.removeAllChildren();
                xmlNode.removeAllChildren();
                jsonNode.removeAllChildren();

                monsters.forEach(m -> targetNode.add(new DefaultMutableTreeNode(m)));

                updateTree();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                System.out.println(ex.getMessage());
            }
        }
    }

    // Контроллер
    // Функция меню для экспорта чудовищь
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser( new File("./data/") );
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                fileHandlerChain.exportData(monsters, file);
                JOptionPane.showMessageDialog(this, "Exported successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                System.out.println(ex.getMessage());
            }
        }
    }

//    // Простое отображение деталей о чудовище
//    private void showDetails() {
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//        if (node == null || node.isRoot()) return;
//
//        Monstr selected = (Monstr) node.getUserObject();
//        JDialog dialog = new JDialog(this, "Details: " + selected.getName(), true);
//        JTextArea textArea = new JTextArea(selected.toString());
//        textArea.setEditable(false);
//        dialog.add(new JScrollPane(textArea));
//        dialog.setBounds(600,200,600, 600);
//        dialog.setVisible(true);
//    }

    private void showDetailsNew() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null || node == rootNode) return;

        Monstr selected = (Monstr) node.getUserObject();
        new EditorDialog(this, selected).setVisible(true);
        updateTree();
    }

    private void updateTree() {
        ((DefaultTreeModel) tree.getModel()).reload();
    }

}