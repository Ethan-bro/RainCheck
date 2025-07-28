package interface_adapter.create_customTag;

import interface_adapter.ViewModel;

public class createCustomTagViewModel extends ViewModel<createCustomTagState> {

    public createCustomTagViewModel() {
        super("Create Custom Tag");
        setState(new createCustomTagState());
    }

}
