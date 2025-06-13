package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Bestiary;
import org.example.model.Monstr;
import java.io.File;
import java.util.List;

public class JsonHandler implements FileImportExportHandler {
    private FileImportExportHandler nextHandler;

    @Override
    public void setNextHandler(FileImportExportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monstr> importData(File file) throws Exception {
        if (!supports(file)) {
            if (nextHandler != null) {
                return nextHandler.importData(file);
            } else {
                throw new Exception("Unsupported format");
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        Bestiary bestiary = mapper.readValue(file, Bestiary.class);
        return bestiary.getBestiary();
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
        ObjectMapper mapper = new ObjectMapper();
        Bestiary bestiary = new Bestiary();
        bestiary.setBestiary(monsters);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, bestiary);
    }

    @Override
    public boolean supports(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }
}