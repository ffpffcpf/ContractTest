package test.cpf;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.github.macdao.moscow.http.RestExecutor;
import com.github.macdao.moscow.http.RestResponse;

public class RestTempExecutor implements RestExecutor {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private MockMvc mock;
	
	public RestTempExecutor(MockMvc mock){
		this.mock = mock;
	}
	
	@Override
	public RestResponse execute(String method, URI uri,
			Map<String, String> headers, Object body) {
		logger.info("============================="+uri.toString());
		ResultActions action=null;
		try {
			action = mock.perform(MockMvcRequestBuilders.get(uri));
			return new RestResponse(action.andReturn().getResponse().getStatus(), getHeadersMap(action.andReturn().getResponse()), action.andReturn().getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private Map<String,String> getHeadersMap (MockHttpServletResponse response){
		Map<String, String> result = new HashMap<String, String>();
		for(String key : response.getHeaderNames()){
			result.put(key, response.getHeaderValue(key).toString());
		}
		return result;
	}

}
