package util.network;

import lombok.Data;
import org.openqa.selenium.devtools.DevTools;

import java.util.List;

@Data
public class NetworkEntity {

    private DevTools devTool;

    private List<DevToolEntity> devToolEntityList;


}
