package org.example.view;

import org.example.model.Monstr;
import org.example.model.Potion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EditorDialog extends JDialog {
    Monstr monstr;
    JFrame parent;
    JDialog current;

    // Заменяем JTextField на JTextArea для полей с длинным текстом
    JTextArea fieldAdditionalInfo;
    JTextField fieldName;
    JTextArea fieldFullInfo;
    JTextArea fieldDescription;
    JTextArea fieldHabitat;
    JTextArea fieldFirstMention;
    JTextArea fieldImmunities;
    JTextArea fieldPhysicalCharacteristics;
    JTextArea fieldIngredients;
    JTextArea fieldPreparationTime;
    JTextArea fieldStrength;

    EditorDialog(JFrame parent, Monstr monstr) {
        super(parent, "Редактирование: " + monstr.getName(), true);
        this.monstr = monstr;
        this.parent = parent;
        this.current = this;
        setBounds(100, 100, 900, 700); // Увеличиваем размер окна
        initEditorDialog();
    }

    void initEditorDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Основные поля Monstr
        panel.add(new JLabel("Имя"));
        fieldName = new JTextField(monstr.getName(), 20);
        fieldName.setEditable(false);
        panel.add(fieldName);

        // Полное описание (многострочное)
        panel.add(new JLabel("Полное описание:"));
        fieldFullInfo = createTextArea(monstr.getFull_info());
        panel.add(new JScrollPane(fieldFullInfo));

        // Описание (многострочное)
        panel.add(new JLabel("Описание:"));
        fieldDescription = createTextArea(monstr.getDescription());
        panel.add(new JScrollPane(fieldDescription));

        // Место обитания (многострочное)
        panel.add(new JLabel("Место обитания:"));
        fieldHabitat = createTextArea(monstr.getHabitat());
        panel.add(new JScrollPane(fieldHabitat));

        // Первое упоминание (многострочное)
        panel.add(new JLabel("Первое упоминание:"));
        fieldFirstMention = createTextArea(monstr.getFirst_mention());
        panel.add(new JScrollPane(fieldFirstMention));

        // Иммунитеты (многострочное)
        panel.add(new JLabel("Иммунитеты:"));
        fieldImmunities = createTextArea(monstr.getImmunities());
        panel.add(new JScrollPane(fieldImmunities));

        // Физические характеристики (многострочное)
        panel.add(new JLabel("Физические характеристики:"));
        fieldPhysicalCharacteristics = createTextArea(monstr.getPhysical_characteristics());
        panel.add(new JScrollPane(fieldPhysicalCharacteristics));

        // Дополнительная информация (многострочное)
        panel.add(new JLabel("Дополнительная информация:"));
        fieldAdditionalInfo = createTextArea(monstr.getAdditional_info());
        panel.add(new JScrollPane(fieldAdditionalInfo));

        // Поля Potion
        Potion potion = monstr.getPotion() != null ? monstr.getPotion() : new Potion();

        // Ингредиенты зелья (многострочное)
        panel.add(new JLabel("Ингредиенты зелья:"));
        fieldIngredients = createTextArea(potion.getIngredients());
        panel.add(new JScrollPane(fieldIngredients));

        // Время приготовления (многострочное)
        panel.add(new JLabel("Время приготовления:"));
        fieldPreparationTime = createTextArea(potion.getPreparation_time());
        panel.add(new JScrollPane(fieldPreparationTime));

        // Сила зелья (многострочное)
        panel.add(new JLabel("Сила зелья:"));
        fieldStrength = createTextArea(potion.getStrength());
        panel.add(new JScrollPane(fieldStrength));

        // Сохраняем изменения
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new saveButtonEvent());
        
        // Используем JScrollPane для всей формы
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    // Вспомогательный метод для создания JTextArea
    private JTextArea createTextArea(String text) {
        JTextArea area = new JTextArea(text, 3, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEtchedBorder());
        return area;
    }

    private class saveButtonEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            monstr.setFull_info(fieldFullInfo.getText());
            monstr.setDescription(fieldDescription.getText());
            monstr.setHabitat(fieldHabitat.getText());
            monstr.setFirst_mention(fieldFirstMention.getText());
            monstr.setImmunities(fieldImmunities.getText());
            monstr.setPhysical_characteristics(fieldPhysicalCharacteristics.getText());
            monstr.setAdditional_info(fieldAdditionalInfo.getText());
            
            Potion potion = monstr.getPotion();
            if (potion == null) {
                potion = new Potion();
                monstr.setPotion(potion);
            }
            potion.setIngredients(fieldIngredients.getText());
            potion.setPreparation_time(fieldPreparationTime.getText());
            potion.setStrength(fieldStrength.getText());

            current.dispose();
        }
    }
}