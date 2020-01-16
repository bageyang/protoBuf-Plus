import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @ClassName ProtoBufConfig
 * @Author NING MEI
 * @Date 2020/1/15 15:32
 * @Version 1.0
 */
@State(name="ProtoBuf-Plus.state")
@Storage(StoragePathMacros.WORKSPACE_FILE)
public class ProtoBufConfig implements PersistentStateComponent<ProtoBufConfig> {
    private String protoPath;
    private String defaultFolderPlace;

    @Nullable
    @Override
    public ProtoBufConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProtoBufConfig protoBufConfig) {
        PropertiesComponent component = PropertiesComponent.getInstance();
        component.setValue("protoPath",protoBufConfig.getProtoPath());
        component.setValue("defaultFolderPlace",protoBufConfig.getDefaultFolderPlace());
    }

   public static ProtoBufConfig getInstance(){
       PropertiesComponent component = PropertiesComponent.getInstance();
       String protoPath = component.getValue("protoPath");
       String defaultFolderPlace = component.getValue("defaultFolderPlace");
       ProtoBufConfig protoBufConfig = new ProtoBufConfig();
       protoBufConfig.setDefaultFolderPlace(defaultFolderPlace);
       protoBufConfig.setProtoPath(protoPath);
       return protoBufConfig;
   }

    public String getProtoPath() {
        return protoPath;
    }

    public void setProtoPath(String protoPath) {
        this.protoPath = protoPath;
    }

    public String getDefaultFolderPlace() {
        return defaultFolderPlace;
    }

    public void setDefaultFolderPlace(String defaultFolderPlace) {
        this.defaultFolderPlace = defaultFolderPlace;
    }


    public boolean compare(ProtoBufConfig o) {
        if (this == o) {
            return true;
        }
        return StringCompare(getProtoPath(),o.getProtoPath()) &&
                StringCompare(getDefaultFolderPlace(),o.getDefaultFolderPlace());
    }

    /**
     * in this method each variable instance of String "" will equals null
     * @param s
     * @param l
     * @return
     */
    private boolean StringCompare(String s,String l){
        if (StringUtils.isBlank(s)&&StringUtils.isBlank(l)) {
            return true;
        }else {
            return s.equals(l);
        }
    }
}
