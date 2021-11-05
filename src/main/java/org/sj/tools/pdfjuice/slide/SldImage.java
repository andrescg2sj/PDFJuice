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
import java.awt.image.BufferedImage;

import org.sj.tools.graphics.elements.Image;

public class SldImage extends Image implements SldObject {
	
	public SldImage(Point2D _position, BufferedImage _image) {
		super(_position, _image);
	}


	public SldImage(Image img) {
		super(img);
	}


	@Override
	public String getText() {
		return "<image>";
	}


	@Override
	public String toHTML() {
		// TODO encode with Base64
		// See: https://www.w3docs.com/snippets/html/how-to-display-base64-images-in-html.html
		return "<img src=\"data:image/png;base64, ...\" />";
	}

}
