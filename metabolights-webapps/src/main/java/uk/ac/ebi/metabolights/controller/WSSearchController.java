/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2014-Oct-23
 * Modified by:   conesa
 *
 *
 * Copyright 2015 EMBL-European Bioinformatics Institute.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package uk.ac.ebi.metabolights.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ebi.metabolights.properties.PropertyLookup;
import uk.ac.ebi.metabolights.service.AppContext;

/**
 * Controller for Metabolights searching using the webservice.
 * @author Pablo Conesa
 */
@Controller
public class WSSearchController extends AbstractController{

	public static final String WS_SEARCH_SUFFIX = "";
	public static final String SEARCH_MODE = "wsSearchResult";
	public static final String MY_SUBMISSIONS_MODE = "wsMySubmissions";
	public static final String HEADER = "sHeader";
	public static final String NORESULTSMESSAGE = "noresultsmessage";
	private static Logger logger = LoggerFactory.getLogger(WSSearchController.class);


    /**
     * Controller for a browse (empty internalSearch) request
     */
    @RequestMapping(value = "browse" + WS_SEARCH_SUFFIX)
    public ModelAndView browse() throws Exception {

		//Trigger the internalSearch based on the filter
		ModelAndView mav = internalSearch("wsBrowse");

		//Add the action to the ModelAndView
		mav.addObject("action", "wsBrowse");

		return mav;
    }

	/**
	 * Controller for a internalSearch request, including possible filters.
	 *
	 */
	@RequestMapping(value = "/search" + WS_SEARCH_SUFFIX, method = {RequestMethod.GET})
	public ModelAndView search (@RequestParam(required=false,value="freeTextQuery") String freeTextQuery) throws Exception {


		//Trigger the internalSearch based on the filter
		ModelAndView mav = internalSearch(SEARCH_MODE);

		//Add the action to the ModelAndView
		mav.addObject("action", "search");
		mav.addObject("freeTextQuery", freeTextQuery);

		return mav;
	}

	private ModelAndView internalSearch(String MAVName) {

		ModelAndView mav = AppContext.getMAVFactory().getFrontierMav(MAVName);
		mav.addObject("user_token", LoginController.getLoggedUser().getApiToken() );

		if (MAVName.equals(SEARCH_MODE)){
			mav.addObject(HEADER, PropertyLookup.getMessage("msg.search.title"));
			mav.addObject(NORESULTSMESSAGE, PropertyLookup.getMessage("msg.search.noresults"));
		} else if (MAVName.equals(MY_SUBMISSIONS_MODE)){
			mav.addObject(HEADER, PropertyLookup.getMessage("msg.mysubmissions.title"));
			mav.addObject(NORESULTSMESSAGE, PropertyLookup.getMessage("msg.mysubmissions.noresults"));
		// Browse mode
		} else {
			mav.addObject(HEADER, PropertyLookup.getMessage("msg.browse.title"));
			mav.addObject(NORESULTSMESSAGE, PropertyLookup.getMessage("msg.browse.noresults"));
		}

		return mav;
	}

//	private ModelAndView internalSearch(HttpServletRequest request, SearchQuery query, String MAVName) throws Exception {
//
//		logger.info("Searching for "+ query);
//
//		//Prepare the query
//		MetabolightsWsClient client = EntryController.getMetabolightsWsClient(request,LoginController.getLoggedUser());
//
//		RestResponse<? extends SearchResult> response = client.search(query);
//
//		if (response.getErr() != null) {
//			throw new Exception("Upps!. Something went wrong during the search." , response.getErr());
//		}
//
//		logger.debug("Found #results = "+response.getContent().getQuery().getPagination().getItemsCount());
//
//		ModelAndView mav = AppContext.getMAVFactory().getFrontierMav(MAVName);
//        mav.addObject("results", response.getContent());
//		mav.addObject("freeTextQuery", response.getContent().getQuery().getText());
//
//    	return mav;
//	}




	@RequestMapping(value = "/mysubmissions")
	public ModelAndView MySubmissionsSearch () {


		//Trigger the internalSearch based on the filter
		ModelAndView mav = internalSearch(MY_SUBMISSIONS_MODE);

		//Add the action to the ModelAndView
		mav.addObject("action", "mysubmissions");
		mav.addObject("usersFullName", LoginController.getLoggedUser().getFullName());


		//Add the message to the user
//		String welcomeMessage;
//
//		//If he doesn't have any study
//        //TODO, the two text messages are no longer displayed, BUT the welcomeMessage string must be present in the JSP
//		if (filter.getInitialHits() ==0){
//			welcomeMessage = PropertyLookup.getMessage("msg.welcomeWithoutStudies",
//					user.getFirstName());
//		} else {
//			welcomeMessage =  PropertyLookup.getMessage("msg.welcomeWithStudies",
//					user.getFirstName(), Integer.toString(filter.getInitialHits()));
//		}
//
//		// Add the message to the response
//		mav.addObject("welcomemessage", welcomeMessage);
//
		return mav;
	}

//	/**request can come with 2 parameters:
//	 * query: freetext query
//	 * form with all the possible filters
//	 * @param request
//	 * @return
//	 */
//	public SearchQuery requestToQuery(HttpServletRequest request){
//
//		SearchQuery query = new SearchQuery();
//
//		// Get the search term
//		query.setText(request.getParameter(Filter.FREE_TEXT_QUERY));
//
//
//		// Get an enumeration of all parameters
//		Enumeration paramenum = request.getParameterNames();
//
//		//Loop through the enumeration
//		while (paramenum.hasMoreElements()) {
//			// Get the name of the request parameter
//			String name = (String)paramenum.nextElement();
//
//			//Which page are we on
//			if (name.equals("pageNumber")) {
//				query.getPagination().setPage(Integer.valueOf(request.getParameter(name)));
//				continue;
//			}
//
////			//Get the Corresponding filterSet
////			FilterSet fs = fss.get(name);
////
////			//If exists...
////			if (fs != null){
////
////				// If the request parameter can appear more than once in the query string, get all values
////				String[] values = request.getParameterValues(name);
////
////				for (int i=0; i<values.length; i++) {
////
////					String val = values[i];
////					if (val == null)
////						val = "";
////
////					//We need to update the status (checked/unchecked)
////					FilterItem fi = fs.getFilterItems().get(val);
////
////					if (fi != null)   // Just in case the value is missing or we cannot access it, this will prevent a null pointer
////						fi.setIsChecked(true);
////					else {
////						fi = new FilterItem(val, METABOLITE_FILTER, val);
////						fi.setUTF8Value(val);
////						fi.setIsChecked(true);
////					}
////
////				}
////			}
//		}
//
//		return query;
//	}
}
