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

package jp.nii.misusecase.actions;


import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import jp.nii.misusecase.KnowledgeRepository;
import jp.nii.misusecase.view.KnowledgeEditorDialog;

import javax.swing.*;

public class UpdateKnowledgeAction implements IPluginActionDelegate {

	public Object run(IWindow window) throws UnExpectedException {
	    try {
	    	String knowledges = KnowledgeRepository.getInstance().getKnowledgesAsString();
			AstahAPI api = AstahAPI.getAstahAPI();
			final KnowledgeEditorDialog dialog = new KnowledgeEditorDialog(api.getViewManager().getMainFrame());
			dialog.setKnowledges(knowledges);
			dialog.addDialogActionListener(new KnowledgeEditorDialog.DialogActionListener() {
				@Override
				public void onOk() {
					String knowledges = dialog.getKnowledges();
					try {
						KnowledgeRepository.getInstance().setKnowledges(knowledges);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onCancel() {
				}
			});
			dialog.setVisible(true);
	    } catch (Exception e) {
	    	JOptionPane.showMessageDialog(window.getParent(), "Unexpected error has occurred.", "Alert", JOptionPane.ERROR_MESSAGE); 
	        throw new UnExpectedException();
	    }
	    return null;
	}


}
