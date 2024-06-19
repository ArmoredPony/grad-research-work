package mlp.initiliazers;

import java.util.Random;

public class HeInitializer extends Initializer {
    private final double COEF;

    public HeInitializer(Random random, int inputNeurons) {
        super(random);

        if (inputNeurons <= 0)
            throw new IllegalArgumentException("'inputNeurons' value must be > 0.");

        COEF = Math.sqrt(2. / inputNeurons);
    }

    public HeInitializer(int inputNeurons) {
        this(new Random(), inputNeurons);
    }

    @Override
    public Double get() {
        return rnd.nextGaussian(0, COEF);
    }
}
