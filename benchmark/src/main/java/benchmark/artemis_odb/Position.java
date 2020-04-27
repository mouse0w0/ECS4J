package benchmark.artemis_odb;

import com.artemis.Component;

public class Position extends Component {
    public double x, y, z;

    public Position() {
    }

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
