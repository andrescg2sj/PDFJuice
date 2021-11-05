/*
 * Apache License
 *
 * Copyright (c) 2021 Andrés González SJ
 *
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sj.tools.pdfjuice.slide;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class SldListItem implements SldObject {
	
	SldText bullet;
	SldObject content;
	
	public SldListItem(SldText blt, SldObject cont) {
		this.bullet = blt;
		this.content = cont;
	}
	

	@Override
	public String getText() {
		return content.getText();
	}

	@Override
	public Rectangle2D getBounds() {
		Rectangle2D r = content.getBounds();
		return r.createUnion(content.getBounds());
	}


	@Override
	public Point2D getPosition() {
		Rectangle2D r = getBounds();
		return new Point2D.Double(r.getMinX(),r.getMinY());
	}
	
	public String toString() {
		return bullet.getText() + " "+content.getText() + "\r\n";
	}


	@Override
	public String toHTML() {
		return "<li>" + content.toHTML() + "</li>";
	}

}
