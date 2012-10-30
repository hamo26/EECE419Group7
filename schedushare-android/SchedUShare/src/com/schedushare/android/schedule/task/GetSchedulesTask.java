package com.schedushare.android.schedule.task;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.android.util.ResourceUriBuilder;
import com.schedushare.common.domain.dto.ScheduleListEntity;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.rest.RestResult;
import com.schedushare.common.domain.rest.RestResultHandler;

/**
 * task to login a user.
 */
public class GetSchedulesTask extends AsyncTask<String, Integer, RestResult<ScheduleListEntity>>{
	
	
	private final RestTemplate restTemplate;
	private final ResourceUriBuilder resourceUriBuilder;
	private final String scheduleResourceUri;
	private final RestResultHandler restResultHandler;
	
	@SuppressWarnings("unchecked")
	@Inject
	public GetSchedulesTask(@Named("restTemplate") 
							final RestTemplate restTemplate,
							@Named("resourceUriBuilder") 
							final ResourceUriBuilder resourceUriBuilder,
							@Named("scheduleResource")
							final String loginResourceUri,
							@Named("restResultHandler")
							final RestResultHandler restResultHandler) {
		super();
		this.restTemplate = restTemplate;
		ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.addAll(Arrays.asList(new GsonHttpMessageConverter(), new StringHttpMessageConverter()));

		this.restTemplate.setMessageConverters(messageConverters);
		this.resourceUriBuilder = resourceUriBuilder;
		this.scheduleResourceUri = loginResourceUri;
		this.restResultHandler = restResultHandler;
	}

	@Override
	protected RestResult<ScheduleListEntity> doInBackground(String... params) {
		String userEmail = params[0];
		String url = resourceUriBuilder.setResourceUri(this.scheduleResourceUri)
										.setId(userEmail + "/notactive")
										.build();
		
		String jsonResult = this.restTemplate.getForObject(url, String.class);
		return restResultHandler.createRestResult(jsonResult, ScheduleListEntity.class);
	}
	
	
}
