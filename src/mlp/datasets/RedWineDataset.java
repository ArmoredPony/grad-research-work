package mlp.datasets;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class RedWineDataset extends Dataset {
    public RedWineDataset() throws FileNotFoundException {
        super(new FileReader("csv/winequality-red.csv"), 1599, 11, 1);
    }
}
