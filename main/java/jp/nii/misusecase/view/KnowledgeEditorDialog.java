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

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.LinkedList;

public class KnowledgeEditorDialog extends JDialog {

    public static interface DialogActionListener {
        void onOk();
        void onCancel();
    }

    private List<DialogActionListener> listeners = new LinkedList<DialogActionListener>();

    private String knowledges = "";

    private JTextArea textArea = null;

    public KnowledgeEditorDialog(JFrame parent) {
        super(parent, "Misusecase Knowledge Editor", true);

        textArea = new JTextArea();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setKnowledges(textArea.getText());
                for (DialogActionListener listener : listeners) {
                    listener.onOk();
                }
                setVisible(false);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (DialogActionListener listener : listeners) {
                    listener.onCancel();
                }
                setVisible(false);
            }
        });
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);

        pack();
        setSize(640, 720);
    }

    public String getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(String knowledges) {
        this.knowledges = knowledges;
        textArea.setText(knowledges);
    }

    public void addDialogActionListener(DialogActionListener listener) {
        this.listeners.add(listener);
    }
}
