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
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    private IDictionary<URI, Double> vectorsNormSquare = new ChainedHashDictionary<URI, Double>();
    

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
       IDictionary<String, Integer> termNumbers = new ChainedHashDictionary<String, Integer>();
       for (Webpage page : pages) {
           IList<String> words = page.getWords();
           ISet<String> added = new ChainedHashSet<String>();
           for (String word : words) {
               if (termNumbers.containsKey(word) && !added.contains(word)) {
                   int docAppered = termNumbers.get(word);
                   termNumbers.put(word, docAppered + 1);
                   added.add(word);
               } else if (!termNumbers.containsKey(word)) {
                   termNumbers.put(word, 1);
                   added.add(word);
               }
           }
       } 
       IDictionary<String, Double> idf = new ChainedHashDictionary<String, Double>();
       for (KVPair<String, Integer> term : termNumbers) {
           idf.put(term.getKey(), Math.log(pages.size() * 1.0/term.getValue()));
       }
       return idf;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Integer> termNumbers = new ChainedHashDictionary<String, Integer>();
        for (String word : words) {
            if (termNumbers.containsKey(word)) {
                int docAppered = termNumbers.get(word);
                termNumbers.put(word, docAppered + 1);
            } else {
                termNumbers.put(word, 1);
            }
        } 
        IDictionary<String, Double> tf = new ChainedHashDictionary<String, Double>();
        for (KVPair<String, Integer> term : termNumbers) {
            tf.put(term.getKey(), (term.getValue()/(words.size() * 1.0)));
            //System.out.println(term.getValue()/(words.size() * 1.0));
        }
        return tf;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        IDictionary<URI, IDictionary<String, Double>> ifIdfs = 
                new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        for (Webpage page : pages) {
            double normSquare = 0.0;
            IDictionary<String, Double> tfIdfsEachPage = new ChainedHashDictionary<String, Double>();
            IList<String> words = page.getWords();
            IDictionary<String, Double> tFsForSinglePage = this.computeTfScores(words);
            for (KVPair<String, Double> tf : tFsForSinglePage) {
                double score = tf.getValue() * this.idfScores.get(tf.getKey());
                tfIdfsEachPage.put(tf.getKey(), score);
                normSquare += score * score;
            }
            ifIdfs.put(page.getUri(), tfIdfsEachPage);
            this.vectorsNormSquare.put(page.getUri(), normSquare);
        }
        return ifIdfs;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        
        IDictionary<String, Double> tfIdfPage = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> tfQuery = this.computeTfScores(query);
        
        double numerator = 0.0;
        double normSquareQuery = 0.0;
        for (KVPair<String, Double> word : tfQuery) {
            double docWordScore = 0.0;
            if (tfIdfPage.containsKey(word.getKey())) {
                docWordScore = tfIdfPage.get(word.getKey());
            }
            
            double queryWordScore = 0.0;
            if (this.idfScores.containsKey(word.getKey())) {
                queryWordScore = word.getValue() * this.idfScores.get(word.getKey());
            } 
            
            numerator += docWordScore * queryWordScore;
            normSquareQuery += queryWordScore * queryWordScore;
        }
        
        double denominator = Math.sqrt(this.vectorsNormSquare.get(pageUri)) * Math.sqrt(normSquareQuery);
        
        if (denominator != 0) {
            return numerator/denominator;
        } else {
            return 0.0;
        }
    }              
}