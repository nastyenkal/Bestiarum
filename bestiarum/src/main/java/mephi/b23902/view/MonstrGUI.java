package mephi.b23902.view;

import mephi.b23902.handlers.FileImportExportHandler;
import mephi.b23902.handlers.JsonHandler;
import mephi.b23902.handlers.XmlHandler;
import mephi.b23902.handlers.YamlHandler;
import mephi.b23902.model.Monstr;

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

    private void setupHandlers() {
        YamlHandler yamlHandler = new YamlHandler();
        XmlHandler xmlHandler = new XmlHandler();
        JsonHandler jsonHandler = new JsonHandler();

        yamlHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(jsonHandler);
        fileHandlerChain = yamlHandler;
    }

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

        // Дерево с группировкой по форматам
        rootNode = new DefaultMutableTreeNode("Чудовища");
        yamlNode = new DefaultMutableTreeNode("YAML");
        xmlNode = new DefaultMutableTreeNode("XML");
        jsonNode = new DefaultMutableTreeNode("JSON");
        
        rootNode.add(yamlNode);
        rootNode.add(xmlNode);
        rootNode.add(jsonNode);

        tree = new JTree(rootNode);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(e -> showMonstrDetails());

        add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    private void showMonstrDetails() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null || node == rootNode || 
            node == yamlNode || node == xmlNode || node == jsonNode) {
            return;
        }

        Object userObject = node.getUserObject();
        if (userObject instanceof Monstr) {
            new EditorDialog(this, (Monstr) userObject).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Ошибка: выбран неверный элемент", 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importData() {
        JFileChooser fileChooser = new JFileChooser(new File("./data/"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                monsters = fileHandlerChain.importData(file);

                if (monsters.isEmpty()) {
                    throw new Exception("Файл не содержит данных");
                }

                // Определяем тип файла и очищаем соответствующую ветку
                String extension = file.getName().split("\\.")[1].toLowerCase();
                DefaultMutableTreeNode targetNode = switch (extension) {
                    case "yaml", "yml" -> yamlNode;
                    case "xml" -> xmlNode;
                    case "json" -> jsonNode;
                    default -> throw new Exception("Неизвестный формат файла");
                };

                targetNode.removeAllChildren();
                for (Monstr monstr : monsters) {
                    targetNode.add(new DefaultMutableTreeNode(monstr));
                }

                updateTree();
                JOptionPane.showMessageDialog(this, 
                    "Успешно загружено " + monsters.size() + " чудовищ в раздел " + extension.toUpperCase());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Ошибка загрузки: " + ex.getMessage(), 
                    "Ошибка", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportData() {
        if (monsters == null || monsters.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Нет данных для экспорта", 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser(new File("./data/"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                fileHandlerChain.exportData(monsters, file);
                JOptionPane.showMessageDialog(this, "Данные успешно экспортированы");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Ошибка экспорта: " + ex.getMessage(), 
                    "Ошибка", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTree() {
        ((DefaultTreeModel) tree.getModel()).reload();
        // Раскрываем все узлы после обновления
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}