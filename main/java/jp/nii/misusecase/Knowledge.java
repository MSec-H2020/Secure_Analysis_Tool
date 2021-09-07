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

import java.util.Set;
import java.util.UUID;

public class Knowledge {

    private String id;
    private String name;
    private String type;
    private String category;
    private String description;
    private String source;
    private Set<String> inherit;
    private Set<String> related;

    public Knowledge() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<String> getInherit() {
        return inherit;
    }

    public void setInherit(Set<String> inherit) {
        this.inherit = inherit;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getRelated() {
        return related;
    }

    public void setRelated(Set<String> related) {
        this.related = related;
    }
}
