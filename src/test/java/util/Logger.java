package util;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.network.NetworkEntity;
import util.network.NetworkHelper;

@Data
@AllArgsConstructor
public class Logger {

    private NetworkHelper networkHelper;

    private NetworkEntity networkEntity;

}
