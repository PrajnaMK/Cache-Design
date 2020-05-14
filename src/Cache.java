import java.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 



import java.lang.*;
import java.net.Inet4Address;
import java.io.File;
import java.io.FileNotFoundException;

public class Cache {
	sim_cache m = new sim_cache();
	int LRU_counter=0;
	int FIFO_Counter=0;
	int Opt_counter=0;
	int Opt_count1=0;
	int Opt_count11=0;
	public int read=0;
	public int write=0;
	public int readmiss=0;
	public int writemiss=0;
	public int writeback=0;
	public int readl2=0;
	public int writel2=0;
	public int writebackl2=0;
	public int l2missrate=0;
	public int l1missrate=0;
	public int traffic=0;
	public int l2readmiss=0;
	public int l2writemiss=0;
	int i;
	
	double mul= m.blck * m.L1A;
	double set = m.L1S/( m.L1A*m.blck);
	double bit_set = Math.log(set)/Math.log(2);
	double block_offset = Math.log(m.blck)/Math.log(2);
	double tags = 32-(bit_set+ block_offset);
	int id =(int) Math.round(tags);
	
	int id3= (int) Math.round(bit_set);
	int d3= id+id3;
	
	int[][] arr_LRU = new int[(int) set][m.L1A];
	int[][] arr_FIFO = new int[(int) set][m.L1A];
	List<String> Opt_arr = new ArrayList<String>();
	
	
	//reading the input file
	public void Readfile() throws FileNotFoundException {
		Scanner s = new Scanner(new File(m.tracefile));
        while(s.hasNextLine()) {
			
			String line1 = s.nextLine();
			
			
			String[] a = line1.split(" ");	
			
			int deci1 = Integer.parseInt(a[1],16);
			String bin1= Integer.toBinaryString(deci1);
			bin1=("00000000000000000000000000000000" + bin1).substring(bin1.length());
			String new_tag1 = bin1.substring(0, id);	
			String index1= bin1.substring(id, d3);
			
			int new_deci1 = Integer.parseInt(new_tag1,2);
			String new_hexa1 = Integer.toHexString(new_deci1);
			
			int new_index1 = Integer.parseInt(index1,2);
			String app_tag1= new_index1+" "+new_hexa1;
			
			Opt_arr.add(app_tag1);
			
			
        }
       
        Scanner s1= new Scanner(new File(m.tracefile));
		while(s1.hasNextLine()) {
			
			String line= s1.nextLine();
			
			
			String[] a = line.split(" ");	
			
			int deci = Integer.parseInt(a[1],16);
			String bin= Integer.toBinaryString(deci);
			bin=("00000000000000000000000000000000" + bin).substring(bin.length());
			String new_tag = bin.substring(0, id);	
			String index= bin.substring(id, d3);
			
			int new_deci = Integer.parseInt(new_tag,2);
			String new_hexa = Integer.toHexString(new_deci);
			
			int new_index = Integer.parseInt(index,2);
	
			
			
			if(a[0].equals("r")) {
				
				
				read(new_index,new_hexa);
				
			}
			else if (a[0].equals("w"))
			{
				
				write(new_index,new_hexa);
				
			}
				
		}
		
		
	}
	
	
	String[][] arr = new String[(int) set][m.L1A];
	
	public void read(int index, String tag) {
		

		read++;
		
		for(int i=0;i<m.L1A;i++) {
			try {
				if (arr[index][i].isEmpty()) {
					
				}
		
		
			    else if(remove(arr[index][i]).equals(tag)) 
			    {
			    	LRU_counter=LRU_counter+1;
			    	arr_LRU[index][i]=LRU_counter;
			    	Opt_arr.remove(0);
					break;
					
				 }
				
			    else if(!(remove(arr[index][i])).equals(tag)) 
			    {
			    	if(i+1 == m.L1A) 
			    	{
			    		
			    		if(m.Repl==0) {
			    			LRU_fun_Read(index, tag);
			    			break;
			    		}
			    		if(m.Repl==1) {
							
							FIFORead(index, tag);
							break;
						}
			    		if(m.Repl==2) {
			    			
			    			optimal_read(index,tag);
			    			Opt_arr.remove(0);
			    		
			    			 break;
			    		}
			    		
			    		
			    		
				    }
			    	
		    	
			    }
				
			}
			
		
        catch(NullPointerException e) {
				
				readmiss++;
				arr[index][i]= tag;
				LRU_counter=LRU_counter+1;
				arr_LRU[index][i]=LRU_counter;
				FIFO_Counter=FIFO_Counter+1;
				arr_FIFO[index][i]=FIFO_Counter+1;
				Opt_arr.remove(0);
				break;
			}
			
	}
		
	}
	
