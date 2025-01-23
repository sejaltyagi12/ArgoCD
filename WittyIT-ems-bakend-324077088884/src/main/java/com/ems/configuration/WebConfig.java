package com.ems.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import com.ems.servicefinder.excelHelper.ExcelViewResolver;
import com.ems.servicefinder.pdfHelper.PdfViewResolver;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * Web config extends the Spring WebMvcAutoConfigurationAdapter to enable MVC to
 * look for injected components
 * 
 * @author Avinash Tyagi
 * @version 2.0
 * @since 2014-03-23
 *
 */
@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan({ "com.ems" })
public class WebConfig extends WebMvcConfigurerAdapter {

	/**
	 * NOT USED (This API uses only the controller functionality).
	 *
	 * @param registry
	 *            the registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

	}

	/**
	 * Used to map JSONs into Objects and vice-versa Internally it is using
	 * Jackson2ObjectMapperFactoryBean, it is formatting all double numbers with
	 * decimal notation,
	 * 
	 * @return ObjectMapper mapper responsible to map json <-> java object
	 */
	@Bean
	public ObjectMapper objectMapper() {
		Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
		bean.setIndentOutput(true);

		JsonSerializer<Double> doubleSerializer = new JsonSerializer<Double>() {
			@Override
			public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider)
					throws IOException, JsonProcessingException {
				jgen.writeNumber(value);
			}
		};

		Map<Class<?>, JsonSerializer<?>> mapping = new HashMap<Class<?>, JsonSerializer<?>>();
		mapping.put(double.class, doubleSerializer);

		bean.setSerializersByType(mapping);

		// bean.setSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		// bean.setSimpleDateFormat(DTFormatterType.YYYY_MM_DD_SPACE_HH_MM.pattern);
		bean.afterPropertiesSet();

		ObjectMapper objectMapper = bean.getObject();
		objectMapper.registerModule(new JodaModule());

		// objectMapper.registerModule(new OranjJacksonModule());
		// objectMapper.registerModule(new Hibernate4Module());

		// objectMapper.registerModule(new GuavaModule());

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		// multipartResolver().setMaxUploadSize(10485760);

		return objectMapper;
	}

	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper());
		return converter;
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		arrayHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
		return arrayHttpMessageConverter;
	}

	/**
	 * Add to converters the mappingJackson2HttpMessageConverter.
	 *
	 * @param converters
	 *            List of HttpMessageConverter
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2HttpMessageConverter());
		converters.add(byteArrayHttpMessageConverter());
	}

	/**
	 * Get the LocalValidatorFactoryBean.
	 * 
	 * @return LocalValidatorFactoryBean Bean validation for Hibernate
	 */
	@Bean
	public LocalValidatorFactoryBean validator() {
		final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		return validator;
	}

	/**
	 * Get the CommonsMultipartResolver.
	 *
	 * @return CommonsMultipartResolver for file upload
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}

	/**
	 * Get the MultipartConfigElement.
	 *
	 * @return MultipartConfigElement with max sizes set
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("10MB");
		factory.setMaxRequestSize("10MB");
		return factory.createMultipartConfig();
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON).favorPathExtension(true);
	}

	/*
	 * Configure ContentNegotiatingViewResolver
	 */
	@Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setContentNegotiationManager(manager);

		// Define all possible view resolvers
		List<ViewResolver> resolvers = new ArrayList<>();

		resolvers.add(pdfViewResolver());
		resolvers.add(excelViewResolver());
		resolver.setViewResolvers(resolvers);

		return resolver;
	}

	/*
	 * Configure View resolver to provide Pdf output using iText library to
	 * generate pdf output for an object content
	 */
	@Bean
	public ViewResolver pdfViewResolver() {
		return new PdfViewResolver();
	}

	/*
	 * Configure View resolver to provide XLS output using Apache POI library to
	 * generate XLS output for an object content
	 */
	@Bean
	public ViewResolver excelViewResolver() {
		return new ExcelViewResolver();
	}

}
