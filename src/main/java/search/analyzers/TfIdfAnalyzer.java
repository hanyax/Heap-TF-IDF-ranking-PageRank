package search.analyzers;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
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
           for (String word : words) {
               if (termNumbers.containsKey(word)) {
                   int docAppered = termNumbers.get(word);
                   termNumbers.put(word, docAppered + 1);
               } else {
                   termNumbers.put(word, 1);
               }
           }
       } 
       IDictionary<String, Double> IDF = new ChainedHashDictionary<String, Double>();
       for (KVPair<String, Integer> term : termNumbers) {
           IDF.put(term.getKey(), Math.log(pages.size() * 1.0/term.getValue()));
       }
       return IDF;
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
        IDictionary<String, Double> TF = new ChainedHashDictionary<String, Double>();
        for (KVPair<String, Integer> term : termNumbers) {
            TF.put(term.getKey(), (term.getValue()/(words.size() * 1.0)));
        }
        return TF;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        IDictionary<URI, IDictionary<String, Double>> TfIdfs = new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        for (Webpage page : pages) {
            IDictionary<String, Double> TfIdfsEachPage = new ChainedHashDictionary<String, Double>();
            IList<String> words = page.getWords();
            IDictionary<String, Double> TFsForSinglePage = this.computeTfScores(words);
            for (KVPair<String, Double> TF : TFsForSinglePage) {
                TfIdfsEachPage.put(TF.getKey(), TF.getValue() * this.idfScores.get(TF.getKey()));
            }
            TfIdfs.put(page.getUri(), TfIdfsEachPage);
        }
        return TfIdfs;
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
        IDictionary<String, Double> TfIdfPage = this.documentTfIdfVectors.get(pageUri);
        
        IDictionary<String, Double> TfIdfQuery = this.computeTfScores(query);
        
        
        for (KVPair<String, Double> word : TfIdfQuery) {
            
            
            double queryWordScore;
            if (this.idfScores.containsKey(word.getKey())) {
                TfIdfQuery.put(word.getKey(), word.getValue() * this.idfScores.get(word.getKey()));
            } else {
                TfIdfQuery.put(word.getKey(), 0.0);
            }
        }
        
        
        
    }
        
    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for(KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output = score * score;
        }
        return Math.sqrt(output);
    }
        
}
