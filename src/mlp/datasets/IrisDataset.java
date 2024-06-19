package mlp.datasets;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class IrisDataset extends Dataset {
    public IrisDataset() throws FileNotFoundException {
        super(new FileReader("csv/iris.csv"), 150, 4, 3);
    }
}
