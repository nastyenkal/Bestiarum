package mephi.b23902.handlers;

// YamlHandler.java

import mephi.b23902.model.Monstr;
import mephi.b23902.model.Bestiary;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class YamlHandler implements FileImportExportHandler {
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

        // Настройка десериализации для Bestiary
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(Bestiary.class, loaderOptions);
        Yaml yaml = new Yaml(new Constructor(Bestiary.class, loaderOptions));

        try (FileInputStream input = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                Bestiary bestiary = yaml.load(reader);
                return bestiary.getBestiary();
        }
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

        Bestiary bestiary = new Bestiary();
        bestiary.setBestiary(monsters);

        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(bestiary, writer);
        }
    }

    @Override
    public boolean supports(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".yaml") || name.endsWith(".yml");
    }
}