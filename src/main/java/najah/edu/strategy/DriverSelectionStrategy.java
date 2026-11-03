package najah.edu.strategy;

import java.util.List;
import najah.edu.domain.Driver;

public interface DriverSelectionStrategy {

    Driver selectDriver(List<Driver> drivers);

}