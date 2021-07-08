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

package jp.nii.misusecase.view;

import jp.nii.misusecase.Knowledge;

import javax.swing.*;

public class KnowledgeView extends JSplitPane {

    private KnowledgeSearchView knowledgeSearchView = null;
    private KnowledgeDetailsView knowledgeDetailsView = null;

    private KnowledgeActionListener listener = null;

    public KnowledgeView() {
        super(JSplitPane.VERTICAL_SPLIT);

        knowledgeSearchView = new KnowledgeSearchView();
        knowledgeSearchView.setKnowledgeSelectedListener(new KnowledgeSelectedListener() {
            @Override
            public void selected(Knowledge knowledge) {
                knowledgeDetailsView.setKnowledge(knowledge);
            }
        });

        knowledgeDetailsView = new KnowledgeDetailsView();
        knowledgeDetailsView.setKnowledgeActionListener(new KnowledgeActionListener() {
            @Override
            public void focus(Knowledge knowledge) {
                if (listener != null) {
                    listener.focus(knowledge);
                }
            }
            @Override
            public void insert(Knowledge knowledge) {
                if (listener != null) {
                    listener.insert(knowledge);
                }
            }
        });

        setTopComponent(knowledgeSearchView);
        setBottomComponent(knowledgeDetailsView);
        setDividerLocation(200);
    }

    void setKnowledgeActionListener(KnowledgeActionListener listener) {
        this.listener = listener;
    }
}
