package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;
import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;
    private ISet<URI> allURI = new ChainedHashSet<URI>();

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        //Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        for (Webpage webpage: webpages) {
            allURI.add(webpage.getUri());
        }
    
        IDictionary<URI, ISet<URI>> pageGraph = new ChainedHashDictionary<URI, ISet<URI>>();
        for (Webpage page: webpages) {
            IList<URI> links = page.getLinks();
            ISet<URI> childenLinks = new ChainedHashSet<URI>();
            ISet<URI> added = new ChainedHashSet<URI>();
            for (URI link : links) {
                if (allURI.contains(link) && !link.equals(page.getUri()) && !added.contains(link)) {
                    childenLinks.add(link);
                    added.add(link);
                }
            }
            pageGraph.put(page.getUri(), childenLinks);
        }
        return pageGraph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> oldPageRanks = new ChainedHashDictionary<URI, Double>();
        for (URI uri : this.allURI) {
            oldPageRanks.put(uri, 1.0/allURI.size());
        }
       
        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            
            // 1) give every web page a new page rank of 0.0
            IDictionary<URI, Double> newPageRanks = new ChainedHashDictionary<URI, Double>();
            for (URI uri : this.allURI) {
                newPageRanks.put(uri, 0.0);
            }
  
            // 2)
            for (KVPair<URI, ISet<URI>> vertex : graph) {
                double oldRank = oldPageRanks.get(vertex.getKey());
                if (vertex.getValue().size() > 0) {
                    for (URI linkedVertex : vertex.getValue()) {
                        newPageRanks.put(linkedVertex, newPageRanks.get(linkedVertex) + (decay * oldRank / vertex.getValue().size()));
                    }
                } else {
                    for (KVPair<URI, Double> newRank : newPageRanks) {
                        newPageRanks.put(newRank.getKey(), newPageRanks.get(newRank.getKey()) + (decay * oldRank / this.allURI.size()));
                    }
                }
            }
            
            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean exist = true;
            for (KVPair<URI, Double> newRank : newPageRanks) {
                // Step 2-3 
                newPageRanks.put(newRank.getKey(), newRank.getValue() + ((1 - decay) / this.allURI.size()));
                double oldRank = oldPageRanks.get(newRank.getKey());
                if (Math.abs(newRank.getValue() - oldRank) > epsilon) {
                    exist = false;
                }
            }
            if (exist) {
                return oldPageRanks;
            } else {
                oldPageRanks = newPageRanks;
            }
        }
        return oldPageRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
