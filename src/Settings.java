import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @ClassName Settings
 * @Author NING MEI
 * @Date 2020/1/15 14:57
 * @Version 1.0
 */
public class Settings implements SearchableConfigurable {
    private SettingsGUI settingsGUI;
    @NotNull
    @Override
    public String getId() {
        return "ProtoBuf-Plus.settings";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "ProtoBuf Compiler";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        this.settingsGUI=new SettingsGUI();
        ProtoBufConfig instance = ProtoBufConfig.getInstance();
        return settingsGUI.getROOTComponent(instance);
    }

    @Override
    public boolean isModified() {
        ProtoBufConfig newConfig = settingsGUI.getNewConfig();
        ProtoBufConfig oldConfig = ProtoBufConfig.getInstance();
        System.out.println("modify: "+!newConfig.compare(oldConfig));
        return !newConfig.compare(oldConfig);
    }

    @Override
    public void apply() throws ConfigurationException {
        ProtoBufConfig newConfig = settingsGUI.getNewConfig();
        newConfig.loadState(newConfig);
    }
}
