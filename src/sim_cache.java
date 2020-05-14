
import java.util.*;
import java.nio.file.Path; 
import java.nio.file.Paths; 
import java.io.*;
import java.nio.file.Path;
public class sim_cache {
	int BLOCKSIZE = 0, L1_SIZE = 0, L1_ASSOC = 0, L2_SIZE = 0, L2_ASSOC = 0, REPLACEMENT_POLICY = 0, INCLUSION_PROPERTY = 0;
	  static String tracefile=null;
	  static String block,L1Size,L1Ass,L2Size,L2Ass,Replace,Inclu;
	  static int blck, L1S,L1A,L2S,L2A,Repl,Incl;;
	  
	
	  
	  public static void main(String args[])
	{
		 String block,L1Size,L1Ass,L2Size,L2Ass,Replace,Inclu;
		sim_cache m = new sim_cache();
		int i = 0;
	    block= Integer.toString(m.BLOCKSIZE);
		L1Size= Integer.toString(m.L1_SIZE);
		L1Ass= Integer.toString(m.L1_ASSOC);
		L2Size= Integer.toString(m.L2_SIZE);
		L2Ass= Integer.toString(m.L2_ASSOC);
		Replace= Integer.toString(m.REPLACEMENT_POLICY);
		Inclu= Integer.toString( m.INCLUSION_PROPERTY);
		m.tracefile = null;
		block = args[0];
		L1Size = args[1];
		L1Ass = args[2];
		L2Size = args[3];
		L2Ass = args[4];
		Replace = args[5];
		Inclu = args[6];
		m.tracefile = args[7];
		
		m.blck= Integer.parseInt(block);
		m.L1S= Integer.parseInt(L1Size);
		m.L1A= Integer.parseInt(L1Ass);
		m.L2S= Integer.parseInt(L2Size);
		m.L2A= Integer.parseInt(L2Ass);
		m.Repl=Integer.parseInt(Replace);;
		m.Incl= Integer.parseInt(Inclu);;
		Cache c= new Cache();
		
		try{
			   c.Readfile();
			   }catch(FileNotFoundException e){
			       System.out.println("file not found");
			   }
		
		
		
		float a=c.readmiss+c.writemiss;
		float b= c.read+ c.write;
		float c1=a/b;
		Path path= Paths.get(m.tracefile);
		Path file= path.getFileName();
		int set= (int) Math.round(c.set);
		
		System.out.println("===== Simulator configuration =====");
		System.out.println("BLOCKSIZE:                "+m.blck);		            
		System.out.println("L1_SIZE:                  "+m.L1S);			              
		System.out.println("L1_ASSOC:                 "+m.L1A);			              
		System.out.println("L2_SIZE:                  "+m.L2S);		               
		System.out.println("L2_ASSOC:                 "+m.L2A);	
		if(m.Repl==0) {
		
			System.out.println("REPLACEMENT POLICY:       LRU");
		}
		else if(m.Repl==1) {
			System.out.println("REPLACEMENT POLICY:       FIFO");
		}
		else
		{
			System.out.println("REPLACEMENT POLICY:       optimal");
		}
		if(m.Incl==0) {
			
			System.out.println("INCLUSION PROPERTY:       non-inclusive");
		}
		else {
			System.out.println("INCLUSION PROPERTY:       inclusive");
		}
					    
		System.out.println("trace_file:               "+file);	
		System.out.println("===== L1 contents =====");
		
		for( i=0;i<set;i++) {
			System.out.print("Set \t"+i+":\t");
			
			for (int j=0;j<m.L1A;j++) {
				
				System.out.print("\t       "+c.arr[i][j]);
				
			}
			System.out.println(" ");
		}
		
		System.out.println("===== Simulation results (raw) =====");
		System.out.println("a. number of L1 reads:        "+c.read);       
		System.out.println("b. number of L1 read misses:  "+c.readmiss);
		System.out.println("c. number of L1 writes:       "+c.write);
		System.out.println("d. number of L1 write misses: "+c.writemiss);
		System.out.println("e. L1 miss rate:              "+c1);
		System.out.println("f. number of L1 writebacks:   "+c.writeback);
		System.out.println("h. number of L2 read:         "+c.readl2);
		System.out.println("h. number of L2 read misses:  "+c.l2readmiss);
		System.out.println("i. number of L2 writes:       "+c.writel2);
		System.out.println("j. number of L2 write misses: "+c.l2writemiss);
		System.out.println("k. L2 miss rate:              "+c.l2missrate);
		System.out.println("l. number of L2 writebacks:   "+c.writebackl2);
		System.out.println("m. total memory traffic:      "+c.traffic);
		   
		
		
		//System.out.println("Optimal array"+c.Opt_arr);

		
		
	}

}
