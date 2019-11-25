//Name: Kamonwan Tangamornphiboon, Patakorn Jearat, Matchatta Toyaem
//Section: 2
//ID: 6088034, 6088065, 6088169
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
	TreeSet<Integer> P = new TreeSet<>();
	TreeSet<Integer> S = new TreeSet<>();
	HashMap<Integer, TreeSet<Integer>> M = new HashMap<>();
	HashMap<Integer, Integer> L = new HashMap<>();
	HashMap<Integer, Double> PR = new HashMap<>();
	double perplexity = 0;
	double d;
	public void initialize(){
		d=0.85;
		try{
			String in;
			TreeSet<Integer> non_sink = new TreeSet<>();
			while((in = input.readLine())!=null){
				List<Integer> all = Arrays.asList(in.split(" ")).stream().map(Integer::valueOf).collect(Collectors.toList());
				P.addAll(all);
				TreeSet<Integer> m = new TreeSet(all.subList(1, all.size()));
				M.put(all.get(0), m);
				non_sink.addAll(m);
				for(int i : m){
					if(L.containsKey(i)){
						L.replace(i, L.get(i)+1);
					}
					else{
						L.put(i, 1);
					}
				}
			}
			S.addAll(P);
			S.removeAll(non_sink);
			for(int i:P){
				double value =1.0/P.size();
				PR.put(i, value);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Computes the perplexity of the current state of the graph. The definition
	 * of perplexity is given in the project specs.
	 */
	public double getPerplexity(){
	    double perplexity;
	    double power = 0.0;
	    for(int p : P){
	        power += PR.get(p)*(Math.log(PR.get(p))/Math.log(2.0));
        }
	    perplexity = Math.pow(2.0, -power);
	    return perplexity;
	}

	/**
	 * Returns true if the perplexity converges (hence, terminate the PageRank algorithm).
	 * Returns false otherwise (and PageRank algorithm continue to update the page scores).
	 */
	int iteration =1;
	public boolean isConverge(){
	    double perplexity = getPerplexity();
	    double unit = Math.floor(this.perplexity)-Math.floor(perplexity);
	    if(iteration>=3){
	    	this.perplexity = perplexity;
            return true;
        }
	    else if(unit==0){
			this.perplexity = perplexity;
			iteration++;
			return false;
		}
	    else{
	    	iteration =1;
	        this.perplexity = perplexity;
            return false;
        }
	}

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
	public void runPageRank(String perplexityOutFilename, String prOutFilename){
		try{
			FileWriter perplexityFile = new FileWriter(perplexityOutFilename);
			PrintWriter writePerplexity = new PrintWriter(perplexityFile);
			FileWriter prFile = new FileWriter(prOutFilename);
			PrintWriter writePR = new PrintWriter(prFile);
			int inter =0;
			while(!isConverge()){
				double sinkPR=0;
				HashMap<Integer, Double> newPR = new HashMap<>();
				for(int p : S){
					sinkPR+=PR.get(p);
				}
				for(int p : P){
					double newPR_value = (1-d)/P.size();
					newPR_value+= d*sinkPR/P.size();
					if(M.containsKey(p)){
						for(int q : M.get(p)){
							newPR_value+=d*PR.get(q)/L.get(q).doubleValue();
						}
					}
					newPR.put(p, newPR_value);
				}
				PR.putAll(newPR);
				if(inter>0){
					writePerplexity.println(this.perplexity);
				}
				inter++;
			}
			for(int p : P){
				writePR.println(p+" "+PR.get(p));
			}
			writePerplexity.println(this.perplexity);
			writePerplexity.close();
			writePR.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Return the top K page IDs, whose scores are highest.
	 */
	public Integer[] getRankedPages(int K){
		Map<Integer, Double> r = PR.entrySet()
				.stream()
				.sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		Integer[] result = new Integer[K];
		for(int i=0; i< K; i++){
			result[i] = (Integer) r.keySet().toArray()[i];
		}
		return result;
	}

	public static void main(String args[])
	{
	long startTime = System.currentTimeMillis();
		PageRanker pageRanker =  new PageRanker();
		pageRanker.loadData("test.dat");
		pageRanker.initialize();
		pageRanker.runPageRank("perplexity.out", "pr_scores.out");
		Integer[] rankedPages = pageRanker.getRankedPages(6);
	double estimatedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;

		System.out.println("Top 100 Pages are:\n"+Arrays.toString(rankedPages));
		System.out.println("Proccessing time: "+estimatedTime+" seconds");
	}
}