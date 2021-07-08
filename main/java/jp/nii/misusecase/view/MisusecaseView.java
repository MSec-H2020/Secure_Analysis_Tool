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

import java.awt.*;
import java.awt.geom.Point2D;

import javax.swing.*;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;
import com.change_vision.jude.api.inf.view.IDiagramViewManager;
import com.change_vision.jude.api.inf.view.IViewManager;
import jp.nii.misusecase.Knowledge;

public class MisusecaseView extends JSplitPane implements IPluginExtraTabView, ProjectEventListener {

	private KnowledgeView knowledgeView = null;
	private KnowledgeBrowserView knowledgeGraphView = null;

	public MisusecaseView() {
		initComponents();
	}

	private void initComponents() {
		knowledgeView = new KnowledgeView();
		knowledgeView.setKnowledgeActionListener(new KnowledgeActionListener() {
			@Override
			public void focus(Knowledge knowledge) {
				knowledgeGraphView.setFocusKnowledge(knowledge);
			}
			@Override
			public void insert(Knowledge knowledge) {
				insertKnowledge(knowledge);
			}
		});
		knowledgeGraphView = new KnowledgeBrowserView();

		setLeftComponent(knowledgeView);
		setRightComponent(knowledgeGraphView);
		setDividerLocation(0.5);

		addProjectEventListener();
	}

	private void insertKnowledge(Knowledge knowledge) {
		try {
			AstahAPI api = AstahAPI.getAstahAPI();
			ProjectAccessor projectAccessor = api.getProjectAccessor();

			IViewManager viewManager = projectAccessor.getViewManager();
			IDiagramViewManager diagramViewManager = viewManager.getDiagramViewManager();
			IDiagram diagram = diagramViewManager.getCurrentDiagram();
			if (diagram instanceof IUseCaseDiagram) {
				insertNote(projectAccessor, (IUseCaseDiagram)diagram, knowledge);
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (InvalidUsingException ex) {
			ex.printStackTrace();
		}
	}

	private String generateUsecaseName(IModel project, Knowledge knowledge)
	{
		for (int i = 1; i < 1000; ++i) {
			String name = knowledge.getName() + "_" + i;
			for (INamedElement element : project.getOwnedElements()) {
				if (element.getName().equals(name)) {
					name = null;
					break;
				}
			}
			if (name != null) {
				return name;
			}
		}
		return null;
	}

	private void insertNote(ProjectAccessor projectAccessor, IUseCaseDiagram useCaseDiagram, Knowledge knowledge) {
		try {
			IModel project = projectAccessor.getProject();

			TransactionManager.beginTransaction();

			IDiagram diagram = projectAccessor.getViewManager().getDiagramViewManager().getCurrentDiagram();
			if (!(diagram instanceof IUseCaseDiagram)) {
				return;
			}
			IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
			UseCaseDiagramEditor useCaseDiagramEditor = diagramEditorFactory.getUseCaseDiagramEditor();
			useCaseDiagramEditor.setDiagram(diagram);
			Point2D location = new Point2D.Double(diagram.getBoundRect().getCenterX(), diagram.getBoundRect().getCenterY());
			useCaseDiagramEditor.createNote(knowledge.getName(), location);

			TransactionManager.endTransaction();

		} catch (ProjectNotFoundException | InvalidEditingException | InvalidUsingException e) {
			e.printStackTrace();
		} finally {
			if (TransactionManager.isInTransaction()) {
				TransactionManager.abortTransaction();
			}
		}
	}

	private void addProjectEventListener() {
		try {
			AstahAPI api = AstahAPI.getAstahAPI();
			ProjectAccessor projectAccessor = api.getProjectAccessor();
			projectAccessor.addProjectEventListener(this);
		} catch (ClassNotFoundException e) {
			e.getMessage();
		}
	}

	@Override
	public void projectChanged(ProjectEvent e) {
	}

	@Override
	public void projectClosed(ProjectEvent e) {
	}

	@Override
	public void projectOpened(ProjectEvent e) {
	}

	@Override
	public void addSelectionListener(ISelectionListener listener) {
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getDescription() {
		return "Show Misusecase View";
	}

	@Override
	public String getTitle() {
		return "Misusecase";
	}

	public void activated() {
	}

	public void deactivated() {
	}
}
