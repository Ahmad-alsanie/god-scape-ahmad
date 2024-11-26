package com.godscape.rs3.utility;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.godscape.system.utility.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

public class Rs3BackupToXml {

    private final XmlMapper xmlMapper;

    public Rs3BackupToXml() {
        this.xmlMapper = new XmlMapper();
        Logger.info("Rs3BackupToXml: Initialized XmlMapper for RS3 profiles.");
    }

    /**
     * Saves RS3 profiles to XML.
     *
     * @param profiles The collection of profiles to save.
     * @param targetDirectory The directory where the XML file will be saved.
     */
    public void saveProfiles(Collection<?> profiles, String targetDirectory) {
        String filePath = Paths.get(targetDirectory, "rs3_profiles.xml").toString();

        try {
            Logger.info("Rs3BackupToXml: Saving RS3 profiles to {}.", filePath);
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), profiles);
            Logger.info("Rs3BackupToXml: Successfully saved RS3 profiles to XML.");
        } catch (IOException e) {
            Logger.error("Rs3BackupToXml: Error saving RS3 profiles to XML - {}", e.getMessage());
        }
    }
}
