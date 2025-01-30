package org.opendatamesh.odm.cli.commands.local.subcommands;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ImportTool<T> {
    public String from();
    public String to();

    T importElement(File descriptorFile, T target, Map<String, String> params) throws IOException;
    
}
