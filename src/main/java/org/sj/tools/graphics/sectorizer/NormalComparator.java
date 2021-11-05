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
package org.sj.tools.graphics.sectorizer;

import java.util.Comparator;

public class NormalComparator implements Comparator<Positionable> {
	
	private static NormalComparator instance;
	
	private NormalComparator() {
	}
	
	public static NormalComparator getInstance() {
		if(instance == null) {
			instance = new NormalComparator();
		}
		return instance;
	}

	@Override
	public int compare(Positionable a, Positionable b) {
		if (a.getPosition().getY() < b.getPosition().getY()) {
			return -1;
		}else if (a.getPosition().getY() > b.getPosition().getY()) {
			return 1;
		} else {
			if (a.getPosition().getX() < b.getPosition().getX()) {
				return -1;
			} else if (a.getPosition().getX() > b.getPosition().getX()) {
				return 1;
			} 
			return 0;
		}
	}

	//TODO: better name
	public static boolean previousPos(Positionable a, Positionable b) {
		return (a.getPosition().getY() < b.getPosition().getY()) ||
				(a.getPosition().getY() == b.getPosition().getY()) &&
				(a.getPosition().getX() < b.getPosition().getX());
	
	}

}
