package mephi.b23902.handlers;

import mephi.b23902.model.Monstr;
import mephi.b23902.model.Potion;

import javax.xml.stream.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
        
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            
            XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);
            Monstr currentMonster = null;
            Potion currentPotion = null;
            StringBuilder currentText = new StringBuilder();
            String currentElement = "";

            while (xmlReader.hasNext()) {
                int event = xmlReader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        currentElement = xmlReader.getLocalName();
                        if ("monstr".equals(currentElement)) {
                            currentMonster = new Monstr();
                        } else if ("potion".equals(currentElement)) {
                            currentPotion = new Potion();
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        currentText.append(xmlReader.getText().trim());
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        String endTag = xmlReader.getLocalName();
                        String text = currentText.toString();
                        currentText.setLength(0);

                        if (currentMonster != null && !"monstr".equals(endTag)) {
                            if (currentPotion != null && !"potion".equals(endTag)) {
                                populatePotionField(currentPotion, endTag, text);
                            } else {
                                populateMonsterField(currentMonster, endTag, text);
                            }
                        }

                        if ("potion".equals(endTag)) {
                            currentMonster.setPotion(currentPotion);
                            currentPotion = null;
                        } else if ("monstr".equals(endTag)) {
                            monsters.add(currentMonster);
                            currentMonster = null;
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            throw new Exception("XML parsing error: " + e.getMessage());
        }
        return monsters;
    }

    private void populateMonsterField(Monstr monster, String tag, String value) {
        if (value == null || value.isEmpty()) return;
        
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
        if (value == null || value.isEmpty()) return;
        
        switch (tag) {
            case "ingredients": potion.setIngredients(value); break;
            case "preparation_time": potion.setPreparation_time(value); break;
            case "strength": potion.setStrength(value); break;
        }
    }

    @Override
    public void exportData(List<Monstr> monsters, File file) throws Exception {
        if (!supports(file)) {
            if (nextHandler != null) {
                nextHandler.exportData(monsters, file);
                return;
            }
            throw new Exception("Unsupported format");
        }

        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try (FileWriter writer = new FileWriter(file)) {
            XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(writer);
            
            xmlWriter.writeStartDocument("UTF-8", "1.0");
            xmlWriter.writeStartElement("bestiary");

            for (Monstr monstr : monsters) {
                xmlWriter.writeStartElement("monstr");
                writeElement(xmlWriter, "name", monstr.getName());
                writeElement(xmlWriter, "description", monstr.getDescription());
                
                if (monstr.getPotion() != null) {
                    xmlWriter.writeStartElement("potion");
                    writeElement(xmlWriter, "ingredients", monstr.getPotion().getIngredients());
                    xmlWriter.writeEndElement();
                }
                
                xmlWriter.writeEndElement();
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.close();
        }
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
}