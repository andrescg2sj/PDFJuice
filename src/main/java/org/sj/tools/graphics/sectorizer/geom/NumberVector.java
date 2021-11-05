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
package org.sj.tools.graphics.sectorizer.geom;

import java.util.Vector;

public class NumberVector  {
	
	Vector<Double> numbers;
	
	public NumberVector() {
		numbers = new Vector<Double>();
	}
	
	public NumberVector(NumberVector v) {
		numbers = new Vector<Double>(v.numbers);
	}
	
	public NumberVector(double v[]) {
		numbers = new Vector<Double>(v.length);
		for(int i=0; i<v.length; i++) {
			insert(v[i]);
		}
	}
	
	public NumberVector(int v[]) {
		numbers = new Vector<Double>(v.length);
		for(int i=0; i<v.length; i++) {
			insert(v[i]);
		}
	}

	

	
	public int size() {
		return numbers.size();
	}
	
	public double[] toArray() {
		double values[] = new double[numbers.size()];
		int i=0;
		for(Double d: numbers) {
			values[i++] = d.doubleValue();
		}
		return values;
	}
	
	public double get(int index) {
		return numbers.get(index).doubleValue();
	}
	
	public void insert(double value) {
		if(numbers.size() == 0) {
			numbers.add(value);
		} else if(value < numbers.firstElement().doubleValue()) {
			numbers.insertElementAt(new Double(value), 0);
		} else if(value > numbers.lastElement().doubleValue()) {
			numbers.add(new Double(value));
		} else {
			int min_i = 0;
			int max_i = numbers.size()-1;
			if(value == numbers.get(min_i).doubleValue() ||
					value == numbers.get(max_i).doubleValue()) {
				return;
			}
			while(max_i - min_i > 1) {
				int i = (min_i + max_i) /2;
				double middleVal = numbers.get(i).doubleValue(); 
				if(value < middleVal) {
					max_i = i;
				} else if(value > middleVal) {
					min_i = i;
				} else {
					return;
				}
			}
			numbers.insertElementAt(new Double(value), max_i);
		}
	}

    public String toString() {
	Vector<String> text= new Vector<String>();
	for(Double d: numbers) {
	    text.add(String.format("%f", d.doubleValue()));
 
	}
	
	return "(" + String.join(", ",text)+")";

    }


}
