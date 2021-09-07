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


import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import jp.nii.misusecase.KnowledgeRepository;

public class ResetKnowledgeAction implements IPluginActionDelegate {

	public Object run(IWindow window) throws UnExpectedException {
		KnowledgeRepository knowledgeRepository = KnowledgeRepository.getInstance();
		try {
			knowledgeRepository.setKnowledges(knowledgeRepository.getDefaultKnowledges());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}


}
