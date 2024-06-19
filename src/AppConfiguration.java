import java.io.Serializable;

import mlp.MLP;

public class AppConfiguration implements Serializable {
    private static final long serialVersionUID = 7443221597683830775L;
    public final int rubricCount;
    public final String[] rubricNames;
    public final MLP model;
    public final String[] wordVectorSorted;

    public AppConfiguration(
            String[] rubricNames,
            MLP model,
            String[] wordVectorSorted) {
        this.rubricNames = rubricNames;
        this.rubricCount = rubricNames.length;
        this.model = model;
        this.wordVectorSorted = wordVectorSorted;
    }
}
