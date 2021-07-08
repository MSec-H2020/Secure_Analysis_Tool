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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Set;

public class KnowledgeDetailsView extends JPanel {

    private JEditorPane contentPane = null;

    private KnowledgeActionListener listener = null;

    private Knowledge knowledge = null;

    public KnowledgeDetailsView() {
        setLayout(new BorderLayout());

        contentPane = new JEditorPane();
        contentPane.setEditable(false);
        contentPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
                if (hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    JEditorPane pane = (JEditorPane) hyperlinkEvent.getSource();
                    if (hyperlinkEvent instanceof HTMLFrameHyperlinkEvent) {
                        HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)hyperlinkEvent;
                        HTMLDocument doc = (HTMLDocument)pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else {
                        try {
                            String description = hyperlinkEvent.getDescription();
                            if (description.startsWith("knowledge://")) {
                                String name = description.substring(12);
                                Set<Knowledge> candidates = KnowledgeRepository.getInstance().search(name, true);
                                if (candidates.size() == 1) {
                                    setKnowledge(candidates.toArray(new Knowledge[0])[0]);
                                }
                            } else {
                                if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                    URL url = hyperlinkEvent.getURL();
                                    Desktop.getDesktop().browse(url.toURI());
                                }
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(contentPane);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton focusButton = new JButton("View in Knowledge Browser");
        focusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.focus(knowledge);
            }
        });
        buttonPanel.add(focusButton);

        JButton insertButton = new JButton("Add the note to Editor");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.insert(knowledge);
            }
        });
        buttonPanel.add(insertButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    void setKnowledgeActionListener(KnowledgeActionListener listener) {
        this.listener = listener;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
        showKnowledge();
    }

    private void showKnowledge() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<style type=\"text/css\">");
        builder.append("div { margin: 10px; }");
        builder.append("</style>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append("<h3>" + knowledge.getName() + "</h3>");
        builder.append("<div>" + knowledge.getDescription().replaceAll("\\n", "<br/>") + "</div>");
        builder.append("<div>");
        builder.append("Source: <a href=\"" + knowledge.getSource() + "\">" + knowledge.getSource() + "</a>");
        builder.append("</div>");

        Set<String> related = knowledge.getRelated();
        if (related != null && related.size() > 0) {
            builder.append("<div>");
            builder.append("See also:");
            builder.append("<ul>");
            for (String name : related) {
                builder.append("<li><a href=\"knowledge://" + name + "\">" + name + "</a></li>");
            }
            builder.append("</ul>");
            builder.append("</div>");
        }
        builder.append("</body>");
        builder.append("</html>");

        contentPane.setContentType("text/html");
        contentPane.setText(builder.toString());
    }
}
