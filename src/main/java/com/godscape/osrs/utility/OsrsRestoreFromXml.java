// OsrsRestoreFromXml.java
package com.godscape.osrs.utility;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.utility.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OsrsRestoreFromXml {

    private final XmlMapper xmlMapper;

    public OsrsRestoreFromXml() {
        this.xmlMapper = new XmlMapper();
        Logger.info("OsrsRestoreFromXml: Initialized with XmlMapper.");
    }

    public List<OsrsProfileSchema> loadProfiles(String filePath) {
        try {
            return xmlMapper.readValue(new File(filePath), xmlMapper.getTypeFactory().constructCollectionType(List.class, OsrsProfileSchema.class));
        } catch (IOException e) {
            Logger.error("OsrsRestoreFromXml: Failed to load profiles from XML - {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
