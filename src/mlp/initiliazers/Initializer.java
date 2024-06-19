package mlp.initiliazers;

import java.util.Random;
import java.util.function.Supplier;

public class Initializer implements Supplier<Double> {
    protected Random rnd;

    public Initializer(Random random) {
        rnd = random;
    }

    public Initializer() {
        this(new Random());
    }

    @Override
    public Double get() {
        return rnd.nextDouble() * 2 - 1;
    }
}
