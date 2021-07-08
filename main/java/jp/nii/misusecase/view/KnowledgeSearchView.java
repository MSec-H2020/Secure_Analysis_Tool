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
import jp.nii.misusecase.KnowledgeRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class KnowledgeSearchView extends JPanel {

    private static class KnowledgeEntry extends JPanel {

        private Knowledge knowledge = null;
        private KnowledgeSelectedListener listener = null;

        public KnowledgeEntry( Knowledge knowledge) {
            this.knowledge = knowledge;
            this.initComponents();
        }

        public void setKnowledgeSelectedListener(KnowledgeSelectedListener listener) {
            this.listener = listener;
        }

        public Knowledge getKnowledge() {
            return knowledge;
        }

        private void initComponents() {
            setLayout(new BorderLayout());
            JLabel label = new JLabel(knowledge.getName());
            label.setForeground(Color.BLUE);
            add(label, BorderLayout.NORTH);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (listener != null) {
                        listener.selected(knowledge);
                    }
                }
            });

            JTextArea body = new JTextArea(knowledge.getDescription());
            body.setLineWrap(true);
            body.setEditable(false);
            add(body, BorderLayout.CENTER);
            body.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (listener != null) {
                        listener.selected(knowledge);
                    }
                }
            });

            setPreferredSize(new Dimension(400, 50));
        }
    }

    private KnowledgeSelectedListener listener = null;

    private Set<Knowledge> knowledges;

    private JPanel searchPanel = null;
    private JTextField keywordField = null;

    private JPanel resultPanel = null;

    public KnowledgeSearchView() {
        this.initComponents();
    }

    public void setKnowledgeSelectedListener(KnowledgeSelectedListener listener) {
        this.listener = listener;
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        searchPanel = new JPanel();
        keywordField = new JTextField(32);
        searchPanel.add(keywordField);
        JButton button = new JButton("Search");
        searchPanel.add(button);
        add(searchPanel, BorderLayout.NORTH);

        button.addActionListener(actionEvent -> {
            knowledges = KnowledgeRepository.getInstance().search(keywordField.getText());
            updateResultPanel();
        });

        resultPanel = new JPanel();
        BoxLayout layout = new BoxLayout(resultPanel, BoxLayout.Y_AXIS);
        resultPanel.setLayout(layout);

        JScrollPane scrollPane = new JScrollPane(resultPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateResultPanel() {
        if (knowledges == null) {
            return;
        }

        resultPanel.removeAll();
        for (Knowledge knowledge : knowledges) {
            KnowledgeEntry entry = new KnowledgeEntry(knowledge);
            entry.setKnowledgeSelectedListener(new KnowledgeSelectedListener() {
                @Override
                public void selected(Knowledge knowledge) {
                    showKnowledge(knowledge);
                }
            });
            resultPanel.add(entry);
            entry.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    KnowledgeEntry entry = (KnowledgeEntry)mouseEvent.getSource();
                    showKnowledge(entry.getKnowledge());
                }
            });
            entry.setVisible(true);
        }
        resultPanel.updateUI();
    }

    private void showKnowledge(Knowledge knowledge)
    {
        if (listener != null) {
            listener.selected(knowledge);
        }
    }
}
