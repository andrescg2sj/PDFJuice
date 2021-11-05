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
package org.sj.tools.graphics.elements;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.sj.tools.graphics.sectorizer.Positionable;

public class Image implements Positionable {
	
	Point2D position;
	BufferedImage image;
	
	public Image(Point2D _position, BufferedImage _image) {
		position = _position;
		image = _image;
	}
	
	public Image(Image img) {
		this.position = img.position;
		this.image = img.image;
	}


	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(position.getX(),position.getY(),
				image.getWidth(), image.getHeight());
	}

}
