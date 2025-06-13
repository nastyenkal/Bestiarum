package org.example.view;

import org.example.model.Monstr;
import org.example.model.Potion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Отображение информации о чудовище с возможностью редактирования
class EditorDialog extends JDialog {
    Monstr monstr;
    JFrame parent;
    JDialog current;

    JTextField fieldAdditionalInfo;
    JTextField fieldName;
    JTextField fieldFullInfo;
    JTextField fieldDescription;
    JTextField fieldHabitat;
    JTextField fieldFirstMention;
    JTextField fieldImmunities;
    JTextField fieldPhysicalCharacteristics;
    JTextField fieldIngredients;
    JTextField fieldPreparationTime;
    JTextField fieldStrength;

    EditorDialog(JFrame parent, Monstr monstr) {
        super(parent, "Редактирование: " + monstr.getName(), true);
        this.monstr = monstr;
        this.parent = parent;
        this.current = this;
        setBounds(100, 100, 800, 600);
        //setLocationRelativeTo(parent);
        initEditorDialog();
    }

    void initEditorDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Основные поля Monstr
        panel.add(new JLabel("Имя"));
        fieldName = new JTextField(monstr.getName(), 300);
        fieldName.setHorizontalAlignment(JTextField.LEFT);
        // Запрещаем редактировать тк имя является узлом в дереве
        fieldName.setEditable(false);
        panel.add(fieldName);

        panel.add(new JLabel("Полное описание:"));
        fieldFullInfo = new JTextField(monstr.getFull_info(), 300);
        fieldFullInfo.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldFullInfo);

        panel.add(new JLabel("Описание:"));
        fieldDescription = new JTextField(monstr.getDescription(), 300);
        fieldDescription.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldDescription);

        panel.add(new JLabel("Место обитания:"));
        fieldHabitat = new JTextField(monstr.getHabitat(), 300);
        fieldHabitat.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldHabitat);

        panel.add(new JLabel("Первое упоминание:"));
        fieldFirstMention = new JTextField(monstr.getFirst_mention(), 300);
        fieldFirstMention.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldFirstMention);

        panel.add(new JLabel("Иммунитеты:"));
        fieldImmunities = new JTextField(monstr.getImmunities(), 300);
        fieldImmunities.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldImmunities);

        panel.add(new JLabel("Физические характеристики:"));
        fieldPhysicalCharacteristics = new JTextField(monstr.getPhysical_characteristics(), 300);
        fieldPhysicalCharacteristics.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldPhysicalCharacteristics);

        panel.add(new JLabel("Дополнительная информация :"));
        fieldAdditionalInfo = new JTextField(monstr.getAdditional_info(), 300);
        fieldAdditionalInfo.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldAdditionalInfo);

        // Поля Potion
        Potion potion = monstr.getPotion() != null ? monstr.getPotion() : new Potion();

        panel.add(new JLabel("Ингредиенты зелья:"));
        fieldIngredients = new JTextField(potion.getIngredients(), 300);
        fieldIngredients.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldIngredients);

        panel.add(new JLabel("Время приготовления:"));
        fieldPreparationTime = new JTextField(potion.getPreparation_time(), 300);
        fieldPreparationTime.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldPreparationTime);

        panel.add(new JLabel("Сила зелья:"));
        fieldStrength = new JTextField(potion.getStrength(), 300);
        fieldStrength.setHorizontalAlignment(JTextField.LEFT);
        panel.add(fieldStrength);

        // Сохраняем изменения
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new saveButtonEvent());
        add(panel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, String label, String value, java.util.function.Consumer<String> setter) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(value != null ? value : "", 300);
        field.setHorizontalAlignment(JTextField.LEFT);
        field.addActionListener(e -> setter.accept(field.getText()));
        panel.add(field);
    }

    private class saveButtonEvent implements ActionListener {
        //final EditorDialog curr = EditorDialog.this;

        @Override
        public void actionPerformed(ActionEvent e) {
            //monstr.setAdditional_info(fieldName.getText());
            monstr.setFull_info(fieldFullInfo.getText());
            monstr.setDescription(fieldDescription.getText());
            monstr.setHabitat(fieldHabitat.getText());
            monstr.setFirst_mention(fieldFirstMention.getText());
            monstr.setImmunities(fieldImmunities.getText());
            monstr.setPhysical_characteristics(fieldPhysicalCharacteristics.getText());
            monstr.setAdditional_info(fieldAdditionalInfo.getText());
            monstr.getPotion().setIngredients(fieldIngredients.getText());
            monstr.getPotion().setPreparation_time(fieldPreparationTime.getText());
            monstr.getPotion().setStrength(fieldStrength.getText());

            current.dispose();
        }
    }

}
