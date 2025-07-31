package interface_adapter.create_customTag;

import interface_adapter.ViewModel;

public class CCTViewModel extends ViewModel<CCTState> {

    public CCTViewModel() {
        super("Create Custom Tag");
        setState(new CCTState());
    }

}
