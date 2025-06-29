package mephi.b23902.handlers;

import mephi.b23902.model.Monstr;
import mephi.b23902.model.Potion;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class XmlHandler implements FileImportExportHandler {
    private FileImportExportHandler nextHandler;

    @Override
    public void setNextHandler(FileImportExportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monstr> importData(File file) throws Exception {
        if (!supports(file)) {
            if (nextHandler != null) return nextHandler.importData(file);
            throw new Exception("Unsupported format");
        }

        List<Monstr> monsters = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(file));

        Monstr currentMonster = null;
        Potion currentPotion = null;
        String currentElement = "";
        boolean inPotion = false;

        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if ("creature".equals(tagName)) {
                        currentMonster = new Monstr();
                    }
                    else if ("potion".equals(tagName)) {
                        currentPotion = new Potion();
                        inPotion = true;
                    }
                    currentElement = tagName;
                    break;

                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.isEmpty()) {
                        if (currentMonster != null && !inPotion) {
                            populateMonsterField(currentMonster, currentElement, text);
                        }
                        else if (currentPotion != null && inPotion) {
                            populatePotionField(currentPotion, currentElement, text);
                        }
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    String endTag = reader.getLocalName();
                    if ("creature".equals(endTag)) {
                        currentMonster.setPotion(currentPotion);
                        monsters.add(currentMonster);
                        currentMonster = null;
                        currentPotion = null;
                        inPotion = false;
                    }
                    else if ("potion".equals(endTag)) {
                        inPotion = false;
                    }
                    break;
            }
        }
        reader.close();
        return monsters;
    }

    @Override
    public void exportData(List<Monstr> monsters, File file) throws Exception {
        if (!supports(file)) {
            if (nextHandler != null) {
                nextHandler.exportData(monsters, file);
                return;
            } else {
                throw new Exception("Unsupported format");
            }
        }
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter(file));

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("bestiary");

        for (Monstr monstr : monsters) {
            writer.writeStartElement("monstr");
            writeElement(writer, "name", monstr.getName());
            writeElement(writer, "full_info", monstr.getFull_info());
            writeElement(writer, "description", monstr.getDescription());
            writeElement(writer, "habitat", monstr.getHabitat());
            writeElement(writer, "first_mention", monstr.getFirst_mention());
            writeElement(writer, "immunities", monstr.getImmunities());
            writeElement(writer, "physical_characteristics", monstr.getPhysical_characteristics());
            writeElement(writer, "additional_info", monstr.getAdditional_info());

            Potion potion = monstr.getPotion();
            if (potion != null) {
                writer.writeStartElement("potion");
                writeElement(writer, "ingredients", potion.getIngredients());
                writeElement(writer, "preparation_time", potion.getPreparation_time());
                writeElement(writer, "strength", potion.getStrength());
                writer.writeEndElement();
            }

            writer.writeEndElement();
        }

        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();
    }

    private void writeElement(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        if (value != null) {
            writer.writeStartElement(name);
            writer.writeCharacters(value);
            writer.writeEndElement();
        }
    }

    @Override
    public boolean supports(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }

    private void populateMonsterField(Monstr monster, String tag, String value) {
        switch (tag) {
            case "name": monster.setName(value); break;
            case "full_info": monster.setFull_info(value); break;
            case "description": monster.setDescription(value); break;
            case "habitat": monster.setHabitat(value); break;
            case "first_mention": monster.setFirst_mention(value); break;
            case "immunities": monster.setImmunities(value); break;
            case "physical_characteristics": monster.setPhysical_characteristics(value); break;
            case "additional_info": monster.setAdditional_info(value); break;
        }
    }

    private void populatePotionField(Potion potion, String tag, String value) {
        switch (tag) {
            case "ingredients": potion.setIngredients(value); break;
            case "preparation_time": potion.setPreparation_time(value); break;
            case "strength": potion.setStrength(value); break;
        }
    }
}