package com.schedushare.android.schedule.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.android.util.ResourceUriBuilder;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.TimeBlockEntity;
import com.schedushare.common.domain.rest.RestResult;
import com.schedushare.common.domain.rest.RestResultHandler;

/**
 * task to login a user.
 */
public class CreateScheduleTask extends AsyncTask<ScheduleEntity, Integer, RestResult<ScheduleEntity>>{
	
	
	private final RestTemplate restTemplate;
	private final ResourceUriBuilder resourceUriBuilder;
	private final String scheduleResourceUri;
	private final RestResultHandler restResultHandler;
	
	@SuppressWarnings("unchecked")
	@Inject
	public CreateScheduleTask(@Named("restTemplate") 
							final RestTemplate restTemplate,
							@Named("resourceUriBuilder") 
							final ResourceUriBuilder resourceUriBuilder,
							@Named("scheduleResource")
							final String scheduleResourceUri,
							@Named("restResultHandler")
							final RestResultHandler restResultHandler) {
		super();
		this.restTemplate = restTemplate;
		ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.addAll(Arrays.asList(new GsonHttpMessageConverter(), new StringHttpMessageConverter()));

		this.restTemplate.setMessageConverters(messageConverters);
		this.resourceUriBuilder = resourceUriBuilder;
		this.scheduleResourceUri = scheduleResourceUri;
		this.restResultHandler = restResultHandler;
	}

	@Override
	protected RestResult<ScheduleEntity> doInBackground(ScheduleEntity... params) {
		String url = resourceUriBuilder.setResourceUri(this.scheduleResourceUri).build();
		
		String jsonResult = this.restTemplate.postForObject(url, params[0], String.class);
		return restResultHandler.createRestResult(jsonResult, ScheduleEntity.class);
	}
	
	
}
