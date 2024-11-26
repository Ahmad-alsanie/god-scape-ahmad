package com.godscape.osrs.utility;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.godscape.system.utility.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

public class OsrsBackupToXml {

    private final XmlMapper xmlMapper;

    public OsrsBackupToXml() {
        this.xmlMapper = new XmlMapper();
        Logger.info("OsrsBackupToXml: Initialized XmlMapper for OSRS profiles.");
    }

    /**
     * Saves OSRS profiles to XML.
     *
     * @param profiles The collection of profiles to save.
     * @param targetDirectory The directory where the XML file will be saved.
     */
    public void saveProfiles(Collection<?> profiles, String targetDirectory) {
        String filePath = Paths.get(targetDirectory, "osrs_profiles.xml").toString();

        try {
            Logger.info("OsrsBackupToXml: Saving OSRS profiles to {}.", filePath);
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), profiles);
            Logger.info("OsrsBackupToXml: Successfully saved OSRS profiles to XML.");
        } catch (IOException e) {
            Logger.error("OsrsBackupToXml: Error saving OSRS profiles to XML - {}", e.getMessage());
        }
    }
}
