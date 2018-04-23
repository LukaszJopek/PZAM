package core;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author Lukasz
 *
 */
class MathUtilsTest {

	@AfterEach
	void tearDown() throws Exception {
	}

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
	
	@Test
	void isDivideOperatiosCorrect() throws Exception {
		assertEquals(1,MathUtils.divide(5, 5)); 
	}

	@Test
	void isDivideOperatiosCorrectWithDelta() throws Exception {
		assertEquals(1,MathUtils.divide(5, 5), 0.5);
	}
	
	//JUNIT4 !!!
//	@Test (expected = IndexOutOfBoundsException.class)
//	void shouldThrowException() {
//		List<String> lista = new ArrayList<String>();
//		String s = lista.get(0);
//	}
	
	@Test
	public void itShouldThrowNullPointerExceptionWhenElementGet() {
	    assertThrows(IndexOutOfBoundsException.class,
	            ()->{
	        		List<String> lista = new ArrayList<String>();
	        		String s = lista.get(0);

	            	});
	}
	
	@Test
	public void itShouldThrowExceptionWhenBIsZero() {
	    assertThrows(Exception.class,
	            ()->{
	        		MathUtils.divide(5, 0);
	            	});
	}
	@Test
	void isReturnACorrectNumber() throws Exception {
		assertEquals(5,MathUtils.parseStringToNumber("5"));
	}
	
	@Test
	void isCorrectNumber() {
		assertEquals(10, MathUtils.parseStringToNumber("10.00"),0.1);
	}
	@Test
	void isInCorrectNumber() {
		assertNotEquals(10, MathUtils.parseStringToNumber("10.5"),0.1);
	}
	@Test
	void isCorrectNumber2() {
		assertNotEquals(-10, MathUtils.parseStringToNumber("-10.5"),0.1);
	}
	@Test
	void isNotNumber() {
		assertThrows(NumberFormatException.class,
	            ()->{ MathUtils.parseStringToNumber("blabla"); });
	}
	

}
