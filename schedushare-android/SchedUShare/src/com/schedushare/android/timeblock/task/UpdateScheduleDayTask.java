package com.schedushare.android.timeblock.task;

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
import com.schedushare.common.domain.dto.TimeBlocksEntity;
import com.schedushare.common.domain.rest.RestResult;
import com.schedushare.common.domain.rest.RestResultHandler;

/**
 * task to login a user.
 */
public class UpdateScheduleDayTask extends AsyncTask<TimeBlocksEntity, Integer, RestResult<TimeBlocksEntity>>{
	
	
	private final RestTemplate restTemplate;
	private final ResourceUriBuilder resourceUriBuilder;
	private final String scheduleTimeBlocksResourceUri;
	private final RestResultHandler restResultHandler;
	
	@SuppressWarnings("unchecked")
	@Inject
	public UpdateScheduleDayTask(@Named("restTemplate") 
							final RestTemplate restTemplate,
							@Named("resourceUriBuilder") 
							final ResourceUriBuilder resourceUriBuilder,
							@Named("scheduleTimeBlocksResource")
							final String timeBlocksResourceUri,
							@Named("restResultHandler")
							final RestResultHandler restResultHandler) {
		super();
		this.restTemplate = restTemplate;
		ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.addAll(Arrays.asList(new GsonHttpMessageConverter(), new StringHttpMessageConverter()));

		this.restTemplate.setMessageConverters(messageConverters);
		this.resourceUriBuilder = resourceUriBuilder;
		this.scheduleTimeBlocksResourceUri = timeBlocksResourceUri;
		this.restResultHandler = restResultHandler;
	}

	@Override
	protected RestResult<TimeBlocksEntity> doInBackground(TimeBlocksEntity... params) {
		TimeBlocksEntity timeBlocksEntity = params[0];
		String url = resourceUriBuilder.setResourceUri(this.scheduleTimeBlocksResourceUri)
									   .setId(timeBlocksEntity.getScheduleId())
									   .build();
		
		this.restTemplate.put(url, timeBlocksEntity);
		return restResultHandler.createPutResult(TimeBlocksEntity.class);
	}
	
	
}
