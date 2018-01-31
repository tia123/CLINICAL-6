package com.aem.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aem.core.HandleCustomFormAction;
@Component(immediate = true, metatype = true, policy = ConfigurationPolicy.REQUIRE, 
label = "Configuration")
@Service(value = HandleCustomFormAction.class)
public class HandleCustomFormActionImpl implements HandleCustomFormAction {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void injestFormData(SlingHttpServletRequest request, Resource resource) {
		@SuppressWarnings("rawtypes")
		Enumeration fieldName = request.getParameterNames();//all form field parameters are retrieved and stored in field name
		ValueMap props = ResourceUtil.getValueMap(resource);
		String apiURL = (String)props.get("api");// authored api is retrieved and the fieldName
		log.debug("apiURL--->"+apiURL);
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			log.debug("client--->"+client);
			HttpPost post = new HttpPost(apiURL);
			log.debug("post--->"+post);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			while(fieldName.hasMoreElements()){				
				String param = (String)fieldName.nextElement();// iterating for fieldName and printing it in list
				String value = request.getParameter(param);
				log.debug("param-->"+param);
				log.debug("value-->"+value);
				nameValuePairs.add(new BasicNameValuePair(param, value));
			}

			String entityValue = URLEncodedUtils.format(nameValuePairs, HTTP.UTF_8);
			entityValue = entityValue.replaceAll("\\+", "%20"); 

			StringEntity stringEntity = new StringEntity(entityValue, HTTP.UTF_8);
			stringEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
			post.setEntity(stringEntity); //posting all the field values to the authored api
			post.addHeader(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);
			HttpResponse response = null;
			response = client.execute(post);//for 

			int responseCode = response.getStatusLine().getStatusCode();//2
			log.info("**POST** request Url: " + post.getURI());
			log.info("Response Code: " + responseCode);

			BufferedReader rd;
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			rd = new BufferedReader(new InputStreamReader(post.getEntity().getContent()));
			StringBuffer param = new StringBuffer();
			line = "";
			while ((line = rd.readLine()) != null) {
				param.append(line);
			}

			log.info("Entity Body: " + param);

		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException occurred :: {}", e);
		} catch (ClientProtocolException e) {
			log.error("ClientProtocolException occurred :: {}", e);
		} catch (IOException e) {
			log.error("IOException occurred :: {}", e);
		}
	}

	@SuppressWarnings("unchecked")
	protected void activate(final ComponentContext componentContext)
			throws Exception {
		//Do nothing
	}

}
