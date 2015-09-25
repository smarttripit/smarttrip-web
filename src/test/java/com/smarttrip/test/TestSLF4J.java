/**
 * 
 */
package com.smarttrip.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songjie
 *
 */
public class TestSLF4J {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(TestSLF4J.class);
		for(int i=1; i<10000; i++){
			logger.info("hello world {}", Math.random());
		}
		
	}
}
