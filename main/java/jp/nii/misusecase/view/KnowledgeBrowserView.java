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
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swing_viewer.DefaultView;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.*;

public class KnowledgeBrowserView extends JPanel {

    private static class CssGenerator {

        private static class CssEntry {
            private String selector;
            private List<String> styles = new LinkedList<String>();

            public CssEntry(String selector, String[] styles) {
                this.selector = selector;
                for (String style : styles) {
                    this.styles.add(style);
                }
            }

            public String getSelector() {
                return selector;
            }

            public List<String> getStyles() {
                return styles;
            }
        }


        private List<CssEntry> stylesheet = new LinkedList<CssEntry>();

        public void add(String key, String[] styles) {
            stylesheet.add(new CssEntry(key, styles));
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (CssEntry entry : stylesheet) {
                builder.append(entry.getSelector() + " {\n");
                for (String style : entry.getStyles()) {
                    builder.append("\t" + style + ";\n");
                }
                builder.append("}\n");
            }
            return builder.toString();
        }
    }

    private String makeStylesheet(Set<Knowledge> knowledges) {
        CssGenerator cssGenerator = new CssGenerator();
        cssGenerator.add("node", new String[] {"shape: circle"});
        cssGenerator.add("node.class", new String[] {"size: 100px, 40px"});
        cssGenerator.add("node.instance", new String[] {"size: 30px, 30px"});

        String[] colors = new String[] {
              "#CC99C9", "#9EC1CF", "#9EE09E", "#FDFD97", "#FEB144", "#FF6663",
        };
        Set<String> categories = KnowledgeRepository.getInstance().getCategories();
        int i = 0;
        for (String category: categories) {
            cssGenerator.add("node." + category, new String[] {"fill-color: " + colors[i % 6]});
            ++i;
        }

        cssGenerator.add("node.focused", new String[] {
                "stroke-mode: plain",
                "stroke-color: red",
                "stroke-width: 3px"
        });

        return cssGenerator.toString();
    }

    private Graph graph = null;
    private Viewer viewer = null;
    private DefaultView view = null;

    private Knowledge focusedKnowledge = null;

    public KnowledgeBrowserView() {
        Set<Knowledge> knowledges = KnowledgeRepository.getInstance().getKnowledges();
        String styleSheet = makeStylesheet(knowledges);

        graph = new MultiGraph("embedded");
        graph.setAttribute("ui.stylesheet", styleSheet);

        for (Knowledge knowledge : knowledges) {
            createNode(graph, knowledge);
        }
        for (Knowledge knowledge : knowledges) {
            createEdge(graph, knowledge);
        }

        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();

        view = (DefaultView)viewer.addDefaultView(false);
        view.getCamera().setViewPercent(0.5);

        setLayout(new BorderLayout());
        add(view, BorderLayout.CENTER);
    }

    public void setFocusKnowledge(Knowledge knowledge)
    {
        unfocusKnowledge(focusedKnowledge);

        this.focusedKnowledge = knowledge;
        focusKnowledge(focusedKnowledge);
    }

    private void focusKnowledge(Knowledge knowledge)
    {
        if (knowledge == null) {
            return;
        }

        Node node = graph.getNode(knowledge.getName());
        if (node == null) {
            return;
        }

        String klass = (String)node.getAttribute("ui.class");
        klass += ", focused";
        node.setAttribute("ui.class", klass);
    }

    private void unfocusKnowledge(Knowledge knowledge)
    {
        if (knowledge == null) {
            return;
        }

        Node node = graph.getNode(knowledge.getName());
        String klass = (String)node.getAttribute("ui.class");
        String[] types = klass.split(",");

        klass = "";
        for (String type : types) {
            type = type.trim();
            if (type.equals("focused")) {
                continue;
            }
            if (klass.length() != 0) {
                klass += ", ";
            }
            klass += type;
        }

        node.setAttribute("ui.class", klass);
    }

    private void createNode(Graph graph, Knowledge knowledge)
    {
        createNode(graph, knowledge.getName(), knowledge.getName(), knowledge.getType() + ", " + knowledge.getCategory());
    }

    private void createNode(Graph graph, String id, String label, String type) {
        Node node = graph.addNode(id);
        node.setAttribute("ui.label", label);
        node.setAttribute("ui.class", type);
    }

    private void createEdge(Graph graph, Knowledge knowledge)
    {
        Set<String> related = knowledge.getInherit();
        if (related == null) {
            return;
        }

        for (String parent : related) {
            createEdge(graph, parent, knowledge.getName(), "");
        }
    }

    private void createEdge(Graph graph, String id1, String id2, String type)
    {
        if (graph.getNode(id1) != null && graph.getNode(id2) != null) {
            Edge edge = graph.addEdge(id1 + "-" + id2, id1, id2, false);
            edge.setAttribute("ui.label", type);
        }
    }

}


