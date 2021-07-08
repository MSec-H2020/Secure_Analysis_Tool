// Copyright Inter-University Research Institute Corporation Research Organization of Information and Systems(ROIS). All rights reserved. 

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package jp.nii.misusecase;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.system.SystemPropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

public class KnowledgeRepository {

    private final String KNOWLEDGE_PROPERTY_KEY = "misusecase.knowledges";

    private static class SingletonHolder {
        private static final KnowledgeRepository INSTANCE = new KnowledgeRepository();
    }

    public static KnowledgeRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Set<Knowledge> knowledges = null;

    private KnowledgeRepository() {
        knowledges = loadKnowledges();
    }

    private Set<Knowledge> loadKnowledges()
    {
        Set<Knowledge> knowledges = null;
        try {
            String json = getKnowledgesAsString();
            final ObjectMapper mapper = new ObjectMapper();
            try {
                knowledges = mapper.readValue(json, new TypeReference<Set<Knowledge>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return knowledges;
    }

    public String getKnowledgesAsString() throws ClassNotFoundException {
        AstahAPI api = AstahAPI.getAstahAPI();
        SystemPropertyAccessor systemPropertyAccessor = api.getSystemPropertyAccessor();
        Properties properties = systemPropertyAccessor.getSystemProperties();
        if (properties.containsKey(KNOWLEDGE_PROPERTY_KEY)) {
            return (String)properties.get(KNOWLEDGE_PROPERTY_KEY);
        } else {
            return getDefaultKnowledges();
        }
    }

    public String getDefaultKnowledges() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("knowledges/knowledges.json");
        if (inputStream != null) {
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStream, writer, "UTF-8");
                return writer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setKnowledges(String knowledges) throws ClassNotFoundException {
        AstahAPI api = AstahAPI.getAstahAPI();
        SystemPropertyAccessor systemPropertyAccessor = api.getSystemPropertyAccessor();
        Properties properties = systemPropertyAccessor.getSystemProperties();
        properties.put(KNOWLEDGE_PROPERTY_KEY, knowledges);
    }

    public Set<Knowledge> getKnowledges() {
        return knowledges;
    }

    public Set<String> getCategories() {
        Set<String> categories = new HashSet<String>();
        for (Knowledge knowledge: knowledges) {
            categories.add(knowledge.getCategory());
        }
        return categories;
    }

    public Set<Knowledge> search(String keyword) {
        return search(keyword, false);
    }

    public Set<Knowledge> search(String keyword, boolean exactMatch) {
        Set<Knowledge> result = new HashSet<Knowledge>();
        for (Knowledge knowledge: knowledges) {
            if (exactMatch) {
                if (knowledge.getName().equals(keyword)) {
                    result.add(knowledge);
                }
            } else {
                if (knowledge.getName().contains(keyword)) {
                    result.add(knowledge);
                }
            }
        }

        return result;
    }

}
