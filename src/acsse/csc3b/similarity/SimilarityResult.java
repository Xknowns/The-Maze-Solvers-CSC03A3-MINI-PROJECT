package acsse.csc3b.similarity;

/**
 * Represents the result of a graph similarity comparison.
 * Contains both a numerical similarity score and a detailed explanation of the comparison.
 *
 * @author The Maze Solvers
 * @version 1.0
 */
public class SimilarityResult {
    private double score;
    private String details;

    /**
     * Constructs a SimilarityResult with a score and descriptive details.
     *
     * @param score similarity score between two graphs
     * @param details textual explanation of the comparison
     */
    public SimilarityResult(double score, String details) {
        this.score = score;
        this.details = details;
    }

    /**
     * Returns the similarity score.
     *
     * @return similarity score as a double
     */
    public double getScore() { return score; }
    
    /**
     * Returns a detailed breakdown of the similarity analysis.
     *
     * @return explanation string of comparison results
     */
    public String getDetails() { return details; }

    /**
     * Returns the similarity score formatted as a percentage string.
     *
     * @return formatted similarity score (e.g., "Similarity Score: 85.23%")
     */
    public String getScoreString() {
        // score is already 0-100 from GraphSimilarity.compare()
        return String.format("Similarity Score: %.2f%%", score);
    }
}