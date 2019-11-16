//Name(s):
//ID
//Section
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * This class implements PageRank algorithm on simple graph structure.
 * Put your name(s), ID(s), and section here.
 *
 */
public class PageRanker {
	
	/**
	 * This class reads the direct graph stored in the file "inputLinkFilename" into memory.
	 * Each line in the input file should have the following format:
	 * <pid_1> <pid_2> <pid_3> .. <pid_n>
	 * 
	 * Where pid_1, pid_2, ..., pid_n are the page IDs of the page having links to page pid_1. 
	 * You can assume that a page ID is an integer.
	 */
	BufferedReader input;
	public void loadData(String inputLinkFilename){
		File inputFile = new File(inputLinkFilename);
		try {
			 input = new BufferedReader(new FileReader(inputFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will be called after the graph is loaded into the memory.
	 * This method initialize the parameters for the PageRank algorithm including
	 * setting an initial weight to each page.
	 */
	HashSet<Integer> P = new HashSet<>();
	HashSet<Integer> S;
	HashMap<Integer, TreeSet> M = new HashMap<>();
	HashMap<Integer, Integer> L = new HashMap<>();
	HashMap<Integer, Double> PR = new HashMap<>();
	double d;
	public void initialize(){
		d =0.85;
		String in;
		HashSet<Integer> s = new HashSet<>();
		try {
			while ((in = input.readLine()) != null) {
				String[] split = in.split(" ");
				int page = Integer.parseInt(split[0]);
				P.add(page);
				TreeSet<Integer> m = new TreeSet<>();
				for (int i = 1; i < split.length; i++) {
					int num = Integer.parseInt(split[i]);
					s.add(num);
					m.add(num);
					if (L.containsKey(num)) {
						L.replace(num, L.get(num) + 1);
					} else {
						L.put(num, 1);
					}
				}
				M.put(page, m);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		S = P;
		S.removeAll(s);
		for (int id : P) {
			PR.put(id, (double) (1 / P.size()));
		}
	}
	
	/**
	 * Computes the perplexity of the current state of the graph. The definition
	 * of perplexity is given in the project specs.
	 */
	public double getPerplexity(){return 0;}
	
	/**
	 * Returns true if the perplexity converges (hence, terminate the PageRank algorithm).
	 * Returns false otherwise (and PageRank algorithm continue to update the page scores). 
	 */
	public boolean isConverge(){return false;}
	
	/**
	 * The main method of PageRank algorithm. 
	 * Can assume that initialize() has been called before this method is invoked.
	 * While the algorithm is being run, this method should keep track of the perplexity
	 * after each iteration. 
	 * 
	 * Once the algorithm terminates, the method generates two output files.
	 * [1]	"perplexityOutFilename" lists the perplexity after each iteration on each line. 
	 * 		The output should look something like:
	 *  	
	 *  	183811
	 *  	79669.9
	 *  	86267.7
	 *  	72260.4
	 *  	75132.4
	 *  
	 *  Where, for example,the 183811 is the perplexity after the first iteration.
	 *
	 * [2] "prOutFilename" prints out the score for each page after the algorithm terminate.
	 * 		The output should look something like:
	 * 		
	 * 		1	0.1235
	 * 		2	0.3542
	 * 		3 	0.236
	 * 		
	 * Where, for example, 0.1235 is the PageRank score of page 1.
	 * 
	 */
	public void runPageRank(String perplexityOutFilename, String prOutFilename){}
	
	
	/**
	 * Return the top K page IDs, whose scores are highest.
	 */
	public Integer[] getRankedPages(int K){return null;}
	
	public static void main(String args[])
	{
	long startTime = System.currentTimeMillis();
		PageRanker pageRanker =  new PageRanker();
		pageRanker.loadData("citeseer.dat");
		pageRanker.initialize();
		pageRanker.runPageRank("perplexity.out", "pr_scores.out");
		Integer[] rankedPages = pageRanker.getRankedPages(100);
	double estimatedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		
		System.out.println("Top 100 Pages are:\n"+Arrays.toString(rankedPages));
		System.out.println("Proccessing time: "+estimatedTime+" seconds");
	}
}