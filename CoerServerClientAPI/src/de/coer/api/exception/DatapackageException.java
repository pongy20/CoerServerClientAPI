package de.coer.api.exception;

public class DatapackageException extends Exception {
	
	private static final long serialVersionUID = 1542344985498758764L;

	public static final String noIdentifier = "Can't create datapackage without identifier.";
	public static final String identifierAlreadyExists = "There is already a method declared with that identifier.";
	public static final String noDatapackage = "Received no datapackage from server.";
	public static final String identifierNotDeclared = "There can't find any methods using that identifiert!";
	
	
	public DatapackageException(String message) {
		super(message);
	}
	
}
