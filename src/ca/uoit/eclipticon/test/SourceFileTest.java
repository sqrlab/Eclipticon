package ca.uoit.eclipticon.test;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uoit.eclipticon.Constants;
import ca.uoit.eclipticon.data.InterestPoint;
import ca.uoit.eclipticon.data.SourceFile;

public class SourceFileTest extends TestCase {

	private SourceFile		_sourceFile			= null;
	private InterestPoint	_interestingPoint	= new InterestPoint( 0, 0, Constants.SEMAPHORE, Constants.SEMAPHORE_ACQUIRE );
	private String			_path				= null;
	private String			_pathUnix			= "/eclipticon/src/ca/uoit/eclipticon/test/SourceFileTest.java";
	private String			_pathWindows		= "\\eclipticon\\src\\ca\\uoit\\eclipticon\\test\\SourceFileTest.java";
	private String			_name				= "SourceFileTest.java";
	private String			_imports			= "package ca.uoit.eclipticon.test;\n\nimport ca.uoit.eclipticon.instrumentation;\nimport ca.uoit.eclipticon.test;";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		if( System.getProperty( "os.name" ).indexOf( "Windows" ) == -1 ) {
			_sourceFile = new SourceFile( new Path( _pathUnix ) );
			_path = _pathUnix;
		}
		else {
			_sourceFile = new SourceFile( new Path( _pathWindows ) );
			_path = _pathWindows;
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSourceFile() {

		boolean result = false;

		if( _sourceFile.getName().equals( _name ) && ( _sourceFile.getPath().toString().equals( _path ) ) ) {
			result = true;
		}
		
		assertEquals( true, result );
	}

	@Test
	public void testGetPath() {
		assertEquals( _path, _sourceFile.getPath().toString() );
	}

	@Test
	public void testGetName() {
		assertEquals( _name, _sourceFile.getName() );
	}

	@Test
	public void testAddAndGetInterestingPoints() {

		_sourceFile.addInterestingPoint( _interestingPoint );

		assertEquals( _interestingPoint, _sourceFile.getInterestingPoints().get( 0 ) );
	}

	@Test
	public void testClearInterestingPoints() {

		_sourceFile.addInterestingPoint( _interestingPoint );
		_sourceFile.clearInterestingPoints();

		assertEquals( 0, _sourceFile.getInterestingPoints().size() );
	}

	@Test
	public void testSetAndGetPackageAndImports() {

		_sourceFile.setPackageAndImports( _imports );

		assertEquals( _imports, _sourceFile.getPackageAndImports() );
	}
}