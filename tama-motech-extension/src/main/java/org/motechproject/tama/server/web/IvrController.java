/**
 * MOTECH PLATFORM OPENSOURCE LICENSE AGREEMENT
 *
 * Copyright (c) 2010-11 The Trustees of Columbia University in the City of
 * New York and Grameen Foundation USA.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Grameen Foundation USA, Columbia University, or
 * their respective contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY GRAMEEN FOUNDATION USA, COLUMBIA UNIVERSITY
 * AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL GRAMEEN FOUNDATION
 * USA, COLUMBIA UNIVERSITY OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.motechproject.tama.server.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

//@Controller
//public class IvrController {
//	
//    @RequestMapping("/hello")
//    public ModelAndView helloWorld() {
//    	System.out.println("in hello controller");
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("hello");
//        mav.addObject("message", "Hello World!");
//        return mav;
//    }
//    
//    @RequestMapping("/test")
//    public ModelAndView test() {
//    	System.out.println("in test controller");
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("test");
//        mav.addObject("message", "Hello World!");
//        return mav;
//    }    
//}

/**
 * 
 * @author Ricky Wang
 */
public class IvrController extends MultiActionController {
	
	private  Logger logger = LoggerFactory.getLogger((IvrController.class));

	/**
	 * URL to see the sample VoiceXML:
	 * http://localhost:8080/motech-platform-server/module/tama/ivr/sample
	 */
	public ModelAndView sample(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("In ivr controller");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sample");
		mav.addObject("message", "Hello World!");
		return mav;
	}
}
