import java.util.*;
import java.io.*;


public class SIMD
{
	final int no_procs;
	public double a;
	public double b;
	public double c;
	public double[] value = new double[2];
	
	public double f(double x)
	{
		return Math.sin(x);
	}
	public double fprime(double x)
	{
		return Math.cos(x);
	}
	public SIMD()
	{
		a = 0;
		b = 100;
		no_procs = 10;
		c = 0.000001;
	}
	public static void main( String args[])
	{
		SIMD S = new SIMD();
		S.start();
		System.out.println(S.a);
	}
	public void start()
	{
		double s;
		double[] y = new double[no_procs+2];
		Thread[] t = new Thread[no_procs];
		while ( (b - a) >= c ) 
		{
			s = (b - a)/(no_procs + 1);
			y[0] = f(a);
			y[no_procs+1] = f(b);
			for(int i = 1; i<=no_procs; i++)
			{
				t[i-1] = new Thread( new Handler(i, s) );
				t[i-1].start();
			}
			for (int i=0; i<no_procs ; i++ )
			{
				try 
				{
					t[i].join();
				}
				catch ( InterruptedException e)
				{
					System.exit(1);
				}
			}
			if( y[no_procs] * y[no_procs+1] < 0)
				value[0] = a + no_procs * s;

			a = value[0];
			b = value[1];
		}
		
		System.out.println(a);
		System.out.println(b);
	}
	class Handler extends Thread
	{
		public int index;
		public double s;
		public double curr;
		public double prev;
		Handler(int i, double s)
		{
			index = i;
			this.s = s;
		}
		public void run()
		{
			curr = f(a + index * s);
			prev = f(a + index*s - s);
			if (curr * prev < 0)
			{
				a = a + (index -1 ) * s;
				b = a + index * s;
			}
			value[0] = a;
			value[1] = b;
		}
	}
}
