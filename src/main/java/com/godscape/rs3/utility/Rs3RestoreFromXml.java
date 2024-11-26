// Rs3RestoreFromXml.java
package com.godscape.rs3.utility;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.utility.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Rs3RestoreFromXml {

    private final XmlMapper xmlMapper;

    public Rs3RestoreFromXml() {
        this.xmlMapper = new XmlMapper();
        Logger.info("Rs3RestoreFromXml: Initialized with XmlMapper.");
    }

    public List<Rs3ProfileSchema> loadProfiles(String filePath) {
        try {
            return xmlMapper.readValue(new File(filePath), xmlMapper.getTypeFactory().constructCollectionType(List.class, Rs3ProfileSchema.class));
        } catch (IOException e) {
            Logger.error("Rs3RestoreFromXml: Failed to load profiles from XML - {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
