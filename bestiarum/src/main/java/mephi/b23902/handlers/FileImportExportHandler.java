package mephi.b23902.handlers;

import mephi.b23902.model.Monstr;

import java.io.File;
import java.util.List;

public interface FileImportExportHandler {
    boolean supports(File file);

    List<Monstr> importData(File file) throws Exception;

    void exportData(List<Monstr> monsters, File file) throws Exception;

    void setNextHandler(FileImportExportHandler nextHandler);

}