package com.jeefw.service.sys;

import java.util.List;

import com.jeefw.model.sys.Information;

import core.service.Service;

/**
 * @author John
 */
public interface InformationService extends Service<Information> {

	List<Information> queryInformationHTMLList(List<Information> resultList);

	void indexingInformation();

	List<Information> queryByInformationName(String name);

}
