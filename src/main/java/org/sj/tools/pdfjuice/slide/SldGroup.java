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

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Collection;
import java.util.LinkedList;

import org.sj.tools.graphics.sectorizer.PosRegionCluster;

public class SldGroup<E extends SldObject> implements SldObject {
	
	LinkedList<E> elements;

	public SldGroup() {
		elements = new LinkedList<E>();
	}

	public SldGroup(Collection<E> objs) {
		elements = new LinkedList<E>(objs);
	}
	
	
	public void add(E sobj) {
		elements.add(sobj);
	}

	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();
		for(SldObject sobj : elements) {
			sb.append(sobj.getText());
		}
		return sb.toString();
	}
	
	
	Rectangle2D toRect2D(RectangularShape rs) {
		return rs.getBounds2D();
	}

	@Override
	public Rectangle2D getBounds() {
		Rectangle2D r = PosRegionCluster.cloneBounds(elements.getFirst()); 
		for(SldObject sobj : elements) {
			Rectangle2D bounds = sobj.getBounds();
			if(r != null && bounds != null) {
				r.add(bounds);
			}
		}
		return r;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//sb.append("(Group):");
		for(E obj: elements) {
			sb.append(obj.toString());
		}
		sb.append("\r\n");
		return sb.toString();
	}

	@Override
	public Point2D getPosition() {
		Rectangle2D r = getBounds();
		return new Point2D.Double(r.getMinX(),r.getMinY());
	}

	@Override
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		//sb.append("(Group):");
		for(E obj: elements) {
			sb.append(obj.toHTML());
		}
		sb.append("\r\n");
		return sb.toString();
	}

}
