package ca.sqrlab.eclipticon.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import ca.sqrlab.eclipticon.Constants;

/**
 * The Class MethodCallValidator will end up verifying that a file's path end up
 * being represented in the package or the import statements of a file. The reasoning 
 * for this is to verify that a synchronized method call is valid by checking the
 * correct import statement (a naive approach to object checking).
 * 
 * @author Chris Forbes, Kevin Jalbert, Cody LeBlanc
 */
public class MethodCallValidator {
	
	// Matcher to match on the package statements
	private Matcher _matcher = null;
	
	/**
	 * Checks if the file's method call is valid by checking the import 
	 * and package statements.
	 * 
	 * @param pathFileClass the path of the current file's method call
	 * @param importsAndPackage the import and package string
	 * @return true, if the method call resides in the imported file
	 */
	public boolean isMethodImportedInFile( Path pathFileClass, String importsAndPackage ) {

		// Take the path of the method class and format the path
		String filePath = pathFileClass.removeFileExtension().toString();
		filePath = filePath.replace( '\\', '.' ); // Replace the windows separators with dots
		filePath = filePath.replace( '/', '.' ); // Replace the unix separators with dots

		// Acquire the workspace path
		Path workspacePath;
		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			workspacePath = (Path)root.getLocation();
		}
		catch( Exception e ) {
			workspacePath = new Path(""); // No workspace if cannot find one
		}

		String workspace = workspacePath.toString();
		workspace = workspace.replace( '\\', '.' ); // Replace the windows separators with dots
		workspace = workspace.replace( '/', '.' ); // Replace the unix separators with dots

		// Remove the workspace path from path of the method class
		filePath = filePath.substring( workspace.length() );

		// If the imports and package string is null then set it an empty string
		if( importsAndPackage == null ) {
			importsAndPackage = "";
		}
		else if (importsAndPackage.isEmpty()){ // Package was empty, therefore default to workspace
			importsAndPackage = "package " + workspace + ";";
		}

		_matcher = null;

		// Match on the package statement
		_matcher = Constants.PATTERN_PACKAGE.matcher( importsAndPackage );
		if( _matcher.find() ) {
			
			// Acquire the package statement
			String packageStatement = _matcher.group();
			packageStatement = packageStatement.replace( "package", "" );
			packageStatement = packageStatement.replace( ";", "" );
			packageStatement = packageStatement.replaceAll( "[\\s]*" , "" );

			if (filePath.indexOf( packageStatement ) != -1){
				return true;
			}
		}
		else {
			// TODO Not sure how to handle defualt package yet (accept it for now)
			return true;
		}

		// Match on the import statement
		_matcher = Constants.PATTERN_IMPORT.matcher( importsAndPackage );
		while( true ) {
			if( _matcher.find() ) {

				// Acquire the import statement
				String importStatement = _matcher.group();
				importStatement = importStatement.replace( "import", "" );
				importStatement = importStatement.replace( ";", "" );
				importStatement = importStatement.replaceAll( "[\\s]*" , "" );

				if (filePath.indexOf( importStatement ) != -1){
					return true;
				}
			}
			else{ // No import found
				return false;
			}
		}
	}
}