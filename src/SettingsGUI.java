import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @ClassName SerringsGUI
 * @Author NING MEI
 * @Date 2020/1/15 15:09
 * @Version 1.0
 */
public class SettingsGUI {
    private JPanel rootPanel;
    private TextFieldWithBrowseButton fileChooser;
    private JLabel compilerText;
    private JTextField folderPlace;
    private JLabel place_text;
    private ProtoBufConfig config;

    public JComponent getROOTComponent(ProtoBufConfig config) {
        fileChooser.addBrowseFolderListener(new TextBrowseFolderListener(new ProtoBufFileDescriptor(true, false, "exe")));
        this.config = config;
        if (notNull(config)) {
            if (notNull(config.getProtoPath())) {
                fileChooser.setText(config.getProtoPath());
            }
            if (notNull(config.getDefaultFolderPlace())) {
                folderPlace.setText(config.getDefaultFolderPlace());
            }
        }
        return rootPanel;
    }

    private boolean notNull(Object o) {
        return Objects.nonNull(o);
    }

    public ProtoBufConfig getNewConfig() {
        config.setProtoPath(fileChooser.getText());
        config.setDefaultFolderPlace(folderPlace.getText());
        return config;
    }

    class ProtoBufFileDescriptor extends FileChooserDescriptor {
        private String extension;

        public ProtoBufFileDescriptor(boolean chooseFiles, boolean chooseFolders, @Nullable String extension) {
            super(chooseFiles, chooseFolders, false, false, false, false);
            this.extension = extension;
        }

        @Override
        public boolean isFileSelectable(VirtualFile file) {
            if (StringUtils.isNotBlank(extension)) {
                return extension.equals(file.getExtension());
            } else {
                return true;
            }
        }
    }
}