	public void write(int index,String tag) {
		write++;
		
		for(int i=0;i<m.L1A;i++) {
			try {
				if(arr[index][i].isEmpty())  
				{
					
				}
			
				else if (remove(arr[index][i]).equals(tag))
					
			  {
					arr[index][i]=tag+" D";
					LRU_counter=LRU_counter+1;
					arr_LRU[index][i]=LRU_counter;
					Opt_arr.remove(0);
				    break;
				    
				
			   }
			
			else 	if(!(remove(arr[index][i])).equals(tag))
		  {
			
				if(i == m.L1A-1) 
				{
					
			       
					if(m.Repl==0) {
		    			LRU_fun_Write(index, tag);
		    			break;
		    		}
					if(m.Repl==0) {
					
						FIFOWrite(index, tag);
						break;
		      }
					if(m.Repl==0) {
						
										
						 optimal_write(index,tag); 
						 Opt_arr.remove(0);
						 break;
					}
				
				}
				
				
		  }
		
	}
		catch(NullPointerException e) 
		{
			   
		        writemiss++;
		
		        arr[index][i]=tag+" D";
		        LRU_counter=LRU_counter+1;
		        arr_LRU[index][i]=LRU_counter;
		        FIFO_Counter=FIFO_Counter+1;
				arr_FIFO[index][i]=FIFO_Counter+1;
				Opt_arr.remove(0);
		        break;
		
	     }
	   }
}
		
	
	
		
	public void LRU_fun_Read(int row, String tag)
	{
		
		int temptr=0;
		int minval = arr_LRU[row][0];
		for(int j=0;j<m.L1A;j++)
		{
			if(arr_LRU[row][j]< minval) {
				minval= arr_LRU[row][j];
				temptr=j;
			}
		}
		if(arr[row][temptr].endsWith(" D")) {
	    	   writeback++;
	       }
        arr[row][temptr]=tag;
        readmiss++;
		LRU_counter=LRU_counter+1;
		arr_LRU[row][temptr]=LRU_counter;
	
	}
	
	
	public void LRU_fun_Write(int row, String tag)
	{
		
		int temptr=0;
		int minval = arr_LRU[row][0];
		for(int j=0;j<m.L1A;j++)
		{
			if(arr_LRU[row][j]< minval) {
				minval= arr_LRU[row][j];
				temptr=j;
			}
		}
		if(arr[row][temptr].endsWith(" D")) {
	    	   writeback++;
	       }
        arr[row][temptr]=tag+" D";
        writemiss++;
		LRU_counter=LRU_counter+1;
		arr_LRU[row][temptr]=LRU_counter;
		
		
	}
		
	
	
	public void FIFORead(int row, String tag) {
		
		
		int temp=0;
		int minval1=arr_FIFO[row][0];
		
		for(int j1=0;j1<m.L1A;j1++)
		{
			if(arr_FIFO[row][j1]< minval1) {
				minval1= arr_FIFO[row][j1];
				temp=j1;
			}
		}
		if(arr[row][temp].endsWith(" D")) {
	    	   writeback++;
	       }
		
		arr[row][temp]=tag;
		readmiss++;
		
		FIFO_Counter=FIFO_Counter+1;
		arr_FIFO[row][temp]=FIFO_Counter+1;
 	
	}
	

	public void FIFOWrite(int row, String tag) {
		
		
		int temp=0;
		int minval1=arr_FIFO[row][0];
		
		for(int j1=0;j1<m.L1A;j1++)
		{
			if(arr_FIFO[row][j1]< minval1) {
				minval1= arr_FIFO[row][j1];
				temp=j1;
			}
		}
		if(arr[row][temp].endsWith(" D")) {
	    	   writeback++;
	       }
			arr[row][temp]=tag+" D";
			writemiss++;
			
			FIFO_Counter=FIFO_Counter+1;
			arr_FIFO[row][temp]=FIFO_Counter+1;
	        
		
		
	}
	
	
			
	public String remove(String arr2) {
		if(arr2.endsWith(" D")) {
			return arr2.substring(0, arr2.length()-2);
			
		}
		return arr2;
	}
	
	
	
	
	public void optimal_read(int row, String tag) {
		
		
		
		List <Integer> t1 =new ArrayList<Integer>(); 
		List <String> a2= new ArrayList<String>();
		
        Opt_count1=0;
		int t2=0;
	
		
		for(int i2=0;i2<m.L1A;i2++) {
			
			
			t1.add(-1);
		}
		
		for(int i2=0;i2<m.L1A;i2++) {
			
			
			a2.add(row+" "+remove(arr[row][i2]));
			
			
		}
			
			
		for(int i2=0;i2<m.L1A;i2++) {
			
				
			for(int z1=0;z1<Opt_arr.size();z1++) {
				
				Opt_count1=Opt_count1+1;
					
				if(a2.get(i2).equals(Opt_arr.get(z1))) {
					
					
					t1.set(i2, Opt_count1);
					
					Opt_count1=0;
					break; 
				}
				
			
					
			}
		
		}
		 for(int i2=0;i2<t1.size();i2++) {
			 
				if(t1.get(i2)==-1)
				{
					t2= i2;
					break;
				}
		 }
		 if(!t1.contains(-1))
		 {
			 t2= t1.indexOf(Collections.max(t1)); 
		 }
			
		
		 if(arr[row][t2].endsWith(" D")) {
				writeback++;
			}
			
		
			arr[row][t2]=tag;
			
			readmiss++;

			
}
		
		
	
public void optimal_write(int row, String tag) {
	
	
	Opt_count1=0;
	List <Integer> t1 =new ArrayList<Integer>(); 
	List <String> a2= new ArrayList<String>();
	
	int t2=0;
	for(int i2=0;i2<m.L1A;i2++) {
		t1.add(-1);
	}
	
	for(int i2=0;i2<m.L1A;i2++) {
		
		
		a2.add(row+" "+remove(arr[row][i2]));
		
		
	}
		
		
	for(int i2=0;i2<m.L1A;i2++) {
			
		for(int z1=0;z1<Opt_arr.size();z1++) 
		{
			
			Opt_count1=Opt_count1+1;
				
			if(a2.get(i2).equals(Opt_arr.get(z1))) {
				t1.set(i2, Opt_count1);
				
				Opt_count1=0;
				break;
			}	
		}


			
	}
	 for(int i2=0;i2<t1.size();i2++) {
		 
			if(t1.get(i2)==-1) 
			{
				t2=i2;
				break;
					
			}
			}
		
		if(!t1.contains(-1))
		{
			t2=t1.indexOf(Collections.max(t1));
		}
			
		if(arr[row][t2].endsWith(" D")) 
		{
			writeback++;
							
		}
	 arr[row][t2]=tag+" D";
	
	 writemiss++;
	

	}

}
	
	
	

	

		