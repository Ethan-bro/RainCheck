package interface_adapter;

import javax.swing.*;

public interface DynamicViewManager {
    void registerView(String viewKey, JComponent panel);
}

