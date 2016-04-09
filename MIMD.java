import java.util.*;
import java.io.*;


public class MIMD
{
	final int no_procs;
	public double a;
	public double b;
	public double c;
	public int r;
	public double root;
	public double[] value = new double[2];
	
	public double f(double x)
	{
		return Math.sin(x);
	}
	public double fprime(double x)
	{
		return Math.cos(x);
	}
	public MIMD()
	{
		a = 0;
		b = 100;
		no_procs = 10;
		c = 0.000001;
		r = 100;
		root = -1;
	}
	public static void main( String args[])
	{
		MIMD M = new MIMD();
		M.start();
	}
	public void start()
	{
		double s;
		Thread[] t = new Thread[no_procs];
		s = (b - a)/(no_procs + 1);
			
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
		
		System.out.println(root);
		
	}
	class Handler extends Thread
	{
		public int index;
		public double curr;
		public double prev;
		public double s;
		Handler(int i, double s)
		{
			index = i;
			this.s = s;
		}
		public void run()
		{
			prev = f(a + index * s);
			int iteration = 0;
			while( iteration < r && root == -1)
			{
				curr = prev - f(prev) / fprime(prev);
				if (Math.abs(curr - prev) < c)
					root = curr;
				prev = curr;
				iteration++;
			}
		}
	}
}
