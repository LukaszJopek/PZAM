package refleksja;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Refleksja_przyklady {

	public static void main(String[] args) {
		Refleksja refleksja = new Refleksja();
		
		Class c;
		c = "foo".getClass();
		System.out.println(c.getName()); 
		
		Class c1;
		c1 = refleksja.getClass();
		System.out.println(c1.getName()); 
		
		
		Class c3;
		Integer A = 0;
		c3 = A.getClass();
		System.out.println(c3.getName()); 
		
		c = boolean[][][].class; 
		System.out.println(c.getName()); 
		
		c = int[][][].class; 
		System.out.println(c.getName()); 
		
		c = double[][][].class; 
		System.out.println(c.getName()); 
		
		c = boolean.class; 
		System.out.println(c.getName()); 
		
		c = Boolean.class; 
		System.out.println(c.getName()); 
		
		Field[] fields = c.getDeclaredFields();
		for(int i=0;i<fields.length;i++) {
			System.out.println("Pola klasy: "+c.getName()+" ("+i+") "+fields[i].getName());
			System.out.println("Modyfikator: "+fields[i].getModifiers());
		}
		
		
		Method[] methods = c.getDeclaredMethods();
		for(int i=0;i<methods.length;i++) {
			System.out.println("Metody klasy: "+c.getName()+" ("+i+") "+methods[i].getName());
			System.out.println("Liczba parametrów metody: "+methods[i].getParameterCount());
			Parameter[] params = methods[i].getParameters();
			for(int j=0;j<params.length;j++) {
				System.out.println(" "+params[j].getName()+" "+params[j].getType());
			}
		}
		
		
		
		try {
			Class cMyClass = Class.forName("java.lang.Boolean");
			System.out.println("CanonicalName: "+cMyClass.getCanonicalName()+" PackageName: "+cMyClass.getPackageName()+"  SimpleName: "+cMyClass.getSimpleName());
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String s;
		Field f;
		Class c5 = Refleksja.class;
		System.out.println(c5.getName()); 
		fields = c5.getDeclaredFields();
		for(int i=0;i<fields.length;i++) {
			System.out.println("Pola klasy: "+c5.getName()+" ("+i+") "+fields[i].getName());
			System.out.println("Modyfikator: "+fields[i].getModifiers());
		}
		
		try {
			f = c5.getField("s");


			Method m = c5.getMethod("publicSetValue", int.class);
			m.invoke(refleksja, 2);
			
			refleksja.printValue();
			
			//m = c5.getMethod("setValue", int.class);
			//m.setAccessible(true);
			//m.invoke(refleksja, 5);
			
			refleksja.printValue();
			Method m2 = c5.getMethod("publicSetValue", int.class);
			m2.invoke(refleksja, 2);
			refleksja.printValue();
			
			 Field privateField = Refleksja.class.getDeclaredField("value");
			 privateField.setAccessible(true);
			 privateField.setInt(refleksja,5);
			 refleksja.printValue();
			 
			 Method privateMethod = Refleksja.class.getDeclaredMethod("setValue", int.class);
			 privateMethod.setAccessible(true);
			 privateMethod.invoke(refleksja, 7);
			 refleksja.printValue();
			 
			Constructor<?>[] cosntrutors = Boolean.class.getConstructors();
			for (int k=0; k<cosntrutors.length; k++) {
				System.out.println("klasa Boolean: "+ cosntrutors[k].getName()+" liczba parametrow: "+cosntrutors[k].getParameterCount());
				
			}
			//Boolean boolean = new Boolean
		
			
		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
/*		Class cl;
		try {
			cl = Class.forName("Refleksja");
			Method method = null;
			try {
				method = cl.getMethod("getWidth");
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				method.invoke(cl.newInstance());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| InstantiationException e) {

				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}*/


	}

}
