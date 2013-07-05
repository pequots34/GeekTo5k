<%@page import="com.geek.exercise.responses.ErrorResponse"%>
<%@page import="java.io.IOException"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="org.codehaus.jackson.map.JsonMappingException"%>
<%@page import="org.codehaus.jackson.JsonGenerationException"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="java.io.StringWriter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
	response.setContentType( "application/json" );
	
	ObjectMapper mapper = new ObjectMapper();
	
	StringWriter output = new StringWriter();
	
	try {
		mapper.writeValue( output, ErrorResponse.newBuilder()
				.setMessage( "Server monkeys were unable to fulfill that unknown request!" )
				.build() );
	} catch ( JsonGenerationException e ) {
	} catch ( JsonMappingException e ) {
	} catch ( IOException e ) {
	}
	
	response.getWriter().write( output.toString() );
%>